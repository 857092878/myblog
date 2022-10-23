package com.rui.myblog.service.impl;

import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.Thread.CommentThread;
import com.rui.myblog.mapper.BlogMapper;
import com.rui.myblog.pojo.Comment;
import com.rui.myblog.mapper.CommentMapper;
import com.rui.myblog.pojo.User;
import com.rui.myblog.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BlogMapper blogMapper;

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    // 自动导入Java邮件发送实现类
    @Autowired
    private JavaMailSender javaMailSender ;

    @Autowired
    private CommentThread commentThread;

    //默认头像地址
    @Value("${comment.avatar}")
    private String avatar;


    /*
    查看评论
     */
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        //查出父级评论
        List<Comment> byBlogIdParentIdNull = commentMapper.findByBlogIdParentId(blogId, Long.parseLong("-1"));
        for(Comment comment : byBlogIdParentIdNull){
            Long id = comment.getId();//父级id
            String parentNickname1 = comment.getNickname();//父级昵称
            List<Comment> childComments = commentMapper.findByBlogIdParentId(blogId,id);//查询一级评论
            //查询出子评论
            combineChildren(blogId, childComments, parentNickname1);
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return byBlogIdParentIdNull;
    }



    /*
    查看一级评论
     */
    private void combineChildren(Long blogId, List<Comment> childComments, String parentNickname1) {
        //判断是否有一级子评论
        if(childComments.size() > 0){
            //循环找出子评论的id
            for(Comment childComment : childComments){
                String parentNickname = childComment.getNickname();//一级评论昵称
                childComment.setParentNickname(parentNickname1);//一级评论的父级id
                tempReplys.add(childComment);
                Long childId = childComment.getId();//一级评论id
                //查询出子二级评论
                recursively(blogId, childId, parentNickname);
            }
        }
    }

    /*
    循环查看子级评论
     */
    private void recursively(Long blogId, Long childId, String parentNickname1) {
        //根据子一级评论的id找到子二级评论
        List<Comment> replayComments = commentMapper.findByBlogIdParentId(blogId,childId);

        if(replayComments.size() > 0){
            for(Comment replayComment : replayComments){
                String parentNickname = replayComment.getNickname();
                replayComment.setParentNickname(parentNickname1);
                Long replayId = replayComment.getId();
                tempReplys.add(replayComment);
                recursively(blogId,replayId,parentNickname);
            }
        }
    }

    /*
    添加评论
     */
    @Override
    public void addComment(Comment comment, HttpSession session) {

        User user = (User) session.getAttribute("user");//取出session中的用户记录

        if (user != null) {
            comment.setAvatar(user.getAvatar());//给评论设置头像
            comment.setAdminComment(true);//给予评论的权限
        }else{
            //设置默认头像
            comment.setAdminComment(false);//给予评论的权限
            comment.setAvatar(avatar);
        }

        //取出该评论的父级id
        Long parentId = comment.getParentComment().getId();
        Comment parentComment = null;//该条评论的父级评论
        if (comment.getParentComment().getId() != null) {
            comment.setParentCommentId(parentId);
            // 根据父评论id查询留言信息
            parentComment = commentMapper.getEmailByParentId(parentId);//该条父级评论
        }
        int saveComment = saveComment(comment, parentComment);
        if (saveComment < 1 ){
            throw new ServiceException("添加评论失败");
        }


    }

    @Override
    public void deleteComment(Comment comment, Long id) {
        int i = commentMapper.deleteById(id);
        if (i <1 ){
            throw new ServiceException("删除失败");
        }
        //查询评论数量并更新
        blogMapper.getCommentCountById(comment.getBlogId());
    }

    /*
    保存评论，并给父级发邮件
     */
    private int saveComment(Comment comment, Comment parentComment) {
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        comment.setCreateTime(localDateTime);
        int insert = commentMapper.saveComment(comment);

        //重新设置博客的评论数
        blogMapper.getCommentCountById(comment.getBlogId());
//        CommentThread commentThread = new CommentThread();
        commentThread.sendComment(comment,parentComment);


//        // 判断是否有父评论，有的话就发送邮件
//        if(!StringUtils.isEmpty(parentComment)){
//            String parentNickname = parentComment.getNickname();//父级昵称
//            String nickName = comment.getNickname();//评论人昵称
//            String content = "亲爱的" + parentNickname + "，您在【朴朴爱吃肉肉客栈】的评论收到了来自" + nickName + "的回复！内容如下：" + "\r\n" + "\r\n" +  comment.getContent();
//            String parentEmail = parentComment.getEmail();//父级邮件
//
//            // 发送邮件
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//            simpleMailMessage.setSubject("朴朴爱吃肉肉客栈评论回复");  //主题
//            simpleMailMessage.setText(content);   //内容
//            simpleMailMessage.setTo(parentEmail); //接收者的邮箱
//            simpleMailMessage.setFrom("purui6662@163.com");//发送者邮箱
//            javaMailSender.send(simpleMailMessage);
//        }
        return insert;
    }


}
