package com.xuyang.work.cache;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Auther: allanyang
 * @Date: 2019/4/8 21:23
 * @Description:
 */
@Data
@ToString
public class XcacheProperty {

    public static final String XCACHE_SWITCH_PROPERTY = "xcache.switch";
    public static final String XCACHE_REDIS_KEY_PROPERTY = "xcache.redis.key";
    public static final String DEFAULT_REDIS_KEY = "zk.redis.key";
    public static final String XCACHE_TIME_PROPERTY = "xcache.default.time";
    public static final String XCACHE_ALARM_TIME_PROPERTY = "xcache.default.alarm.time";
    public static final String XCACHE_LOCK_DEFAULT_TIMEOUT_PROPERTY = "xcache.lock.default.timeout";
    public static final String XCACHE_LOCK_EXCLUDE_ENV = "xcache.lock.enclude.env";

    private static final boolean DEFAULT_XCACHE_SWITCH = false;
    private static final int DEFAULT_XCACHE_SECONDS = 300;
    private static final int DEFAULT_XCACHE_ALARM_SECONDS = 180;
    private static final int DEFAULT_LOCK_TIMEOUT_MILLISSECONDS = 50;

    /**
     * 系统使用redisKey
     */
    private String xcacheRedisKey;

    /**
     * 缓存开关
     */
    private boolean xcacheSwitch = DEFAULT_XCACHE_SWITCH;

    /**
     * 默认缓存时间
     */
    private int cacheSeconds = DEFAULT_XCACHE_SECONDS;

    /**
     * 默认缓存预警时间
     */
    private int cacheAlarmSeconds = DEFAULT_XCACHE_ALARM_SECONDS;

    /**
     * 重新loak加锁时间
     */
    private int lockTimeoutMillisSeconds = DEFAULT_LOCK_TIMEOUT_MILLISSECONDS;

    /**
     * 不适用cache环境，方便测试
     */
    private List<String> excludeEnvList;

}