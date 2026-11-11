package com.sqx.domain.repository.mapper.pay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.pay.PayDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/1
 */
@Mapper
public interface PayDetailsDao extends BaseMapper<PayDetails> {

    PayDetails selectById(@Param("id") Long id);

    PayDetails selectByRemark(@Param("remark") String remark);

    PayDetails selectByOrderId(@Param("orderId") String orderId);

    int updateState(@Param("id") Long id, @Param("state") Integer state, @Param("time") String time, @Param("tradeNo") String tradeNo);

    IPage<Map<String, Object>> selectPayDetails(Page<Map<String, Object>> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userId") Long userId, @Param("state") Integer state);

    Double selectSumPay(@Param("createTime") String createTime, @Param("endTime") String endTime, @Param("userId") Long userId);

    Double selectSumMember(@Param("time") String time, @Param("flag") Integer flag);


    Double selectSumPayByState(@Param("time") String time, @Param("flag") Integer flag, @Param("state") Integer state);

    Double selectSumPayByClassify(@Param("time") String time, @Param("flag") Integer flag, @Param("classify") Integer classify,@Param("payClassify") Integer payClassify);


    int selectPayCount(Long userId);

    Double instantselectSumPay(@Param("date") String date, @Param("userId") Long userId);


    BigDecimal payMessage(@Param("date") String date, @Param("type") Integer type, @Param("classify") Integer classify);

    BigDecimal payMessageByRefund(@Param("date")String date, @Param("type")Integer type);

    BigDecimal queryPayMoney(String date, Integer type);
}
