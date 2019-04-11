package com.xuyang.work.cache.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xuyang.work.cache.utils.NumberUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 09:54
 * @Description:
 */
public class LocalCache<String, XcacheInstance> {

    private static final int DEFAULT_MAX_SIZE = 100;
    private LoadingCache<String, XcacheInstance> loadingCache;

    public LocalCache(int maxSize, int alarmTime, int expireTime, CacheLoader<String, XcacheInstance> cacheLoader) {
        this.loadingCache = CacheBuilder.newBuilder().maximumSize(NumberUtil.nullOrZero(maxSize) ? DEFAULT_MAX_SIZE : maxSize).refreshAfterWrite(alarmTime, TimeUnit.SECONDS).expireAfterAccess(expireTime, TimeUnit.SECONDS).softValues().build(cacheLoader);
    }

    public void put(String key, XcacheInstance xcacheInstance) throws ExecutionException {
        loadingCache.put(key, xcacheInstance);
    }

    public XcacheInstance get(String key) throws ExecutionException {
        return loadingCache.getIfPresent(key);
    }

    public void clear(String key) {
        loadingCache.invalidate(key);
    }
}