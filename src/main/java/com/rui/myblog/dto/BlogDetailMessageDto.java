package com.rui.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/17 19:05
 * Version1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDetailMessageDto {
    //博客信息
    private Long id;
    private String firstPicture;
    private String flag;
    private String title;
    private String content;
    private Integer views;
    private Integer commentCount;
    private LocalDateTime updateTime;
    private boolean commentabled;
    private boolean shareStatement;
    private boolean appreciation;
    private String nickname;
    private String avatar;

    //分类名称
    private String typeName;
}
