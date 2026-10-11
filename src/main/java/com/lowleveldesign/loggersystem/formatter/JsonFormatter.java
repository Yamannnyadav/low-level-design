package com.lowleveldesign.loggersystem.formatter;

import com.lowleveldesign.loggersystem.model.LogMessage;

public class JsonFormatter implements LogFormatter {

    @Override
    public String format(LogMessage logMessage) {
        return "{"
                + "\"timestamp\":\"" + logMessage.getTimestamp() + "\","
                + "\"level\":\"" + logMessage.getLevel() + "\","
                + "\"thread\":\"" + logMessage.getThreadName() + "\","
                + "\"logger\":\"" + logMessage.getLoggerName() + "\","
                + "\"message\":\"" + escape(logMessage.getMessage()) + "\""
                + "}";
    }

    private String escape(String input) {
        return input == null ? "" : input.replace("\"", "\\\"");
    }
}
