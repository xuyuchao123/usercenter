package com.xyc.userc.entity;

import java.io.Serializable;

/**
 * Created by 1 on 2021/4/7.
 */
public class Student implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer stuId;
    private String stuName;
    private String stuSubject;

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuSubject() {
        return stuSubject;
    }

    public void setStuSubject(String stuSubject) {
        this.stuSubject = stuSubject;
    }
}
