package com.rui.myblog.mapper;

import com.rui.myblog.pojo.Message;
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
public interface MessageMapper extends BaseMapper<Message> {

    //查询父级留言
    List<Message> findByParentId(@Param("ParentId") Long ParentId);

    Message getEmailByParentId(Long parentId);

    //保存留言
    int saveMessage(Message message);

}
