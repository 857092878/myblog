package com.rui.myblog.mapper;

import com.rui.myblog.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author purui
 * @since 2022-09-17
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    // 查询父级评论
    List<Comment> findByBlogIdParentId(@Param("blogId") Long blogId, @Param("blogParentId") Long blogParentId);

    //保存评论
    int saveComment(Comment comment);

    //根据妒忌评论id查询留言信息
    Comment getEmailByParentId(Long parentId);
}
