package com.rui.myblog.redisMethod.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.rui.myblog.pojo.Friend;
import com.rui.myblog.pojo.Memory;
import com.rui.myblog.redisMethod.MemoryRemplate;
import com.rui.myblog.service.IMemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 15:42
 * Version1.0.0
 */
@Service
public class MemoryRemplateImpl implements MemoryRemplate {

    @Autowired
    private IMemoryService memoryService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String MEMORY_KEY = "MEMORY_KEY";
    @Override
    public List<Memory> memoryList() {
        String str = stringRedisTemplate.opsForValue().get(MEMORY_KEY);

        List<Memory> memories;

        if (StrUtil.isBlank(str)){
            memories = memoryService.listMemory();
            stringRedisTemplate.opsForValue().set(MEMORY_KEY, JSONUtil.toJsonStr(memories));
        }else{
            memories = JSONObject.parseArray(str, Memory.class);
        }
        return memories;
    }

    @Override
    public void memoryDelete() {
        stringRedisTemplate.delete(MEMORY_KEY);
    }
}
