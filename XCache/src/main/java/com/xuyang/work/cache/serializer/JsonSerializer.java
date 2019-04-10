package com.xuyang.work.cache.serializer;

import com.alibaba.fastjson.JSON;
import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.conf.XcacheConf;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 20:25
 * @Description:
 */
public class JsonSerializer implements CacheSerializer {

    @Override
    public <T> String serialize(XcacheInstance<T> value, XcacheConf xcacheConf) throws Exception {
        if (value == null) {
            throw new IllegalArgumentException("value can't be null");
        }
        return JSON.toJSONString(value);
    }

    @Override
    public <T> XcacheInstance<T> deserialize(String value, XcacheConf xcacheConf) throws Exception {
        if (xcacheConf == null) {
            throw new IllegalArgumentException("xcacheConf can't be null");
        }

        if (value == null) {
            return null;
        }

        XcacheInstance cache = JSON.parseObject(value, XcacheInstance.class);
        if (cache.getT() != null) {
            T t = JSON.parseObject(JSON.toJSONString(cache.getT()), xcacheConf.getReturnType());
            cache.setT(t);
        }
        return cache;
    }
}