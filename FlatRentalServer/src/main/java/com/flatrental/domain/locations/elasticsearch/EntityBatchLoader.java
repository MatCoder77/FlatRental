package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.infrastructure.exception.ThrowingConsumer;
import com.flatrental.infrastructure.query.BatchStreamQueryRepository;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class EntityBatchLoader<T> extends Producer<List<T>> {

    private final Class<T> entityClass;
    private final int batchSize;
    private final BatchStreamQueryRepository batchStreamQueryRepository;

    public EntityBatchLoader(Class<T> entityClass,
                             int batchSize,
                             BatchStreamQueryRepository batchStreamQueryRepository,
                             BlockingQueue<List<T>> buffer) {
        super(buffer);
        this.entityClass = entityClass;
        this.batchSize = batchSize;
        this.batchStreamQueryRepository = batchStreamQueryRepository;
    }

    @Override
    protected void produce() {
        try(Stream<List<T>> queryStream = batchStreamQueryRepository.getBatchedResultStream(entityClass, batchSize)) {
            queryStream.forEach(ThrowingConsumer.wrapper(buffer::put));
        }
    }

}
