package com.rui.myblog.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/17 15:05
 * Version1.0.0
 */

public class CopyUtils {
    public static <S,T> List<T> CopyList(List<S> list1,Class<T> vo){

        List<T> list2 = new ArrayList<>();

        for (S s:list1){
            T t = null;
            try {
                t = vo.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(s,t);
            list2.add(t);
        }

        return list2;
    }
}
