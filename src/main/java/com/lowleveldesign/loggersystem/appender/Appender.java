package com.lowleveldesign.loggersystem.appender;

import com.lowleveldesign.loggersystem.enums.LogLevel;
import com.lowleveldesign.loggersystem.formatter.LogFormatter;
import com.lowleveldesign.loggersystem.model.LogMessage;

public interface Appender {

    void append(LogMessage logMessage);
    void setFormatter(LogFormatter formatter);
    void setThreshold(LogLevel level);
    LogLevel getThreshold();
    void close();

}

