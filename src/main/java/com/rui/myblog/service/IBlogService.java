package com.rui.myblog.service;

import com.rui.myblog.dto.*;
import com.rui.myblog.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author purui
 * @since 2022-09-16
 */
public interface IBlogService extends IService<Blog> {

    //最近评论
    List<NewCommentDto> getNewComment();
    //最近推荐博客
    List<NewRecommenededBlogDto> getRecommendedBlog();
    //所有博客信息
    List<BlogAllDto> getAllBlog();
    //所有博客信息带搜索功能
    List<BlogAllDto> getAllBlog(String query);
    //统计博客信息
    Map<String,Object> blogMessage();

    BlogDetailMessageDto getDetailedBlogMessage(Long id);
    //根据TypeId查询博客列表，显示在分类页面
    List<BlogAllDto> getByTypeId(Long id);

    //保存博客
    int saveBlog(BlogDto blogDto);

    //管理员博客列表
    List<BlogQueryDto> getAllBlogQuery();

    void deleteBlog(Long id);

    BlogShowDto getBlogById(Long id);

    //保存博客
    int updateBlog(BlogShowDto showBlog);

    //搜索管理
    List<BlogQueryDto> getBlogBySearch(BlogSearchDto searchBlog);

    int upload(MultipartFile file, Long id, String title,String content, Long typeId,String description,String recommend,String shareStatement, String appreciation, String commentabled,Long userId);
}
