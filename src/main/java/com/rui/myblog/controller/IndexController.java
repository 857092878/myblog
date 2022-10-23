package com.rui.myblog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rui.myblog.dto.BlogAllDto;
import com.rui.myblog.dto.BlogDetailMessageDto;
import com.rui.myblog.dto.NewCommentDto;
import com.rui.myblog.dto.NewRecommenededBlogDto;
import com.rui.myblog.redisMethod.impl.BlogRemplateImpl;
import com.rui.myblog.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
@Controller
public class IndexController {

    @Autowired
    private IBlogService blogService;
    @Autowired
    private BlogRemplateImpl blogRemplate;

    //分页查询博客列表
    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, RedirectAttributes attributes){

        //用此方法 返回page
        PageHelper.startPage(pageNum,10);
        //查询博客列表
        List<BlogAllDto> allBlog = blogService.getAllBlog();
        //查询最新推荐博客
        List<NewRecommenededBlogDto> recommendedBlog = blogService.getRecommendedBlog();
        //查询最新评论
        List<NewCommentDto> newComments = blogService.getNewComment();

        //手动分页
        PageInfo<BlogAllDto> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("recommendedBlogs", recommendedBlog);
        model.addAttribute("newComment",newComments);
        return "index";
    }

    //搜索博客
    @PostMapping("/search")
    public String search(Model model,
                         @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                         @RequestParam String query) {
        PageHelper.startPage(pageNum, 1000);
        List<BlogAllDto> searchBlog = blogService.getAllBlog(query);

        PageInfo<BlogAllDto> pageInfo = new PageInfo<>(searchBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    //博客信息
    @GetMapping("/footer/blogmessage")
    public String blogMessage(Model model){
        /*
        统计博客条数
        统计访问总数
        统计评论总数
        统计留言总数
         */
        Map<String, Object> map = blogService.blogMessage();
        model.addAttribute("blogTotal",map.get("blogTotal"));
        model.addAttribute("blogViewTotal",map.get("blogViewTotal"));
        model.addAttribute("blogCommentTotal",map.get("blogCommentTotal"));
        model.addAttribute("blogMessageTotal",map.get("blogMessageTotal"));
        return "index :: blogMessage";
    }

    //跳转博客详情页面
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        BlogDetailMessageDto detailedBlog = blogService.getDetailedBlogMessage(id);
        model.addAttribute("blog", detailedBlog);
        return "blog";
    }


}