package com.sqx.domain.repository.service.cash;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.Config;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.common.core.utils.Query;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.app.UserMoneyDetails;
import com.sqx.domain.repository.entity.sys.CashOut;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.mapper.cash.CashDao;
import com.sqx.domain.repository.mapper.cash.CashJpaRepository;
import com.sqx.domain.repository.mapper.cash.CashRepository;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import com.sqx.domain.repository.mapper.pay.CashOutDao;
import com.sqx.domain.repository.mapper.userinfo.UserRepository;
import com.sqx.domain.repository.service.app.UserMoneyDetailsService;
import com.sqx.domain.repository.service.app.impl.UserServiceImpl;
import com.sqx.domain.repository.service.money.IMoneyDetailsService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.wx.AccountService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 提现的service实现类
 */
@Service
public class CashServiceImpl  extends ServiceImpl<CashOutDao, CashOut> implements ICashService {
    private Logger logger = LoggerFactory.getLogger(CashServiceImpl.class);
    @Autowired
    private CashJpaRepository cashJpaRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private CommonRepository commonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IMoneyDetailsService iMoneyDetailsService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService iMessageService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CashDao cashDao;
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Override
    public Result findOne(long id) {
        return ResultUtil.success(cashJpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(long id) {
        cashJpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public Result findOne(String relationId) {
        return ResultUtil.success(cashRepository.findOne(relationId));
    }

    @Override
    public Result findByPhone(String phone) {
        return ResultUtil.success(cashRepository.findByPhone(phone));
    }

    @Override
    @Transactional
    public Result updateIsOut(Boolean isOut, Long id) {
        return ResultUtil.success(cashRepository.updateIsOut(isOut, id));
    }

    @Override
    public Result cashMoney(Long userId, BigDecimal money, Integer classify) {

        UserEntity userEntity = userService.selectUserById(userId);
        if (classify == null) {
            classify = 1;
        }
        CommonInfo one = commonInfoService.findOne(112);
        if (one != null && money.compareTo(new BigDecimal(one.getValue())) < 0) {
            return ResultUtil.error(-100,"输入金额不满足最低提现金额，请重新输入");
        }

        //查询账户的余额
        if (userEntity.getBalance().compareTo(money) < 0) {
            return ResultUtil.error(-100,"金额不足，请输入正确的金额");
        }

        //最高提现金额
        CommonInfo one2 = commonInfoService.findOne(153);
        if (one2 != null && money.compareTo(new BigDecimal(one2.getValue())) > 0) {
            return ResultUtil.error(-100,"输入金额过大，不能大于" + one2.getValue() + "，请重新输入");
        }

        if (classify == 2 || classify == 3) {
            String value = commonInfoService.findOne(244).getValue();
            if ("2".equals(value)) {
                if (StringUtils.isEmpty(userEntity.getWxImg())) {
                    return ResultUtil.error(-100,"请绑定微信提现收款码！");
                }
            }
        }

        CommonInfo one3 = commonInfoService.findOne(152);
        //根据当前提现的金额获取手续费
        BigDecimal rate = money.multiply(new BigDecimal(one3.getValue()));
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        //扣除金额
        BigDecimal deductMoney;

        //用户余额减去提现的金额和手续费是否大于0
        if (userEntity.getBalance().subtract(money).subtract(rate).compareTo(BigDecimal.ZERO) >= 0) {
            deductMoney = rate.add(money);
        } else {
            //减去提现的费用
            money = money.subtract(rate);
            deductMoney = money.add(rate);
        }
        userEntity.setBalance(userEntity.getBalance().subtract(deductMoney));
        userService.updateById(userEntity);
//        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
//        userMoney.setMoney(userMoney.getMoney().subtract(deductMoney));
//        userMoneyService.updateById(userMoney);
        userMoneyDetails.setMoney(deductMoney);
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("提现：" + money);
        userMoneyDetails.setType(2);
        userMoneyDetails.setClassify(2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userMoneyDetails.setCreateTime(sdf.format(new Date()));

        CashOut cashOut = new CashOut();
        cashOut.setState(0);
        cashOut.setClassify(classify);
        if (classify == 2 || classify == 3) {
            cashOut.setWxImg(userEntity.getWxImg());
            userMoneyDetails.setContent("微信提现：" + money + "，扣除收益：" + deductMoney + "，手续费：" + rate);
        } else {
            userMoneyDetails.setContent("支付宝提现：" + money + "，扣除收益：" + deductMoney + "，手续费：" + rate);
        }
        cashOut.setZhifubao(userEntity.getZhiFuBao());
        cashOut.setZhifubaoName(userEntity.getZhiFuBaoName());
        cashOut.setMoney(money.toString());
        cashOut.setCreateAt(sdf.format(new Date()));
        cashOut.setUserId(String.valueOf(userEntity.getUserId()));
        cashOut.setRate(rate.doubleValue());
        cashOut.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        baseMapper.insert(cashOut);
        userMoneyDetailsService.save(userMoneyDetails);
        //扣除金额直接在数据库进行操作
        return Result.success("10037");
    }

    @Override
    public PageUtils selectCashOutList(Map<String, Object> params) {
        String zhifubaoName = (String) params.get("zhifubaoName");
        String zhifubao = (String) params.get("zhifubao");
        String userId = String.valueOf(params.get("userId"));
        String classify = String.valueOf(params.get("classify"));
        String state = String.valueOf(params.get("state"));
        IPage<CashOut> page = this.page(
                new Query<CashOut>().getPage(params),
                new QueryWrapper<CashOut>()
                        .eq(StringUtils.isNotBlank(zhifubaoName), "zhifubao_name", zhifubaoName)
                        .eq(StringUtils.isNotBlank(zhifubao), "zhifubao", zhifubao)
                        .eq(StringUtils.isNotBlank(userId) && !"null".equals(userId), "user_id", userId)
                        .eq(StringUtils.isNotBlank(classify) && !"null".equals(classify) && "1".equals(classify), "classify", classify)
                        .eq(StringUtils.isNotBlank(state) && !"null".equals(state), "state", state)
                        .and(StringUtils.isNotBlank(classify) && !"null".equals(classify) && "2".equals(classify), wrapper -> wrapper.eq("classify", 2).or().eq("classify", 3))
                        .orderByDesc("id")
        );
        return new PageUtils(page);
    }

    @Override
    public Double sumMoney(String time, Integer flag) {
        return cashDao.sumMoney(time, flag);
    }

    @Override
    public Integer countMoney(String time, Integer flag) {
        return cashDao.countMoney(time, flag);
    }

    @Override
    public Integer stayMoney(String time, Integer flag) {
        return cashDao.stayMoney(time, flag);
    }

    @Override
    public Integer weiXinXCXMoney(String time, Integer flag) {
        return cashDao.weiXinXCXMoney(time, flag);
    }

    @Override
    public BigDecimal zfbMoney(String time, Integer flag) {
        return cashDao.zfbMoney(time, flag);
    }

    @Override
    public BigDecimal daiTXMoney(String time, Integer flag) {
        return cashDao.daiTXMoney(time, flag);
    }

    @Override
    public BigDecimal weiXinMoney(String time, Integer flag) {
        return cashDao.weiXinMoney(time,flag);
    }

    @Override
    public BigDecimal yiTXMoney(String time, Integer flag) {
        return cashDao.yiTXMoney(time,flag);
    }

    @Override
    public BigDecimal refuseMoney(String time, Integer flag) {
        return cashDao.refuseMoney(time,flag);
    }

    @Override
    public Integer yiTXCount(String time, Integer flag) {
        return cashDao.yiTXCount(time,flag);
    }

    @Override
    public Integer zfbMoneyCount(String time, Integer flag) {
        return cashDao.zfbMoneyCount(time,flag);
    }

    @Override
    public Integer refuseMoneyCount(String time, Integer flag) {
        return cashDao.refuseMoneyCount(time,flag);
    }


    //非线程安全实现，需要修改
    static class SingleByUserId {
        static Map<String, Object> map = new HashMap<>();

        static Object getById(String userId) {
            if (map.get(userId) != null) {
                return map.get(userId);
            } else {
                Object object = new Object();
                map.put(userId, object);
                return object;
            }
        }
    }

    /**
     * 结算成功通知
     *
     * @param openId
     * @param date
     * @param money
     * @param totalMoney
     * @param url
     */
    private void totalSuccess(String openId, String name, String date, String money, String totalMoney, String url) {
        UserEntity userByWxId = userRepository.getUserByWxId(openId);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(6));
        messageInfo.setContent(name + " 您好！您上月【确认收货订单】结算信息如下，请尽快申请提现！结算金额【" + money + "】进入【我的】点击【可提现金额】申请提现，提现周期到【下个月25号】,过期清零哦");
        messageInfo.setTitle("本月结算通知");
        messageInfo.setUserName(userByWxId.getUserName());
        messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
        iMessageService.saveBody(messageInfo);
        if (userByWxId.getClientid() != null) {
            userService.pushToSingle("本月结算通知", name + " 您好！您上月【确认收货订单】结算信息如下，请尽快申请提现！结算金额【" + money + "】进入【我的】点击【可提现金额】申请提现，提现周期到【下个月25号】,过期清零哦", userByWxId.getClientid());
        }

        CommonInfo three = commonRepository.findOne(37);
        String apkey = Config.JieSuanNotification;
        if (three != null) {
            apkey = three.getValue();
        }

        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        data.put("thing1", new TemplateMessageItem("您好！您上月【确认收货订单】已经结算", "#d71345"));
        data.put("amount4", new TemplateMessageItem(money + " 元", "#d71345"));
        accountService.sendWxMessage(apkey, data, openId, url);
    }

}
