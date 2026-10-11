package com.lowleveldesign.loggersystem.core;

import com.lowleveldesign.loggersystem.appender.Appender;
import com.lowleveldesign.loggersystem.config.LoggerConfig;
import com.lowleveldesign.loggersystem.enums.LogLevel;
import com.lowleveldesign.loggersystem.model.LogMessage;

public class Logger {

    private final String name;
    private final LoggerConfig config;

    public Logger(String name, LoggerConfig config) {
        this.name = name;
        this.config = config;
    }

    public void trace(String message) { log(LogLevel.TRACE, message, null); }
    public void debug(String message) { log(LogLevel.DEBUG, message, null); }
    public void info(String message) { log(LogLevel.INFO, message, null); }
    public void warn(String message) { log(LogLevel.WARN, message, null); }
    public void error(String message) { log(LogLevel.ERROR, message, null); }
    public void error(String message, Throwable throwable) { log(LogLevel.ERROR, message, throwable); }
    public void fatal(String message, Throwable throwable) { log(LogLevel.FATAL, message, throwable); }

    private void log(LogLevel level, String message, Throwable throwable) {

        if (!level.isGreaterOrEqual(config.getLevel())) return;

        LogMessage logMessage = LogMessage.builder()
                .level(level)
                .loggerName(name)
                .message(message)
                .throwable(throwable)
                .build();

        for (Appender appender : config.getAppenders()) {
            appender.append(logMessage);
        }
    }
}
