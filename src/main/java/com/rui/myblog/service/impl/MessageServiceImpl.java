package com.rui.myblog.service.impl;

import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.Thread.MessageThread;
import com.rui.myblog.pojo.Message;
import com.rui.myblog.mapper.MessageMapper;
import com.rui.myblog.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author purui
 * @since 2022-09-17
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;

    // 自动导入Java邮件发送实现类
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageThread messageThread;

    @Value("${spring.mail.username}")
    private String username;

    //存放迭代找出的所有子代的集合
    private List<Message> tempReplys = new ArrayList<>();

    public List<Message> listMessage() {
        //查询出父节点
        List<Message> messages = messageMapper.findByParentId(Long.parseLong("-1"));
        for(Message message : messages){
            Long id = message.getId();//父级id
            String parentNickname1 = message.getNickname();//父级昵称
            List<Message> childMessages = messageMapper.findByParentId(id);//一级留言

            //查询出子留言
            combineChildren(childMessages, parentNickname1);
            message.setReplyMessages(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return messages;
    }

    private void combineChildren(List<Message> childMessages, String parentNickname1) {
        //判断是否有一级子回复
        if(childMessages.size() > 0){
            //循环找出子留言的id
            for(Message childMessage : childMessages){
                String parentNickname = childMessage.getNickname();//一级留言昵称
                childMessage.setParentNickname(parentNickname1);//设置父级昵称
                tempReplys.add(childMessage);//添加进
                Long childId = childMessage.getId();
                //查询二级以及所有子集回复
                recursively(childId, parentNickname);
            }
        }
    }

    /*
    查询回复
     */
    private void recursively(Long childId, String parentNickname1) {
        //根据子一级留言的id找到子二级留言
        List<Message> replayMessages = messageMapper.findByParentId(childId);

        if(replayMessages.size() > 0){
            for(Message replayMessage : replayMessages){
                String parentNickname = replayMessage.getNickname();
                replayMessage.setParentNickname(parentNickname1);
                Long replayId = replayMessage.getId();
                tempReplys.add(replayMessage);
                //循环迭代找出子集回复
                recursively(replayId,parentNickname);
            }
        }
    }


    @Override
    public Message getEmailByParentId(Long parentId) {
        return messageMapper.getEmailByParentId(parentId);
    }

    @Override
    //存储留言信息
    public int saveMessage(Message message,Message parentMessage) {
        message.setCreateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

//         判断是否有父评论，有的话就发送邮件
//        if(!StringUtils.isEmpty(parentMessage)){
//            String parentNickname = parentMessage.getNickname();
//            String nickName = message.getNickname();
//            String comtent = "亲爱的" + parentNickname + "，您在【朴朴爱吃肉肉客栈】的评论收到了来自" + nickName + "的回复！内容如下：" + "\r\n" + "\r\n" +  message.getContent();
//            String parentEmail = parentMessage.getEmail();
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//            simpleMailMessage.setFrom("purui6662@163.com");
//            simpleMailMessage.setTo(parentEmail);
//            simpleMailMessage.setSubject("朴朴爱吃肉肉客栈评论回复");
//            simpleMailMessage.setText(comtent);
//            javaMailSender.send(simpleMailMessage);
//        }
//
//        MessageThread messageThread = new MessageThread();
//        messageThread
        messageThread.sendMessage( message,parentMessage);
        return messageMapper.saveMessage(message);
    }

    @Override
    public void deleteMessage(Long id) {
        int i = messageMapper.deleteById(id);
        if (i < 1){
            throw new ServiceException("删除留言失败");
        }
    }
}
