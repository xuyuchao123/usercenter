package com.xyc.userc.dao;

import com.xyc.userc.entity.Blacklist;
import com.xyc.userc.vo.BlacklistVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 1 on 2020/8/24.
 */
public interface BlacklistMapper
{
    List<BlacklistVo> selectBlacklist(@Param("name")String name, @Param("mobile")String mobile,
                                      @Param("createName")String createName,@Param("createMobile")String createMobile);

    int insertBlacklist(Blacklist blacklist);

    List<Blacklist> selectByMobileOpenIdId(Integer mobileOpenIdId);

    void deleteBlacklist(Integer mobileOpenIdId);


}
