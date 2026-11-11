package com.sqx.domain.repository.service.cash;


import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.PageUtils;

import java.math.BigDecimal;
import java.util.Map;

public interface ICashService {
    /**
     * 获取一个对象
     */
    Result findOne(long id);

    /**
     * 删除一个
     */
    Result delete(long id);
    /**
     * 通过私域id获取对象
     */
    Result findOne(String relationId);
    /**
     * 通过手机号获取
     */
    Result findByPhone(String phone);
    /**
     * 更新提现状态
     */
    Result updateIsOut(Boolean isOut, Long id);

    Result cashMoney(Long userId, BigDecimal money, Integer classify);

    PageUtils selectCashOutList(Map<String, Object> params);

    Double sumMoney(String time, Integer flag);

    Integer countMoney(String time, Integer flag);

    Integer stayMoney(String time, Integer flag);

    BigDecimal daiTXMoney(String time, Integer flag);

    BigDecimal weiXinMoney(String time, Integer flag);

    BigDecimal yiTXMoney(String time, Integer flag);

    BigDecimal refuseMoney(String time, Integer flag);

    Integer yiTXCount(String time, Integer flag);

    Integer zfbMoneyCount(String time, Integer flag);

    Integer refuseMoneyCount(String time, Integer flag);

    Integer weiXinXCXMoney(String time, Integer flag);

    BigDecimal zfbMoney(String time, Integer flag);

}
