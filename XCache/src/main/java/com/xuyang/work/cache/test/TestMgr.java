package com.xuyang.work.cache.test;

import com.xuyang.work.cache.XcacheType;
import com.xuyang.work.cache.annotation.Xcache;
import org.springframework.stereotype.Component;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 17:08
 * @Description:
 */
@Component
public class TestMgr {


    @Xcache(type = XcacheType.LOCAL, prefix = "Test", alarmTime = 20, time = 10)
    public String get() throws InterruptedException {

        Thread.sleep(5000);
        return "111";
    }
}