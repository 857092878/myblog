package com.rui.myblog.redisMethod;

import com.rui.myblog.pojo.Friend;
import com.rui.myblog.pojo.Memory;

import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/24 14:34
 * Version1.0.0
 */
public interface MemoryRemplate {

    //redis快速查询记忆列表
    List<Memory> memoryList();

    //redis删除
    void memoryDelete();
}
