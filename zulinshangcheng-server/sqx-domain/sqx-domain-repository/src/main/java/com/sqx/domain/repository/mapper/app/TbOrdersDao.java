package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.app.TbOrders;

import java.math.BigDecimal;
import java.util.Optional;

/**
* @author Admin
* @description 针对表【tb_orders(用户订单表)】的数据库操作Mapper
* @createDate 2024-09-14 14:43:13
* @Entity generator.domain.TbOrders
*/
public interface TbOrdersDao extends BaseMapper<TbOrders> {

    Optional<TbOrders> selectByOrderId(Long id);

    BigDecimal queryPlatformMoney(String date, Integer type);
}




