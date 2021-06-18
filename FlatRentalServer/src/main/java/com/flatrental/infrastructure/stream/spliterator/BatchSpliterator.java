package com.flatrental.infrastructure.stream.spliterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;

public class BatchSpliterator<T> implements Spliterator<List<T>> {

    private final Spliterator<T> sourceSpliterator;
    private final int batchSize;

    public BatchSpliterator(Spliterator<T> sourceSpliterator, int batchSize) {
        Objects.requireNonNull(sourceSpliterator);
        validateIfBatchSizeIsPositive(batchSize);
        this.sourceSpliterator = sourceSpliterator;
        this.batchSize = batchSize;
    }

    private void validateIfBatchSizeIsPositive(int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Batch size have to be positive integer.");
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> action) {
        List<T> batch = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize && sourceSpliterator.tryAdvance(batch::add); i++);
        if (!batch.isEmpty()) {
            action.accept(batch);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<List<T>> trySplit() {
        if (batchSize >= estimateSize()) {
            return null;
        }
        return Optional.ofNullable(sourceSpliterator.trySplit())
                .map(newSpliterator -> new BatchSpliterator<>(newSpliterator, batchSize))
                .orElse(null);
    }

    @Override
    public long estimateSize() {
        return calculateEstimatedBatchSize(sourceSpliterator.estimateSize());
    }

    private long calculateEstimatedBatchSize(long estimatedSourceSize) {
        if (estimatedSourceSize == 0) {
            return 0;
        }
        return (long) Math.ceil(estimatedSourceSize / (double) batchSize);
    }

    @Override
    public int characteristics() {
        return sourceSpliterator.characteristics() & ~SUBSIZED;
    }

}
