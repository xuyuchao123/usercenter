package com.xyc.userc.entity;

import java.io.Serializable;

/**
 * Created by 1 on 2020/10/22.
 */
public class Violation implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String typeName;
    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
