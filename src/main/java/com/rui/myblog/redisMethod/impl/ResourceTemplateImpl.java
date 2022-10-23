package com.rui.myblog.redisMethod.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rui.myblog.mapper.ResourcesMapper;
import com.rui.myblog.pojo.Resources;
import com.rui.myblog.redisMethod.ResourceTemplate;
import com.rui.myblog.service.IResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 14:58
 * Version1.0.0
 */

@Service
public class ResourceTemplateImpl implements ResourceTemplate {

    @Autowired
    private ResourcesMapper resourcesMapper;
    @Autowired
    private IResourcesService resourcesService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String RESOURCES_KEY = "RESOURCES_KEY";

    private static final String STUDE_RESOURCES_KEY = "STUDE_RESOURCES_KEY";


    @Override
    public List<Resources> ResourcesList() {
        String str = stringRedisTemplate.opsForValue().get(RESOURCES_KEY);

        List<Resources> resourceses;

        if (StrUtil.isBlank(str)){
            resourceses = resourcesService.listResources();
            stringRedisTemplate.opsForValue().set(RESOURCES_KEY, JSONUtil.toJsonStr(resourceses));
        }else{
            resourceses = JSONObject.parseArray(str, Resources.class);
        }
        return resourceses;
    }

    @Override
    public void resourcesDelete() {
        stringRedisTemplate.delete(RESOURCES_KEY);
        stringRedisTemplate.delete(STUDE_RESOURCES_KEY);
    }

    @Override
    public List<Resources> StudyResourcesList() {
        String str = stringRedisTemplate.opsForValue().get(STUDE_RESOURCES_KEY);

        List<Resources> resourceses;

        if (StrUtil.isBlank(str)){
            resourceses = resourcesService.getResource("学习资源");
            stringRedisTemplate.opsForValue().set(RESOURCES_KEY, JSONUtil.toJsonStr(resourceses));
        }else{
            resourceses = JSONObject.parseArray(str, Resources.class);
        }
        return resourceses;
    }
}
