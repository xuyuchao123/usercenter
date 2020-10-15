package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2020/10/14.
 */
public class PcRole implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 角色名称
     * role_name
     */
    private String roleName;


    private Permission permission;

    private Application application;

    /**
     * 1：已删除 0：未删除
     * is_deleted
     */
    private Byte isDeleted;

    /**
     * 创建时间
     * gmt_create
     */
    private Date gmtCreate;

    /**
     * 修改时间
     * gmt_modified
     */
    private Date gmtModified;

    /**
     * 创建人id
     * user_create
     */
    private Long userCreate;

    /**
     * 修改人id
     * user_modified
     */
    private Long userModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Long userCreate) {
        this.userCreate = userCreate;
    }

    public Long getUserModified() {
        return userModified;
    }

    public void setUserModified(Long userModified) {
        this.userModified = userModified;
    }
}
