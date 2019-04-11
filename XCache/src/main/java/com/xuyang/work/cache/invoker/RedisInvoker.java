package com.xuyang.work.cache.invoker;

import com.google.common.collect.ImmutableMap;
import com.xuyang.work.cache.SerializeType;
import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.XcacheModelConf;
import com.xuyang.work.cache.conf.XcacheConf;
import com.xuyang.work.cache.serializer.CacheSerializer;
import com.xuyang.work.cache.serializer.JavaSerializer;
import com.xuyang.work.cache.serializer.JsonSerializer;
import com.xuyang.work.cache.serializer.ProtoStuffSerializer;

import java.util.Map;

/**
 * redis 缓存执行器
 */
public class RedisInvoker extends AbstractXcacheInvoker {

    private static JsonSerializer jsonSerializer = new JsonSerializer();
    private static JavaSerializer javaSerializer = new JavaSerializer();
    private static ProtoStuffSerializer protoStuffSerializer = new ProtoStuffSerializer();

    private static Map<SerializeType, CacheSerializer> serializeType2Serializer = ImmutableMap.of(SerializeType.JSON, jsonSerializer,
            SerializeType.JAVA, javaSerializer,
            SerializeType.PROTO_STUFF, protoStuffSerializer);


    public RedisInvoker() {
        super();
    }

    @Override
    protected <T> XcacheInstance<T> doGet(boolean master, XcacheConf conf, XcacheModelConf modelConf, String key) throws Exception {
        return null;
    }

    @Override
    public <T> void set(XcacheConf conf, XcacheModelConf modelConf, String key, XcacheInstance<T> cache) throws Exception {

    }

    @Override
    public void clear(XcacheConf conf, XcacheModelConf modelConf, String key) {

    }

    /*@Override
    public <T> XcacheInstance<T> doGet(boolean master, XcacheConf conf, XcacheModelConf modelConf, String key) throws Exception {
        JedisProxy slave = master ? JedisProxy.getMasterCache(XcacheHelper.getRedisKey()) : JedisProxy.getSlaveCache(XcacheHelper.getRedisKey());
        String value = slave.get(key);
        if (null == value) {
            return null;
        }

        CacheSerializer serializer = serializeType2Serializer.get(conf.getSerializeType());
        if (null == serializer) {
            throw new IllegalStateException("illegal serialtype " + conf.getSerializeType());
        }

        try {
            return serializer.deserialize(value, conf);
        } catch (Exception e) {
            if (master) {
                clear(conf, modelConf, key);
            }
            throw new XcacheSerializeException("serialize error value{" + value + "} conf{" + conf + "}", e);
        }
    }

    @Override
    public <T> void set(XcacheConf conf, XcacheModelConf xcacheModelConf, String key, XcacheInstance<T> instance) throws Exception {
        if (null == instance) {
            return;
        }

        JedisProxy master = JedisProxy.getMasterCache(XcacheHelper.getRedisKey());
        CacheSerializer serializer = serializeType2Serializer.get(conf.getSerializeType());
        if (null == serializer) {
            throw new IllegalStateException("illegal serialtype " + conf.getSerializeType());
        }

        String value = serializer.serialize(instance, conf);
        if (null != value) {
            master.setex(key, conf.getSecodes(), value);
        }
    }

    @Override
    public void clear(XcacheConf conf, XcacheModelConf modelConf, String key) throws Exception {
        JedisProxy.getMasterCache(XcacheHelper.getRedisKey()).del(key);
    }*/
}
