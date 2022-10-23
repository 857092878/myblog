package com.rui.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/17 11:34
 * Version1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogAllDto {
    //博客信息
    private Long id;
    private String title;
    private String firstPicture;
    private Integer views;
    private Integer commentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String description;

    //分类名称
    private String typeName;

    //用户名
    private String nickname;
    //用户头像
    private String avatar;

}
