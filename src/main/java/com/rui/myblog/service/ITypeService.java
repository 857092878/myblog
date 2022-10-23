package com.rui.myblog.service;

import com.rui.myblog.dto.TypeDto;
import com.rui.myblog.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
public interface ITypeService extends IService<Type> {

    //查询所有分类
    List<TypeDto> getAllTypeAndBlog();

    Type getType(Long id);

    List<TypeDto> getAllType();

    //根据name查询TypeDto
    TypeDto getTypeByName(String name);

    //保存type
    int saveType(TypeDto type);

    //修改分类
    int updateType(TypeDto type);

    //删除分类
    void deleteType(Long id);
}
