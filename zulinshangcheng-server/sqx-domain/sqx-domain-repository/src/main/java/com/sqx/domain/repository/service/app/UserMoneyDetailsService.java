package com.sqx.domain.repository.service.app;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;

import java.math.BigDecimal;

public interface UserMoneyDetailsService extends IService<UserMoneyDetails> {
    Result queryUserMoneyDetails(Integer page, Integer limit, Long userId, Integer classify, Integer type);
    Double monthIncome(String date,Long userId);

    void setParentMoneyDetails(Long userId, Long parentId, BigDecimal money, Integer userLevel);

    BigDecimal getOneProfit(Long userId, Integer level);

    Result queryCreditScore(Integer page, Integer limit, Long userId, Integer classify, Integer type);

    Result queryGoldNum(Integer page, Integer limit, Long userId, Integer classify, Integer type);

    Result queryUserDetails(Integer page, Integer limit, Long userId, Integer classify, Integer type);

}
