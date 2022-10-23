package com.rui.myblog.service;

import com.rui.myblog.pojo.Resources;
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
public interface IResourcesService extends IService<Resources> {

    //获取资源
    List<Resources> getResource(String Resource);

    //获取资源
    Resources getResourceByid(Long id);

    Long getResourceTotal(String Resource);

    int savePicture(Resources resources);

    //查询照片资源
    List<Resources> listResources();

    //编辑资源
    int updateResource(Resources resources);

    //删除资源
    void deleteResource(Long id);

    //发布资源
    void changePublished(boolean b, Long id);

    //发送给管理员
    int upload(Resources resources);

    int savefriend(MultipartFile file, Long id, String resourceName,String resourceAddress, String resourceDescription);
}
