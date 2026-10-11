package com.lowleveldesign.loggersystem.appender;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileAppender extends AbstractAppender {

    private final BufferedWriter writer;
    private final Object lock = new Object();

    public FileAppender(String filePath) {
        try {
            this.writer = new BufferedWriter(new FileWriter(filePath, true));
        } catch (IOException e) {
            throw new RuntimeException("Unable to open log file: " + filePath, e);
        }
    }

    @Override
    protected void doAppend(String formattedMessage) {
        synchronized (lock) {
            try {
                writer.write(formattedMessage);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("Failed to write log message: " + e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        synchronized (lock) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Failed to close log file: " + e.getMessage());
            }
        }
    }
}
