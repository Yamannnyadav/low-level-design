package com.lowleveldesign.loggersystem.appender;

import com.lowleveldesign.loggersystem.enums.LogLevel;
import com.lowleveldesign.loggersystem.formatter.LogFormatter;
import com.lowleveldesign.loggersystem.formatter.SimpleFormatter;
import com.lowleveldesign.loggersystem.model.LogMessage;

public abstract class AbstractAppender implements Appender {

    protected volatile LogFormatter formatter = new SimpleFormatter();
    protected volatile LogLevel threshold = LogLevel.TRACE;

    @Override
    public void setFormatter(LogFormatter formatter) { this.formatter = formatter; }

    @Override
    public void setThreshold(LogLevel level) { this.threshold = level; }

    @Override
    public LogLevel getThreshold() { return threshold; }

    @Override
    public final void append(LogMessage logMessage) {
        if (logMessage.getLevel().isGreaterOrEqual(threshold)) {
            doAppend(formatter.format(logMessage));
        }
    }

    protected abstract void doAppend(String formattedMessage);
}
