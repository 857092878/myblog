package com.rui.myblog.service;

import com.rui.myblog.pojo.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author purui
 * @since 2022-09-17
 */
public interface IMessageService extends IService<Message> {

    //查询所有留言信息
    List<Message> listMessage();

    //查询父级留言信息
    Message getEmailByParentId(Long parentId);

    //保存留言信息
    int saveMessage(Message message, Message parentMessage);

    void deleteMessage(Long id);
}
