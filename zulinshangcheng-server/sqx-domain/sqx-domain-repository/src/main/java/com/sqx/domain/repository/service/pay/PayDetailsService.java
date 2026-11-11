package com.sqx.domain.repository.service.pay;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.domain.repository.entity.pay.PayDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public interface PayDetailsService extends IService<PayDetails> {

    PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state);

//    PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state);

    Double selectSumPay(String createTime, String endTime, Long userId);

    Double selectSumMember(String time, Integer flag);

    Double selectSumPayByState(String time, Integer flag, Integer state);

    Double selectSumPayByClassify(String time, Integer flag, Integer classify,Integer payClassify);

    Double instantselectSumPay(String date, Long userId);

    HashMap<String,Object> payMessage(String date, Integer type);

    Result refundPayDetails(Long orderId,BigDecimal money, String remark);
}
