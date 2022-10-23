package com.rui.myblog.dto;

import com.rui.myblog.pojo.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/19 16:11
 * Version1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeDto {

    private Long id;
    private String name;
    private List<Blog> blogs = new ArrayList<>();
}
