package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 自营商城首页图标
 */
@Data
@Entity
public class SelfActivity implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String createAt;
    @Column
    private String image_url;
    @Column
    private String url;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String state; //1商品分类 2精选好物 3热卖榜单 4每日上新 5拼团购 6邀请图 7首页邀请图
}
