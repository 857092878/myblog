package com.rui.myblog.controller;

import com.rui.myblog.annotiation.AccessLimit;
import com.rui.myblog.dto.BlogDetailMessageDto;
import com.rui.myblog.pojo.Comment;
import com.rui.myblog.pojo.User;
import com.rui.myblog.service.IBlogService;
import com.rui.myblog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
@Controller
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IBlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    //查询评论列表
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }

    //新增评论
    @PostMapping("/comments")
    @AccessLimit(seconds = 15, maxCount = 3) //15秒内 允许请求3次
    public String addComment(Comment comment, HttpSession session, Model model) {

        //添加评论
        commentService.addComment(comment,session);

        List<Comment> comments = commentService.listCommentByBlogId(comment.getBlogId());//查出评论
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }

    //删除评论
    @GetMapping("/comment/{blogId}/{id}/delete")
    public String delete(@PathVariable Long blogId, @PathVariable Long id, Comment comment, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user != null) {
            commentService.deleteComment(comment,id);
        }
        BlogDetailMessageDto detailedBlog = blogService.getDetailedBlogMessage(blogId);
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("blog", detailedBlog);
        model.addAttribute("comments", comments);
        return "blog";
    }

}
