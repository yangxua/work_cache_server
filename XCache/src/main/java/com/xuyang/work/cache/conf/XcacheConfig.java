package com.xuyang.work.cache.conf;

import com.xuyang.work.cache.XcacheProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @Auther: allanyang
 * @Date: 2019/4/8 21:18
 * @Description:
 */
@Slf4j
@Configurable
@PropertySource("classpath:server.properties")
public class XcacheConfig {

    @Autowired
    private ConfigurableEnvironment env;

    @Bean
    public XcacheProperty toCacheConfig() {
        XcacheProperty property = new XcacheProperty();
        if (StringUtils.isEmpty(env.getProperty(XcacheProperty.XCACHE_SWITCH_PROPERTY)) || !env.getProperty(XcacheProperty.XCACHE_SWITCH_PROPERTY, Boolean.class)) {
            return property;
        }

        try {
            //@todo 开关未必一直时开着的，这个要根据excluedEvnList来判断
            property.setXcacheSwitch(true);
            if (!StringUtils.isEmpty(env.getProperty(XcacheProperty.XCACHE_REDIS_KEY_PROPERTY))) {
                property.setXcacheRedisKey(env.getProperty(XcacheProperty.XCACHE_REDIS_KEY_PROPERTY));
            } else if (StringUtils.isEmpty(env.getProperty(XcacheProperty.DEFAULT_REDIS_KEY))) {
                property.setXcacheRedisKey(env.getProperty(XcacheProperty.DEFAULT_REDIS_KEY));
            } else {
                throw new IllegalArgumentException(XcacheProperty.XCACHE_REDIS_KEY_PROPERTY + "of server.properties can not be empty");
            }

            if (!StringUtils.isEmpty(env.getProperty(XcacheProperty.XCACHE_TIME_PROPERTY))) {
                property.setCacheSeconds(env.getProperty(XcacheProperty.XCACHE_TIME_PROPERTY, Integer.class));
                if (property.getCacheSeconds() <= 0) {
                    throw new IllegalArgumentException(XcacheProperty.XCACHE_TIME_PROPERTY + " of server.properties must be bigger than 0");
                }
            }

            if (!StringUtils.isEmpty(env.getProperty(XcacheProperty.XCACHE_ALARM_TIME_PROPERTY))) {
                property.setCacheAlarmSeconds(env.getProperty(XcacheProperty.XCACHE_ALARM_TIME_PROPERTY, Integer.class));
                if (property.getCacheAlarmSeconds() <= 0) {
                    throw new IllegalArgumentException(XcacheProperty.XCACHE_ALARM_TIME_PROPERTY + " of server.properties must bigger than 0");
                }
            }

            if (!StringUtils.isEmpty(env.getProperty(XcacheProperty.XCACHE_LOCK_DEFAULT_TIMEOUT_PROPERTY))) {
                property.setLockTimeoutMillisSeconds(env.getProperty(XcacheProperty.XCACHE_LOCK_DEFAULT_TIMEOUT_PROPERTY, Integer.class));
                if (property.getLockTimeoutMillisSeconds() <= 0) {
                    throw new IllegalArgumentException(XcacheProperty.XCACHE_LOCK_DEFAULT_TIMEOUT_PROPERTY + " of server.properties must bigger than 0");
                }
            }

            if (StringUtils.isEmpty(XcacheProperty.XCACHE_LOCK_EXCLUDE_ENV)) {
                property.setExcludeEnvList(Arrays.asList(StringUtils.split(env.getProperty(XcacheProperty.XCACHE_LOCK_EXCLUDE_ENV), ",")));
            }
        } catch (Exception e) {

            //log.warn("xcache config error", e);
        }

        return property;
    }
}