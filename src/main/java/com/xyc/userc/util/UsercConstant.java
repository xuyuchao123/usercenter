package com.xyc.userc.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 2020/9/29.
 */
public class UsercConstant
{
    public static final String OPENID = "Wechat-Oauth2-Key";

    public static final Map<String,String> ROLECODERELMAP;

    public static final Map<String,String> ROLECODERELMAP_REV;

    static
    {
        ROLECODERELMAP = new HashMap<>();
        ROLECODERELMAP.put(RoleTypeEnum.ROLE_JLY_XC.getRoleCode(), "XC");
        ROLECODERELMAP.put(RoleTypeEnum.ROLE_JLY_BC.getRoleCode(), "BC");
        ROLECODERELMAP.put(RoleTypeEnum.ROLE_JLY_KHB.getRoleCode(), "HP");
        ROLECODERELMAP.put(RoleTypeEnum.ROLE_KDY.getRoleCode(), "KDY");
        ROLECODERELMAP.put(RoleTypeEnum.ROLE_HBGK.getRoleCode(), "HBGK");
        ROLECODERELMAP.put(RoleTypeEnum.ROLE_HLDD.getRoleCode(), "HLDD");
        ROLECODERELMAP.put(RoleTypeEnum.ROLE_JLY_OTHER.getRoleCode(), "other");

        ROLECODERELMAP_REV = new HashMap<>();
        ROLECODERELMAP_REV.put("XC",RoleTypeEnum.ROLE_JLY_XC.getRoleCode());
        ROLECODERELMAP_REV.put("BC",RoleTypeEnum.ROLE_JLY_BC.getRoleCode());
        ROLECODERELMAP_REV.put("HP",RoleTypeEnum.ROLE_JLY_KHB.getRoleCode());
        ROLECODERELMAP_REV.put("KDY",RoleTypeEnum.ROLE_KDY.getRoleCode());
        ROLECODERELMAP_REV.put("HBGK",RoleTypeEnum.ROLE_HBGK.getRoleCode());
        ROLECODERELMAP_REV.put("HLDD",RoleTypeEnum.ROLE_HLDD.getRoleCode());
        ROLECODERELMAP_REV.put("other",RoleTypeEnum.ROLE_JLY_OTHER.getRoleCode());

    }
}
