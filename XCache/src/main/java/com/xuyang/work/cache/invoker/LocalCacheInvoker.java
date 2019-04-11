package com.xuyang.work.cache.invoker;

import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.XcacheModelConf;
import com.xuyang.work.cache.cache.LocalCache;
import com.xuyang.work.cache.cache.LocalCacheContext;
import com.xuyang.work.cache.conf.XcacheConf;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 16:37
 * @Description:
 */
public class LocalCacheInvoker extends AbstractXcacheInvoker {


    @Override
    protected <T> XcacheInstance<T> doGet(boolean master, XcacheConf conf, XcacheModelConf modelConf, String key) throws Exception {
        LocalCache<String, XcacheInstance> localCache = LocalCacheContext.getLocalCache(conf, modelConf);
        return (XcacheInstance<T>)localCache.get(key);
    }

    @Override
    public <T> void set(XcacheConf conf, XcacheModelConf modelConf, String key, XcacheInstance<T> cache) throws Exception {
        LocalCache<String, XcacheInstance> localCache = LocalCacheContext.getLocalCache(conf, modelConf);
        localCache.put(key, cache);
    }

    @Override
    public void clear(XcacheConf conf, XcacheModelConf modelConf, String key) {
        LocalCache<String, XcacheInstance> localCache = LocalCacheContext.getLocalCache(conf, modelConf);
        localCache.clear(key);
    }
}