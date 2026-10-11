package com.lowleveldesign.loggersystem.manager;

import com.lowleveldesign.loggersystem.appender.Appender;
import com.lowleveldesign.loggersystem.config.LoggerConfig;
import com.lowleveldesign.loggersystem.core.Logger;
import com.lowleveldesign.loggersystem.enums.LogLevel;

import java.util.concurrent.ConcurrentHashMap;

public class LogManager {

    private static volatile LogManager instance;

    private final ConcurrentHashMap<String, Logger> loggerRegistry = new ConcurrentHashMap<>();
    private volatile LoggerConfig defaultConfig = new LoggerConfig(LogLevel.INFO);

    private LogManager() { }

    public static LogManager getInstance() {
        if (instance == null) {
            synchronized (LogManager.class) {
                if (instance == null) instance = new LogManager();
            }
        }
        return instance;
    }

    public void setDefaultConfig(LoggerConfig config) { this.defaultConfig = config; }

    public Logger getLogger(Class<?> clazz) { return getLogger(clazz.getName()); }

    public Logger getLogger(String name) {
        return loggerRegistry.computeIfAbsent(name, n -> new Logger(n, defaultConfig));
    }

    public void shutdown() {
        for (Appender appender : defaultConfig.getAppenders()) {
            appender.close();
        }
    }
}
