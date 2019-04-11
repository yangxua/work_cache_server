package com.xuyang.work.cache.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joeson Chan
 */
public class ApplicationThreadFactory implements ThreadFactory {

    private final AtomicInteger count = new AtomicInteger(0);

    private String flag;

    public ApplicationThreadFactory(String flag) {
        this.flag = flag;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "pool-" + flag + "-" + count.incrementAndGet());
    }
}