package com.rui.myblog.redisMethod;

import com.rui.myblog.pojo.Friend;
import com.rui.myblog.pojo.Picture;

import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 14:34
 * Version1.0.0
 */
public interface FriendRemplate {

    //redis快速查询朋友列表
    List<Friend> friendList();

    //redis删除
    void friendDelete();
}
