package com.rui.myblog.service;

import com.rui.myblog.pojo.Memory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author purui
 * @since 2022-09-16
 */
public interface IMemoryService extends IService<Memory> {

    //查询记忆
    List<Memory> listMemory();

    //保存记忆
    int saveMemory(Memory memory);

    //查询记忆
    Memory getMemory(Long id);

    //更新记忆
    int updateMemory(Memory memory);

    //删除记忆
    void deleteMemory(Long id);

    //上传记忆
    int upload(MultipartFile file, Long id, String memory);

}
