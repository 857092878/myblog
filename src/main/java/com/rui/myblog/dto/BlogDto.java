package com.rui.myblog.dto;

import com.rui.myblog.pojo.Comment;
import com.rui.myblog.pojo.Type;
import com.rui.myblog.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/20 8:51
 * Version1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto {
    private Long id;
    private String title;
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private Integer commentCount;
    private boolean appreciation;
    private boolean shareStatement;
    private boolean commentabled;
    private boolean published;
    private boolean recommend;
    private Date createTime;
    private Date updateTime;
    private String description;

    private Type type;
    private User user;
    private Long typeId;
    private Long userId;
    private List<Comment> comments = new ArrayList<>();
}
