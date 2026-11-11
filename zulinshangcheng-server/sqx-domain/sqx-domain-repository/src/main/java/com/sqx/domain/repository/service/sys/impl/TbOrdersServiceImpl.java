package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.CustomBeanUtils;
import com.sqx.domain.repository.entity.app.*;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.app.*;
import com.sqx.domain.repository.mapper.sys.*;
import com.sqx.domain.repository.mapper.userCertification.UserCertificationDao;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import com.sqx.domain.repository.service.sys.*;
import org.apache.catalina.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static cn.hutool.db.StatementUtil.prepareStatement;

/**
 * @author Admin
 * @description 针对表【tb_orders(用户订单表)】的数据库操作Service实现
 * @createDate 2024-09-14 14:43:13
 */
@Service
public class TbOrdersServiceImpl extends ServiceImpl<TbOrdersDao, TbOrders>
        implements TbOrdersService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCertificationDao userCertificationDao;

    @Autowired
    private GoodsSpecificationsDao goodsSpecificationsDao;

    @Autowired
    private LeaseDaysDao leaseDaysDao;

    @Autowired
    private TbCouponUserService couponUserService;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private BusinessDao businessDao;

    @Autowired
    private ICommonInfoService commonInfoService;

    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;

    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Autowired
    private UserMoneyDao userMoneyDao;

    @Autowired
    private MessageInfoDao messageInfoDao;

    @Autowired
    private OrdersHistoryDao ordersHistoryDao;
    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addOrders(TbOrders tbOrders) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            UserEntity userEntity = userDao.selectById(tbOrders.getUserId());
            if (userEntity == null) {
                return Result.failed("用户未登录");
            }
            UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>()
                    .eq("user_id", tbOrders.getUserId()));
            if (userCertification != null && userCertification.getStatus() != 1) {
                return Result.failed("请先完成实名认证");
            }
            tbOrders.setUserId(userEntity.getUserId());
            if (userEntity.getPhone() != null) {
                tbOrders.setPhone(userEntity.getPhone());
            }
            if (userEntity.getUserName() != null) {
                tbOrders.setConsignee(userEntity.getUserName());
            }
            LeaseDays leaseDays = leaseDaysDao.selectById(tbOrders.getDayId());
            if (leaseDays != null) {
                tbOrders.setLeaseTerm(Integer.valueOf(leaseDays.getDayName()));
            } else {
                return Result.failed("请选择租赁天数");
            }
            if (tbOrders.getGoodsId() != null) {
                Goods goods = goodsDao.selectById(tbOrders.getGoodsId());
                if (goods != null) {
                    tbOrders.setGoodsId(goods.getId());
//                    tbOrders.setBuyoutMoney(goods.getPurchaseMoney());
//                    tbOrders.setDeposit(goods.getAntecedentMoney());
                    GoodsSpecifications goodsSpecifications = goodsSpecificationsDao.selectById(tbOrders.getSpecId());
                    if (goodsSpecifications != null) {
                        if (tbOrders.getPayNum() != null && goodsSpecifications.getStockQuantity() <= tbOrders.getPayNum()) {
                            return Result.failed("库存不足");
                        } else if (tbOrders.getPayNum() != null) {
                            tbOrders.setDailyRent(goods.getPrice());
                            goodsSpecifications.setStockQuantity(goodsSpecifications.getStockQuantity() - tbOrders.getPayNum());
                            goodsSpecificationsDao.updateById(goodsSpecifications);
                        }
                    }
                }
            } else {
                return Result.failed("请选择规格");
            }
            if (tbOrders.getTakeState() == 1) {
                if (tbOrders.getFreight() == null) {
                    tbOrders.setFreight(BigDecimal.ZERO);
                } else {
                    tbOrders.setFreight(tbOrders.getFreight());
                }
            } else if (tbOrders.getTakeState() == 2) {
                tbOrders.setFreight(BigDecimal.ZERO);
                Business business = businessDao.selectById(tbOrders.getBusinessId());
                if (business != null) {
                    tbOrders.setPickupAddress(business.getBusinessAddress());
                }
            }
            // 预估价格 = 日租金 * 天数 * 购买的数量 + 押金 + 运费
            tbOrders.setEstimateMoney(
                    tbOrders.getDailyRent()
                            .multiply(BigDecimal.valueOf(tbOrders.getLeaseTerm()))
                            .multiply(BigDecimal.valueOf(tbOrders.getPayNum()))
                            .add(tbOrders.getDeposit())
                            .add(tbOrders.getFreight()));
            // 总租金 = 日租金 * 天数 * 购买的数量 + 运费
            tbOrders.setGrossRent(
                    tbOrders.getDailyRent()
                            .multiply(BigDecimal.valueOf(tbOrders.getLeaseTerm()))
                            .multiply(BigDecimal.valueOf(tbOrders.getPayNum()))
                            .add(tbOrders.getFreight()));
            String rate = commonInfoService.findOne(509).getValue();
            BigDecimal rate1 = new BigDecimal(rate);
            if (tbOrders.getUserCouponsId() != null) {
                TbCouponUser tbCouponUser = couponUserService.getById(tbOrders.getUserCouponsId());
                if (tbCouponUser == null) {
                    return Result.failed("该优惠券不存在");
                }
                if (tbCouponUser.getStatus() == 1) {
                    return Result.failed("该优惠券已使用");
                }
                if (tbCouponUser.getStatus() == 2) {
                    return Result.failed("该优惠券已失效");
                }
                if (tbOrders.getEstimateMoney().compareTo(tbCouponUser.getMoney()) > 0) {
                    tbOrders.setCouponMoney(tbCouponUser.getMoney());
                    tbOrders.setPayMoney(tbOrders.getEstimateMoney().subtract(tbCouponUser.getMoney()));
                    // 商家本单收入 = 预估价格 - (预估价格 * 平台抽成比例)
                    tbOrders.setMerchantIncome(tbOrders.getPayMoney().subtract(tbOrders.getPayMoney().multiply(rate1)));
                    tbCouponUser.setStatus(1);
                    couponUserService.updateById(tbCouponUser);
                } else {
                    return Result.failed("优惠券金额不足");
                }
            } else {
                tbOrders.setPayMoney(tbOrders.getEstimateMoney());
                tbOrders.setMerchantIncome(tbOrders.getEstimateMoney().subtract(tbOrders.getEstimateMoney().multiply(rate1)));
            }
            tbOrders.setCreateTime(new Date());
            tbOrders.setOrderNumber(generateOrder());
            tbOrders.setPayStatus(0);
            if (tbOrders.getAddressId() != null) {
                Address address = addressDao.selectById(tbOrders.getAddressId());
                if (address == null) {
                    return Result.failed("请选择收货地址");
                }
                tbOrders.setAddressDetail(address.getAddressDetail());
            }
            return baseMapper.insert(tbOrders) > 0 ? Result.success() : Result.failed("下单失败");
        } catch (Exception e) {
            log.error("下单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.failed("下单失败");
    }

    @Override
    public Result getOrdersList(Integer page, Integer limit, TbOrders tbOrders) {
//        UserEntity userEntity = userDao.selectById(tbOrders.getUserId());
//        if (userEntity == null) {
//            return Result.failed("用户未登录");
//        }
        Page<TbOrders> pageInfo = new Page<>(page, limit);
        LambdaQueryWrapper<TbOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(tbOrders.getUserId() != null, TbOrders::getUserId, tbOrders.getUserId());
        queryWrapper.eq(tbOrders.getPayStatus() != null, TbOrders::getPayStatus, tbOrders.getPayStatus());
        queryWrapper.like(tbOrders.getOrderNumber() != null, TbOrders::getOrderNumber, tbOrders.getOrderNumber());
        queryWrapper.orderByDesc(TbOrders::getCreateTime);
        return Result.success(baseMapper.selectPage(pageInfo, queryWrapper));
    }

    @Override
    public Result getOrdersById(Long id, Long userId) {
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.failed("用户未登录");
        }
        TbOrders tbOrders = baseMapper.selectById(id);
        if (tbOrders != null) {
            return Result.success(tbOrders);
        }
        return Result.failed("订单不存在");
    }

    @Transactional
    @Override
    public Result selfPickupByGoods(Long userId, String orderNumber) {
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.failed("用户未登录");
        }
        TbOrders tbOrders = baseMapper.selectOne(new LambdaQueryWrapper<TbOrders>().eq(TbOrders::getOrderNumber, orderNumber)
                .eq(TbOrders::getUserId, userId));
        if (tbOrders == null) {
            return Result.failed("订单不存在");
        }
        if (tbOrders.getPayStatus() != 1) {
            return Result.failed("订单状态异常,请刷新后重试");
        }
        if (tbOrders.getTakeState() != 2) {
            return Result.failed("提货方式有误，请检查");
        }
        String rate = commonInfoService.findOne(509).getValue();
        BigDecimal rate1 = new BigDecimal(rate);
        if (tbOrders.getCancellationTime() == null && tbOrders.getOrderNumber().equals(orderNumber)) {
            tbOrders.setCancellationTime(new Date());
            tbOrders.setPayStatus(3);
            tbOrders.setPlatformMoney(tbOrders.getPayMoney().multiply(rate1));
            baseMapper.updateById(tbOrders);
            Business business = businessDao.selectById(tbOrders.getBusinessId());
//            UserMoneyDetails userMoneyDetails = userMoneyDetailsDao.selectOne(new QueryWrapper<UserMoneyDetails>()
//                    .eq("user_id", tbOrders.getUserId())
//                    .eq("relation_id",tbOrders.getOrderNumber()));
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(business.getUserId());
            userMoneyDetails.setTitle("支付订单：" + tbOrders.getOrderNumber());
            userMoneyDetails.setMoney(tbOrders.getMerchantIncome());
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(1);
            userMoneyDetails.setContent("商家订单收入：" + tbOrders.getMerchantIncome());
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(date);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetails.setRelationId(Long.valueOf(tbOrders.getOrderNumber()));
            userMoneyDetailsDao.insert(userMoneyDetails);
            UserEntity userEntity1 = userDao.selectById(business.getUserId());
            userEntity1.setBalance(userEntity1.getBalance().add(tbOrders.getMerchantIncome()));
            userDao.updateById(userEntity1);
            UserMoney userMoney = userMoneyDao.selectOne(new LambdaQueryWrapper<UserMoney>()
                    .eq(UserMoney::getUserId, business.getUserId()));
            userMoney.setMoney(userMoney.getMoney().add(tbOrders.getMerchantIncome()));
            userMoneyDao.updateById(userMoney);
            // 收货成功提醒
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContent("您已成功收货！");
            messageInfo.setTitle("系统通知");
            messageInfo.setState(String.valueOf(4));
            messageInfo.setUserName(userEntity.getUserName());
            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
            Date date1 = new Date();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatter = simpleDateFormat1.format(date1);
            messageInfo.setCreateAt(formatter);
            messageInfo.setIsSee("0");
            messageInfoDao.insert(messageInfo);
            return Result.success("提货成功");
        } else {
            return Result.failed("订单号核销失败,请检查后重试");
        }
    }

    @Transactional
    @Override
    public Result merchantShipping(Long userId, Long id) {
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.failed("用户未登录");
        }
        Business business = businessDao.selectOne(new LambdaQueryWrapper<Business>().eq(Business::getUserId, userId));
        if (business == null) {
            return Result.failed("该商家不存在");
        }
        if (business.getStatus() != 1) {
            return Result.failed("请先认证商家");
        }
        TbOrders tbOrders = baseMapper.selectById(id);
        if (tbOrders == null) {
            return Result.failed("订单不存在");
        }
        if (tbOrders.getPayStatus() != 1) {
            return Result.failed("订单状态异常,请刷新后重试");
        }
        if (tbOrders.getTakeState() != 1) {
            return Result.failed("订单发货方式有误，请检查");
        }
        tbOrders.setPayStatus(2);
        tbOrders.setDeliveryTime(new Date());
        baseMapper.updateById(tbOrders);
        // 发货成功提醒
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent("您的订单已发货，请注意查收！");
        messageInfo.setTitle("系统通知");
        messageInfo.setState(String.valueOf(4));
        UserEntity user = userDao.selectById(tbOrders.getUserId());
        messageInfo.setUserId(String.valueOf(user.getUserId()));
        messageInfo.setUserName(user.getUserName());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatter = simpleDateFormat.format(date);
        messageInfo.setCreateAt(formatter);
        messageInfo.setIsSee("0");
        messageInfoDao.insert(messageInfo);
        return Result.success("发货成功");
    }

    @Override
    public Result selectOrdersByBusiness(Integer page, Integer limit,TbOrders tbOrders) {
        Page<TbOrders> pageInfo = new Page<>(page, limit);
        LambdaQueryWrapper<TbOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(tbOrders.getBusinessId() != null, TbOrders::getBusinessId, tbOrders.getBusinessId());
        queryWrapper.eq(tbOrders.getPayStatus() != null, TbOrders::getPayStatus, tbOrders.getPayStatus());
        queryWrapper.like(tbOrders.getOrderNumber() != null, TbOrders::getOrderNumber, tbOrders.getOrderNumber());
        queryWrapper.orderByDesc(TbOrders::getCreateTime);
        return Result.success(baseMapper.selectPage(pageInfo, queryWrapper));
    }

    @Transactional
    @Override
    public Result receiveGoods(Long userId, Long id) {
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.failed("用户未登录");
        }
        TbOrders tbOrders = baseMapper.selectById(id);
        if (tbOrders == null) {
            return Result.failed("订单不存在");
        }
        if (tbOrders.getPayStatus() != 2) {
            return Result.failed("订单状态异常,请刷新后重试");
        }
        if (tbOrders.getTakeState() != 1) {
            return Result.failed("收货方式有误，请检查");
        }
        tbOrders.setPayStatus(3);
        baseMapper.updateById(tbOrders);
        Business business = businessDao.selectById(tbOrders.getBusinessId());
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(business.getUserId());
        userMoneyDetails.setTitle("支付订单：" + tbOrders.getOrderNumber());
        userMoneyDetails.setMoney(tbOrders.getMerchantIncome());
        userMoneyDetails.setType(1);
        userMoneyDetails.setClassify(1);
        userMoneyDetails.setContent("商家订单收入：" + tbOrders.getMerchantIncome());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        userMoneyDetails.setCreateTime(format);
        userMoneyDetails.setRelationId(Long.valueOf(tbOrders.getOrderNumber()));
        userMoneyDetailsDao.insert(userMoneyDetails);
        UserEntity userEntity1 = userDao.selectById(business.getUserId());
        userEntity1.setBalance(userEntity1.getBalance().add(tbOrders.getMerchantIncome()));
        userDao.updateById(userEntity1);
        UserMoney userMoney = userMoneyDao.selectOne(new LambdaQueryWrapper<UserMoney>()
                .eq(UserMoney::getUserId, business.getUserId()));
        userMoney.setMoney(userMoney.getMoney().add(tbOrders.getMerchantIncome()));
        userMoneyDao.updateById(userMoney);
        return Result.success("收货成功");
    }

    @Transactional
    @Override
    public Result continuingLease(TbOrders tbOrders){
        // 1. 查询源表中的数据
        Optional<TbOrders> optionalTbOrders = baseMapper.selectByOrderId(tbOrders.getId());
        if (!optionalTbOrders.isPresent()) {
            throw new IllegalArgumentException("该订单不存在,id为： " + tbOrders.getId());
        }
        UserEntity userEntity = userDao.selectById(tbOrders.getUserId());
        if (userEntity == null) {
            return Result.failed("用户未登录");
        }
        TbOrders tbOrders1 = optionalTbOrders.get();
        if (tbOrders1 == null) {
            return Result.failed("订单不存在");
        }
        if (tbOrders1.getPayStatus() != 3) {
            return Result.failed("订单状态异常,请刷新后重试");
        }
        LeaseDays leaseDays = leaseDaysDao.selectById(tbOrders.getDayId());
        if (leaseDays != null) {
            tbOrders1.setLeaseTerm(Integer.valueOf(leaseDays.getDayName()));
        } else {
            return Result.failed("租赁天数不存在");
        }
        OrdersHistory ordersHistory = new OrdersHistory();
        try {
            CustomBeanUtils.copyNotNullProperties(tbOrders1, ordersHistory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("复制失败", e);
        }
        ordersHistoryDao.insert(ordersHistory);
        tbOrders1.setContinueDate(new Date());
        if(tbOrders.getFreight()==null){
            tbOrders.setFreight(BigDecimal.ZERO);
        }
        // 总租金 = 日租金 * 天数 * 购买的数量 + 运费
        tbOrders1.setGrossRent(
                tbOrders1.getDailyRent()
                        .multiply(BigDecimal.valueOf(tbOrders1.getLeaseTerm()))
                        .multiply(BigDecimal.valueOf(tbOrders1.getPayNum()))
                        .add(tbOrders.getFreight()));
        tbOrders1.setEstimateMoney(tbOrders1.getGrossRent());
        String rate = commonInfoService.findOne(509).getValue();
        BigDecimal rate1 = new BigDecimal(rate);
        if (tbOrders.getUserCouponsId() != null) {
            TbCouponUser tbCouponUser = couponUserService.getById(tbOrders.getUserCouponsId());
            if (tbCouponUser == null) {
                return Result.failed("该优惠券不存在");
            }
            if (tbCouponUser.getStatus() == 1) {
                return Result.failed("该优惠券已使用");
            }
            if (tbCouponUser.getStatus() == 2) {
                return Result.failed("该优惠券已失效");
            }
            if (tbOrders.getEstimateMoney().compareTo(tbCouponUser.getMoney()) > 0) {
                tbOrders1.setCouponMoney(tbCouponUser.getMoney());
                tbOrders1.setPayMoney(tbOrders.getEstimateMoney().subtract(tbCouponUser.getMoney()));
                // 商家本单收入 = 预估价格 - (预估价格 * 平台抽成比例)
                tbOrders1.setMerchantIncome(tbOrders.getPayMoney().subtract(tbOrders.getPayMoney().multiply(rate1)));
                tbCouponUser.setStatus(1);
                couponUserService.updateById(tbCouponUser);
            } else {
                return Result.failed("优惠券金额不足");
            }
        } else {
            tbOrders1.setPayMoney(tbOrders1.getEstimateMoney());
            tbOrders1.setMerchantIncome(tbOrders1.getEstimateMoney().subtract(tbOrders1.getEstimateMoney().multiply(rate1)));
        }
        tbOrders1.setPayStatus(7);
        baseMapper.updateById(tbOrders1);
        return Result.success("续租下单成功");
    }

    @Override
    @Transactional
    public Result buyout(Long userId, Long id){
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.failed("用户未登录");
        }
        // 1. 查询源表中的数据
        Optional<TbOrders> optionalTbOrders = baseMapper.selectByOrderId(id);
        if (!optionalTbOrders.isPresent()) {
            throw new IllegalArgumentException("该订单不存在,id为： " + id);
        }
        TbOrders tbOrders = optionalTbOrders.get();
        if (tbOrders == null) {
            return Result.failed("订单不存在");
        }
        // 如果买断的话需将之前的订单复制到历史订单表
        OrdersHistory ordersHistory = new OrdersHistory();
        try {
            CustomBeanUtils.copyNotNullProperties(tbOrders, ordersHistory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("复制失败", e);
        }
        ordersHistoryDao.insert(ordersHistory);
        tbOrders.setPayStatus(8);
        GoodsSpecifications goodsSpecifications = goodsSpecificationsDao.selectById(tbOrders.getSpecId());
        if (goodsSpecifications == null) {
            return Result.failed("商品规格不存在");
        }
        Goods goods = goodsDao.selectById(tbOrders.getGoodsId());
        if (goods == null){
            return Result.failed("商品不存在");
        }
        String rate = commonInfoService.findOne(509).getValue();
        BigDecimal rate1 = new BigDecimal(rate);
        if (tbOrders.getUserCouponsId() != null) {
            TbCouponUser tbCouponUser = couponUserService.getById(tbOrders.getUserCouponsId());
            if (tbCouponUser == null) {
                return Result.failed("该优惠券不存在");
            }
            if (tbCouponUser.getStatus() == 1) {
                return Result.failed("该优惠券已使用");
            }
            if (tbCouponUser.getStatus() == 2) {
                return Result.failed("该优惠券已失效");
            }
//            if (goods.getPurchaseMoney().compareTo(tbCouponUser.getMoney()) > 0) {
//                tbOrders.setCouponMoney(tbCouponUser.getMoney());
//                tbOrders.setPayMoney(goods.getPurchaseMoney().subtract(tbCouponUser.getMoney()));
//                // 商家本单收入 = 预估价格 - (预估价格 * 平台抽成比例)
//                tbOrders.setMerchantIncome(tbOrders.getPayMoney().subtract(tbOrders.getPayMoney().multiply(rate1)));
//                tbCouponUser.setStatus(1);
//                couponUserService.updateById(tbCouponUser);
//            } else {
//                return Result.failed("优惠券金额不足");
//            }
        }
//        // 商品买断价格 = 买断价 - 押金 - 租赁付款金额
//        tbOrders.setBuyoutMoney(goods.getPurchaseMoney()
//                .subtract(goods.getAntecedentMoney())
//                .subtract(tbOrders.getGrossRent()));
//        tbOrders.setPayMoney(tbOrders.getBuyoutMoney());
        // 商家本单收入 = 预估价格 - (预估价格 * 平台抽成比例)
        tbOrders.setMerchantIncome(tbOrders.getPayMoney().subtract(tbOrders.getPayMoney().multiply(rate1)));
        baseMapper.updateById(tbOrders);
        Business business = businessDao.selectById(tbOrders.getBusinessId());
        UserEntity user = userDao.selectById(business.getUserId());
        MessageInfo msgInfo = new MessageInfo();
        msgInfo.setContent("您有一笔订单已被买断");
        msgInfo.setTitle("系统通知");
        msgInfo.setState(String.valueOf(6));
        msgInfo.setUserName(user.getUserName());
        msgInfo.setUserId(String.valueOf(user.getUserId()));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(date);
        msgInfo.setCreateAt(currentTime);
        messageInfoDao.insert(msgInfo);
        return Result.success("买断下单成功");
    }

    @Override
    public Result returnOrder(TbOrders tbOrders) {
        UserEntity userEntity = userDao.selectById(tbOrders.getUserId());
        if (userEntity == null) {
            return Result.failed("用户未登录");
        }
        TbOrders tbOrders1 = baseMapper.selectById(tbOrders.getId());
        if (tbOrders1 == null) {
            return Result.failed("订单不存在");
        }
        if (tbOrders1.getTakeState() == 1) {
            if (tbOrders1.getPayStatus() != 4) {
                return Result.failed("订单状态异常,请刷新后重试");
            }
            tbOrders.setExpressName(tbOrders.getExpressName());
            tbOrders.setExpressNumber(tbOrders.getExpressNumber());
            tbOrders.setPayStatus(5);
            baseMapper.updateById(tbOrders);
            Business business = businessDao.selectById(tbOrders1.getBusinessId());
            UserEntity user = userDao.selectById(business.getUserId());
            MessageInfo msgInfo = new MessageInfo();
            msgInfo.setContent("您有一笔订单正在归还中");
            msgInfo.setTitle("系统通知");
            msgInfo.setState(String.valueOf(6));
            msgInfo.setUserName(user.getUserName());
            msgInfo.setUserId(String.valueOf(user.getUserId()));
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(date);
            msgInfo.setCreateAt(currentTime);
            messageInfoDao.insert(msgInfo);
            return Result.success("正在归还,已发货");
        } else if (tbOrders1.getTakeState() == 2) {
//            if (tbOrders.getOrderNumber().equals(tbOrders1.getOrderNumber())){
            tbOrders.setPayStatus(6);
            baseMapper.updateById(tbOrders);
            return Result.success("已完成归还");
//            }else {
//                return Result.failed("订单编号错误");
//            }
        }
        return Result.failed("商品归还异常,请刷新后重试");
    }

    @Override
    public Result businessDelivery(Long userId, Long id) {
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.failed("用户未登录");
        }
        TbOrders tbOrders = baseMapper.selectById(id);
        if (tbOrders == null) {
            return Result.failed("订单不存在");
        }
        if (tbOrders.getPayStatus() != 5) {
            return Result.failed("订单状态异常,请刷新后重试");
        }
        if (tbOrders.getTakeState() != 1) {
            return Result.failed("商品状态异常，请刷新后重试");
        }
        tbOrders.setPayStatus(6);
        baseMapper.updateById(tbOrders);
        return Result.success("订单已完成");
    }

    @Override
    public Result payOrderBack(Long orderId, BigDecimal money, Integer classify) {
        TbOrders tbOrders = baseMapper.selectById(orderId);
        if (tbOrders != null) {
            tbOrders.setPayStatus(1);
//                tbOrders.setIsPay(1);
            tbOrders.setPayTime(new Date());

            String title = "";
            if (classify == 1 || classify == 2 || classify == 3) {
                //微信
                title = "微信";
                tbOrders.setPayWay(2);
            } else if (classify == 4 || classify == 5) {
                //支付宝
                title = "支付宝";
                tbOrders.setPayWay(3);
            } else {
                //余额
                title = "余额";
                tbOrders.setPayWay(1);
            }
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(tbOrders.getUserId());
            userMoneyDetails.setType(2);
            userMoneyDetails.setState(1);
            userMoneyDetails.setClassify(1);
            userMoneyDetails.setMoney(money);
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = simpleDateFormat.format(date);
            userMoneyDetails.setCreateTime(currentTime);
            userMoneyDetails.setTitle("支付订单" + tbOrders.getOrderNumber());
            userMoneyDetails.setContent(title + "支付订单" + tbOrders.getOrderNumber() + "成功,花费" + money + "元");
            userMoneyDetailsService.save(userMoneyDetails);
            baseMapper.updateById(tbOrders);
            //写入上级及分成金额
//                updateIndentRateMoney(tbOrders, tbOrders.getUserId());
            BigDecimal pingMoney = BigDecimal.ZERO;
//                if (tbOrders.getUserScaleMoney() != null && tbOrders.getUserScaleMoney().compareTo(BigDecimal.ZERO) > 0) {
//                    pingMoney = pingMoney.subtract(tbOrders.getUserScaleMoney());
//                }
//                if (tbOrders.getUserTwoMoney() != null && tbOrders.getUserTwoMoney().compareTo(BigDecimal.ZERO) > 0) {
//                    pingMoney = pingMoney.subtract(tbOrders.getUserTwoMoney());
//                }

//                tbOrders.setPingMoney(pingMoney);

            return baseMapper.updateById(tbOrders) > 0 ? Result.success("支付成功") : Result.failed("支付失败");
        } else {
            return Result.failed("未查询到订单,请联系管理员");
        }
    }


    public String generateOrder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        Random random = new Random();
        int i = (int) ((Math.random() * 9 + 1) * 10000);
        return format + i;
    }
}




