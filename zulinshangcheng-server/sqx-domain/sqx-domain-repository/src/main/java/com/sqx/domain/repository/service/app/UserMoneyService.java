package com.sqx.domain.repository.service.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserMoney;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;

import java.math.BigDecimal;

public interface UserMoneyService extends IService<UserMoney> {

    UserMoney selectUserMoneyByUserId(Long userId);

    void updateMoney(int i, Long userId, double money);

    Double selectMyProfit(Long userId);

    IPage<UserMoneyDetails> balanceDetailed(Long page, Long limit, UserMoneyDetails userMoneyDetails);


    Result rechargeMoney(Long userId, BigDecimal money, Integer classify) throws Exception;

    Result moneyPayIndent(Long userId, Long orderId);
}
