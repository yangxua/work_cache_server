package com.xuyang.work.cache.lock;

import com.xuyang.work.cache.conf.XcacheConf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 18:40
 * @Description:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LocalCacheLoadLock extends ReentrantLock {

    private String key;

    private XcacheConf xcacheConf;

    private long timeMillis;

    @Override
    protected Thread getOwner() {
        return super.getOwner();
    }
}