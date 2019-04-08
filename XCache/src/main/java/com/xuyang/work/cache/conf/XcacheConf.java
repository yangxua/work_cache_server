package com.xuyang.work.cache.conf;

import com.xuyang.work.cache.SerializeType;
import com.xuyang.work.cache.XcacheType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @Auther: allanyang
 * @Date: 2019/4/8 21:06
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class XcacheConf {

    /**
     * 前缀
     */
    private String prefix;

    /**
     * Xcache版本号
     */
    private String version;

    /**
     * 过期时间
     */
    private int seconds;

    /**
     * 警告时间
     */
    private int alarmSeconds;

    /**
     * 争取锁的时间
     */
    private long lockTimeOutMills;

    /**
     * Xcache类型
     */
    private XcacheType xcacheType;

    /**
     * 序列化类型
     */
    private SerializeType serializeType;

    /**
     * 返回类型
     */
    private Type returnType;

    /**
     * 调用的方法
     */
    private Method method;

    /**
     * 缓存null
     */
    private boolean cacheNull;

    /**
     * 缓存key
     * 用:组装成的
     */
    private String preCacheKey;

    /**
     * localCache最大缓存个数
     */
    private int maxSize;
}