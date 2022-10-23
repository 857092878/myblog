package com.rui.myblog.service;

import com.rui.myblog.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author purui
 * @since 2022-09-17
 */
public interface ICommentService extends IService<Comment> {

    //查出评论
    List<Comment> listCommentByBlogId(Long blogId);

    //添加评论
    void addComment(Comment comment, HttpSession session);

    void deleteComment(Comment comment, Long id);
}
