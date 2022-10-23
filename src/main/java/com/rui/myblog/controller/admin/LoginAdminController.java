package com.rui.myblog.controller.admin;

import com.rui.myblog.pojo.User;
import com.rui.myblog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/20 18:56
 * Version1.0.0
 */

@Controller
@RequestMapping("/admin")
public class LoginAdminController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    /*
    登陆
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (user == null) {
            attributes.addFlashAttribute("message", "账号错误");
            return "redirect:/admin";

        }

        if (!password.equals(user.getPassword() )) {
            attributes.addFlashAttribute("message", "密码错误");
            return "redirect:/admin";
        }
        user.setPassword(null);
        session.setAttribute("user",user);
        session.setMaxInactiveInterval(-1);     // 设置session永不过期
        return "admin/index";
    }

    /*
    注销
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
