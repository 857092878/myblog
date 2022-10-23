package com.rui.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/17 11:39
 * Version1.0.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewRecommenededBlogDto {
    private Long id;
    private String title;
    private String firstPicture;
    private boolean recommend;
}
