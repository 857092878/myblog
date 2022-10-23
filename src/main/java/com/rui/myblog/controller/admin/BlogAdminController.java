package com.rui.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rui.myblog.dto.*;
import com.rui.myblog.pojo.User;
import com.rui.myblog.redisMethod.impl.BlogRemplateImpl;
import com.rui.myblog.service.IBlogService;
import com.rui.myblog.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Author 蒲锐
 * @CreateTme 2022/9/20 8:47
 * Version1.0.0
 */
@Controller
@RequestMapping("/admin")
public class BlogAdminController {

    @Autowired
    private IBlogService blogService;
    @Autowired
    private ITypeService typeService;
    @Autowired
    private BlogRemplateImpl blogRemplate;
    @Value("${upload}")
    private String files;

    //跳转博客新增页面
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("blog", new BlogDto());
        return "admin/blogs-input";
    }



    //博客列表
    @RequestMapping("/blogs")
    public String blogs(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){

        //按照排序字段 倒序 排序
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum,10,orderBy);
        List<BlogQueryDto> list = blogService.getAllBlogQuery();
        PageInfo<BlogQueryDto> pageInfo = new PageInfo<BlogQueryDto>(list);

        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("pageInfo",pageInfo);
        return "admin/blogs";

    }

    //删除博客
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        blogRemplate.BlogDelete();
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }

    //跳转编辑修改文章
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        BlogShowDto blogById = blogService.getBlogById(id);
        List<TypeDto> allType = typeService.getAllType();
        model.addAttribute("blog", blogById);
        model.addAttribute("types", allType);
        return "admin/blogs-input";
    }

//    //编辑修改文章
//    @PostMapping("/blogs/{id}")
//    public String editPost(@Valid BlogShowDto showBlog, RedirectAttributes attributes) {
//        int b = blogService.updateBlog(showBlog);
//        if(b == 0){
//            attributes.addFlashAttribute("message", "修改失败");
//        }else {
//            attributes.addFlashAttribute("message", "修改成功");
//            blogRemplate.BlogDelete();
//        }
//        return "redirect:/admin/blogs";
//    }

    //搜索博客管理列表
    @PostMapping("/blogs/search")
    public String search(BlogSearchDto searchBlog, Model model,
                         @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        List<BlogQueryDto> blogBySearch = blogService.getBlogBySearch(searchBlog);
        PageHelper.startPage(pageNum, 10);
        PageInfo<BlogQueryDto> pageInfo = new PageInfo<>(blogBySearch);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/blogs :: blogList";
    }
//    //博客新增
//    @PostMapping("/blogs")
//    public String post(BlogDto blogDto, RedirectAttributes attributes, HttpSession session){
//        //新增的时候需要传递blog对象，blog对象需要有user
//        blogDto.setUser((User) session.getAttribute("user"));
//        //设置blog的type
//        blogDto.setType(typeService.getType(blogDto.getType().getId()));
//        //设置blog中typeId属性
//        blogDto.setTypeId(blogDto.getType().getId());
//        //设置用户id
//        blogDto.setUserId(blogDto.getUser().getId());
//
//        int b = blogService.saveBlog(blogDto);
//        if(b == 0){
//            attributes.addFlashAttribute("message", "新增失败");
//        }else {
//            attributes.addFlashAttribute("message", "新增成功");
//            blogRemplate.BlogDelete();
//        }
//        return "redirect:/admin/blogs";
//    }

    @RequestMapping("/blogs/upload")
    public String blogPost(@RequestParam(value = "file") MultipartFile file,
                             Long id,
                             String title,
                             String content,
                             Long typeId,
                             String description,
                             String recommend,
                             String shareStatement,
                             String appreciation,
                             String commentabled,
                             RedirectAttributes attributes, HttpSession session){

        User user = (User) session.getAttribute("user");
        Long userId = user.getId();

        int b = blogService.upload(file, id, title, content, typeId, description, recommend, shareStatement, appreciation, commentabled,userId);

        if(b == 0){
            attributes.addFlashAttribute("message", "失败");
        }else {
            attributes.addFlashAttribute("message", "成功");
            blogRemplate.BlogDelete();
        }
        return "redirect:/admin/blogs";
    }

    //图片添加
    @GetMapping("/blogs/picture/add")
    public String pictureAdd(){
        return "admin/pictureUpload.html";
    }
    //图片添加
    @PostMapping("/blogs/picture/upload")
    @ResponseBody
    public String pictureUpload(@RequestParam(value = "file") MultipartFile file){

        if (file == null){
            return "<h2>文件上传失败</h2>";
        }

        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = files; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名

        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return"<h2>" + "图片地址为："+ "http" + "端口号:8088" + fileName + "</h2>";
    }

}
