package com.rui.myblog.service;

import com.rui.myblog.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author purui
 * @since 2022-09-18
 */
public interface IUserService extends IService<User> {

    //查询用户
    User checkUser(String username, String password);
}
