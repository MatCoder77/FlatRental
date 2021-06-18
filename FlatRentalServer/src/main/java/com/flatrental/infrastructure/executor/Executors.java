package com.flatrental.infrastructure.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class Executors {

    private static final int CORE_POOL_SIZE = 100;
    private static final int MAX_POOL_SIZE = 200;
    private static final int QUEUE_CAPACITY = 2000;
    private static final String REJECTION_MSG = "There are too many awaiting indexation tasks.";

    @Bean
    public AsyncTaskExecutor indexationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setRejectedExecutionHandler(new AbortPolicyWithMessage(REJECTION_MSG));
        executor.initialize();
        return executor;
    }

}
