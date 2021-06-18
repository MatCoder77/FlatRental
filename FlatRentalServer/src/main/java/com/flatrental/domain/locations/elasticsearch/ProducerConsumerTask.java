package com.flatrental.domain.locations.elasticsearch;

import org.springframework.core.task.AsyncTaskExecutor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProducerConsumerTask<T> {

    private Collection<? extends Producer<? extends T>> producers;
    private Collection<? extends Consumer<? super T>> consumers;

    public ProducerConsumerTask(Collection<? extends Producer<? extends T>> producers, Collection<? extends Consumer<? super T>> consumers) {
        this.producers = producers;
        this.consumers = consumers;
    }

    public Future<?> runAsync(AsyncTaskExecutor executor) {
        List<? extends Future<?>> producerTasks = producers.stream()
                .map(producer -> producer.startAsync(executor))
                .collect(Collectors.toList());
        List<? extends Future<?>> consumerTasks = consumers.stream()
                .map(consumer -> consumer.startAsync(executor))
                .collect(Collectors.toList());
        List<? extends CompletableFuture<?>> allTasks = Stream.of(producerTasks, consumerTasks)
                .flatMap(Collection::stream)
                .map(this::makeCompletableFuture)
                .collect(Collectors.toList());
        return CompletableFuture.allOf(allTasks.toArray(CompletableFuture<?>[]::new));
    }

    private CompletableFuture<?> makeCompletableFuture(Future<?> future) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException| ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
