package com.lowleveldesign.loggersystem.enums;

public enum LogLevel {

    TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);

    private final int severity;

    LogLevel(int severity) { this.severity = severity; }

    public int getSeverity() { return severity; }

    public boolean isGreaterOrEqual(LogLevel other) {
        return this.severity >= other.severity;
    }
}
