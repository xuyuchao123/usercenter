package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_user
 *
 * @mbggenerated do_not_delete_during_merge
 */
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String openId;

    private String mobilePhone;

    private String carNum;

    private List<Role> roles;

    public User(String openId,String mobilePhone, List<Role> roles)
    {
//        this.setOpenid(openId);
//        this.setNickname(nickName);
//        this.setSex(sex);
//        this.setProvince(province);
//        this.setCity(city);
//        this.setCountry(country);
//        this.setHeadimgurl(headImgUrl);
//        this.setPrivilege(privilege);
//        this.setUnionid(unionId);
        this.openId = openId;
        this.mobilePhone = mobilePhone;
//        this.carNum = carNum;
        this.roles = roles;
    }

    public User()
    {

    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities()
//    {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
//        for (Role role : roles)
//        {
//            authorities.add(new SimpleGrantedAuthority(String.valueOf(role.getId())));
//        }
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.getOpenid();
//    }
//
//    @Override
//    public String getUsername() {
//        return this.getNickname();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked()
//    {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled()
//    {
//
//        return true;
//    }
}
