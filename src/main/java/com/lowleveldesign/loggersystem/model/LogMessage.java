package com.lowleveldesign.loggersystem.model;

import com.lowleveldesign.loggersystem.enums.LogLevel;

import java.time.Instant;

public final class LogMessage {

    private final LogLevel level;
    private final String loggerName;
    private final String message;
    private final Instant timestamp;
    private final String threadName;
    private final Throwable throwable;

    private LogMessage(Builder builder) {
        this.level = builder.level;
        this.loggerName = builder.loggerName;
        this.message = builder.message;
        this.timestamp = builder.timestamp;
        this.threadName = builder.threadName;
        this.throwable = builder.throwable;
    }

    public LogLevel getLevel() { return level; }
    public String getLoggerName() { return loggerName; }
    public String getMessage() { return message; }
    public Instant getTimestamp() { return timestamp; }
    public String getThreadName() { return threadName; }
    public Throwable getThrowable() { return throwable; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {

        private LogLevel level;
        private String loggerName;
        private String message;
        private Instant timestamp = Instant.now();
        private String threadName = Thread.currentThread().getName();
        private Throwable throwable;

        public Builder level(LogLevel level) { this.level = level; return this; }
        public Builder loggerName(String loggerName) { this.loggerName = loggerName; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        public Builder threadName(String threadName) { this.threadName = threadName; return this; }
        public Builder throwable(Throwable throwable) { this.throwable = throwable; return this; }

        public LogMessage build() { return new LogMessage(this); }
    }
}
