package com.rui.myblog.Thread;

import com.rui.myblog.pojo.Comment;
import com.rui.myblog.pojo.Message;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author 蒲锐
 * @CreateTme 2022/10/9 21:29
 * Version1.0.0
 */

@Component
public class CommentThread {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendComment(Comment comment, Comment parentComment){
        if(!StringUtils.isEmpty(parentComment)){
            Object obj = new Object();
            new Thread(){
                @Override
                public void run() {
                    synchronized (obj){
                        String parentNickname = parentComment.getNickname();
                        String nickName = comment.getNickname();
                        String comtent = "亲爱的" + parentNickname + "，您在【朴朴爱吃肉肉客栈】的评论收到了来自" + nickName + "的回复！内容如下：" + "\r\n" + "\r\n" +  comment.getContent();
                        String parentEmail = parentComment.getEmail();
                        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                        simpleMailMessage.setFrom("purui6662@163.com");
                        simpleMailMessage.setTo(parentEmail);
                        simpleMailMessage.setSubject("朴朴爱吃肉肉客栈评论回复");
                        simpleMailMessage.setText(comtent);
                        javaMailSender.send(simpleMailMessage);
                    }
                }
            }.start();
        }
    }
}
