package com.flatrental.domain.locations.elasticsearch;

import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Producer<T> {

    protected final BlockingQueue<? super T> buffer;
    protected AtomicBoolean isRunning = new AtomicBoolean(false);

    public Producer(BlockingQueue<? super T> buffer) {
        this.buffer = buffer;
    }

    public Future<?> startAsync(AsyncTaskExecutor executor) {
        setRunning(true);
        return executor.submit(this::start);
    }

    public void start() {
        setRunning(true);
        produce();
        setRunning(false);
    }

    abstract protected void produce();

    public boolean isRunning() {
        return isRunning.get();
    }

    public void setRunning(boolean isRunning) {
        this.isRunning.set(isRunning);
    }

}
