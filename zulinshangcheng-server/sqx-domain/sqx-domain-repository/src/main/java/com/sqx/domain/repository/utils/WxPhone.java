package com.sqx.domain.repository.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxPhone {

    @ApiModelProperty(value ="加密手机号（微信返回）")
    private String decryptData;

    @ApiModelProperty(value ="session_key")
    private String key;

    @ApiModelProperty(value ="iv（微信返回）")
    private String iv;

}
