package com.flatrental.infrastructure.executor;

import java.text.MessageFormat;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class AbortPolicyWithMessage implements RejectedExecutionHandler {

    private final String message;
    private static final String REJECTION_MSG = "Task {0} was rejected from {1}.";

    public AbortPolicyWithMessage(String message) {
        this.message = message;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        String exceptionMessage = getRejectionInfo(r, executor) + " " + message;
        throw new RejectedExecutionException(exceptionMessage);
    }

    private String getRejectionInfo(Runnable r, ThreadPoolExecutor executor) {
        return MessageFormat.format(REJECTION_MSG, r.toString(), executor.toString());
    }

}
