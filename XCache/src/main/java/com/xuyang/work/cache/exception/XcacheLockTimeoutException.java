package com.xuyang.work.cache.exception;

/**
 * @Auther: allanyang
 * @Date: 2019/4/9 17:08
 * @Description:
 */
public class XcacheLockTimeoutException extends RuntimeException {

    public XcacheLockTimeoutException() {
    }

    public XcacheLockTimeoutException(String msg) {
        super(msg);
    }

    public XcacheLockTimeoutException(Throwable cause) {
        super(cause);
    }
}