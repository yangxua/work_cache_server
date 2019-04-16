package com.xuyang.work.cache.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by ssh on 2016/7/20.
 */
@Component
@Lazy(false)
public class SpringHelpUtil implements ApplicationContextAware {
    private static volatile ApplicationContext appCtx;

    /**
     * 此方法可以把ApplicationContext对象inject到当前类中作为一个静态成员变量。
     *
     * @param applicationContext ApplicationContext 对象.
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == appCtx) {
            synchronized (SpringHelpUtil.class) {
                if (null == appCtx) {
                    appCtx = applicationContext;
                }
            }
        }
    }

    public static ApplicationContext getApplicationContext() {
        return appCtx;
    }


    /**
     * 这是一个便利的方法，帮助我们快速得到一个BEAN
     *
     * @param beanName bean的名字
     * @return 返回一个bean对象
     */
    public static Object getBean(String beanName) {
        return appCtx.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clz) {
        return appCtx.getBean(clz);
    }

    public static ApplicationContext getXmlContext(String xml) {
        if (appCtx != null) {
            return appCtx;
        }
        xml = (xml == null || xml.equalsIgnoreCase("")) ? "spring-config.xml" : xml;
        return new ClassPathXmlApplicationContext(xml);
    }

    /**
     * <pre>
     * 动态注入spring bean,注意不要在spring初始化的时候动态注册bean
     * </pre>
     *
     * @param clz  类型
     * @param beanName 名称
     */
    public static void registerSpringBean(Class<?> clz, String beanName) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) appCtx;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clz);
        // 设置注入属性，没有则不用设置
        // beanDefinitionBuilder.addPropertyReference("userAcctDAO", "UserAcctDAO");
        // 注册bean
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
    }
}

