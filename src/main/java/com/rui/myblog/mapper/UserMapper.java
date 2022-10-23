package com.rui.myblog.mapper;

import com.rui.myblog.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author purui
 * @since 2022-09-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
