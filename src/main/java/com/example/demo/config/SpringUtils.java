package com.example.demo.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Lazy(value = false)
public class SpringUtils implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) {
		SpringUtils.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T) context.getBean(beanName);
	}

	public static <T> T getBean(Class<T> beanType) {
		return context.getBean(beanType);
	}

    public static <T> Map<String, T> getBeansOfType(Class<T> clz) {
       return context.getBeansOfType(clz);
    }
}
