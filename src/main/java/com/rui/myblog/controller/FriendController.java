package com.rui.myblog.controller;


import com.rui.myblog.redisMethod.FriendRemplate;
import com.rui.myblog.service.IFriendService;
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
public class FriendController {
    @Autowired
    private IFriendService friendService;
    @Autowired
    private FriendRemplate friendRemplate;

    @GetMapping("/friends")
    public String friends(Model model) {
        model.addAttribute("friendlinks",friendRemplate.friendList());
        return "friends";
    }
}
