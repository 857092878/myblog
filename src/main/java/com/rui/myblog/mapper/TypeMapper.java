package com.rui.myblog.mapper;

import com.rui.myblog.dto.TypeDto;
import com.rui.myblog.pojo.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
@Mapper
public interface TypeMapper extends BaseMapper<Type> {
//    查询所有分类
    List<TypeDto> getAllTypeAndBlog();

}
