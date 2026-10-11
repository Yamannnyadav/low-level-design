package com.lowleveldesign.loggersystem.execute;

import com.lowleveldesign.loggersystem.appender.AsyncAppender;
import com.lowleveldesign.loggersystem.appender.ConsoleAppender;
import com.lowleveldesign.loggersystem.appender.FileAppender;
import com.lowleveldesign.loggersystem.config.LoggerConfig;
import com.lowleveldesign.loggersystem.core.Logger;
import com.lowleveldesign.loggersystem.enums.LogLevel;
import com.lowleveldesign.loggersystem.formatter.JsonFormatter;
import com.lowleveldesign.loggersystem.manager.LogManager;

public class ExecuteLoggerSystem {

    public static void main(String[] args) throws InterruptedException {

        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setThreshold(LogLevel.DEBUG);

        FileAppender fileAppender = new FileAppender("application.log");
        fileAppender.setFormatter(new JsonFormatter());
        fileAppender.setThreshold(LogLevel.INFO);
        AsyncAppender asyncFileAppender = new AsyncAppender(fileAppender, 1000);

        LoggerConfig rootConfig = new LoggerConfig(LogLevel.DEBUG);
        rootConfig.addAppender(consoleAppender);
        rootConfig.addAppender(asyncFileAppender);
        LogManager.getInstance().setDefaultConfig(rootConfig);

        Logger logger = LogManager.getInstance().getLogger(ExecuteLoggerSystem.class);
        logger.debug("Application starting up");
        logger.info("Loaded configuration successfully");

        Runnable task = () -> {
            Logger workerLogger = LogManager.getInstance().getLogger("WorkerThread");
            for (int i = 0; i < 5; i++) {
                workerLogger.info("Processing item " + i);
            }
        };

        Thread worker1 = new Thread(task, "Worker-1");
        Thread worker2 = new Thread(task, "Worker-2");
        worker1.start();
        worker2.start();
        worker1.join();
        worker2.join();

        try {
            throw new RuntimeException("Simulated failure");
        } catch (RuntimeException e) {
            logger.error("Something went wrong", e);
        }

        logger.info("Application shutting down");
        LogManager.getInstance().shutdown();
    }
}
