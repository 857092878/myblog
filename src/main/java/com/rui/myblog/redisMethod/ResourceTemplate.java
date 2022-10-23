package com.rui.myblog.redisMethod;

import com.rui.myblog.pojo.Resources;

import java.util.List;

public interface ResourceTemplate {
    //redis快速查询图片列表
    List<Resources> ResourcesList();

    //redis删除
    void resourcesDelete();

    List<Resources> StudyResourcesList();
}
