package com.rui.myblog.annotiation;


import java.lang.annotation.*;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/18 14:22
 * Version1.0.0
 */

/*
定义拦截多次请求ip
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {
    int seconds();
    int maxCount();
    boolean needLogin()default true;
}
