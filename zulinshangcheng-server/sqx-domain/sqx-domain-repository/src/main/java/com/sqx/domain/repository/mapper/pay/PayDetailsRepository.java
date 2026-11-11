package com.sqx.domain.repository.mapper.pay;

import com.sqx.domain.repository.entity.pay.PayDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface PayDetailsRepository extends Repository<PayDetails, Long> {

    @Query(value = "select * from pay_details d where d.id=:id",nativeQuery = true)
    PayDetails selectById(@Param("id") Long id);

    @Query(value = "select * from pay_details d where d.order_id=:orderId",nativeQuery = true)
    PayDetails selectByOrderId(@Param("orderId") String orderId);

    @Transactional
    @Modifying
    @Query(value = "update pay_details set state=:state,pay_time=:time where id=:id",nativeQuery = true)
    int updateState(@Param("id") Long id, @Param("state") Integer state, @Param("time") Date time);

    @Query(value = "select d.id,d.classify,d.order_id as orderId,d.money,d.user_id as userId,u.nick_name as nickName,u.phone,d.state,d.create_time as createTime,d.pay_time as payTime from pay_details d " +
            "left join user_info u on u.id=d.user_id " +
            "where date_format(d.create_time,'%Y-%m-%d') between :startTime and :endTime",
            countQuery = "select count(*)  from pay_details d where date_format(d.create_time,'%Y-%m-%d') between :startTime and :endTime",
            nativeQuery = true)
    Page<Map<String,Object>> selectPayDetails(Pageable pageable, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "select d.id,d.classify,d.order_id as orderId,d.money,d.user_id as userId,u.nick_name as nickName,u.phone,d.state,d.create_time as createTime,d.pay_time as payTime from pay_details d " +
            "left join user_info u on u.id=d.user_id " +
            "where d.user_id in (select user_id from user_info where phone=:phone) and date_format(d.create_time,'%Y-%m-%d') between :startTime and :endTime",
            countQuery ="select count(*) from pay_details d where d.user_id in (select user_id from user_info where phone=:phone) and date_format(d.create_time,'%Y-%m-%d') between :startTime and :endTime" ,
            nativeQuery = true)
    Page<Map<String,Object>> selectPayDetails(Pageable pageable, @Param("phone") String phone, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "select ifnull(sum(money),0.00) from pay_details where `state`=1 and pay_time like concat('%',:time,'%') and classify=:classify ",nativeQuery = true)
    BigDecimal selectPayMoneyStatistics(@Param("time") String time,@Param("classify") Integer classify);


}
