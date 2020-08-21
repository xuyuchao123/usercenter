package com.xyc.userc.util;

/**
 * Created by 1 on 2020/8/20.
 * 角色类型枚举
 */
public enum RoleTypeEnum
{
    ROLE_ADMIN("ADMIN", "管理员"),

    ROLE_SJ_0("SJ0", "客户、司机0"),

    ROLE_JCY("JCY", "安环处、海力物流检查人员"),

    ROLE_PDY("PDY", "抓拍系统判定人员"),

    ROLE_JLY("JLY", "海力物流计量员"),

    ROLE_GLY("GLY", "安环处、海力物流管理员"),

    ROLE_JLY_XC("JLY_XC", "海力物流计量员（线材）"),

    ROLE_JLY_BC("JLY_BC", "海力物流计量员（螺纹）"),

    ROLE_JLY_HP("JLY_HP", "海力物流计量员（宽厚板）"),

    ROLE_JLY_OTHER("JLY_OTHER", "海力物流计量员（其它）"),

    ROLE_SJ_1("SJ1", "客户、司机1");

    private String roleCode;
    private String roleName;

    RoleTypeEnum(String roleCode, String roleName)
    {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}