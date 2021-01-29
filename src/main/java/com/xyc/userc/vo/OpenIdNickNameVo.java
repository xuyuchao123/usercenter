package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2020/12/22.
 */
public class OpenIdNickNameVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String openId;
    private String nickName;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
