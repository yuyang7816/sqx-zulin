package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * 自营商城Banner
 */
@Entity
@Data
public class SelfBanner {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
    @Column
    private String imgUrl; //图片地址
    @Column
    private String linkUrl; //跳转地址
    @Column
    private String createAt; //创建时间
    @Column
    private String sort; //排序
    @Column
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
