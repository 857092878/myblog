package com.rui.myblog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rui.myblog.pojo.Memory;
import com.rui.myblog.redisMethod.MemoryRemplate;
import com.rui.myblog.service.IMemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/20 18:56
 * Version1.0.0
 */

@Controller
@RequestMapping("/admin")
public class MemoryAdminController {

    @Autowired
    private IMemoryService memoryService;
    @Autowired
    private MemoryRemplate memoryRemplate;

    // 查询流年记列表
    @GetMapping("/memorys")
    public String pictures(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum,10);
        List<Memory> listMemory = memoryRemplate.memoryList();
        PageInfo<Memory> pageInfo = new PageInfo<Memory>(listMemory);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/memorys";
    }

    //    跳转新增页面
    @GetMapping("/memorys/input")
    public String input(Model model) {
        model.addAttribute("memory", new Memory());
        return "admin/memorys-input";
    }


    @RequestMapping("/memorys/upload")
    public String memoryPost(@RequestParam(value = "file") MultipartFile file,
                             Long id,
                             String memory){

        memoryService.upload(file,id,memory);
        memoryRemplate.memoryDelete();


        return "redirect:/admin/memorys";
    }


//    //    流年记新增
//    @PostMapping("/memorys")
//    public String post(@Valid Memory memory, BindingResult result, RedirectAttributes attributes){
//
//        if(result.hasErrors()){
//            return "admin/memorys-input";
//        }
//
//        memory.setCreateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
//        int P = memoryService.saveMemory(memory);
//        if (P == 0 ) {
//            attributes.addFlashAttribute("message", "新增记忆失败,没有权限");
//        } else {
//            attributes.addFlashAttribute("message", "新增记忆成功");
//        }
//        return "redirect:/admin/memorys";
//    }

    //    跳转流年编辑页面
    @GetMapping("/memorys/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("memory", memoryService.getMemory(id));
        return "admin/memorys-input";
    }

//    //    编辑流年
//    @PostMapping("/memorys/{id}")
//    public String editPost(@Valid Memory memory, RedirectAttributes attributes) {
//
//        int P = memoryService.updateMemory(memory);
//        if (P == 0 ) {
//            attributes.addFlashAttribute("message", "编辑记忆失败");
//        } else {
//            attributes.addFlashAttribute("message", "编辑记忆成功");
//        }
//        return "redirect:/admin/memorys";
//    }

    //    删除记忆
    @GetMapping("/memorys/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        memoryService.deleteMemory(id);
        memoryRemplate.memoryDelete();
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/memorys";
    }
}
