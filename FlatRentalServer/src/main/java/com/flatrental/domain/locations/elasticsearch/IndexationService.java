package com.flatrental.domain.locations.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrental.infrastructure.query.BatchStreamQueryRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class IndexationService {

    private static final int BATCH_SIZE = 1000;
    private static final int BUFFER_CAPACITY = 10;
    private static final int CONSUMERS_NUMBER = 5;

    private final BatchStreamQueryRepository batchStreamQueryRepository;
    private final RestHighLevelClient elasticClient;
    private final ObjectMapper objectMapper;
    @Qualifier("indexationExecutor")
    private final AsyncTaskExecutor indexationExecutor;

    public <T, R> void startIndexation(Index index, Class<T> entityClass, Function<T, R> mapper) {
        BlockingQueue<List<T>> buffer = new ArrayBlockingQueue(BUFFER_CAPACITY);
        Producer<List<T>> entityLoader = getEntityLoader(entityClass, buffer);
        List<Consumer<List<T>>> entityIndexers = getEntityIndexers(index, mapper, buffer, entityLoader);
        ProducerConsumerTask<List<T>> indexationTask = new ProducerConsumerTask<>(Collections.singletonList(entityLoader), entityIndexers);
        indexationTask.runAsync(indexationExecutor);
    }

    private <T> Producer<List<T>> getEntityLoader(Class<T> entityClass, BlockingQueue<List<T>> buffer) {
        return new EntityBatchLoader<T>(entityClass, BATCH_SIZE, batchStreamQueryRepository, buffer);
    }

    private <T, R> List<Consumer<List<T>>> getEntityIndexers(Index index, Function<T, R> mapper, BlockingQueue<List<T>> buffer, Producer<List<T>> producer) {
        return Stream.generate(() -> getEntityIndexer(index, mapper, buffer, producer))
                .limit(CONSUMERS_NUMBER)
                .collect(Collectors.toList());
    }

    private <T, R> Consumer<List<T>> getEntityIndexer(Index index, Function<T, R> mapper, BlockingQueue<List<T>> buffer, Producer<List<T>> producer) {
        return new EntityBatchIndexer<>(index, mapper, elasticClient, objectMapper, buffer, Collections.singletonList(producer));
    }

}
