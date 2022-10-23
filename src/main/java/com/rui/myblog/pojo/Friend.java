package com.rui.myblog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_friend")
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String blogaddress;

    private String blogname;

    private LocalDateTime createTime;

    private String pictureaddress;


}
