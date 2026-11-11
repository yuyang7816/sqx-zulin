package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;

@Mapper
public interface UserMoneyDetailsDao extends BaseMapper<UserMoneyDetails> {
    Double monthIncome(@Param("date") String date,@Param("userId") Long userId);
    Double selectMyProfit(Long userId);

    BigDecimal getOneProfit(@Param("userId") Long userId, @Param("level") Integer level);

    IPage<HashMap<String, Object>> getProfitList(@Param("pages") Page<HashMap<String, Object>> pages, @Param("userId") Long userId, @Param("type") Integer type);

    BigDecimal payMessageByRefund(@Param("date") String date,
                                  @Param("type") Integer type);
}
