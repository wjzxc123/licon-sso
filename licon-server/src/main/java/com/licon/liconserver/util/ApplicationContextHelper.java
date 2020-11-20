package com.licon.liconserver.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/19 15:58
 */
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static Object getBeanByName(String beanName){
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBeanByNameAndType(String beanName,Class<T> clazz){
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanName,clazz);
    }
}
