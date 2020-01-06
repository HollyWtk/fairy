package com.fairy.configure;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Import;

import com.fairy.utils.commons.SpringMvcException;



@Import({CacheConfig.class,WebSecurityConfig.class,SpringMvcException.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableFairy {
}
