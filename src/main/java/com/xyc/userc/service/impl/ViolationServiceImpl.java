package com.xyc.userc.service.impl;

import com.xyc.userc.service.ViolationService;
import com.xyc.userc.util.BusinessException;
import com.xyc.userc.util.JsonResultEnum;
import com.xyc.userc.util.PageUtil;
import com.xyc.userc.vo.BindedUserRoleVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xyc.userc.dao.ViolationMapper;
import com.xyc.userc.entity.Violation;
import com.xyc.userc.entity.ViolationDetail;
import com.xyc.userc.entity.ViolationInfo;
import com.xyc.userc.vo.ViolationInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by 1 on 2020/10/22.
 */
@Service("violationService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class ViolationServiceImpl implements ViolationService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ViolationServiceImpl.class);

    @Value("${file.rootPath}")
    private String fileRootPath;

    @Value("${file.sonPath}")
    private String fileSonPath;


    //获取主机端口
    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Autowired
    private ViolationMapper violationMapper;

    @Override
    public List<Violation> getAllViolation() throws Exception {
        LOGGER.info("进入查询违章大类方法");
        List<Violation> violations = violationMapper.selectAllViolation();
        LOGGER.info("结束查询违章大类方法");
        return violations;
    }

    @Override
    public List<ViolationDetail> getViolationDetail(Integer typeId) throws Exception {
        LOGGER.info("进入查询违章细类方法 typeId={}", typeId);
        List<ViolationDetail> violationDetails = violationMapper.selectViolationDetail(typeId);
        LOGGER.info("结束查询违章细类方法 typeId={}", typeId);
        return violationDetails;
    }

    @Override
    public void addViolationInfo(String billDep,String billTime,String billStaff,String billNum,String fineAmt,
                                 String violationImgPath,String paymentStatus,String fineReason) throws Exception
    {
        LOGGER.info("进入新增违章信息方法");
        List<ViolationInfoVo> violationInfoVos = violationMapper.selectViolationInfo(null,null,null,null,billNum);
        if(violationInfoVos != null && violationInfoVos.size() > 0)
        {
            LOGGER.error("该违章序列号已存在 billNum={}", billNum);
            throw new BusinessException(JsonResultEnum.VIOLATIONINFO_EXIST);
        }
        Date date = new Date();
        //获取本机ip
        String host = InetAddress.getLocalHost().getHostAddress();
        violationImgPath = "//"+ host + ":" + port + contextPath + "/static/violationimg/" + violationImgPath;
        LOGGER.info("violationImgPath:{}",violationImgPath);
        ViolationInfo violationInfo = new ViolationInfo(null,billDep,new Date(Long.valueOf(billTime)),billStaff,billNum,"",fineReason,fineAmt,violationImgPath,
                paymentStatus,date,1,date,1,1,1);
        int insertCnt = violationMapper.insertViolationInfo(violationInfo);
        LOGGER.info("结束新增违章信息方法");
    }

    @Override
    public List getViolationInfo(String billType,String billDep,String billTime,String paymentStatus,
                                                  String billNum,String page,String size) throws Exception {
        LOGGER.info("进入查询违章信息方法");
        if(billType != null)
        {
            if("手动开单".equals(billType))
            {
                billType = "1";
            }
            else if("自动开单".equals(billType))
            {
                billType = "2";
            }
            else
            {
                billType = "0";
            }
        }
        if (billTime != null)
        {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-dd");
            billTime = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(billTime)), ZoneId.systemDefault()));
            LOGGER.info("billTime:{}", billTime);
        }
        List<ViolationInfoVo> violationInfoVos = violationMapper.selectViolationInfo(billType, billDep, billTime, paymentStatus, billNum);
        List resList = PageUtil.getPageInfoList(violationInfoVos,page,size);
        LOGGER.info("结束查询违章信息方法");
        return resList;
    }

//    @Override
//    public byte[] getViolationImg(Integer id) throws Exception {
//        LOGGER.info("进入查询违章图片方法");
//        Map<String, Object> map = violationMapper.selectViolationImg(id);
//        byte[] bytes = (byte[]) map.get("violationImg");
//        LOGGER.info("结束查询违章图片方法");
//        return bytes;
//    }

    @Override
    public String uploadViolationImg(MultipartFile violationImg) throws Exception
    {
        LOGGER.info("进入上传违章图片方法");
        if (violationImg.isEmpty())
        {
            LOGGER.error("未选择需上传的文件");
            throw new BusinessException(JsonResultEnum.FILE_NOT_EXIST);
        }
        // 设置文件上传后的路径
        String filePath = fileRootPath + fileSonPath;
        // 获取文件名后缀名
        String suffix = violationImg.getOriginalFilename();
        String prefix = suffix.substring(suffix.lastIndexOf(".")+1);
        //文件重命名
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newFileName = uuid +"." +  prefix;
        //创建文件路径
        File dest = new File(filePath + newFileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists())
        {
            //假如文件不存在即重新创建新的文件已防止异常发生
            dest.getParentFile().mkdirs();
        }
        //transferTo（dest）方法将上传文件写到服务器上指定的文件
        violationImg.transferTo(dest);
        LOGGER.info("newFileName:{}",newFileName);
        LOGGER.info("结束上传违章图片方法");
        return newFileName;
    }

    @Override
    public void removeViolationInfo(List billNumList) throws Exception
    {
        LOGGER.info("开始删除违章信息方法");
        if(billNumList.size() == 0)
        {
            return;
        }
        violationMapper.deleteViolationInfo(billNumList);
        LOGGER.info("结束删除违章信息方法");
    }
}
