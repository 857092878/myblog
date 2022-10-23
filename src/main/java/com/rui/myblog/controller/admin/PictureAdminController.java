package com.rui.myblog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.pojo.Picture;
import com.rui.myblog.redisMethod.PictureRemplate;
import com.rui.myblog.service.IPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 照片墙后台管理控制器
 * @Date: Created in 11:30 2020/6/15
 * @Author: ONESTAR
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
@Controller
@RequestMapping("/admin")
public class PictureAdminController {

    @Autowired
    private IPictureService pictureService;

    @Autowired
    private PictureRemplate pictureRemplate;

    //    查询照片列表
    @GetMapping("/pictures")
    public String pictures(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum,10);

        List<Picture> pictures = pictureRemplate.pictureList();
        PageInfo<Picture> pageInfo = new PageInfo<Picture>(pictures);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/pictures";
    }

    //    跳转新增页面
    @GetMapping("/pictures/input")
    public String input(Model model) {
        model.addAttribute("picture", new Picture());
        return "admin/pictures-input";
    }

    //    照片新增
    @RequestMapping("/pictures/upload")
    public String picturePost(@RequestParam(value = "file") MultipartFile file,
                              Long id,
                              String picturename,
                              String picturetime,
                              String picturedescription
                             ){


        int P = pictureService.savePicture(file, id, picturename, picturetime, picturedescription);
        if (P == 0 ) {
           throw new ServiceException("失败");
        }
        pictureRemplate.pictureDelete();

        return "redirect:/admin/pictures";
    }

    //    跳转照片编辑页面
    @GetMapping("/pictures/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("picture", pictureService.getPicture(id));
        return "admin/pictures-input";
    }


    //    删除照片
    @GetMapping("/pictures/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        pictureService.deletePicture(id);
        pictureRemplate.pictureDelete();
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/pictures";
    }


}