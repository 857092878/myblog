package com.rui.myblog.redisMethod;

import com.rui.myblog.dto.BlogAllDto;
import com.rui.myblog.dto.NewRecommenededBlogDto;
import com.rui.myblog.pojo.Friend;

import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 14:34
 * Version1.0.0
 */
public interface BlogRemplate {

    //redis快速查询朋友列表
    List<BlogAllDto> blogAllDtoList();
    List<NewRecommenededBlogDto> newRecommenededBlogDto();

    //redis删除
    void BlogDelete();
}
