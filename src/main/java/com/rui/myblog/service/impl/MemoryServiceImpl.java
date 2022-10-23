package com.rui.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rui.myblog.Exception.ServiceException;
import com.rui.myblog.pojo.Memory;
import com.rui.myblog.mapper.MemoryMapper;
import com.rui.myblog.service.IMemoryService;
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
 * @since 2022-09-16
 */
@Service
public class MemoryServiceImpl extends ServiceImpl<MemoryMapper, Memory> implements IMemoryService {
    @Autowired
    private MemoryMapper memoryMapper;
    @Value("${upload}")
    private String files;


    @Override
    public List<Memory> listMemory() {

        LambdaQueryWrapper<Memory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Memory::getId);
        List<Memory> memories = memoryMapper.selectList(queryWrapper);
        return memories;
    }

    @Override
    public int saveMemory(Memory memory) {
        int insert = memoryMapper.insert(memory);

        return insert;
    }

    @Override
    public Memory getMemory(Long id) {
        Memory memory = memoryMapper.selectById(id);
        if (memory == null){
            throw new ServiceException("查询失败");
        }
        return memory;
    }

    @Override
    public int updateMemory(Memory memory) {
        return memoryMapper.updateById(memory);
    }

    @Override
    public void deleteMemory(Long id) {
        int i = memoryMapper.deleteById(id);
        if (i < 1){
            throw new ServiceException("删除记忆失败，没有权限");
        }
    }


    @Override
    public int upload(MultipartFile file,Long id,String memory){

        if (file.isEmpty()) {
            return 0;
        }
        String parentPath = System.getProperty("user.dir");


        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = files; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名

        LambdaQueryWrapper<Memory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Memory::getPictureAddress,fileName);
        Memory memory1 = memoryMapper.selectOne(queryWrapper);

        if (memory1 != null){
            return 0;
        }

        Memory memory2 = new Memory();
        memory2.setMemory(memory);
        memory2.setId(id);
        memory2.setPictureAddress(fileName);
        memory2.setCreateTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        if (id != null){
            memoryMapper.updateById(memory2);
        }
        memoryMapper.insert(memory2);


        try{
            String pictureAddress = memoryMapper.selectById(id).getPictureAddress();
            File dest = new File(filePath + pictureAddress);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
        }catch (Exception e){
            System.out.println(e);
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
        return 0;
    }
}
