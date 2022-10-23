package com.rui.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.pojo.Friend;
import com.rui.myblog.pojo.Resources;
import com.rui.myblog.mapper.ResourcesMapper;
import com.rui.myblog.service.IResourcesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
@Service
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources> implements IResourcesService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${upload}")
    private String files;

    @Override
    public List<Resources> getResource(String Resource) {
        LambdaQueryWrapper<Resources> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resources::getFirstType,Resource);
        return resourcesMapper.selectList(queryWrapper);
    }

    @Override
    public Resources getResourceByid(Long id) {
        return resourcesMapper.selectById(id);
    }


    @Override
    public Long getResourceTotal(String Resource) {
        LambdaQueryWrapper<Resources> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resources::getFirstType,Resource);
        Long count = Long.valueOf(resourcesMapper.selectCount(queryWrapper));
        return count;
    }

    @Override
    public int savePicture(Resources resources) {
        int insert = resourcesMapper.insert(resources);
        return insert;
    }

    @Override
    public List<Resources> listResources() {
        LambdaQueryWrapper<Resources> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resources::getPublished,true);
        queryWrapper.orderByDesc(Resources::getCreateTime);
        return resourcesMapper.selectList(queryWrapper);
    }

    @Override
    public int updateResource(Resources resources) {
        return resourcesMapper.updateById(resources);
    }

    @Override
    public void deleteResource(Long id) {
        int i = resourcesMapper.deleteById(id);
        if (i < 1){
            throw new ServiceException("删除资源失败，没有权限");
        }
    }

    @Override
    public void changePublished(boolean b, Long id) {
        Resources resources = resourcesMapper.selectById(id);
        if (resources == null){
            throw new ServiceException("该数据不存在");
        }
        resources.setPublished(b);
        int i = resourcesMapper.updateById(resources);
        if (i<1){
            throw new ServiceException("修改失败");
        }
    }

    @Override
    public int upload(Resources resources) {
        LambdaQueryWrapper<Resources> queryWrapper = new LambdaQueryWrapper<Resources>();
        queryWrapper.eq(Resources::getResourceName, resources.getResourceName());
        queryWrapper.eq(Resources::getPublished,true);
        Resources resources1 = resourcesMapper.selectOne(queryWrapper);
        if (resources1 != null){
            throw new ServiceException("已有资源，添加失败");
        }

        String content = "有网友为朴朴爱吃肉肉客栈新添了一个学习资源，为" + resources.getResourceName() + "请登陆网站查看是否上次";

        // 发送邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("朴朴爱吃肉肉客栈新增资源");  //主题
        simpleMailMessage.setText(content);   //内容
        simpleMailMessage.setTo("857092878@qq.com"); //接收者的邮箱
        simpleMailMessage.setFrom("purui6662@163.com");//发送者邮箱
        javaMailSender.send(simpleMailMessage);
//        resourcesMapper.insert(resources);
        return resourcesMapper.insert(resources);
    }

    @Override
    public int savefriend(MultipartFile file, Long id, String resourceName,String resourceAddress, String resourceDescription) {
        if (file.isEmpty()) {
            return 0;
        }
        String parentPath = System.getProperty("user.dir");


        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = files; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名

        LambdaQueryWrapper<Resources> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resources::getPictureAddress,fileName);
        Resources resources = resourcesMapper.selectOne(queryWrapper);

        if (resources != null){
            return 0;
        }
        try{
            File del = new File(file + resourcesMapper.selectById(id).getPictureAddress());
            if (del.exists()){
                del.delete();
            }
        }catch (Exception e){
            System.out.println(e);
        }

        Resources resources1 = new Resources();
        resources1.setFirstType("学习资源");
        resources1.setSecondType("学习资源");
        resources1.setPublished(true);
        resources1.setResourceName(resourceName);
        resources1.setPictureAddress(fileName);
        resources1.setResourceAddress(resourceAddress);
        resources1.setResourceDescription(resourceDescription);
        resources1.setCreateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        if (id == null){
            resourcesMapper.insert(resources1);
        }else {
            resources1.setId(id);
            resourcesMapper.updateById(resources1);
        }


        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
