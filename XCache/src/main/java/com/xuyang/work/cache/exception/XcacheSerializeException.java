package com.xuyang.work.cache.exception;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 17:10
 * @Description:
 */
public class XcacheSerializeException extends RuntimeException {

    public XcacheSerializeException(String msg) {
        super(msg);
    }

    public XcacheSerializeException(Throwable cause) {
        super(cause);
    }
}