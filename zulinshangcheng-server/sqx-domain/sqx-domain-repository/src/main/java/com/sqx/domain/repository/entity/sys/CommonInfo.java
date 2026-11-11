package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sqx.common.core.config.SingleValueToArrayDeserializer;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 通用配置管理
 */
@Data
@Entity
@TableName("common_info")
public class CommonInfo implements Serializable {

    @Id()
    @TableId(type = IdType.INPUT)
    @JsonDeserialize(using = SingleValueToArrayDeserializer.class)
    private Long id;

    private String createAt;

    private Integer type; //1表示客服二维码 2表示公众号二维码 3表示全局佣金是否开启 4注册客服渠道id配置 5、佣金规则 6、

    private String value;

    private String max;

    private String min;

    private String conditionFrom;

}

