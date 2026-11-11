package com.sqx.domain.repository.entity.sys;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class CashOut implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String createAt;
    @Column
    private String outAt;
    @Column
    private String money;
    @Column
    private Boolean isOut;
    @Column
    private String relationId;
    @Column
    private String userId;
    @Column
    private String zhifubao;
    @Column
    private String zhifubaoName;
    @Column
    private String orderNumber;
    @Column
    private Integer state;//-1 转账失败，0，待转账，1转账成功
    @Column
    private String refund;//退款原因

    /**
     * 提现方式 1支付宝 2微信小程序  3微信公众号
     */
    @Excel(name = "提现方式", replace = {"_null", "支付宝_1", "微信小程序_2", "微信公众号_3"}, orderNum = "6")
    @ApiModelProperty("提现方式 1支付宝 2微信小程序  3微信公众号")
    private Integer classify;


    /**
     * 手续费
     */
    @ExcelIgnore
    @Excel(name = "手续费")
    @ApiModelProperty("手续费")
    private Double rate;

    /**
     * 微信手动图片
     */
    @Excel(name = "收款二维码", orderNum = "5")
    @ApiModelProperty("收款二维码")
    private String wxImg;


    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getZhifubaoName() {
        return zhifubaoName;
    }

    public void setZhifubaoName(String zhifubaoName) {
        this.zhifubaoName = zhifubaoName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getOutAt() {
        return outAt;
    }

    public void setOutAt(String outAt) {
        this.outAt = outAt;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Boolean getOut() {
        return isOut;
    }

    public void setOut(Boolean out) {
        isOut = out;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getZhifubao() {
        return zhifubao;
    }

    public void setZhifubao(String zhifubao) {
        this.zhifubao = zhifubao;
    }
}
