package com.rui.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.pojo.Memory;
import com.rui.myblog.pojo.Picture;
import com.rui.myblog.mapper.PictureMapper;
import com.rui.myblog.service.IPictureService;
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
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    @Autowired
    private PictureMapper pictureMapper;
    @Value("${upload}")
    private String files;

    @Override
    public List<Picture> listPicture() {
        return pictureMapper.selectList(null);
    }


    @Override
    public Picture getPicture(Long id) {
        return pictureMapper.selectById(id);
    }

    @Override
    public int updatePicture(Picture picture) {
        return pictureMapper.updateById(picture);
    }

    @Override
    public void deletePicture(Long id) {
        int i = pictureMapper.deleteById(id);
        if (i < 1){
            throw new ServiceException("编辑图片失败，没有权限");
        }
    }

    @Override
    public int savePicture(MultipartFile file, Long id, String picturename, String picturetime, String picturedescription) {

        if (file.isEmpty()) {
            return 0;
        }
        String parentPath = System.getProperty("user.dir");


        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = files; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名

        LambdaQueryWrapper<Picture> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Picture::getPictureaddress,fileName);
        Picture picture = pictureMapper.selectOne(queryWrapper);

        if (picture != null){
            return 0;
        }
        try{
            File del = new File(file + pictureMapper.selectById(id).getPictureaddress());
            if (del.exists()){
                del.delete();
            }
        }catch (Exception e){
            System.out.println(e);
        }

        Picture picture1 = new Picture();
        picture1.setPictureaddress(fileName);
        picture1.setPicturename(picturename);
        picture1.setPicturetime(picturename);
        picture1.setPicturedescription(picturedescription);
        if (id == null){
            pictureMapper.insert(picture1);
        }else {
            picture1.setId(id);
            pictureMapper.updateById(picture1);
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
