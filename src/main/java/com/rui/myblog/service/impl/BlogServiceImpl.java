package com.rui.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.dto.*;
import com.rui.myblog.mapper.CommentMapper;
import com.rui.myblog.mapper.MessageMapper;
import com.rui.myblog.mapper.UserMapper;
import com.rui.myblog.pojo.Blog;
import com.rui.myblog.mapper.BlogMapper;
import com.rui.myblog.pojo.Memory;
import com.rui.myblog.pojo.User;
import com.rui.myblog.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rui.myblog.utils.CopyUtils;
import com.rui.myblog.utils.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author purui
 * @since 2022-09-16
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;
    @Value("${upload}")
    private String files;

    @Override
    public List<NewCommentDto> getNewComment() {

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        //取出最新的4条
        queryWrapper.orderByDesc(Blog::getCreateTime).last("limit 4");

        List<Blog> blogs = blogMapper.selectList(queryWrapper);

        return CopyUtils.CopyList(blogs,NewCommentDto.class);
    }

    @Override
    public List<NewRecommenededBlogDto> getRecommendedBlog() {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        //取出最新的4条
        queryWrapper.orderByDesc(Blog::getCreateTime).last("limit 4");

        List<Blog> blogs = blogMapper.selectList(queryWrapper);

        return CopyUtils.CopyList(blogs,NewRecommenededBlogDto.class);

    }

    @Override
    public List<BlogAllDto> getAllBlog() {
//        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<Blog>();
//
//        queryWrapper.orderByDesc(Blog::getCreateTime);
//        List<Blog> blogs = blogMapper.selectList(queryWrapper);
        return blogMapper.getFirstPageBlog();
    }

    @Override
    public List<BlogAllDto> getAllBlog(String query) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("create_time");

        queryWrapper.like("title",query);

        List<Blog> blogs = blogMapper.selectList(queryWrapper);

        return CopyUtils.CopyList(blogs, BlogAllDto.class);
    }

    @Override
    public Map<String, Object> blogMessage() {
        /*
        统计博客条数
        统计访问总数
        统计评论总数
        统计留言总数
         */
        Map<String,Object> map = new HashMap<>();

        map.put("blogTotal",blogMapper.selectCount(null));
        map.put("blogViewTotal",blogMapper.selectViewCount());
        map.put("blogCommentTotal",commentMapper.selectCount(null));
        map.put("blogMessageTotal",messageMapper.selectCount(null));

        return map;
    }

    @Override
    public BlogDetailMessageDto getDetailedBlogMessage(Long id) {
        BlogDetailMessageDto blogDetailMessageDto = blogMapper.selectDetailedBlogMessage(id);

        if (null == blogDetailMessageDto){
            throw new ServiceException("该条内容不存在");
        }

        //将取得的内容转成markdown显示在html上
        blogDetailMessageDto.setContent(MarkdownUtils.markdownToHtmlExtensions(blogDetailMessageDto.getContent()));

        blogMapper.updateAddViews(id);
        blogMapper.getCommentCountById(id);

        return blogDetailMessageDto;
    }

    @Override
    public List<BlogAllDto> getByTypeId(Long id) {
        return blogMapper.getByTypeId(id);
    }

    @Override
    public int saveBlog(BlogDto blogDto) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDto,blog);
        blog.setCreateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        blog.setUpdateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        blog.setViews("0");
        blog.setCommentCount(0);
        int insert = blogMapper.insert(blog);
        return insert;
    }

    @Override
    public List<BlogQueryDto> getAllBlogQuery() {
        List<BlogQueryDto> allBlogQuery = blogMapper.getAllBlogQuery();
        return allBlogQuery;
    }

    @Override
    public void deleteBlog(Long id) {
        int i = blogMapper.deleteById(id);
        if (i <1){
            throw new ServiceException("删除博客失败，没有权限");
        }
    }

    @Override
    public BlogShowDto getBlogById(Long id) {
        Blog blog = blogMapper.selectById(id);
        BlogShowDto blogShowDto = new BlogShowDto();
        BeanUtils.copyProperties(blog,blogShowDto);
        return blogShowDto;
    }

    @Override
    public int updateBlog(BlogShowDto showBlog) {
        showBlog.setUpdateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        Blog blog = new Blog();
        BeanUtils.copyProperties(showBlog,blog);
        int i = blogMapper.updateById(blog);
        return i;
    }

    @Override
    public List<BlogQueryDto> getBlogBySearch(BlogSearchDto searchBlog) {
        List<BlogQueryDto> blogQueryDtos = blogMapper.getBlogBySearch(searchBlog);
        return blogQueryDtos;
    }

    @Override
    public int upload(MultipartFile file,
                          Long id,
                          String title,
                          String content,
                          Long typeId,
                          String description,
                          String recommend,
                          String shareStatement,
                          String appreciation,
                          String commentabled,
                          Long userId) {

        if (file.isEmpty()) {
            return 0;
        }
        String parentPath = System.getProperty("user.dir");


        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = files; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getFirstPicture,fileName);
        Blog blog = blogMapper.selectOne(queryWrapper);
        if (blog != null){
            return 0;
        }
        try{
            File del = new File(file + blog.getFirstPicture());
            if (del.exists()){
                del.delete();
            }
        }catch (Exception e){
            System.out.println("没有图片");
        }


        Blog blog1 = new Blog();
        blog1.setTitle(title);
        blog1.setContent(content);
        blog1.setFlag("1");
        blog1.setFirstPicture(fileName);
        blog1.setTypeId(typeId);
        blog1.setRecommend(recommend != null );
        blog1.setDescription(description);
        blog1.setShareStatement(shareStatement != null);
        blog1.setAppreciation(appreciation != null);
        blog1.setCommentabled(commentabled != null);
        blog1.setCreateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        blog1.setUpdateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        blog1.setViews("0");
        blog1.setUserId(userId);
        blog1.setPublished(true);

        if (id == null){
            blog1.setCommentCount(0);
            blogMapper.insert(blog1);

//            BlogShowDto blogShowDto = new BlogShowDto();
//            blogShowDto.setTitle(title);
//            blogShowDto.setContent(content);
//            blogShowDto.setFlag("1");
//            blogShowDto.setFirstPicture(fileName);
//            blogShowDto.setTypeId(typeId);
//            blogShowDto.setRecommend(recommend != null );
//            blogShowDto.setShareStatement(shareStatement != null);
//            blogShowDto.setAppreciation(appreciation != null);
//            blogShowDto.setCommentabled(commentabled != null);
//            updateBlog(blogShowDto);

        }else {
//            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
//            queryWrapper1.eq(User::getUsername, username);
//
//            User user = userMapper.selectOne(queryWrapper1);
            blog1.setId(id);
            blogMapper.updateById(blog1);
//            BlogDto blogDto = new BlogDto();
//            blogDto.setTitle(title);
//            blogDto.setContent(content);
//            blogDto.setFlag("1");
//            blogDto.setFirstPicture(fileName);
//            blogDto.setTypeId(typeId);
//            blogDto.setRecommend(recommend != null );
//            blogDto.setShareStatement(shareStatement != null);
//            blogDto.setAppreciation(appreciation != null);
//            blogDto.setCommentabled(commentabled != null);
//            saveBlog(blogDto);
        }

        try{
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return 1;
    }

}
