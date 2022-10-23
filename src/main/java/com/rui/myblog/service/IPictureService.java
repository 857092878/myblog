package com.rui.myblog.service;

import com.rui.myblog.pojo.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author purui
 * @since 2022-09-19
 */
public interface IPictureService extends IService<Picture> {

    //查询所有图片
    List<Picture> listPicture();



    //查找图片
    Picture getPicture(Long id);

    //更新图片
    int updatePicture(Picture picture);

    //删除图片
    void deletePicture(Long id);

    int savePicture(MultipartFile file, Long id, String picturename, String picturetime, String picturedescription);
}
