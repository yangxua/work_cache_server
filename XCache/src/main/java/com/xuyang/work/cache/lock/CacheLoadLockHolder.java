package com.xuyang.work.cache.lock;

import com.xuyang.work.cache.conf.XcacheConf;
import com.xuyang.work.cache.exception.XcacheLockTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 18:44
 * @Description:
 */
@Slf4j
public class CacheLoadLockHolder {

    private static ConcurrentHashMap<String, LocalCacheLoadLock> cacheLoadLockMap = new ConcurrentHashMap<>();

    public static boolean tryLock(XcacheConf xcacheConf, String key) {
        if (null == xcacheConf) {
            throw new IllegalArgumentException("cacheConf can't be null");
        }

        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key can't be null");
        }

        LocalCacheLoadLock lock = put(key, xcacheConf, true);
        if (lock == null) {
            return false;
        }

        return lock.tryLock();
    }

    public static void lock(XcacheConf xcacheConf, String key) {
        if (null == xcacheConf) {
            throw new IllegalArgumentException("cacheConf can't be null");
        }

        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key can't be null");
        }

        LocalCacheLoadLock localCacheLoadLock = put(key, xcacheConf, false);

        localCacheLoadLock.lock();
    }

    public static void unlock(XcacheConf xcacheConf, String key) {
        if (null == xcacheConf) {
            throw new IllegalArgumentException("cacheConf can't be null");
        }

        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key can't be null");
        }

        LocalCacheLoadLock lock = cacheLoadLockMap.get(key);

        if (null != lock && Thread.currentThread().equals(lock.getOwner())) {
            if (cacheLoadLockMap.remove(key, lock)) {
                lock.unlock();
            }
            //gc
            lock = null;
        }
    }

    public static LocalCacheLoadLock put(String key, XcacheConf xcacheConf, boolean tryLock) {
        LocalCacheLoadLock lock = new LocalCacheLoadLock(key, xcacheConf, System.currentTimeMillis());

        for (;;) {
            LocalCacheLoadLock mapSetLock = cacheLoadLockMap.putIfAbsent(key, lock);
            if (mapSetLock == null) {
                return lock;
            } else if (tryLock) {
                return null;
            }

            //线程可重入
            if (Thread.currentThread().equals(mapSetLock.getOwner())) {
                return mapSetLock;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            if (System.currentTimeMillis() - lock.getTimeMillis() > xcacheConf.getLockTimeOutMills()) {
                throw new XcacheLockTimeoutException("xcacheconf:" + xcacheConf);
            }
        }
    }
}