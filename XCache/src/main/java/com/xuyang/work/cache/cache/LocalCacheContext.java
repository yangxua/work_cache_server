package com.xuyang.work.cache.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Maps;
import com.xuyang.work.cache.XcacheHelper;
import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.XcacheModelConf;
import com.xuyang.work.cache.conf.XcacheConf;
import com.xuyang.work.cache.utils.SpringHelpUtil;

import java.util.Map;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 10:38
 * @Description:
 */
public class LocalCacheContext {

    private static final Map<XcacheConf, LocalCache<String, XcacheInstance>> conf2Cache = Maps.newConcurrentMap();

    public static LocalCache<String, XcacheInstance> getLocalCache(final XcacheConf conf, final XcacheModelConf modelConf) {
        if (!conf2Cache.containsKey(conf)) {
            synchronized (LocalCacheContext.class) {
                if (!conf2Cache.containsKey(conf)) {
                    conf2Cache.put(conf, new LocalCache<>(conf.getMaxSize(), conf.getAlarmSeconds(), conf.getSeconds(), new CacheLoader<String, XcacheInstance>() {
                        @Override
                        public XcacheInstance load(String key) throws Exception {
                            Object[] args = XcacheHelper.parseParam(conf, key);
                            Object t = conf.getMethod().invoke(SpringHelpUtil.getBean(conf.getMethod().getDeclaringClass()), args);
                            return new XcacheInstance(t, modelConf, conf, key);
                        }
                    }));
                }
            }
        }

        return conf2Cache.get(conf);
    }
}