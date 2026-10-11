package com.lowleveldesign.loggersystem.formatter;

import com.lowleveldesign.loggersystem.model.LogMessage;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SimpleFormatter implements LogFormatter {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault());

    @Override
    public String format(LogMessage logMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append(TIMESTAMP_FORMAT.format(logMessage.getTimestamp()))
                .append(" [").append(logMessage.getThreadName()).append("] ")
                .append(logMessage.getLevel()).append(" ")
                .append(logMessage.getLoggerName()).append(" - ")
                .append(logMessage.getMessage());

        if (logMessage.getThrowable() != null) {
            sb.append(System.lineSeparator());
            for (StackTraceElement element : logMessage.getThrowable().getStackTrace()) {
                sb.append("\tat ").append(element).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
