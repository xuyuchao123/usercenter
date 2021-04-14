package com.xyc.userc.dao;

import com.xyc.userc.entity.CarNumFrozen;
import com.xyc.userc.vo.CarNumFrozenVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 1 on 2021/4/13.
 */
public interface FreezeMapper
{
    List<CarNumFrozenVo> selectCarNumFrozenVo(@Param("carNum")String carNum, @Param("frozenStatus")Integer frozenStatus);

    List<CarNumFrozen> selectCarNumFrozen(@Param("carNum")String carNum,@Param("frozenStatus")Integer frozenStatus);

    List<CarNumFrozen> selectByCarNums(@Param("list")List carNumList);

    void updateCarNumFrozens(@Param("list")List carNumFrozenAddVos);

    void updateCarNumUnFrozens(@Param("list")List carNums);

    void insertAllCarNumFrozen(@Param("list")List<CarNumFrozen> carNumFrozenList);

    void updateAllCarNumFrozen(@Param("list")List<CarNumFrozen> carNumFrozenList);
}
