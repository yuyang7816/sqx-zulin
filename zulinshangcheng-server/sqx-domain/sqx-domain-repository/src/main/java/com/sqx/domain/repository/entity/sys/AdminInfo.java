package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 后台用户管理
 */
@Data
@Entity
public class AdminInfo implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String createAt;
    @Column
    private String image_url;
    @Column
    private String phone;
    @Column
    private String userName;
    @Column
    private String pwd;
    @Column
    private String address;
    @Column
    private String state;
    @Column(insertable = false, columnDefinition = "int default 0")
    private int grade;
    @Column
    private long characterId;
    @Column
    private String characterNames;
    @Transient
    private List<Long> roleIdList;

    public List<Long> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public String getCharacterNames() {
        return characterNames;
    }

    public void setCharacterNames(String characterNames) {
        this.characterNames = characterNames;
    }

    public long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
