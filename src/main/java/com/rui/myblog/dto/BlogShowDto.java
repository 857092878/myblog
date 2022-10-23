package com.rui.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/20 9:55
 * Version1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogShowDto {
    private Long id;
    private String flag;
    private String title;
    private String content;
    private Long typeId;
    private String firstPicture;
    private String description;
    private boolean recommend;
    private boolean published;
    private boolean shareStatement;
    private boolean appreciation;
    private boolean commentabled;
    private LocalDateTime updateTime;
}
