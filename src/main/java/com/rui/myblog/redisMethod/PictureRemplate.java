package com.rui.myblog.redisMethod;

import com.rui.myblog.pojo.Picture;

import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 14:34
 * Version1.0.0
 */
public interface PictureRemplate {

    //redis快速查询图片列表
    List<Picture> pictureList();

    //redis删除
    void pictureDelete();
}
