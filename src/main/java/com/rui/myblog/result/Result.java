package com.rui.myblog.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/22 21:58
 * Version1.0.0
 */

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -4505655308965878999L;

    //请求成功返回码为：0000
    private static final String successCode = "0000";
    //返回数据
    private T data;
    //返回码
    private String code;
    //返回描述
    private String msg;

    public Result(){
        this.code = successCode;
        this.msg = "请求成功";
    }

    public Result(String code, String msg){
        this();
        this.code = code;
        this.msg = msg;
    }
    public Result(String code, String msg, T data){
        this();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public Result(T data){
        this();
        this.data = data;
    }
}
