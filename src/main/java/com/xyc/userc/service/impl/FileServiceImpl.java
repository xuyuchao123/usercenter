package com.xyc.userc.service.impl;

import com.xyc.userc.service.FileService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by 1 on 2020/10/16.
 */
@Service("fileService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class FileServiceImpl implements FileService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public void uploadAttach(MultipartFile file) throws Exception
    {
        if (file == null || file.isEmpty())
        {
            LOGGER.error("未选择需上传的文件");
            throw new BusinessException(JsonResultEnum.FILE_NOT_EXIST);
        }
        LOGGER.info("开始上传附件方法 filename：{}",file.getOriginalFilename());
        String filePath = new File("upload/").getAbsolutePath();
        File fileUpload = new File(filePath);
        if(!fileUpload.exists())
        {
            fileUpload.mkdirs();
        }
        fileUpload = new File(filePath, UUID.randomUUID() + file.getOriginalFilename());
//        if(fileUpload.exists())
//        {
//            LOGGER.error("需要上传的文件已存在");
//            throw new BusinessException(JsonResultEnum.FILE_EXIST);
//        }
        file.transferTo(fileUpload);
        LOGGER.info("结束上传附件方法 filename：{}",file.getOriginalFilename());
    }
}
