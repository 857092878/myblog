package com.rui.myblog.redisMethod.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.rui.myblog.dto.BlogAllDto;
import com.rui.myblog.dto.NewRecommenededBlogDto;
import com.rui.myblog.redisMethod.BlogRemplate;
import com.rui.myblog.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 15:58
 * Version1.0.0
 */
@Service
public class BlogRemplateImpl implements BlogRemplate {

    @Autowired
    private IBlogService blogService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String BLOGALLDTO_KEY = "BLOGALLDTO_KEY";
    public static final String NEWRECOMMENEDEDBLOGDTO_KEY = "NEWRECOMMENEDEDBLOGDTO_KEY";

    @Override
    public List<BlogAllDto> blogAllDtoList() {
        String str = stringRedisTemplate.opsForValue().get(BLOGALLDTO_KEY);

        List<BlogAllDto> allBlog;

        if (StrUtil.isBlank(str)){
            allBlog = blogService.getAllBlog();
            stringRedisTemplate.opsForValue().set(BLOGALLDTO_KEY, JSONUtil.toJsonStr(allBlog));
        }else{
            allBlog = JSONObject.parseArray(str, BlogAllDto.class);
        }
        return allBlog;
    }

    @Override
    public List<NewRecommenededBlogDto> newRecommenededBlogDto() {
        String str = stringRedisTemplate.opsForValue().get(NEWRECOMMENEDEDBLOGDTO_KEY);

        List<NewRecommenededBlogDto> recommendedBlog;

        if (StrUtil.isBlank(str)){
            recommendedBlog = blogService.getRecommendedBlog();
            stringRedisTemplate.opsForValue().set(NEWRECOMMENEDEDBLOGDTO_KEY, JSONUtil.toJsonStr(recommendedBlog));
        }else{
            recommendedBlog = JSONObject.parseArray(str, NewRecommenededBlogDto.class);
        }
        return recommendedBlog;
    }

    @Override
    public void BlogDelete() {
        stringRedisTemplate.delete(BLOGALLDTO_KEY);
        stringRedisTemplate.delete(NEWRECOMMENEDEDBLOGDTO_KEY);
    }
}
