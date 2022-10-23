package com.rui.myblog.dto;

import com.rui.myblog.pojo.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/20 9:32
 * Version1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogQueryDto {
    private Long id;
    private String title;
    private Date createTime;
    private Date updateTime;
    private Boolean recommend;
    private Boolean published;
    private Long typeId;
    private TypeDto typeDto;
}
