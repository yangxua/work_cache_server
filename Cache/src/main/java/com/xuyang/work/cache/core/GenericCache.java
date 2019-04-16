package com.xuyang.work.cache.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: allanyang
 * @Date: 2019/4/16 15:23
 * @Description:
 */
public abstract class GenericCache<T> {

    private Map<String, T> cacheMap;

    public GenericCache() {
        if (null == cacheMap) {
            cacheMap = new ConcurrentHashMap<>();
        }
    }

    /**
     * 重新加载缓存
     */
    protected abstract void load();

    /**
     * 加入缓存
     */
    protected void put(String key, T val) {
        cacheMap.put(key, val);
    }

    /**
     * 从缓存中获取
     */
    protected T get(String key) {
        return cacheMap.get(key);
    }

    /**
     * 清空缓存
     */
    protected void clear() {
        cacheMap.clear();
    }

    /**
     * 移除某个缓存
     */
    protected void remove(String key) {
        cacheMap.remove(key);
    }

    /**
     * 获取所有缓存map
     */
    protected Map<String, T> getAll() {
        Map<String, T> res = new HashMap<>();

        for (Map.Entry<String, T> entry : cacheMap.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }

        return res;
    }

    /**
     * 获取所有值
     */
    protected List<T> getAllList() {
        List<T> res = new ArrayList<>();

        for (Map.Entry<String,T> entry : cacheMap.entrySet()) {
            res.add(entry.getValue());
        }

        return res;
    }

    /**
     * 获取所有key
     */
    protected List<String> key() {
        List<String> res = new ArrayList<>();

        for (Map.Entry<String, T> entry : cacheMap.entrySet()) {
            res.add(entry.getKey());
        }

        return res;
    }
}