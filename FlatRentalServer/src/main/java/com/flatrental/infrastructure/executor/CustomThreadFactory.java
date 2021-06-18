package com.flatrental.infrastructure.executor;

import java.util.concurrent.ThreadFactory;

public class CustomThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnable) {
        return null;
    }

}
