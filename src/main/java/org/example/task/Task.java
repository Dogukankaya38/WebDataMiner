package org.example.task;

import org.openqa.selenium.WebDriver;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Task {
    private static final Logger LOGGER = Logger.getLogger(Task.class.getName());

    public abstract void doJob(WebDriver driver);

    public boolean isTimeTick() {
        return true;
    }

    public void delay(int delayInSeconds) {
        try {
            Thread.sleep(delayInSeconds * 1000L);
            LOGGER.log(Level.INFO, "Delayed for {0} seconds", delayInSeconds);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "InterruptedException occurred during delay", e);
        }
    }
}
