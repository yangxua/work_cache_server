package com.xuyang.work.cache;

import com.xuyang.work.cache.conf.XcacheConf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 17:17
 * @Description:
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XcacheInstance<T> implements Serializable {

    /**
     * 缓存实例
     */
    private T t;

    /**
     * 缓存版本
     */
    private String version;

    /**
     * 多机器并行控制，启动的版本号是1
     */
    private long mmcc;

    /**
     * sign不一致则认为对象已经改动，则从新reload
     */
    private String modelSign;

    /**
     * 缓存时间戳
     */
    private long timeMillis;

    /**
     * 缓存预警时间戳
     */
    private long alarmTimeMillis;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 缓存key值
     */
    private String key;


    public XcacheInstance(T t, XcacheModelConf xcacheModelConf, XcacheConf xcacheConf, String xcacheInstanceKey) {
        Date now = new Date();
        this.t = t;
        this.version = xcacheConf.getVersion();
        this.modelSign = xcacheModelConf.getSign();
        this.timeMillis = now.getTime() + xcacheConf.getSeconds() * 1000;
        this.alarmTimeMillis = now.getTime() + xcacheConf.getAlarmSeconds() * 1000;
        this.prefix = xcacheConf.getPrefix();
        this.key = xcacheInstanceKey;
    }
}