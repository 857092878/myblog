package com.rui.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.pojo.Friend;
import com.rui.myblog.mapper.FriendMapper;
import com.rui.myblog.pojo.Picture;
import com.rui.myblog.service.IFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements IFriendService {

    @Autowired
    private FriendMapper friendMapper;
    @Value("${upload}")
    private String files;

    @Override
    public List<Friend> listFriend() {
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Friend::getCreateTime);
        return friendMapper.selectList(queryWrapper);
    }

    @Override
    public Friend getFriendByBlogaddress(String blogaddress) {
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getBlogaddress,blogaddress);
        return friendMapper.selectOne(queryWrapper);
    }

    @Override
    public int saveFriend(Friend friend) {
        return friendMapper.insert(friend);
    }

    @Override
    public Friend getFriendById(Long id) {
        Friend friend = friendMapper.selectById(id);
        return friend;
    }

    @Override
    public int updateFriend(Friend friend) {
        int i = friendMapper.updateById(friend);
        return i;
    }

    @Override
    public void deleteFriend(Long id) {
        int i = friendMapper.deleteById(id);
        if (i < 1){
            throw new ServiceException("没有权限删除");
        }
    }

    @Override
    public int savePicture(MultipartFile file, Long id, String blogname, String blogaddress) {
        if (file.isEmpty()) {
            return 0;
        }
        String parentPath = System.getProperty("user.dir");


        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = files; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名

        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getPictureaddress,fileName);
        Friend friend = friendMapper.selectOne(queryWrapper);

        if (friend != null){
            return 0;
        }

        Friend friend1 = new Friend();
        friend1.setPictureaddress(fileName);
        friend1.setBlogaddress(blogaddress);
        friend1.setBlogname(blogname);
        friend1.setCreateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        if (id == null){
            friendMapper.insert(friend1);
        }else {
            friend1.setId(id);
            friendMapper.updateById(friend1);
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
