package com.lowleveldesign.notificationsystem.model;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.enums.DeliveryStatus;
import com.lowleveldesign.notificationsystem.enums.NotificationType;
import com.lowleveldesign.notificationsystem.enums.Priority;

import java.time.LocalDateTime;

/**
 * One concrete delivery: one message, to one user, over ONE channel.
 * A request fanning out to 3 channels creates 3 Notifications, so each
 * channel is tracked, retried and can fail independently.
 */
public class Notification {
    private final String id;
    private final long sequence; // global ordering; tie-breaker inside the priority queue
    private final String userId;
    private final ChannelType channelType;
    private final NotificationType type;
    private final Priority priority;
    private final String subject;
    private final String body;
    private final LocalDateTime createdAt = LocalDateTime.now();

    private DeliveryStatus status = DeliveryStatus.PENDING; // guarded by 'this'
    private int attempts;                                    // guarded by 'this'
    private String failureReason;                            // guarded by 'this'

    public Notification(long sequence, String userId, ChannelType channelType, NotificationType type,
                        Priority priority, String subject, String body) {
        this.id = "N-" + sequence;
        this.sequence = sequence;
        this.userId = userId;
        this.channelType = channelType;
        this.type = type;
        this.priority = priority;
        this.subject = subject;
        this.body = body;
    }

    public synchronized int recordAttempt() { return ++attempts; }

    public synchronized void markSent() { status = DeliveryStatus.SENT; failureReason = null; }

    public synchronized void markRetrying(String reason) { status = DeliveryStatus.RETRYING; failureReason = reason; }

    public synchronized void markFailed(String reason) { status = DeliveryStatus.FAILED; failureReason = reason; }

    public synchronized void markSkipped(String reason) { status = DeliveryStatus.SKIPPED; failureReason = reason; }

    public synchronized DeliveryStatus getStatus() { return status; }
    public synchronized int getAttempts() { return attempts; }
    public synchronized String getFailureReason() { return failureReason; }

    public String getId() { return id; }
    public long getSequence() { return sequence; }
    public String getUserId() { return userId; }
    public ChannelType getChannelType() { return channelType; }
    public NotificationType getType() { return type; }
    public Priority getPriority() { return priority; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
