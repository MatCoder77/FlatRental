package com.flatrental.domain.locations.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public abstract class Consumer<T> {

    protected final BlockingQueue<? extends T> buffer;
    private final Collection<? extends Producer<? extends T>> producers;

    public Future<?> startAsync(AsyncTaskExecutor executor) {
        return executor.submit(this::consume);
    }

    public void start() {
        consume();
    }

    abstract protected void consume();

    public boolean isAnyProducerRunning() {
        return producers.stream()
                .anyMatch(Producer::isRunning);
    }

}
