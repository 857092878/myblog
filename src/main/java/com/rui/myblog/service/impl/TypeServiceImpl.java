package com.rui.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.dto.TypeDto;
import com.rui.myblog.pojo.Type;
import com.rui.myblog.mapper.TypeMapper;
import com.rui.myblog.service.ITypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rui.myblog.utils.CopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Override
    public List<TypeDto> getAllTypeAndBlog() {
        return typeMapper.getAllTypeAndBlog();
    }

    @Override
    public Type getType(Long id) {
        return typeMapper.selectById(id);
    }

    @Override
    public List<TypeDto> getAllType() {
        List<Type> types = typeMapper.selectList(null);
        return CopyUtils.CopyList(types,TypeDto.class);
    }

    @Override
    public TypeDto getTypeByName(String name) {
        LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Type::getName,name);
        Type type = typeMapper.selectOne(queryWrapper);
        if (type == null){
            return null;
        }
        TypeDto typeDto = new TypeDto();
        BeanUtils.copyProperties(type,typeDto);
        return typeDto;
    }

    @Override
    public int saveType(TypeDto type) {
        Type type1 = new Type();
        BeanUtils.copyProperties(type,type1);
        return typeMapper.insert(type1);
    }

    @Override
    public int updateType(TypeDto type) {
        Type type1 = new Type();
        BeanUtils.copyProperties(type,type1);
        return typeMapper.updateById(type1);
    }

    @Override
    public void deleteType(Long id) {
        int i = typeMapper.deleteById(id);
        if (i < 1){
            throw new ServiceException("删除失败，没有权限");
        }
    }

}
