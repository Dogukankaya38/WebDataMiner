package org.example.task;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ScheduledTask extends Task {
    private static final Logger LOGGER = Logger.getLogger(ScheduledTask.class.getName());

    private final int minDelay;
    private final int maxDelay;
    private long lastWorkTime = 0;

    public ScheduledTask(int minDelay, int maxDelay) {
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }

    @Override
    public boolean isTimeTick() {
        long currentTime = System.currentTimeMillis();
        if (lastWorkTime == 0) {
            lastWorkTime = currentTime;
            LOGGER.log(Level.INFO, "Initial work time set at {0}", currentTime);
            return true;
        }

        int delayInSeconds = calculateRandomDelay();
        long delayInMillis = delayInSeconds * 1000L;

        if (currentTime - lastWorkTime > delayInMillis) {
            lastWorkTime = currentTime;
            LOGGER.log(Level.INFO, "Scheduled task time tick. Next delay: {0} seconds", delayInSeconds);
            return true;
        }

        LOGGER.log(Level.FINE, "Task not yet ready to run. Current time: {0}, Last work time: {1}, Delay: {2} seconds",
                new Object[]{currentTime, lastWorkTime, delayInSeconds});
        return false;
    }

    private int calculateRandomDelay() {
        Random random = new Random();
        int delay = random.nextInt(maxDelay - minDelay + 1) + minDelay;
        LOGGER.log(Level.INFO, "Calculated random delay: {0} seconds", delay);
        return delay;
    }
}



