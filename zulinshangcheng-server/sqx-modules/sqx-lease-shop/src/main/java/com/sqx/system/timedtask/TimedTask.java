package com.sqx.system.timedtask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.Goods;
import com.sqx.domain.repository.entity.sys.LeaseDays;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.entity.sys.SelfCouponUser;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponUserMapper;
import com.sqx.domain.repository.mapper.selfOrder.OrdersDao;
import com.sqx.domain.repository.mapper.sys.GoodsDao;
import com.sqx.domain.repository.mapper.sys.LeaseDaysDao;
import com.sqx.domain.repository.service.selfGoodsSku.SelfGoodsSkuService;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class TimedTask {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private ICommonInfoService commonInfoService;

    @Autowired
    private SelfCouponUserMapper selfCouponUserMapper;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private SelfGoodsSkuService selfGoodsSkuService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LeaseDaysDao leaseDaysDao;

    /**
     * 待支付取消订单
     */
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void waitPayCancelOrder() {
        List<Orders> tbOrdersList = ordersDao.selectList(new QueryWrapper<Orders>()
                .eq("status", 0));
        int minute = Integer.parseInt(commonInfoService.findOne(417).getValue());
        for (Orders order : tbOrdersList) {
            String createAt = order.getCreateAt();
            // 定义自定义日期时间格式化器
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime createTime = LocalDateTime.parse(createAt, formatter);
            // 将 LocalDateTime 转换为 ZonedDateTime
            ZonedDateTime createTimeZoned = createTime.atZone(ZoneId.systemDefault());
            ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
            Duration duration = Duration.between(createTimeZoned, now);
            if (duration.toMinutes() > minute) {
                //将优惠券修改为未使用状态
                if (order.getCouponUserId() != null) {
                    SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(order.getCouponUserId());
                    if (couponUser != null) {
                        couponUser.setStatus(1);
                        selfCouponUserMapper.updateCouponUserId(couponUser);
                    }
                }
                Goods goods = goodsDao.selectById(order.getGoodsId());
                if (goods != null) {
                    selfGoodsSkuService.addStock(order.getSkuId(), order.getNumber());
                }
                order.setStatus(9);
                order.setCancelTime(new Date());
                ordersDao.updateById(order);
            }
        }
    }

    /**
     * 到期会员检测，如果到期将用户改为非会员
     */
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void checkMember() {
        // 获取当前时间的Instant对象，并转换为LocalDateTime
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        List<UserEntity> userList = userDao.selectList(new QueryWrapper<UserEntity>()
                .eq("is_vip", 1)
                .lt("vip_end_time", now)
        );
        for (UserEntity user : userList) {
            user.setIsVip(2);
            userDao.updateById(user);
        }
    }

    /**
     * 查询订单列表是包邮或者邮寄，查询物流单号，如果是已签收，就直接计算起租日和归还日（1小时检测一次）
     */
    @Scheduled(cron = "0 0 */1 * * ?", zone = "Asia/Shanghai")
    public void checkOrder() {
        List<Orders> ordersList = ordersDao.selectList(new QueryWrapper<Orders>()
                .in("is_express", Arrays.asList(1,2))
                .eq("status", 2));
        for (Orders order : ordersList) {
            if (order.getSellerNumber() != null) {
                UserEntity userEntity = userDao.selectById(order.getUserId());
                if (userEntity != null && userEntity.getPhone() != null && userEntity.getPhone().length() >= 4) {
                    String phone = userEntity.getPhone();
                    // 截取最后四位数字
                    String lastFourDigits = phone.substring(phone.length() - 4);
                    String data = ordersService.findExpressByOrderNum(order.getSellerNumber() + ":" + lastFourDigits);
                    if ("3".equals(data)) {
                        order.setLeaseStartDate(new Date());
                        LeaseDays leaseDays = leaseDaysDao.selectById(order.getDayId());
                        if (leaseDays != null && leaseDays.getDayName() != null) {
                            LocalDate leaseStartDate = order.getLeaseStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                            int daysToAdd = Integer.parseInt(leaseDays.getDayName());
                            // 计算归还日期
                            LocalDate returnDate = leaseStartDate.plusDays(daysToAdd);
                            // 设置归还日期
                            order.setReturnDate(Date.from(returnDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
                            order.setStatus(4);
                            ordersDao.updateById(order);
                        }
                    }
                }
            }
        }
    }

    /**
     * 查询订单是否逾期，如已逾期，就将订单改为逾期
     */
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void checkOrder1() {
        List<Orders> ordersList = ordersDao.selectList(new QueryWrapper<Orders>()
                .in("status", Arrays.asList(1, 2, 3, 4, 7)));
        List<Orders> upOrders = new ArrayList<>();
        for (Orders order : ordersList) {
            if (order.getReturnDate() != null) {
                LocalDate returnDate = order.getReturnDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (returnDate.isBefore(LocalDate.now())) {
                    order.setOverdueStatus(1);
                    upOrders.add(order);
                }
            }
        }
        // 批量更新
        if (!upOrders.isEmpty()) {
            ordersDao.batchUpdate(upOrders);
        }
    }

}
