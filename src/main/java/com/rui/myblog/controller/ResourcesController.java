package com.rui.myblog.controller;


import com.rui.myblog.annotiation.AccessLimit;
import com.rui.myblog.pojo.Resources;
import com.rui.myblog.redisMethod.ResourceTemplate;
import com.rui.myblog.service.IResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
public class ResourcesController {
    @Autowired
    private IResourcesService resourcesService;
    @Autowired
    private ResourceTemplate resourceTemplate;

    @GetMapping("/resources")
    public String resources(Model model){

        // 因为这里就这几种资源，所以写死了，有需要的可以加一个资源分类表
        String studyResource = "学习资源";

        //获取资源列表
        model.addAttribute("studyresource", resourceTemplate.StudyResourcesList());
        //获取资源数量
        model.addAttribute("studyResourceTotle",resourcesService.getResourceTotal(studyResource));
        return "resources";
    }

    // 资源新增
    @AccessLimit(seconds = 15, maxCount = 3) //15秒内 允许请求3次
    @PostMapping("/resources")
    public String post(Resources resources, BindingResult result, RedirectAttributes attributes){
        resources.setPublished(false);
        resources.setFirstType("学习资源");
        resources.setSecondType("学习资源");
        if(result.hasErrors()){
            return "admin/resources-input";
        }

        int P = resourcesService.upload(resources);
        if (P == 0 ) {
            attributes.addFlashAttribute("message", "添加资源失败");
        } else {
            attributes.addFlashAttribute("message", "添加资源成功，管理员审核通过后即可在该页面查看，请耐心等待~");
        }
        return "redirect:/resources";

    }
}
