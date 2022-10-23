package com.rui.myblog.redisMethod.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.rui.myblog.pojo.Friend;
import com.rui.myblog.pojo.Resources;
import com.rui.myblog.redisMethod.FriendRemplate;
import com.rui.myblog.service.IFriendService;
import com.rui.myblog.service.IPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 15:27
 * Version1.0.0
 */
@Service
public class FriendRemplateImpl implements FriendRemplate {
    @Autowired
    private IFriendService friendService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String FRIEND_KEY = "FRIEND_KEY";

    @Override
    public List<Friend> friendList() {
        String str = stringRedisTemplate.opsForValue().get(FRIEND_KEY);

        List<Friend> friends;

        if (StrUtil.isBlank(str)){
            friends = friendService.listFriend();
            stringRedisTemplate.opsForValue().set(FRIEND_KEY, JSONUtil.toJsonStr(friends));
        }else{
            friends = JSONObject.parseArray(str, Friend.class);
        }
        return friends;
    }

    @Override
    public void friendDelete() {
        stringRedisTemplate.delete(FRIEND_KEY);
    }
}
