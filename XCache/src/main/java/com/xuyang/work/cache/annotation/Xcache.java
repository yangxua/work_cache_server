package com.xuyang.work.cache.annotation;

import com.xuyang.work.cache.SerializeType;
import com.xuyang.work.cache.XcacheType;

import java.lang.annotation.*;

/**
 * @Auther: allanyang
 * @Date: 2019/4/8 20:35
 * @Description:
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Xcache {

    int DEFAULT_MAX_SIZE = 100;

    /**
     * 缓存类型
     */
    XcacheType type() default XcacheType.LOCAL;

    /**
     * 序列化类型
     */
    SerializeType serializeType() default SerializeType.JSON;

    /**
     * 缓存版本，默认1.0.0
     */
    String version() default "1.0.0";

    /**
     * 缓存前缀，区分不同缓存，全局唯一
     */
    String prefix();

    /**
     * 预警时间，单为秒
     */
    int alarmTime();

    /**
     * 缓存时间（expire）
     * @return
     */
    int time();

    /**
     * 获取方法锁时间，建议比业务方法时间短一些
     * @return
     */
    long lockmillis() default 0L;

    /**
     * 防止雪球
     * @return
     */
    boolean cacheNull() default false;

    /**
     * method最大缓存个数，值对local有效
     * @return
     */
    int maxSize() default DEFAULT_MAX_SIZE;
}