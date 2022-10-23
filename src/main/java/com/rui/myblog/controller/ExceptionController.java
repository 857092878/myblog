package com.rui.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/16 23:15
 * Version1.0.0
 */

@Controller
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Throwable e){
        return "error/404";

    }
}
