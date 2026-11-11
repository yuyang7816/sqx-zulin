package com.sqx.domain.repository.mapper.selfOrder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.domain.repository.entity.sys.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/9 13:48
 */
@Mapper
public interface OrdersDao extends BaseMapper<Orders> {
    Integer selectOrdersCountStatisticsByYear(Integer flag, String time, Integer status);

    Integer selectAllCount(String date, Integer type, Integer classify);

    IPage<Orders> selectPageByAll(@Param("pages")IPage<Orders> pages,
                                  @Param("orderNum") String orderNum,
                                  @Param("status")String status,
                                  @Param("title")String title,
                                  @Param("mobile")String mobile,
                                  @Param("storeId")Long storeId,
                                  @Param("isExpress")Integer isExpress,
                                  @Param("orderType")Integer orderType,
                                  @Param("consignee")String consignee,
                                  @Param("sellerNumber")String sellerNumber,
                                  @Param("expressNumber")String expressNumber,
                                  @Param("startTime")String startTime,
                                  @Param("endTime")String endTime);

    void batchUpdate(@Param("upOrders") List<Orders> upOrders);

    int updateByOrderId(Orders entityOrders);
}
