# DESIGN LOGGER SYSTEM

## Problem Statement

Design a Logging Framework that applications can use to record diagnostic
messages at different severity levels, route those messages to one or more
destinations (console, file, database, etc.), format them consistently,
and do so safely and efficiently under concurrent use from many threads.

---

# Functional Requirements

* Client (application code) should be able to log a message at a given
  severity level: TRACE, DEBUG, INFO, WARN, ERROR, FATAL.
* System should filter out messages below a configured threshold level
  (e.g. if threshold is INFO, DEBUG/TRACE messages are dropped).
* System should support multiple log destinations (Appenders) simultaneously
  for a single logger — e.g. Console + File at the same time.
* System should support pluggable message formats (e.g. plain text, JSON)
  independently per Appender.
* System should support multiple, independently-named Logger instances
  (typically one per class), all sharable via a central manager.
* System should allow logging an exception/stack trace alongside a message.

---

# Non-Functional Requirements

* The system should be thread-safe: multiple threads logging concurrently
  must not corrupt output or crash the appenders.
* The design should be extensible (Open/Closed) — adding a new Appender
  (e.g. DatabaseAppender) or Formatter should require no changes to
  Logger, LogManager, or existing appenders.
* Logging should add minimal latency to the calling thread; slow I/O
  (e.g. disk/network writes) should optionally be done asynchronously.
* The system should not lose log messages under normal operating load,
  and should fail gracefully (never throw/crash the host application)
  if a destination is temporarily unavailable.

---

# Core Entities

* LogMessage
* LogLevel (enum)
* Logger
* LoggerConfig
* Appender (Console, File, Async-decorated, ...)
* LogFormatter (Simple, Json)
* LogManager

---

# Entities and Their Relationships

## LogLevel (enum)

TRACE;
DEBUG;
INFO;
WARN;
ERROR;
FATAL;

int severity; // used for threshold comparisons

---

## LogMessage

LogLevel level;
String loggerName;
String message;
Instant timestamp;
String threadName;
Throwable throwable; // optional

Immutable; constructed via a nested Builder.

---

## LogFormatter (interface) — Strategy

String format(LogMessage logMessage);

Implementations: SimpleFormatter (plain text), JsonFormatter (JSON line).

---

## Appender (interface) — Strategy

void append(LogMessage logMessage);
void setFormatter(LogFormatter formatter);
void setThreshold(LogLevel level);
LogLevel getThreshold();
void close();

Implementations:
* AbstractAppender — template-method base (checks threshold, calls formatter,
  delegates raw write to subclass).
    * ConsoleAppender — writes to System.out.
    * FileAppender — writes to a file via BufferedWriter.
* AsyncAppender — Decorator wrapping any Appender; pushes messages onto an
  in-memory BlockingQueue and writes them from a single background thread,
  so the calling thread never blocks on slow I/O.

---

## LoggerConfig

LogLevel level; // minimum severity this config accepts
List<Appender> appenders; // destinations messages are routed to

---

## Logger

String name;
LoggerConfig config;

void trace(String message);
void debug(String message);
void info(String message);
void warn(String message);
void error(String message);
void error(String message, Throwable t);
void fatal(String message, Throwable t);

Builds a LogMessage and forwards it to every Appender in its LoggerConfig,
after checking the message's level against the config's threshold.

---

## LogManager — Singleton + Factory

ConcurrentHashMap<String, Logger> loggerRegistry;
LoggerConfig defaultConfig;

static LogManager getInstance();
void setDefaultConfig(LoggerConfig config);
Logger getLogger(Class<?> clazz);
Logger getLogger(String name);
void shutdown(); // flush/close all appenders

---

# Relationships Summary

* LogManager (1) —creates/caches→ (many) Logger
* Logger (many) —share→ (1) LoggerConfig
* LoggerConfig (1) —routes to→ (many) Appender
* Appender (1) —uses→ (1) LogFormatter
* Appender (1) —consumes→ (many) LogMessage
* AsyncAppender (1) —decorates→ (1) Appender (Decorator pattern)

---

# Design Patterns Used

* **Singleton** — LogManager (single global access point).
* **Factory** — LogManager.getLogger(...) creates/caches Logger instances.
* **Strategy** — Appender and LogFormatter are interchangeable strategies.
* **Template Method** — AbstractAppender fixes the "check threshold → format
  → write" skeleton; subclasses only implement the write step.
* **Decorator** — AsyncAppender wraps any Appender to add asynchronous
  behavior without changing its interface or the wrapped class.
* **Builder** — LogMessage.Builder for readable, optional-field construction.