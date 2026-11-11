package com.sqx.domain.repository.service.vipDetails.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.app.UserMoney;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import com.sqx.domain.repository.entity.app.VipDetails;
import com.sqx.domain.repository.entity.app.vo.PaymentFormVO;
import com.sqx.domain.repository.entity.pay.PayDetails;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.vipDetails.VipDetailsDao;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import com.sqx.domain.repository.service.app.UserMoneyService;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.pay.AliPayService;
import com.sqx.domain.repository.service.pay.PayDetailsService;
import com.sqx.domain.repository.service.pay.WxService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.vipDetails.VipDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class VipDetailsServiceImpl extends ServiceImpl<VipDetailsDao, VipDetails> implements VipDetailsService {
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private WxService wxService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private UserDao userDao;

    @Override
    public IPage<VipDetails> getVipDetailsList(Integer page, Integer limit, Integer isEnable) {
        Page<VipDetails> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        QueryWrapper<VipDetails> wrapper = new QueryWrapper<>();
        if (isEnable != null) {
            wrapper.eq("is_enable", isEnable);
        }
        wrapper.orderByAsc("vip_name_type");
        return vipDetailsDao.selectPage(pages, wrapper);

    }

    @Override
    public IPage<VipDetails> selectVipDetailsList(Integer page, Integer limit,Integer isEnable) {
        Page<VipDetails> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
        }
        QueryWrapper<VipDetails> wrapper = new QueryWrapper<>();
        if (isEnable != null) {
            wrapper.eq("is_enable", isEnable);
        }
        wrapper.orderByAsc("vip_month_count");
        return vipDetailsDao.selectPage(pages, wrapper);
    }

    @Override
    public Result insetVipDetails(VipDetails vipDetails) {
        VipDetails name = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("vip_name", vipDetails.getVipName()));
        if (name != null) {
            return Result.failed("会员分类名称不能重复");
        }
        vipDetails.setUpdateTime(new Date());
        return Result.upStatus(vipDetailsDao.insert(vipDetails));

    }

    @Override
    public Result updateVipDetails(VipDetails vipDetails) {
        vipDetails.setUpdateTime(new Date());
        return Result.upStatus(vipDetailsDao.updateById(vipDetails));
    }

    @Override
    public Result deleteVipDetails(Long id) {
        return Result.upStatus(vipDetailsDao.deleteById(id));


    }

    @Override
    public Result userBuyVip(Long userId, PaymentFormVO paymentFormVO) throws Exception {
        VipDetails vipDetails = vipDetailsDao.selectById(paymentFormVO.getVipDetailsId());
        if (vipDetails == null) {
            return Result.failed("当前会员类型不存在");
        }
        if (paymentFormVO.getClassify() == 0) {
            //余额
            return balancePay(userId, paymentFormVO.getVipDetailsId());
        } else if (paymentFormVO.getClassify() == 1 || paymentFormVO.getClassify() == 2 || paymentFormVO.getClassify() == 3) {
            //微信
            return wxService.payMoney(userId, vipDetails.getMoney(), vipDetails.getId(), paymentFormVO.getClassify(),3);
        } else if (paymentFormVO.getClassify() == 4 || paymentFormVO.getClassify() == 5) {
            //支付宝支付
            return aliPayService.payMoney(userId, vipDetails.getMoney(), vipDetails.getId(), paymentFormVO.getClassify(),3);
        }
            return Result.failed("支付方式错误");
        }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result balancePay(Long userId, Long id) {
        VipDetails vipDetails = vipDetailsDao.selectById(id);
        if (vipDetails == null) {
            return Result.failed("当前会员类型不存在");
        }
        UserEntity wallet = userService.getById(userId);
        if (wallet.getBalance().compareTo(vipDetails.getMoney()) >= 0) {
            userDao.updateReduceMoney(wallet.getUserId(),vipDetails.getMoney().doubleValue());
            buyVip(userId, vipDetails, 0);
            return Result.success("购买成功");
        } else {
            return Result.failed("你的余额不足,请充值后重试");
        }
    }

    /**
     * 微信 支付宝（购买会员、充值金额）回调
     *
     * @param outTradeNo 支付订单号
     */
    @Override
    public void callbackBusiness(String outTradeNo) {
        PayDetails payDetails = payDetailsService.getOne(new QueryWrapper<PayDetails>().eq("order_id", outTradeNo));
        VipDetails details = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("id", payDetails.getParentId()));
        if (payDetails.getState() == 1) {
            if (payDetails.getType() == 3) {
                buyVip(payDetails.getUserId(), details, payDetails.getClassify());
            } else {
//                userMoneyService.updateMoney(1, payDetails.getUserId(), payDetails.getMoney());
                UserMoneyDetails moneyDetails = new UserMoneyDetails();
                moneyDetails.setTitle("余额充值");
                moneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                moneyDetails.setUserId(payDetails.getUserId());
                moneyDetails.setClassify(payDetails.getClassify());
                moneyDetails.setType(1);
                moneyDetails.setState(2);
                moneyDetails.setContent("余额充值:"+ payDetails.getMoney());
                moneyDetails.setCreateTime(DateUtils.format(new Date()));
                userMoneyDetailsService.save(moneyDetails);
            }
        }

    }

    @Override
    public Result giveUserVip(Long userId, Integer num) {
        UserEntity userEntity = userService.getById(userId);
        if (userEntity.getVipEndTime() != null) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                userEntity.setVipEndTime(LocalDateTime.now().plusDays(num));
            } else {
                userEntity.setVipEndTime(userEntity.getVipEndTime().plusDays(num));
            }
        } else {
            userEntity.setVipEndTime(LocalDateTime.now().plusDays(num));
        }
        userEntity.setIsVip(1);
        userService.updateById(userEntity);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setUserId(userId.toString());
        messageInfo.setTitle("赠送会员");
        messageInfo.setContent("系统赠送您会员共："+ num +"天");
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Override
    public Result cancelUserVip(Long userId) {
        UserEntity userEntity = userService.getById(userId);
        userEntity.setIsVip(0);
        userEntity.setVipEndTime(LocalDateTime.now());
        return userService.updateById(userEntity) ? Result.success() : Result.failed();
    }

    @Override
    public void callBackByVip() {
        List<PayDetails> payDetailsList = payDetailsService.list(new QueryWrapper<PayDetails>()
                .eq("type", 3)
                .eq("state", 1));
        for (PayDetails payDetails : payDetailsList) {
            UserEntity userEntity = userService.getById(payDetails.getUserId());
            userEntity.setIsVip(1);
            userEntity.setVipEndTime(LocalDateTime.now().plusMonths(payDetails.getBuyNum()));
            userService.updateById(userEntity);
        }
    }

    /**
     * @param userId   用户id
     * @param details  会员信息
     * @param classify 支付类型
     */
    private void buyVip(Long userId, VipDetails details, Integer classify) {
        UserEntity userEntity = userService.getById(userId);
        userEntity.setIsVip(1);
        if (userEntity.getVipEndTime() != null) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                userEntity.setVipEndTime(LocalDateTime.now().plusMonths(details.getVipMonthCount()));
            } else {
                userEntity.setVipEndTime(userEntity.getVipEndTime().plusMonths(details.getVipMonthCount()));
            }
        } else {
            userEntity.setVipEndTime(LocalDateTime.now().plusMonths(details.getVipMonthCount()));
        }
        userService.updateById(userEntity);
        UserMoneyDetails moneyDetails = new UserMoneyDetails();
        moneyDetails.setTitle("购买会员<"+details.getVipName()+">");
        moneyDetails.setMoney(details.getMoney());
        moneyDetails.setUserId(userId);
        moneyDetails.setClassify(2);
        moneyDetails.setType(2);
        moneyDetails.setState(2);
        moneyDetails.setContent("购买会员<"+details.getVipName()+">,花费:"+details.getMoney());
        moneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetailsService.save(moneyDetails);
//        CommonInfo commonInfo = commonInfoService.findOne(209);
//        if ("是".equals(commonInfo.getValue())) {
//            UserEntity parentEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("invitation_code", userEntity.getInviterCode()));
            //一级分佣
//            if (parentEntity != null) {
//                if (parentEntity.getZhiRate().compareTo(BigDecimal.ZERO) > 0) {
//                    BigDecimal rate = details.getMoney().multiply(parentEntity.getZhiRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
//                    parentEntity.setBalance(parentEntity.getBalance().add(rate));
//                    userService.updateById(parentEntity);
//                    userMoneyDetailsService.setParentMoneyDetails(userId, parentEntity.getUserId(), rate, 1);
//                    UserEntity parentParentEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("invitation_code", parentEntity.getInviterCode()));
//                    //二级分佣
//                    if (parentParentEntity != null) {
//                        if (parentParentEntity.getFeiRate().compareTo(BigDecimal.ZERO) > 0) {
//                            rate = details.getMoney().multiply(parentParentEntity.getFeiRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
//                            parentParentEntity.setBalance(parentParentEntity.getBalance().add(rate));
//                            userService.updateById(parentParentEntity);
//                            userMoneyDetailsService.setParentMoneyDetails(userId, parentParentEntity.getUserId(), rate, 2);
//                        }
//                    }
//                }
//            }
//        }
    }
}
