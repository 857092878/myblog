package com.rui.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/20 10:29
 * Version1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogSearchDto {
    private String title;
    private Long typeId;
}
