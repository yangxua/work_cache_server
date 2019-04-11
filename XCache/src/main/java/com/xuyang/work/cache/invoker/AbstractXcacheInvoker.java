package com.xuyang.work.cache.invoker;

import com.alibaba.fastjson.JSON;
import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.XcacheModelConf;
import com.xuyang.work.cache.conf.XcacheConf;
import com.xuyang.work.cache.exception.XcacheLockTimeoutException;
import com.xuyang.work.cache.exception.XcacheSerializeException;
import com.xuyang.work.cache.lock.CacheLoadLockHolder;
import com.xuyang.work.cache.thread.ApplicationThreadFactory;
import com.xuyang.work.cache.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 14:11
 * @Description:
 */
@Slf4j
public abstract class AbstractXcacheInvoker implements XcacheInvoker {

    /**
     * JOB专用executor
     */
    private final ExecutorService XCACHE_LOAD_EXECUTOR = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()-1, Runtime.getRuntime().availableProcessors()*2, 500L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ApplicationThreadFactory("xcache-load"));

    /**
     * 加载缓存
     */
    @Override
    public <T> XcacheInstance<T> get(ProceedingJoinPoint pjp, XcacheConf conf, XcacheModelConf modelConf, String key) throws Throwable {
        XcacheInstance<T> cache = get(false, conf, modelConf, key);
        if (cache == null) {
            try {
                CacheLoadLockHolder.lock(conf, key);
                return load(pjp, conf, modelConf, key);
            } catch (XcacheLockTimeoutException e) {
                log.warn("xcache time out, ProceedingJoinPoint ：" + pjp.getSignature());
                return returnLockTimeOutCache(conf, modelConf, key);
            } finally {
                CacheLoadLockHolder.unlock(conf, key);
            }
        } else {
            return returnFreshCache(pjp, conf, modelConf, key, cache);
        }
    }

    private <T> XcacheInstance<T> returnFreshCache(ProceedingJoinPoint pjp, final XcacheConf conf, final XcacheModelConf modelConf, final String key, XcacheInstance<T> cache) throws Throwable {
        if (cache == null) {
            return null;
        } else if (!isValid(cache, modelConf)) {
            if (new Date().before(new Date(cache.getTimeMillis())) && StringUtil.equals(cache.getModelSign(), modelConf.getSign())) {
                XCACHE_LOAD_EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        boolean lock = false;
                        try {
                            if (CacheLoadLockHolder.tryLock(conf, key)) {
                                load(pjp, conf, modelConf, key);
                            }
                        } catch (XcacheLockTimeoutException e) {
                            log.warn("xcache lock time out error, ProceedingJoinPoint ：" + pjp.getSignature());
                        } catch (Throwable t) {
                            log.error("xcache handler error, ProceedingJoinPoint ：" + pjp.getSignature(), t);
                        } finally {
                            CacheLoadLockHolder.unlock(conf, key);
                        }

                    }
                });
            } else {
                try {
                    CacheLoadLockHolder.lock(conf, key);
                    cache = load(pjp, conf, modelConf, key);
                } catch (XcacheLockTimeoutException e) {
                    return returnLockTimeOutCache(conf, modelConf, key);
                } finally {
                    CacheLoadLockHolder.unlock(conf, key);
                }
            }
        }
        return cache;
    }

    private <T> XcacheInstance<T> returnLockTimeOutCache(XcacheConf conf, XcacheModelConf modelConf, String key) throws Exception {
        return get(true, conf, modelConf, key);
    }

    protected <T> XcacheInstance<T> load(ProceedingJoinPoint pjp, XcacheConf conf, XcacheModelConf modelConf, String key) throws Exception {
        XcacheInstance<T> cache = get(false, conf, modelConf, key);
        if (!isValid(cache, modelConf)) {
            cache = load0(pjp, conf, modelConf, key);

            if (null != cache && (null != cache.getT() || conf.isCacheNull())) {
                set(conf, modelConf, key, cache);
            }
            return cache;
        }

        return cache;
    }

    private <T> XcacheInstance<T> load0(ProceedingJoinPoint pjp, XcacheConf conf, XcacheModelConf modelConf, String key) {
        log.debug("begin reload cache cacheConf:{}" + JSON.toJSONString(conf));

        Object t = null;
        try {
            t = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        log.debug("end reload cache cacheConf:{}" + JSON.toJSONString(conf));
        return new XcacheInstance(t, modelConf, conf, key);
    }

    public <T> XcacheInstance<T> get(boolean master, XcacheConf conf, XcacheModelConf modelConf, String key) throws Exception {
        try {
            XcacheInstance<T> cache = doGet(master, conf, modelConf, key);
            log.debug("get cache key:{} result:{}", key, cache);
            return cache;
        } catch (XcacheSerializeException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    protected abstract <T> XcacheInstance<T> doGet(boolean master, XcacheConf conf, XcacheModelConf modelConf, String key) throws Exception;

    private boolean isValid(XcacheInstance cache, XcacheModelConf modelConf) {
        if (cache == null) {
            return false;
        }

        if (!StringUtil.equals(cache.getModelSign(), modelConf.getSign())) {
            return false;
        }

        if (new Date().after(new Date(cache.getAlarmTimeMillis()))) {
            return false;
        }
        
        return true;
    }

}