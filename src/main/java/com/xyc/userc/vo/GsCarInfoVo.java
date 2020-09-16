package com.xyc.userc.vo;

import java.io.Serializable;

/**
 * Created by 1 on 2020/9/16.
 */
public class GsCarInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String clsbdh;

    private String fdjh;

    public String getClsbdh() {
        return clsbdh;
    }

    public void setClsbdh(String clsbdh) {
        this.clsbdh = clsbdh;
    }

    public String getFdjh() {
        return fdjh;
    }

    public void setFdjh(String fdjh) {
        this.fdjh = fdjh;
    }
}
