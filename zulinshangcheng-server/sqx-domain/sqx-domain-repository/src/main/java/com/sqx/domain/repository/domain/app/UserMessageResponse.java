package com.sqx.domain.repository.domain.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageResponse implements Serializable {
    /**
     * 总人数
     */
    private int totalNumber;
    /**
     * 普通用户人数
     */
    private int userNumber;
    /**
     * 会员人数
     */
    private int vipUserNumber;

    /**
     * 小程序人数
     */
    private int wxUserNumber;

    /**
     * APP人数
     */
    private int appUserNumber;

    /**
     * H5人数
     */
    private int h5UserNumber;

    /**
     * 公众号人数
     */
    private int publicUserNumber;
}
