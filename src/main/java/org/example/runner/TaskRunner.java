package org.example.runner;

import org.example.task.Task;
import org.openqa.selenium.WebDriver;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskRunner extends Thread {
    private static final Logger LOGGER = Logger.getLogger(TaskRunner.class.getName());
    private static final Object LOCK = new Object();
    private final List<Task> queue = new LinkedList<>();
    private final WebDriver driver;

    public TaskRunner(WebDriver driver) {
        this.driver = driver;
        // Optionally set the thread as a daemon thread
        // setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task task = null;
                synchronized (LOCK) {
                    if (queue.isEmpty()) {
                        LOGGER.log(Level.INFO, "Queue is empty. Waiting for tasks.");
                        LOCK.wait();
                    }
                    task = queue.remove(0);
                }
                if (task != null) {
                    if (task.isTimeTick()) {
                        LOGGER.log(Level.INFO, "[TASK STARTED {0}]", task.getClass().getSimpleName());
                        task.doJob(driver);
                        LOGGER.log(Level.INFO, "[TASK ENDED {0}]", task.getClass().getSimpleName());
                    } else {
                        LOGGER.log(Level.INFO, "[Time is not ticked for {0}]", task.getClass().getSimpleName());
                        addTask(task);
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "TaskRunner thread interrupted", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in TaskRunner", e);
            } finally {
                _sleep();
            }
        }
    }

    private void _sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Sleep interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    public void addTask(Task task) {
        synchronized (LOCK) {
            queue.add(task);
            LOCK.notifyAll();
            LOGGER.log(Level.INFO, "Task added: {0}", task.getClass().getSimpleName());
        }
    }
}

