package com.rui.myblog.redisMethod.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.rui.myblog.pojo.Picture;
import com.rui.myblog.redisMethod.PictureRemplate;
import com.rui.myblog.service.IPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 14:34
 * Version1.0.0
 */
@Service
public class PictureRemplateImpl implements PictureRemplate {
    @Autowired
    private IPictureService pictureService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String PICTURE_KEY = "PICTURE_KEY";

    @Override
    public List<Picture> pictureList(){
        String str = stringRedisTemplate.opsForValue().get(PICTURE_KEY);

        List<Picture> pictures;

        if (StrUtil.isBlank(str)){
            pictures = pictureService.listPicture();
            stringRedisTemplate.opsForValue().set(PICTURE_KEY, JSONUtil.toJsonStr(pictures));
        }else{
            pictures = JSONObject.parseArray(str, Picture.class);
        }
        return pictures;
    }

    @Override
    public void pictureDelete() {
        stringRedisTemplate.delete(PICTURE_KEY);
    }
}
