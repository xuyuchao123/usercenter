package com.xyc.userc.dao;

import com.xyc.userc.entity.Violation;
import com.xyc.userc.entity.ViolationDetail;
import com.xyc.userc.entity.ViolationInfo;
import com.xyc.userc.vo.UserInfoVo;
import com.xyc.userc.vo.ViolationInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/10/22.
 */
public interface ViolationMapper
{
    List<Violation> selectAllViolation();

    List<ViolationDetail> selectViolationDetail(@Param("typeId")Integer typeId);

    int insertViolationInfo(ViolationInfo violationInfo);

    List<ViolationInfoVo> selectViolationInfo(@Param("billType")String billType, @Param("billDep")String billDep,
                                              @Param("billTime")String billTime, @Param("paymentStatus")String paymentStatus,
                                              @Param("billNum")String billNum);

    Map<String,Object> selectViolationImg(Integer id);

    void deleteViolationInfo(@Param("list")List billNumList);
}
