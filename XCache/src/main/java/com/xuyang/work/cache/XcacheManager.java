package com.xuyang.work.cache;

import com.google.common.collect.ImmutableMap;
import com.xuyang.work.cache.conf.XcacheConf;
import com.xuyang.work.cache.invoker.LocalCacheInvoker;
import com.xuyang.work.cache.invoker.RedisInvoker;
import com.xuyang.work.cache.invoker.XcacheInvoker;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Map;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 11:02
 * @Description:
 */
public class XcacheManager {

    private static Map<XcacheType, XcacheInvoker> type2Invoker = ImmutableMap.of(XcacheType.LOCAL, new LocalCacheInvoker(), XcacheType.REDIS, new RedisInvoker());

    public static XcacheInstance get(ProceedingJoinPoint pjp) throws Throwable {
        if (null == pjp) {
            throw new IllegalArgumentException("methodInvocation can not be null");
        }

        XcacheConf conf = XcacheHelper.getCacheConf(pjp);
        if (null == conf) {
            throw new IllegalStateException(String.format("can not find XcacheConf annotation on method{%s}", XcacheHelper.getMethodName(conf.getMethod())));
        }

        XcacheInvoker invoker = type2Invoker.get(conf.getXcacheType());
        if (null == invoker) {
            throw new IllegalStateException("now support cache type: " + conf.getXcacheType());
        }

        XcacheModelConf modleConf = XcacheHelper.getCacheModelConf(conf.getMethod().getReturnType(), true);
        if (null == modleConf) {
            throw new IllegalStateException("cacheModelConf can not be null");
        }

        String cacheInstanceKey = XcacheHelper.getCacheInstanceKey(conf, pjp);
        return invoker.get(pjp, conf, modleConf, cacheInstanceKey);
    }
}