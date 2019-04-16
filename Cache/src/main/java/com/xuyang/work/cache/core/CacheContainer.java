package com.xuyang.work.cache.core;

import com.xuyang.work.cache.utils.SpringHelpUtil;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: allanyang
 * @Date: 2019/4/16 16:36
 * @Description:
 */
public class CacheContainer<T> {

    private Map<String, GenericCache<T>> cacheMap;
    private static CacheContainer INSTANCE;

    private CacheContainer() {}

    public static CacheContainer getInstance() {
        if (null == INSTANCE) {
            synchronized (CacheContainer.class) {
                if (null == INSTANCE) {
                    CacheContainer tmp = new CacheContainer();
                    tmp.init();
                    INSTANCE = tmp;
                }
            }
        }

        return INSTANCE;
    }

    private void init() {
        cacheMap = new ConcurrentHashMap<>();
        ApplicationContext ctx = SpringHelpUtil.getApplicationContext();
        String[] beanNames = ctx.getBeanNamesForType(GenericCache.class);

        for (String beanName : beanNames) {
            GenericCache cache = (GenericCache) SpringHelpUtil.getBean(beanName);
            String cacheName = String.format("%c%s", Character.toUpperCase(beanName.charAt(0)), beanName.substring(1));
            cacheMap.put(cacheName, cache);
        }

        for (Map.Entry<String, GenericCache<T>> entry : cacheMap.entrySet()) {
            entry.getValue().load();
        }
    }

    public GenericCache<T> getCahce(String cacheName) {
        return cacheMap.get(cacheName);
    }

    public T get(String cacheName, String key) {
        GenericCache<T> cache = cacheMap.get(cacheName);
        if (null != cache) {
            return cache.get(key);
        }

        return null;
    }

    public Map<String, T> getAll(String cacheName) {
        GenericCache<T> cache = cacheMap.get(cacheName);
        if (null != cache) {
            return cache.getAll();
        }
        return null;
    }

    public List<T> getAllList(String cacheName) {
        GenericCache<T> cache = cacheMap.get(cacheName);
        if (null != cache) {
            return cache.getAllList();
        }

        return null;
    }
}