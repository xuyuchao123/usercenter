package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_menu
 *
 * @mbggenerated do_not_delete_during_merge
 */
public class Menu implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 菜单名称
     * menu_name
     */
    private String menuName;

    /**
     * requestpath
     * menu_url
     */
    private Requestpath requestpath;

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
     * 父菜单id
     * parent_id
     */
    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Requestpath getRequestpath() {
        return requestpath;
    }

    public void setRequestpath(Requestpath requestpath) {
        this.requestpath = requestpath;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}