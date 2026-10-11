package com.lowleveldesign.loggersystem.config;

import com.lowleveldesign.loggersystem.appender.Appender;
import com.lowleveldesign.loggersystem.enums.LogLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoggerConfig {

    private volatile LogLevel level;
    private final List<Appender> appenders = new ArrayList<>();

    public LoggerConfig(LogLevel level) { this.level = level; }

    public LogLevel getLevel() { return level; }
    public void setLevel(LogLevel level) { this.level = level; }

    public synchronized void addAppender(Appender appender) { appenders.add(appender); }

    public synchronized List<Appender> getAppenders() {
        return Collections.unmodifiableList(new ArrayList<>(appenders));
    }
}
