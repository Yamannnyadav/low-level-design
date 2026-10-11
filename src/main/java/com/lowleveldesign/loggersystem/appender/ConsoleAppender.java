package com.lowleveldesign.loggersystem.appender;

public class ConsoleAppender extends AbstractAppender {

    private final Object lock = new Object();

    @Override
    protected void doAppend(String formattedMessage) {
        synchronized (lock) {
            System.out.println(formattedMessage);
        }
    }

    @Override
    public void close() { }
}
