package com.rui.myblog.controller;

import com.rui.myblog.redisMethod.MemoryRemplate;
import com.rui.myblog.service.IMemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
@Controller
public class ArchiveController {

    @Autowired
    private IMemoryService memoryService;
    @Autowired
    private MemoryRemplate memoryRemplate;

    @GetMapping("/archives")
    public String archive(Model model){
        model.addAttribute("memorys",  memoryRemplate.memoryList());
        return "archives";
    }

}