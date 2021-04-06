package com.xyc.userc.service;

import com.xyc.userc.entity.Violation;
import com.xyc.userc.entity.ViolationDetail;
import com.xyc.userc.entity.ViolationInfo;
import com.xyc.userc.vo.ViolationInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by 1 on 2020/10/22.
 */
public interface ViolationService
{
    List<Violation> getAllViolation() throws Exception;

    List<ViolationDetail> getViolationDetail(Integer typeId) throws Exception;

    void addViolationInfo(String billDep,String billTime,String billStaff,String billNum,String fineAmt,
                          String violationImgPath,String paymentStatus,String fineReason) throws Exception;

    List getViolationInfo(String billType,String billDep,String billTime,String paymentStatus,
                                           String billNum,String page,String size) throws Exception;

//    byte[] getViolationImg(Integer id) throws Exception;

    String uploadViolationImg(MultipartFile violationImg) throws Exception;

    void removeViolationInfo(List billNumList) throws Exception;
}
