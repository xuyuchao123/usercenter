package com.xyc.userc.entity;

import java.io.Serializable;

/**
 * Created by 1 on 2020/10/22.
 */
public class ViolationDetail implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String itemName;
    private String fineAmt;
    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getFineAmt() {
        return fineAmt;
    }

    public void setFineAmt(String fineAmt) {
        this.fineAmt = fineAmt;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
