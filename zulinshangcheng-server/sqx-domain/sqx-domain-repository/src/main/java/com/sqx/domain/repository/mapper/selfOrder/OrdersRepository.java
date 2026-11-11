package com.sqx.domain.repository.mapper.selfOrder;

import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.sys.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrdersRepository extends Repository<Orders, Long> {

    /**
     * 查询待处理订单个数（1待付款 2已付款 3已发货 4已收货 5已取消 6退款中 7已退款）
     */
    @Query(value = "select count(id) from Orders where status in(2,6)")
    int pendingOrder();

    /**
     * 查询拼团中订单
     */
    @Query(value = "from Orders where status = 9")
    List<Orders> findPinkList();

    //订单状态（1待付款 2已付款 3已发货 4已收货 5已取消 6退款中 7已退款 8拒绝退款 9拼团中 10已评价）
    @Query(value = "select count(o.id) from Orders o where o.userId =:userId and o.status in(2,3,4,6,8,9,10)  and o.payTime like concat('%',:payTime,'%')")
    int todayPayCount(@Param("userId") Long userId, @Param("payTime") String payTime);

    @Query(value = "select count(o.id) from Orders o where o.userId =:userId and o.status in(2,3,4,6,8,9,10)  and o.payTime like concat('%',:payTime,'%')")
    int monthPayCount(@Param("userId") Long userId, @Param("payTime") String payTime);

    @Query(value = "select sum(o.payMoney) from Orders o where o.userId =:userId and o.status in(2,3,4,6,8,9,10)  and o.payTime like concat('%',:payTime,'%')  and o.orderType in(1,2,3)")
    Double monthPayNumber(@Param("userId") Long userId, @Param("payTime") String payTime);

    @Query(value = "select sum(o.payMoney) from Orders o where o.userId =:userId and o.status in(2,3,4,6,8,9,10)  and o.payTime like concat('%',:payTime,'%')  and o.orderType in(1,2,3)")
    Double lastMonthPayNumber(@Param("userId") Long userId, @Param("payTime") String payTime);

    /**
     * 查询所有代发货/待收货的普通物流订单
     * @return
     */
    @Query(value = "from Orders o where o.status =:status and o.isExpress=1 and o.orderType in(1,2,3)")
    List<Orders> findAllByStatus(@Param("status") Integer status);

    /**
     * 订单个数统计
     */
    @Query(value = "select count(o.id) from Orders o where o.userId =:userId and o.status =:status")
    int countByStatusAndUserId(@Param("userId") Long userId, @Param("status") Integer status);

    //查询用户订单
    @Query(value = "from Orders o where o.userId =:userId")
    List<Orders> findByUserId(@Param("userId") Long userId);

    /*
    统计订单
    订单状态（1待付款 2已付款 3已发货 4已收货 5已取消 6退款中 7已退款 8拒绝退款 9拼团中 10已评价）
    订单类型（1普通订单 2拼团订单 3秒杀订单 4积分订单）
     */
    @Query(value = "from Orders o where o.status in(2,3,4,6,7,8,9,10) and o.orderType in(1,2,3)")
    List<Orders> findByStatus();

    /**
     * 检测复购
     * @return
     */
    @Query(value = "select count(o.id) from Orders o where o.userId=:userId and o.goodsId=:goodsId and o.status in(2,3,4,6,8,9,10)")
    int checkIsRepeat(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
    /**
     * 复购率
     * select d.dept_name ,round(avg(nvl(e.emp_wage,0)),2) sal from employee e left join department d on e.dept_id=d.dept_id group by d.dept_name;
     * @return
     */
    @Query(value = "select count(o.id) from Orders o where o.isRepeat=1 and o.goodsId=:goodsId and o.status in(2,3,4,6,8,9,10)")
    int repeat(@Param("goodsId") Long goodsId);
    @Query(value = "select count(o.id) from Orders o where o.goodsId=:goodsId and o.status in(2,3,4,6,8,9,10)")
    int repeatSum(@Param("goodsId") Long goodsId);

    @Query(value = "select ifnull(sum(pay_money),0.00) from orders where pay_time like concat('%',:time,'%') and status in(2,3,4,6,8,9,10) and pay_way in (1,2,3) ",nativeQuery = true)
    BigDecimal selectOrdersPayWxSumMoney(String time);

    @Query(value = "select ifnull(sum(pay_money),0.00) from orders where pay_time like concat('%',:time,'%') and status in(2,3,4,6,8,9,10) and pay_way in (4,5) ",nativeQuery = true)
    BigDecimal selectOrdersPayZfbSumMoney(String time);

}