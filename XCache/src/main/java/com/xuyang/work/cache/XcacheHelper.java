package com.xuyang.work.cache;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.xuyang.work.cache.annotation.Xcache;
import com.xuyang.work.cache.conf.XcacheConf;
import com.xuyang.work.cache.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @Auther: allanyang
 * @Date: 2019/4/10 14:14
 * @Description:
 */
@Slf4j
public class XcacheHelper {

    private static Map<Class, XcacheModelConf> clazz2CacheModelConf = Maps.newConcurrentMap();
    private static Map<String, XcacheConf> method2CacheConf = Maps.newConcurrentMap();

    public static XcacheConf getCacheConf(ProceedingJoinPoint pjp) {
        if (pjp == null) {
            throw new IllegalArgumentException("pjp can't be null");
        }

        String methodName = getMethodName(getMethod(pjp));
        if (!method2CacheConf.containsKey(methodName)) {
            synchronized (XcacheHelper.class) {
                if (!method2CacheConf.containsKey(methodName)) {
                    method2CacheConf.put(methodName, generateCacheConf(pjp));
                }
            }
        }

        return method2CacheConf.get(methodName);
    }

    public static XcacheModelConf getCacheModelConf(Class clazz, boolean inherit) {
        if (null == clazz) {
            return null;
        }

        if (!clazz2CacheModelConf.containsKey(clazz)) {
            synchronized (XcacheHelper.class) {
                if (!clazz2CacheModelConf.containsKey(clazz)) {
                    clazz2CacheModelConf.put(clazz, generateCacheModelConf(clazz, inherit));
                }
            }
        }

        return clazz2CacheModelConf.get(clazz);
    }

    private static XcacheModelConf generateCacheModelConf(Class clazz, boolean inherit) {
        if (null == clazz) {
            return null;
        }
        
        XcacheModelConf conf = new XcacheModelConf();
        List<Field> fields = ReflectUtil.getFields(clazz, inherit);
        conf.setFields(fields);
        conf.setClazz(clazz);
        return generateCacheModelSign(conf);
    }

    private static XcacheModelConf generateCacheModelSign(XcacheModelConf conf) {
        if (null == conf) {
            throw new IllegalArgumentException("conf can't be null");
        }

        List<Field> fields = CollectionUtil.select(conf.getFields(), new CollectionUtil.Selector<Field>() {
            @Override
            public boolean select(Field field) {
                return null != field;
            }
        });

        fields = Ordering.from(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        }).sortedCopy(fields);
        final StringBuilder sb = new StringBuilder(conf.getClazz().getSimpleName() + StringUtil.UNDERLINE);
        CollectionUtil.forEach(fields, new CollectionUtil.Handler1<Field>() {
            @Override
            public void handle(Field field) {
                sb.append(field.getGenericType()).append(StringUtil.DOT).append(field.getName());
            }
        });

        conf.setFields(fields);
        conf.setSign(CodecUtil.md5HexString(sb.toString().getBytes(Charsets.UTF_8)));

        return conf;
    }

    private static XcacheConf generateCacheConf(ProceedingJoinPoint pjp) {
        if (pjp == null) {
            throw new IllegalArgumentException("pjp不能为null");
        }

        Method method = getMethod(pjp);
        Xcache cache = method.getAnnotation(Xcache.class);
        if (cache == null) {
            throw new IllegalArgumentException("cache annotation is null on " + getMethodName(method));
        }
        if (method.getReturnType().equals(Void.TYPE)) {
            throw new IllegalArgumentException("returnType can't be null on " + getMethodName(method));
        }

        XcacheProperty cacheProperty = SpringHelpUtil.getBean(XcacheProperty.class);
        if (cacheProperty == null) {
            throw new IllegalArgumentException("cacheProperty can't be null");
        }

        XcacheConf conf = new XcacheConf();
        conf.setPrefix(cache.prefix());
        conf.setCacheNull(cache.cacheNull());
        conf.setAlarmSeconds(NumberUtil.nullOrZero(cache.alarmTime()) ? cacheProperty.getCacheAlarmSeconds() : cache.alarmTime());
        conf.setLockTimeOutMills(NumberUtil.nullOrZero(cache.lockmillis()) ? cacheProperty.getLockTimeoutMillisSeconds() : cache.lockmillis());
        conf.setMaxSize(cache.maxSize());
        conf.setMethod(method);
        conf.setReturnType(method.getGenericReturnType());
        conf.setSeconds(NumberUtil.nullOrZero(cache.time()) ? cacheProperty.getCacheSeconds() : cache.time());
        conf.setSerializeType(cache.serializeType());
        conf.setVersion(cache.version());

        StringBuilder sb = new StringBuilder();
        sb.append(conf.getSerializeType()).append(StringUtil.COLON);
        sb.append(conf.getPrefix()).append(StringUtil.COLON);
        sb.append("%s").append(StringUtil.COLON);
        sb.append(conf.getVersion());
        conf.setPreCacheKey(sb.toString());
        return conf;
    }

    public static String getMethodName(Method method) {
        if(method == null) {
            return StringUtil.EMPTY;
        }

        return method.getDeclaringClass().getName() + StringUtil.DOT + method.getName();
    }


    public static Method getMethod(ProceedingJoinPoint pjp) {
        if (pjp == null) {
            throw new IllegalArgumentException("pjp can't be null");
        }

        //获取接口的方法
        return ((MethodSignature)pjp.getSignature()).getMethod();

        //下面方法是获取具体类的方法
        /*Signature sig = pjp.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        MethodSignature msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        Method method = null;
        try {
            method = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;*/
    }

    public static Object[] parseParam(XcacheConf conf, String cacheInstanceKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(conf.getSerializeType()).append(StringUtil.COLON);
        sb.append(conf.getPrefix()).append(StringUtil.COLON);
        cacheInstanceKey.replace(sb.toString(), StringUtil.EMPTY);
        return JSON.parseObject(cacheInstanceKey.substring(0, cacheInstanceKey.indexOf(StringUtil.COLON)),new Object[1].getClass());
    }

    public static String getRedisKey(XcacheConf conf) {
        XcacheProperty property = SpringHelpUtil.getBean(XcacheProperty.class);
        return property.getXcacheRedisKey();
    }

    public static String getCacheInstanceKey(XcacheConf conf, ProceedingJoinPoint pjp) {
        return String.format(conf.getPreCacheKey(), JSON.toJSONString(pjp.getArgs()));
    }

}