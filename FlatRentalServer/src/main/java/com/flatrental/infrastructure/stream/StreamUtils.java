package com.flatrental.infrastructure.stream;

import com.flatrental.infrastructure.stream.spliterator.BatchSpliterator;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class StreamUtils {

    public static <T> Stream<List<T>> batchedStream(Stream<T> sourceStream, int batchSize) {
        return StreamSupport.stream(new BatchSpliterator<>(sourceStream.spliterator(), batchSize), false);
    }

}
