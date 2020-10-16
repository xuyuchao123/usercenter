package com.xyc.userc.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 1 on 2020/10/16.
 */
public interface FileService
{
    public void uploadAttach(MultipartFile file) throws Exception;
}
