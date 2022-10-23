package com.rui.myblog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rui.myblog.dto.*;
import com.rui.myblog.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author purui
 * @since 2022-09-16
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {


    //查询首页最新博客列表信息
    List<BlogAllDto> getFirstPageBlog();
    //统计访问总数
    Integer selectViewCount();

    //查询blog,user,type连接的博客详细信息
    BlogDetailMessageDto selectDetailedBlogMessage(Long id);

    //增加浏览次数
    int updateAddViews(Long id);

    //查询评论数量并更新
    int getCommentCountById(Long id);

    List<BlogAllDto> getByTypeId(Long id);

    //查询文章管理列表
    List<BlogQueryDto> getAllBlogQuery();

    List<BlogQueryDto> getBlogBySearch(BlogSearchDto searchBlog);



}
