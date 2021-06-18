package com.flatrental.domain.locations.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrental.infrastructure.exception.ThrowingConsumer;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class EntityBatchIndexer<T, R> extends Consumer<List<T>>{

    private static final Long TIMEOUT = 100L;

    private final Index targetIndex;
    private final Function<T, R> mapper;
    private final RestHighLevelClient elasticClient;
    private final ObjectMapper objectMapper;

    public EntityBatchIndexer(Index targetIndex,
                              Function<T, R> mapper,
                              RestHighLevelClient elasticClient,
                              ObjectMapper objectMapper,
                              BlockingQueue<List<T>> buffer,
                              Collection<? extends Producer<? extends List<T>>> producers) {
        super(buffer, producers);
        this.targetIndex = targetIndex;
        this.mapper = mapper;
        this.elasticClient = elasticClient;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void consume() {
        while (shouldContinueProcessing()) {
            try {
                Optional.ofNullable(buffer.poll(TIMEOUT, TimeUnit.MILLISECONDS))
                        .ifPresent(ThrowingConsumer.wrapper(entitiesToIndex -> indexObjects(entitiesToIndex, mapper)));
            } catch (Exception e) {
                log.error("Exception occurred during indexation: {}", e);
                throw new RuntimeException(e);
            }
        }
    }

    private boolean shouldContinueProcessing() {
        return isAnyProducerRunning() || !buffer.isEmpty();
    }

    private void indexObjects(List<T> entitiesToBeIndexed, Function<T, R> mapper) throws IOException {
        BulkRequest bulkRequest = entitiesToBeIndexed.stream()
                .map(mapper)
                .map(this::mapToIndexRequest)
                .collect(Collectors.collectingAndThen(Collectors.toList(), this::mapToBulkRequest));
        elasticClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    private BulkRequest mapToBulkRequest(Collection<IndexRequest> indexRequests) {
        return new BulkRequest()
                .add(indexRequests.toArray(IndexRequest[]::new))
                .timeout("5m");
    }

    private IndexRequest mapToIndexRequest(Object object) {
        try {
            String jsonSource = objectMapper.writeValueAsString(object);
            return new IndexRequest(targetIndex.getName())
                    .source(jsonSource, XContentType.JSON);
        } catch (JsonProcessingException e) {
            log.error("Error during json processing", e);
            throw new IllegalArgumentException("wrong");
        }
    }

}
