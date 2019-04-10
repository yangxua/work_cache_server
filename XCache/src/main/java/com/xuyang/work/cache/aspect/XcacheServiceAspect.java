package com.xuyang.work.cache.aspect;

import com.xuyang.work.cache.XcacheInstance;
import com.xuyang.work.cache.XcacheProperty;
import com.xuyang.work.cache.exception.XcacheLoadException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: allanyang
 * @Date: 2019/4/10 11:16
 * @Description:
 */
@Slf4j
@Aspect
@Component
public class XcacheServiceAspect {

    @Autowired
    private XcacheProperty property;

    public XcacheServiceAspect() {}

    @Pointcut("@annotation(com.xuyang.work.cache.annotation.Xcache)")
    public void annotationPointCut() {}

    @Around("annotationPointCut()")
    public Object doServiceAspect(ProceedingJoinPoint pjp) throws Throwable {
        try {
            if (!property.isXcacheSwitch()) {
                pjp.proceed();
            }

            XcacheInstance cache = XcacheManager.get(pjp);
            log.debug("load from xcache ProceedingJoinPoint ：" + pjp.getSignature() + "result:" + cache);
            if (cache != null) {
                return cache.getT();
            } else {
                return pjp.proceed();
            }
        } catch (XcacheLoadException e) {
            log.error("xcache load exception " + e.getMessage(), e);
            throw e;
        } catch (Throwable e) {
            log.error("xcache handler error, ProceedingJoinPoint ：" + pjp.getSignature(), e);

            return pjp.proceed();
        }
    }
}