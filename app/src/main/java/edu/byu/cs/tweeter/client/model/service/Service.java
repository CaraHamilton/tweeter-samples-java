package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Service<T> {

    protected Service() {}

    public void executeSingleThread(T task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute((Runnable) task);
    }

    public void executeFixedThread(T task) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute((Runnable) task);
    }
}
