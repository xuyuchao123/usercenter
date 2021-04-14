package com.xyc.userc.service;

import com.xyc.userc.vo.CarNumFrozenAddVo;
import com.xyc.userc.vo.CarNumFrozenVo;

import java.util.List;

/**
 * Created by 1 on 2021/4/13.
 */
public interface FreezeService
{
    List<CarNumFrozenVo> queryCarNumFrozen(String carNum, String frozenStatus, String page, String size) throws Exception;

    void freezeCarNum(List<CarNumFrozenAddVo> carNumFrozenAddVos) throws Exception;

    void unFreezeCarNum(List<String> carNums) throws Exception;

    void refreshCarNumViolation() throws Exception;
}
