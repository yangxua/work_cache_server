package com.xuyang.work.cache.serializer;

import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.conf.XcacheConf;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 20:14
 * @Description:
 */
public interface CacheSerializer {

    /**
     * 序列化
     */
    <T> String serialize(XcacheInstance<T> value, XcacheConf xcacheConf) throws Exception;

    /**
     * 反序列化
     */
    <T> XcacheInstance<T> deserialize(String value, XcacheConf xcacheConf) throws Exception;
}