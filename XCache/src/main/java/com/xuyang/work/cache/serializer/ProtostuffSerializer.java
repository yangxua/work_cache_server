package com.xuyang.work.cache.serializer;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.conf.XcacheConf;
import com.xuyang.work.cache.utils.ProtoStuffUtil;

/**
 * @Auther: allanyang
 * @Date: 2019/4/10 11:04
 * @Description:
 */
public class ProtoStuffSerializer implements CacheSerializer {

    @Override
    public <T> String serialize(XcacheInstance<T> value, XcacheConf xcacheConf) throws Exception {
        if (value == null) {
            throw new IllegalArgumentException("value can't be null");
        }
        return new String(ProtoStuffUtil.serialize(value), Charsets.ISO_8859_1.name());
    }

    @Override
    public <T> XcacheInstance<T> deserialize(String value, XcacheConf xcacheConf) throws Exception {
        if (xcacheConf == null) {
            throw new IllegalArgumentException("xcacheConf can't be null");
        }

        if (value == null) {
            return null;
        }

        XcacheInstance<T> cache = ProtoStuffUtil.deserialize(value.getBytes(Charsets.ISO_8859_1.name()), XcacheInstance.class);
        if (cache.getT() != null) {
            T t = JSON.parseObject(JSON.toJSONString(cache.getT()), xcacheConf.getReturnType());
            cache.setT(t);
        }
        return cache;
    }
}