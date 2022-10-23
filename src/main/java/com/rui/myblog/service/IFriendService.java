package com.rui.myblog.service;

import com.rui.myblog.pojo.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
public interface IFriendService extends IService<Friend> {

    //查询所有友链
    List<Friend> listFriend();

    //根据网站查看好友
    Friend getFriendByBlogaddress(String blogaddress);

    //保存添加
    int saveFriend(Friend friend);

    Friend getFriendById(Long id);

    //更新好友
    int updateFriend(Friend friend);

    //删除好友
    void deleteFriend(Long id);

    int savePicture(MultipartFile file, Long id, String blogname, String blogaddress);
}
