package com.xyc.userc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1 on 2021/3/2.
 */
public class QRCodeStrInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String qRCodeStr;
    private Long gmtCreate;

    public String getqRCodeStr() {
        return qRCodeStr;
    }

    public void setqRCodeStr(String qRCodeStr) {
        this.qRCodeStr = qRCodeStr;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
