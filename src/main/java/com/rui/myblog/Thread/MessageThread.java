package com.rui.myblog.Thread;

import com.rui.myblog.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
public class MessageThread {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMessage(Message message,Message parentMessage){
        if(!StringUtils.isEmpty(parentMessage)){
            Object obj = new Object();
            new Thread(){
                @Override
                public void run() {
                    synchronized (obj){
//                        for (int i = 0; i < 100; i++) {
//                            System.out.println(i);
//
//                        }
                        String parentNickname = parentMessage.getNickname();
                        String nickName = message.getNickname();
                        String comtent = "亲爱的" + parentNickname + "，您在【朴朴爱吃肉肉客栈】的评论收到了来自" + nickName + "的回复！内容如下：" + "\r\n" + "\r\n" +  message.getContent();
                        String parentEmail = parentMessage.getEmail();
                        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                        simpleMailMessage.setFrom("purui6662@163.com");
                        simpleMailMessage.setTo(parentEmail);
                        simpleMailMessage.setSubject("朴朴爱吃肉肉客栈评论回复");
                        simpleMailMessage.setText(comtent);
                        javaMailSender.send(simpleMailMessage);

                        System.out.println(javaMailSender);
                        System.out.println(simpleMailMessage);
                    }
                }
            }.start();
        }
    }
}
