package ru.otus.javabootcamp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutor {
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  private final Runnable task;
  private final int periodInSeconds;

  public ScheduledExecutor(Runnable task, int periodInSeconds){
  this.task = task;
  this.periodInSeconds = periodInSeconds;
  }

  public void start(){
    scheduler.scheduleAtFixedRate(task, periodInSeconds, periodInSeconds, TimeUnit.SECONDS);
  }
}
