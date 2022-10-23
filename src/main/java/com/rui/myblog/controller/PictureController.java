package com.rui.myblog.controller;

import com.rui.myblog.pojo.Picture;
import com.rui.myblog.redisMethod.PictureRemplate;
import com.rui.myblog.service.IPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
@Controller
public class PictureController {


    @Autowired
    private IPictureService pictureService;
    @Autowired
    private PictureRemplate pictureRemplate;

    @GetMapping("/picture")
    public String friends(Model model) {

        List<Picture> pictures = pictureRemplate.pictureList();

        model.addAttribute("pictures",pictures);
        return "picture";
    }

}
