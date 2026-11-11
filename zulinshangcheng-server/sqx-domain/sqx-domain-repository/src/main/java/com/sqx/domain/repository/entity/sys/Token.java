package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户信息表
 */
@Entity
@Data
public class Token implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String userId;
    @Column
    private String uuid;
    @Column(columnDefinition = "varchar(255) comment'管理员id'")
    private String adminId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
