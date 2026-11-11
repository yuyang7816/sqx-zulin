package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.TbCouponUser;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.entity.sys.TbCoupon;
import com.sqx.domain.repository.mapper.app.TbCouponUserDao;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import com.sqx.domain.repository.service.app.UserMoneyService;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.sys.TbCouponService;
import com.sqx.domain.repository.service.sys.TbCouponUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
@Service
public class TbCouponUserServiceImpl extends ServiceImpl<TbCouponUserDao, TbCouponUser> implements TbCouponUserService {
    @Autowired
    private TbCouponService tbCouponService;
    @Autowired
    private TbCouponUserDao couponUserDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMoneyService userMoneyService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserMoneyDetailsService moneyDetailsService;
    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    /**
     * 统一支付回调
     *
     * @param payType    支付类型1水贝 2微信 3支付宝
     * @param couponId   购买的优惠券id
     * @param userId     用户id
     * @param buyNum     购买数量
     * @param totalPrice 总价
     * @return
     */
    @Override
    public Result businessCallback(Integer payType, Long userId, Long couponId, Integer buyNum, BigDecimal totalPrice) {
        TbCoupon tbCoupon = tbCouponService.getById(couponId);
        if (payType == 1) {
            userMoneyService.updateMoney(2, userId, totalPrice.doubleValue());
        }
        UserMoneyDetails details = new UserMoneyDetails();
        details.setUserId(userId);

        details.setClassify(6);
        details.setType(2);
        details.setMoney(totalPrice);
//        details.setPayType(payType);
        String content;
        if (payType == 1) {
            content = "水贝";
        } else if (payType == 2) {
            content = "微信";
        } else if (payType == 3) {
            content = "支付宝";
        } else {
            return Result.failed("支付参数错误");
        }
        details.setTitle(content + "支付" + totalPrice + "购买优惠券" + tbCoupon.getCouponName() + "共" + buyNum * tbCoupon.getCouponNum() + "个");
        details.setContent(content + "支付" + totalPrice + "购买优惠券" + tbCoupon.getCouponName() + "共" + buyNum * tbCoupon.getCouponNum() + "个");
        details.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        moneyDetailsService.save(details);
        TbCouponUser couponUser = new TbCouponUser();
        for (int i = 0; i < buyNum * tbCoupon.getCouponNum(); i++) {
            //copy对象
            BeanUtils.copyProperties(tbCoupon, couponUser);
            couponUser.setUserId(userId);
            couponUser.setCreateTime(new Date());
            //是否是永久有效
            if (tbCoupon.getValidDays() != null && tbCoupon.getValidDays() != 0) {
                //如果不是永久
                Calendar instance = Calendar.getInstance();
                instance.setTime(new Date());
                instance.add(Calendar.DATE, tbCoupon.getValidDays());
                couponUser.setExpirationTime(instance.getTime());
                couponUser.setValidDays(tbCoupon.getValidDays());
            } else {
                couponUser.setValidDays(0);
            }
            couponUser.setStatus(0);
            couponUserDao.insert(couponUser);
        }
        UserEntity userEntity = userService.getById(userId);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTitle("优惠券购买成功");
        messageInfo.setContent("优惠券购买成功");
        messageInfo.setState(String.valueOf(5));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        if (StringUtils.isNotBlank(userEntity.getClientid())) {
            userService.pushToSingle(messageInfo.getTitle(), messageInfo.getContent(), userEntity.getClientid());
        }

        return Result.success();
    }

    @Override
    public IPage<TbCouponUser> getMyCouponList(Integer page, Integer limit, TbCouponUser couponUser) {

        Page<TbCouponUser> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return couponUserDao.getMyCouponList(pages, couponUser);
    }



    @Override
    public Result receiveActivity(Long userId, Long couponId) {
        reentrantReadWriteLock.readLock().lock();
        try {
            TbCoupon tbCoupon = tbCouponService.getById(couponId);
            if (tbCoupon.getCouponNum()>0){
                if (tbCoupon.getIsEnable() != 1) {
                    return Result.failed("40069");
                }
                Long num = couponUserDao.selectCount(new QueryWrapper<TbCouponUser>().eq("user_id", userId).eq("coupon_id", tbCoupon.getCouponId()));
                if (tbCoupon.getMaxReceive() != 0) {
                    if (tbCoupon.getMaxReceive() <= num) {
                        return Result.failed("40070");
                    }
                }
                giveCoupon(tbCoupon, userId);
                tbCoupon.setCouponNum(tbCoupon.getCouponNum() - 1);
                tbCouponService.updateById(tbCoupon);
                return Result.success();
            }else {
                return Result.failed("40071");
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }
        return Result.failed("40072");
    }



    /**
     * 赠送优惠券计算
     *
     * @param tbCoupon
     * @param userId
     */
    @Override
    public void giveCoupon(TbCoupon tbCoupon, Long userId) {
        TbCouponUser couponUser = new TbCouponUser();
        //copy对象
        BeanUtils.copyProperties(tbCoupon, couponUser);
        couponUser.setUserId(userId);
        couponUser.setCreateTime(new Date());
        //是否是永久有效
        if (tbCoupon.getValidDays() != null && tbCoupon.getValidDays() != 0) {
            //如果不是永久
            Calendar instance = Calendar.getInstance();
            instance.setTime(new Date());
            instance.add(Calendar.DATE, tbCoupon.getValidDays());
            couponUser.setExpirationTime(instance.getTime());
            couponUser.setValidDays(tbCoupon.getValidDays());
        } else {
            couponUser.setValidDays(0);
        }
        couponUser.setStatus(0);
        couponUserDao.insert(couponUser);
    }

    @Override
    public Result giveUserCoupon(String userIds, Long couponId, Integer num) {
        TbCoupon tbCoupon = tbCouponService.getById(couponId);
        String[] split = userIds.split(",");
        for (String userId : split) {
            for (int i = 0; i < num; i++) {
                giveCoupon(tbCoupon, Long.valueOf(userId));
            }
        }
        return Result.success();
    }

    @Override
    public IPage<TbCouponUser> getMyOrderCouponList(Integer page, Integer limit, TbOrders tbspOrder) {

        Page<TbCouponUser> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }

        return  baseMapper.getMyOrderCouponList(pages,tbspOrder.getEstimateMoney(),tbspOrder.getUserId());
    }


}
