package com.xyc.userc.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by 1 on 2020/10/13.
 */
public class PcUser implements Serializable,UserDetails
{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     * user_num
     */
    private String userName;

    /**
     * 用户密码
     * user_num
     */
    private String password;


    /**
     * 用户真实姓名
     * user_name
     */
    private String userRealName;

    /**
     * 用户手机号
     * mobile
     */
    private String mobile;

    /**
     * 1：已删除 0：未删除
     * is_deleted
     */
    private Byte isDeleted;

    /**
     * 1：已启用 2：未启用
     * is_enable
     */
    private Byte isEnable;

    /*
    *  1：已锁定 2：未锁定
    *  is_locked
    * */
    private Byte isLocked;


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

    /**
     * 上次登录时间
     * last_login_time
     */
    private Date lastLoginTime;



    private List<PcRole> roles;

    public List<PcRole> getRoles() {
        return roles;
    }

    public void setRoles(List<PcRole> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Byte getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    public Byte getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Byte isLocked) {
        this.isLocked = isLocked;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (PcRole pcRole : roles)
        {
            authorities.add(new SimpleGrantedAuthority(String.valueOf(pcRole.getId())));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        if(isLocked == 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        if(isEnable == 1)
        {
            return true;
        }
        return false;
    }
}
