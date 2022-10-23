package com.rui.myblog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.pojo.Resources;
import com.rui.myblog.redisMethod.ResourceTemplate;
import com.rui.myblog.service.IResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class ResourceAdminController {

    boolean flage;

    @Autowired
    private IResourcesService resourcesService;
    @Autowired
    private ResourceTemplate resourceTemplate;


    //    查询照片列表
    @GetMapping("/resources")
    public String pictures(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum,10);
        resourceTemplate.ResourcesList();
//        List<Resources> listResources = resourcesService.listResources();
        List<Resources> listResources = resourceTemplate.ResourcesList();
        PageInfo<Resources> pageInfo = new PageInfo<Resources>(listResources);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/resources";
    }

    //    跳转新增页面
    @GetMapping("/resources/input")
    public String input(Model model) {
        model.addAttribute("resource", new Resources());
        return "admin/resources-input";
    }


    //    资源新增
    @PostMapping("/resources")
    public String post(@Valid Resources resources, BindingResult result, RedirectAttributes attributes){
        resources.setPublished(true);
        if(result.hasErrors()){
            return "admin/resources-input";
        }

        int P = resourcesService.savePicture(resources);
        if (P == 0 ) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        resourceTemplate.resourcesDelete();
        return "redirect:/admin/resources";
    }

    //    跳转照片编辑页面
    @GetMapping("/resources/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("resource", resourcesService.getResourceByid(id));
        return "admin/resources-input";
    }

    //    编辑相册
//    @PostMapping("/resources/{id}")
//    public String editPost(@Valid Resources resources, RedirectAttributes attributes) {
//
//        int P = resourcesService.updateResource(resources);
//        if (P == 0 ) {
//            attributes.addFlashAttribute("message", "编辑失败");
//        } else {
//            attributes.addFlashAttribute("message", "编辑成功");
//        }
//        return "redirect:/admin/resources";
//    }

    //    删除资源
    @GetMapping("/resources/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        resourcesService.deleteResource(id);
        resourceTemplate.resourcesDelete();
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/resources";
    }

    @GetMapping("/resources/{id}/public")
    public String is_published(@PathVariable Long id, RedirectAttributes attributes){

        Resources resources = resourcesService.getResourceByid(id);

        flage = resources.getPublished();
        if(!flage){
            resourcesService.changePublished(true,id);
            attributes.addFlashAttribute("message", "成功发布");
            flage = !flage;
        }else{
            resourcesService.changePublished(false,id);
            attributes.addFlashAttribute("message", "取消发布");
            flage = !flage;
        }
        resourceTemplate.resourcesDelete();

        return "redirect:/admin/resources";
    }

    @RequestMapping("/resources/upload")
    public String rsourcesPost(@RequestParam(value = "file") MultipartFile file,
                             Long id,
                             String resourceName,
                             String resourceAddress,
                             String resourceDescription,
                             RedirectAttributes attributes
    ){


        int P = resourcesService.savefriend(file, id, resourceName,resourceAddress, resourceDescription);
        if (P == 0 ) {
            throw new ServiceException("失败");
        }
        resourceTemplate.resourcesDelete();
        return "redirect:/admin/resources";
    }


}