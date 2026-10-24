# DESIGN NOTIFICATION SYSTEM

## Problem Statement

Design a Notification System that lets other services send messages to users over
multiple channels (Email, SMS, Push) without blocking on slow delivery. It must
respect user preferences, support templated content and priorities, retry transient
failures with backoff, and track the delivery status of every message, while
staying correct under many concurrent senders.

---

# Functional Requirements

* Client (another service) should be able to send a notification to a user by
  giving a template id, template parameters, a type and a priority.
* System should support multiple channels (EMAIL, SMS, PUSH) and deliver one
  message over several channels at once.
* System should support message templates with {placeholders} that are filled
  from request parameters.
* System should respect user preferences: a user can disable channels and mute
  categories (e.g. PROMOTIONAL). ALERT notifications can never be muted.
* System should deliver higher-priority notifications first (HIGH > MEDIUM > LOW).
* System should retry transient delivery failures with exponential backoff up to a
  maximum number of attempts, and fail immediately on permanent errors
  (e.g. user has no phone number).
* System should track the status of every delivery
  (PENDING, RETRYING, SENT, FAILED, SKIPPED) and expose per-user history.
* System should notify interested parties (listeners) of status changes.

---

# Non-Functional Requirements

* The system should be thread-safe under concurrent senders and concurrent workers.
* send() should be low latency and non-blocking: delivery is asynchronous, so a
  slow or failing channel never slows callers down.
* The design should be extensible: a new channel, retry policy or listener must be
  addable without modifying existing classes (Open/Closed).
* Failures must be isolated: one channel failing (or being slow) must not affect
  deliveries on other channels.
* Reliability: transient failures are retried; no notification silently
  disappears (every one ends in SENT, FAILED or SKIPPED).

---

# Core Entities

* User
* UserPreference
* NotificationTemplate
* NotificationRequest
* Notification
* NotificationChannel (Email, SMS, Push)
* RetryPolicy (ExponentialBackoff)
* NotificationListener

---

# Entities and Their Relationships

## Enums

ChannelType: EMAIL, SMS, PUSH
NotificationType: TRANSACTIONAL, PROMOTIONAL, ALERT // ALERT is not user-mutable
Priority: LOW(1), MEDIUM(2), HIGH(3)
DeliveryStatus: PENDING, RETRYING, SENT, FAILED, SKIPPED

---

## User

String id;
String name;
String email; // nullable
String phone; // nullable
String deviceToken; // nullable
UserPreference preference;

---

## UserPreference

Set<ChannelType> enabledChannels; // all enabled by default
Set<NotificationType> mutedTypes;

---

## NotificationTemplate

String id;
String subjectTemplate; // "Order {orderId} shipped"
String bodyTemplate; // "Hi {name}, your order {orderId} is on its way."

renderSubject(params), renderBody(params) // throws if a placeholder has no value

---

## NotificationRequest (what the caller asks for)

String userId;
String templateId;
NotificationType type;
Priority priority;
Map<String, String> params;
Set<ChannelType> channels; // empty = all channels the user has enabled

Immutable; built with a Builder.

---

## Notification (one delivery: one message, one user, ONE channel)

String id;
long sequence; // FIFO tie-breaker inside a priority level
String userId;
ChannelType channelType;
NotificationType type;
Priority priority;
String subject;
String body;
DeliveryStatus status;
int attempts;
String failureReason;

A request that fans out to 3 channels creates 3 Notifications, so each channel is
tracked, retried and can fail independently. Status changes are synchronized.

---

## NotificationChannel (interface) - Strategy

ChannelType getType();
boolean send(User user, Notification notification);

Contract: true = delivered; false/exception = transient failure (retry);
NonRetryableDeliveryException = permanent failure (no retry).

Implementations: EmailChannel, SmsChannel, PushChannel. ChannelRegistry maps
ChannelType -> implementation.

---

## RetryPolicy (interface) - Strategy

boolean shouldRetry(int attemptsMade);
long nextDelayMillis(int attemptsMade);

Implementation: ExponentialBackoffRetryPolicy(maxAttempts, baseDelayMillis).

---

## NotificationListener (interface) - Observer

void onStatusChange(Notification notification);

Implementation: ConsoleNotificationListener. NotificationEventPublisher fans out
to all listeners and shields delivery from listener exceptions.

---

## Services

* UserService - register/lookup users.
* TemplateService - register/lookup templates.
* NotificationRepository - stores notifications; history per user; counts by status.
* NotificationService (facade) - the public send(request) and getHistory(userId).
* NotificationDispatcher - async engine: PriorityBlockingQueue + worker pool +
  retry scheduler.

---

# Send Flow

1. Look up user and template; render subject and body from params.
2. Candidate channels = request channels, or all channels if none given.
3. Drop channels the user has disabled.
4. If the type is user-mutable and the user muted it, mark each as SKIPPED.
5. Otherwise create one Notification (PENDING) per remaining channel, save it, and
   submit it to the dispatcher queue.
6. Return immediately; delivery happens asynchronously.

# Delivery Flow (worker thread)

1. Take the highest-priority notification (FIFO within the same priority).
2. Call the matching channel.
3. true -> mark SENT. Permanent error -> mark FAILED.
4. Transient failure -> if the RetryPolicy allows, mark RETRYING and re-queue after
   the backoff delay; otherwise mark FAILED ("gave up after N attempts").
5. Publish every status change to the listeners.

---

# Relationships Summary

* User (1) -has-> (1) UserPreference
* NotificationRequest (1) -fans out to-> (many) Notification (one per channel)
* Notification (many) -belongs to-> (1) User
* Notification (1) -rendered from-> (1) NotificationTemplate
* ChannelRegistry (1) -holds-> (many) NotificationChannel
* NotificationDispatcher (1) -uses-> (1) RetryPolicy, (1) ChannelRegistry
* NotificationDispatcher / NotificationService (1) -publish to-> (1) NotificationEventPublisher
* NotificationEventPublisher (1) -notifies-> (many) NotificationListener

---

# Design Patterns Used

* **Strategy** - NotificationChannel and RetryPolicy are swappable.
* **Observer** - NotificationListener / NotificationEventPublisher for status changes.
* **Producer-Consumer** - NotificationService produces, dispatcher workers consume.
* **Builder** - NotificationRequest.Builder.
* **Facade** - NotificationService hides rendering, preferences and dispatching.
* **Registry / Factory-like lookup** - ChannelRegistry.
* **Dependency Injection** - every collaborator is passed through constructors.

---

# Concurrency Notes

* send() only creates objects and enqueues, so it never blocks on delivery.
* PriorityBlockingQueue is thread-safe; ordering is priority weight (descending),
  then sequence (ascending, FIFO).
* Notification status/attempt changes are synchronized on the notification.
* Registries use ConcurrentHashMap; listeners use CopyOnWriteArrayList;
  preference sets use ConcurrentHashMap key sets.
* An AtomicInteger tracks in-flight notifications, so awaitCompletion() can wait
  for a clean drain (retries included) before shutdown.

---

# Possible Extensions

* Durable queue (Kafka / SQS) and database instead of in-memory structures, so
  nothing is lost on restart.
* Idempotency keys so a retried request cannot send a duplicate.
* Per-channel worker pools / rate limits, circuit breaker, and fallback channels
  (e.g. SMS when push fails).
* Quiet hours / Do-Not-Disturb, scheduled and batched (digest) notifications.
* Distinguish more error types (rate-limited vs invalid recipient) in channels.