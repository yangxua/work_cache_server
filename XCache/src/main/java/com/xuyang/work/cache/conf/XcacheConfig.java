package com.xuyang.work.cache.conf;

import com.xuyang.work.cache.XcacheProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

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

        } catch (Exception e) {
           // log.warn("xcache config error", e);
        }

        return property;
    }
}