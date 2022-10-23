package com.rui.myblog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.pojo.Friend;
import com.rui.myblog.redisMethod.FriendRemplate;
import com.rui.myblog.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/20 17:47
 * Version1.0.0
 */
@Controller
@RequestMapping("/admin")
public class FriendAdminController {
    @Autowired
    private IFriendService friendService;
    @Autowired
    private FriendRemplate friendRemplate;

    //查询所有友链
    @GetMapping("/friendlinks")
    public String friend(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageHelper.startPage(pageNum,10);
        List<Friend> listFriendLink = friendRemplate.friendList();
        PageInfo<Friend> pageInfo = new PageInfo<Friend>(listFriendLink);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/friendlinks";
    }

    //跳转友链新增页面
    @GetMapping("/friendlinks/input")
    public String input(Model model) {
        model.addAttribute("friendlink", new Friend());
        return "admin/friendlinks-input";
    }

    //友链新增
//    @PostMapping("/friendlinks")
//    public String post(@Valid Friend friend, BindingResult result, RedirectAttributes attributes){
//
//        Friend type1 = friendService.getFriendByBlogaddress(friend.getBlogaddress());
//        if (type1 != null) {
//            attributes.addFlashAttribute("message", "不能添加相同的网址");
//            return "redirect:/admin/friendlinks/input";
//        }
//
//        if(result.hasErrors()){
//            return "admin/friendlinks-input";
//        }
//        friend.setCreateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
//        int F = friendService.saveFriend(friend);
//        if (F == 0 ) {
//            attributes.addFlashAttribute("message", "新增失败");
//        } else {
//            attributes.addFlashAttribute("message", "新增成功");
//        }
//        return "redirect:/admin/friendlinks";
//    }
    @RequestMapping("/friend/upload")
    public String friendPost(@RequestParam(value = "file") MultipartFile file,
                              Long id,
                              String blogname,
                              String blogaddress
    ){


        int P = friendService.savePicture(file, id, blogname, blogaddress);
        if (P == 0 ) {
            throw new ServiceException("失败");
        }
        return "redirect:/admin/friendlinks";
    }

    //跳转友链修改页面
    @GetMapping("/friendlinks/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("friendlink", friendService.getFriendById(id));
        return "admin/friendlinks-input";
    }

    //编辑修改友链
    @PostMapping("/friendlinks/{id}")
    public String editPost(@Valid Friend friend, RedirectAttributes attributes) {
        int t = friendService.updateFriend(friend);
        friendRemplate.friendDelete();
        if (t == 0 ) {
            attributes.addFlashAttribute("message", "编辑失败");
        } else {
            attributes.addFlashAttribute("message", "编辑成功");
        }
        return "redirect:/admin/friendlinks";
    }

    //删除友链
    @GetMapping("/friendlinks/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        friendService.deleteFriend(id);
        friendRemplate.friendDelete();
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/friendlinks";
    }
}
