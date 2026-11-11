package com.sqx.domain.repository.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("vip_details")
@ApiModel("会员详情")
public class VipDetails implements Serializable {


    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("月数")
    @TableField("vip_month_count")
    private Integer vipMonthCount;

    @ApiModelProperty("是否启用(1启用 0未启用)")
    @TableField("is_enable")
    private Integer isEnable;

    @ApiModelProperty("会员价格")
    private BigDecimal money;

    @ApiModelProperty("会员名称")
    private String vipName;


    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 奖励
     */
   @ApiModelProperty("奖励")
    private BigDecimal award;

    /**
     * 会员类型0月1季2年
     */
    @ApiModelProperty("会员类型0月1季2年")
    private Integer vipNameType;


}
