package com.lowleveldesign.loggersystem.formatter;

import com.lowleveldesign.loggersystem.model.LogMessage;

public interface LogFormatter {
    String format(LogMessage logMessage);
}
