package com.xyc.userc.dao;

import com.xyc.userc.entity.Violation;
import com.xyc.userc.entity.ViolationDetail;
import com.xyc.userc.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 1 on 2020/10/22.
 */
public interface ViolationMapper
{
    List<Violation> selectAllViolation();

    List<ViolationDetail> selectViolationDetail(@Param("typeId")String typeId);
}
