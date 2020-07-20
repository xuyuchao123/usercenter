package com.xyc.userc.entity;

import java.io.Serializable;

/**
 * Created by 1 on 2020/7/20.
 */
public class Requestpath implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * url
     */
    private String url;

    /**
     * description
     */
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
