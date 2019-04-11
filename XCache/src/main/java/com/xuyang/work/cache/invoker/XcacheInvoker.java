package com.xuyang.work.cache.invoker;

import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.XcacheModelConf;
import com.xuyang.work.cache.conf.XcacheConf;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 11:32
 * @Description:
 */
public interface XcacheInvoker {

    /**
     * 加载缓存
     */
    <T> void set(XcacheConf conf, XcacheModelConf modelConf, String key, XcacheInstance<T> cache) throws Exception;

    /**
     * 获取缓存
     */
    <T> XcacheInstance<T> get(ProceedingJoinPoint pjp, XcacheConf conf, XcacheModelConf modelConf, String key) throws Throwable;

    /**
     * 清空缓存
     */
    void clear(XcacheConf conf, XcacheModelConf modelConf, String key);
}