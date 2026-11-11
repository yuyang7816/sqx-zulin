package com.sqx.system.cash;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.sqx.common.core.constant.AliPayConstants;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.enums.ExceptionEnum;
import com.sqx.common.core.utils.Config;
import com.sqx.common.core.utils.HttpClient;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.pay.AliPayWithdrawModel;
import com.sqx.domain.repository.entity.sys.CashOut;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.mapper.cash.CashJpaRepository;
import com.sqx.domain.repository.mapper.cash.CashRepository;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import com.sqx.domain.repository.mapper.userinfo.UserJpaRepository;
import com.sqx.domain.repository.mapper.userinfo.UserRepository;
import com.sqx.domain.repository.service.app.impl.UserServiceImpl;
import com.sqx.domain.repository.service.cash.ICashService;
import com.sqx.domain.repository.service.money.IMoneyDetailsService;
import com.sqx.domain.repository.service.pay.PayDetailsService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.wx.AccountService;
import com.sqx.domain.repository.service.wx.AccountTokenServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(value = "用户结算管理", tags = {"用户结算管理"})
@RequestMapping(value = "/cash")
public class CashController {
    private Logger logger = LoggerFactory.getLogger(CashController.class);
    @Autowired
    private ICashService iCashService;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CashJpaRepository cashJpaRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private CommonRepository commonRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MessageService iMessageService;
    @Autowired
    private IMoneyDetailsService iMoneyDetailsService;
    @Autowired
    private PayDetailsService payDetailsService;



    @RequestMapping(value = "/sendMsg", method = RequestMethod.GET)
    @ApiOperation("管理平台主动推送消息")
    @ResponseBody
    public Result sendMsg(String title, String content, String phone, Integer flag){
        if(flag==1){
            //根据手机号推送
            UserEntity userByPhone = userRepository.getUserByPhone(phone);
            if(null==userByPhone){
                return ResultUtil.error(-100,"手机号不存在！");
            }
            send(userByPhone,title,content);
        }else{
            //所有人推送
            List<UserEntity> userInfos = userRepository.selectUserList();
            //用户数量较大  使用多线程推送  根据用户数量进行拆分  同时按照3个线程进行推送
            int count = userInfos.size() / 3;
            new Thread(() -> {
                for(int i=0 ;i<count;i++){
                    send(userInfos.get(i),title,content);
                }
            }).start();
            new Thread(() -> {
                for(int i=count ;i<count*2;i++){
                    send(userInfos.get(i),title,content);
                }
            }).start();
            new Thread(() -> {
                for(int i=count*2 ;i<userInfos.size();i++){
                    send(userInfos.get(i),title,content);
                }
            }).start();
           /* for(UserInfo userByPhone:userInfos){

            }*/
        }
        return ResultUtil.success();
    }


    private void send(UserEntity userByPhone,String title,String content){
        if (userByPhone.getClientid() != null) {
            userService.pushToSingle(title, content, userByPhone.getClientid());
        }
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent(content);
        messageInfo.setTitle(title);
        messageInfo.setState(String.valueOf(8));
        messageInfo.setUserName(userByPhone.getUserName());
        messageInfo.setUserId(String.valueOf(userByPhone.getUserId()));
        iMessageService.saveBody(messageInfo);
    }

    /**
     * 查询充值记录
     *
     * @return
     */
    @RequestMapping(value = "/selectPayDetails", method = RequestMethod.POST)
    @ApiOperation("查询充值记录")
    @ResponseBody
    public Result selectPayDetails(@ApiParam("页码")int page,
                                   @ApiParam("条数")int limit,String phone,Long userId,String startTime,String endTime) {
//        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
//        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return ResultUtil.success(payDetailsService.selectPayDetails(page,limit,startTime,endTime,userId,1));
    }



    /**
     * 管理平台查询某个用户的提现记录
     *
     * @return
     */
    @RequestMapping(value = "/phone/{phone}", method = RequestMethod.GET)
    @ApiOperation("管理平台查询某个用户的提现记录")
    @ResponseBody
    public Result findByPhone(@PathVariable String phone) {
        return iCashService.findByPhone(phone);
    }

    /**
     * 管理平台查询用户提现列表
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/page/{page}/{size}", method = RequestMethod.GET)
    @ApiOperation("管理平台查询用户提现列表")
    @ResponseBody
    public Result getList(@PathVariable int page, @PathVariable int size) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return ResultUtil.success(cashJpaRepository.findAll(pageable));
    }

    @RequestMapping(value = "/selectCashList", method = RequestMethod.GET)
    @ApiOperation("管理平台查询用户提现列表")
    @ResponseBody
    public Result selectCashList(Integer page,Integer size,String zhifubaoName,String relationId,Integer state) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return ResultUtil.success(cashRepository.selectCashList(pageable,zhifubaoName,relationId,state));
    }

    @ApiOperation("财务提现统计")
    @GetMapping("/statisticsCashMoney")
    public Result statisticsMoney(@ApiParam("时间（年月日）")String time,
                                  @ApiParam("筛选1 按年月日 2年月 3年")Integer flag) {
        // 累计提现金额(所有的)
        Double sumMoney = iCashService.sumMoney(time, flag);
        // 累计提现笔数(所有的)
        Integer countMoney = iCashService.countMoney(time, flag);
        // 待转账次数
        Integer stayMoney = iCashService.stayMoney(time, flag);
        // 待提现金额
        BigDecimal daiTXMoney = iCashService.daiTXMoney(time, flag);
        // 累计已提现金额（成功的）
        BigDecimal yiTXMoney = iCashService.yiTXMoney(time, flag);
        // 累计提现次数(成功的)
        Integer yiTXCount = iCashService.yiTXCount(time, flag);
        // 微信总提现次数
        Integer weiXinXCXMoney = iCashService.weiXinXCXMoney(time, flag);
        // 支付宝总提现次数
        Integer zfbMoneyCount = iCashService.zfbMoneyCount(time, flag);
        // 微信已提现金额
        BigDecimal weiXinMoney = iCashService.weiXinMoney(time, flag);
        // 支付宝已提现金额
        BigDecimal zfbMoney = iCashService.zfbMoney(time, flag);
        // 申请提现金额
        BigDecimal applyMoney = iCashService.daiTXMoney(time, flag);
        // 拒绝提现金额
        BigDecimal refuseMoney = iCashService.refuseMoney(time, flag);
        // 拒绝提现笔数
        Integer refuseMoneyCount = iCashService.refuseMoneyCount(time, flag);


        Map<String, Object> map = new HashMap<>();
        map.put("sumMoney", sumMoney == null ? 0.00 : sumMoney);
        map.put("countMoney", countMoney == null ? 0 : countMoney);
        map.put("stayMoney", stayMoney == null ? 0 : stayMoney);
        map.put("daiTXMoney", daiTXMoney == null ? 0.00 : daiTXMoney);
        map.put("yiTXMoney", yiTXMoney == null ? 0.00 : yiTXMoney);
        map.put("yiTXCount", yiTXCount == null ? 0 : yiTXCount);
        map.put("weiXinXCXMoney", weiXinXCXMoney == null ? 0 : weiXinXCXMoney);
        map.put("weiXinMoney", weiXinMoney == null ? 0.00 : weiXinMoney);
        map.put("zfbMoney", zfbMoney == null ? 0.00 : zfbMoney);
        map.put("applyMoney", applyMoney == null ? 0.00 : applyMoney);
        map.put("refuseMoney", refuseMoney == null ? 0.00 : refuseMoney);
        map.put("zfbMoneyCount", zfbMoneyCount == null ? 0 : zfbMoneyCount);
        map.put("refuseMoneyCount", refuseMoneyCount == null ? 0 : refuseMoneyCount);
        return Result.success(map);
    }



    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("管理平台修改用户提现状态")
    @ResponseBody
    public Result updateIsOut(@RequestParam Boolean isOut, @RequestParam Long id) {
        return iCashService.updateIsOut(isOut, id);
    }



    /**
     * 管理平台确认提现
     *
     * @param cashId
     * @return
     */
    @RequestMapping(value = "/update/{cashId}", method = RequestMethod.POST)
    @ApiOperation("管理平台确认提现")
    @ResponseBody
    public Result update(@PathVariable Long cashId) {
        CashOut one1 = cashJpaRepository.findById(cashId).orElse(null);
        CommonInfo url = commonRepository.findOne(19);
        CashOut one = cashJpaRepository.findById(cashId).orElse(null);
        if (one == null) {
            return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
        }
        assert one1 != null;
        if (one1.getOut()) {
            return ResultUtil.error(9999, one1.getZhifubaoName() + "转账失败！原因是用户已转账");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        one.setOut(true);
        one.setOutAt(sdf.format(now));
        CashOut save = cashJpaRepository.save(one);
        String userId = save.getUserId();
        UserEntity userInfo = userJpaRepository.getOne(Long.valueOf(userId));
        if (userInfo != null && userInfo.getOpenId() != null) {
            //推送提现通知消息
            cashOutSuccess(userInfo.getOpenId(), save.getOutAt(), save.getMoney(), save.getZhifubao(), url.getValue());
        }
        return ResultUtil.success(one1.getZhifubaoName() + "转账成功");
    }


    @RequestMapping(value = "/alipay/{cashId}", method = RequestMethod.POST)
    @ApiOperation("管理平台确认提现")
    @ResponseBody
    public Result alipayPay(@PathVariable Long cashId) {
        /**
         * 初始化客户端
         */
       AlipayClient alipayClient = new DefaultAlipayClient(AliPayConstants.REQUEST_URL,
               commonRepository.findOne(63).getValue(), commonRepository.findOne(65).getValue(), AliPayConstants.FORMAT,
                AliPayConstants.CHARSET, commonRepository.findOne(64).getValue(), AliPayConstants.SIGNTYPE);
        CashOut one1 = cashJpaRepository.findById(cashId).orElse(null);
        //获取全局名称
        //是否开启企业支付
        CommonInfo commonInfo = commonRepository.findOne(51);
        CashOut one = cashJpaRepository.findById(cashId).orElse(null);
        if (one == null) {
            return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
        }
        assert one1 != null;
        if (one1.getOut()) {
            return ResultUtil.error(9999, one1.getZhifubaoName() + "转账失败！原因是用户已转账");
        }
        String date = String.valueOf(System.currentTimeMillis());
        if (commonInfo.getValue() != null && commonInfo.getValue().equals("是")) {
            val aliPayWithdrawModel = AliPayWithdrawModel.builder()
                    .out_biz_no(date)
                    .amount(new BigDecimal(one.getMoney()))
                    .payee_account(one.getZhifubao())
                    .payee_real_name(one.getZhifubaoName())
                    .payee_type(AliPayConstants.PAY_TYPE)
                    .remark(commonRepository.findOne(12).getValue() + "佣金结算")
                    .build();
            String json = JSON.toJSONString(aliPayWithdrawModel);
            //实例化连接对象
            AlipayFundTransToaccountTransferRequest withdrawRequest = new AlipayFundTransToaccountTransferRequest();
            withdrawRequest.setBizContent(json);
            try {
                AlipayFundTransToaccountTransferResponse response = alipayClient.execute(withdrawRequest);
                if (AliPayConstants.SUCCESS_CODE.equalsIgnoreCase(response.getCode())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    one.setOut(true);
                    one.setOrderNumber(date);
                    one.setOutAt(sdf.format(new Date()));
                    CashOut save = cashJpaRepository.save(one);
                    UserEntity userInfo = userJpaRepository.getOne(Long.valueOf(save.getUserId()));
                    if (userInfo != null && userInfo.getOpenId() != null) {
                        //提现通知消息
                        cashOutSuccess(userInfo.getOpenId(), save.getOutAt(), save.getMoney(), save.getZhifubao(), commonRepository.findOne(19).getValue());
                    }
                    return ResultUtil.success(one1.getZhifubaoName() + "转账成功");
                } else {
                    return ResultUtil.error(9999, one1.getZhifubaoName() + "转账失败！" + response.getSubMsg());
                }
            } catch (AlipayApiException e) {
                logger.error("零钱提现异常原因:" + e.getMessage());
                e.printStackTrace();
                return ResultUtil.error(9999, one1.getZhifubaoName() + "转账失败！" + e.getMessage());

            }
        } else {
            //人工转账后改变状态的
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            one.setOut(true);
            one.setOrderNumber(date);
            one.setOutAt(sdf.format(now));
            CashOut save = cashJpaRepository.save(one);
            String userId = save.getUserId();
            UserEntity userInfo = userJpaRepository.getOne(Long.valueOf(userId));
            if (userInfo != null && userInfo.getOpenId() != null) {
                //推送提现通知消息
                cashOutSuccess(userInfo.getOpenId(), save.getOutAt(), save.getMoney(), save.getZhifubao(), commonRepository.findOne(19).getValue());
            }
            return ResultUtil.success(one1.getZhifubaoName() + "转账成功");
        }
    }

    @RequestMapping(value = "/v1/alipay/{cashId}", method = RequestMethod.POST)
    @ApiOperation("管理平台确认提现")
    @ResponseBody
    public Result v1alipayPay(@PathVariable Long cashId) {
        /**
         * 初始化客户端
         */
        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConstants.REQUEST_URL,
                commonRepository.findOne(63).getValue(), commonRepository.findOne(65).getValue(), AliPayConstants.FORMAT,
                AliPayConstants.CHARSET, commonRepository.findOne(64).getValue(), AliPayConstants.SIGNTYPE);
        CashOut one1 = cashJpaRepository.findById(cashId).orElse(null);
        //获取全局名称
        //是否开启企业支付
        CommonInfo commonInfo = commonRepository.findOne(51);
        CashOut one = cashJpaRepository.findById(cashId).orElse(null);
        if (one == null) {
            return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
        }
        assert one1 != null;
        if (one1.getState()!=0) {
            return ResultUtil.error(9999, one1.getZhifubaoName() + "转账失败！原因是用户已转账");
        }
        String date = String.valueOf(System.currentTimeMillis());
        if (commonInfo.getValue() != null && commonInfo.getValue().equals("是")) {
            val aliPayWithdrawModel = AliPayWithdrawModel.builder()
                    .out_biz_no(date)
                    .amount(new BigDecimal(one.getMoney()))
                    .payee_account(one.getZhifubao())
                    .payee_real_name(one.getZhifubaoName())
                    .payee_type(AliPayConstants.PAY_TYPE)
                    .remark(commonRepository.findOne(12).getValue() + "佣金结算")
                    .build();
            String json = JSON.toJSONString(aliPayWithdrawModel);
            //实例化连接对象
            AlipayFundTransToaccountTransferRequest withdrawRequest = new AlipayFundTransToaccountTransferRequest();
            withdrawRequest.setBizContent(json);
            try {
                AlipayFundTransToaccountTransferResponse response = alipayClient.execute(withdrawRequest);
                if (AliPayConstants.SUCCESS_CODE.equalsIgnoreCase(response.getCode())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    one.setState(1);
                    one.setOrderNumber(date);
                    one.setOutAt(sdf.format(new Date()));
                    CashOut save = cashJpaRepository.save(one);
                    UserEntity userInfo = userJpaRepository.getOne(Long.valueOf(save.getUserId()));
                    if (userInfo != null && userInfo.getOpenId() != null) {
                        //提现通知消息
                        cashOutSuccess(userInfo.getOpenId(), save.getOutAt(), save.getMoney(), save.getZhifubao(), commonRepository.findOne(19).getValue());
                    }
                    if(StringUtils.isNotEmpty(userInfo.getOpenId())){
                        CommonInfo msgs2 = commonRepository.findOne(512);
                        List<String> msgLists2=new ArrayList<>();
                        msgLists2.add(save.getMoney());
                        msgLists2.add(save.getOutAt());
                        msgLists2.add("具体到账时间请已支付宝为主，注意查收");
                        AccountTokenServiceImpl.sendMsg(userInfo.getOpenId(),msgs2.getValue(),msgLists2,3);
                    }
                    return ResultUtil.success(one1.getZhifubaoName() + "转账成功");
                } else {
                    return ResultUtil.error(9999, one1.getZhifubaoName() + "转账失败！" + response.getSubMsg());
                }
            } catch (AlipayApiException e) {
                logger.error("零钱提现异常原因:" + e.getMessage());
                e.printStackTrace();
                return ResultUtil.error(9999, one1.getZhifubaoName() + "转账失败！" + e.getMessage());

            }
        } else {
            //人工转账后改变状态的
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            one.setState(1);
            one.setOrderNumber(date);
            one.setOutAt(sdf.format(now));
            CashOut save = cashJpaRepository.save(one);
            String userId = save.getUserId();
            UserEntity userInfo = userJpaRepository.getOne(Long.valueOf(userId));
            if (userInfo != null && userInfo.getOpenId() != null) {
                //推送提现通知消息
                cashOutSuccess(userInfo.getOpenId(), save.getOutAt(), save.getMoney(), save.getZhifubao(), commonRepository.findOne(19).getValue());
            }
            if(StringUtils.isNotEmpty(userInfo.getOpenId())){
                CommonInfo msgs2 = commonRepository.findOne(512);
                List<String> msgLists2=new ArrayList<>();
                msgLists2.add(save.getMoney());
                msgLists2.add(save.getOutAt());
                msgLists2.add("具体到账时间请已支付宝为主，注意查收");
                AccountTokenServiceImpl.sendMsg(userInfo.getOpenId(),msgs2.getValue(),msgLists2,3);
            }
            return ResultUtil.success(one1.getZhifubaoName() + "转账成功");
        }
    }

    @RequestMapping(value = "/v1/refund/{cashId}/{content}", method = RequestMethod.POST)
    @ApiOperation("管理平台退款")
    @ResponseBody
    public Result refund(@PathVariable("cashId") Long cashId,@PathVariable("content") String content) {
        CashOut one = cashJpaRepository.findById(cashId).orElse(null);
        if (one == null) {
            return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
        }
        if(one.getState()!=0){
            return ResultUtil.error(-100,"已经转账或退款!");
        }
        String date = String.valueOf(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        one.setState(-1);
        one.setRefund(content);
        one.setOrderNumber(date);
        one.setOutAt(sdf.format(now));
        CashOut save = cashJpaRepository.save(one);
        String userId = save.getUserId();
        UserEntity userInfo = userJpaRepository.getOne(Long.valueOf(userId));
        if(userInfo!=null){
            //将金额退还
            userRepository.updateJiFenAdd(save.getMoney(),Long.parseLong(save.getUserId()));
            iMoneyDetailsService.saveBody(userInfo.getUserId(), 1, 4, sdf.format(now) + "退款提醒", content, Double.parseDouble(save.getMoney()));
            if (userInfo.getOpenId() != null) {
                //推送提现通知消息
                refundSuccess(userInfo.getOpenId(), save.getOutAt(), save.getMoney(),  commonRepository.findOne(19).getValue(),content);
            }
            if(StringUtils.isNotEmpty(userInfo.getOpenId())){
                CommonInfo msgs2 = commonRepository.findOne(513);
                List<String> msgLists2=new ArrayList<>();
                msgLists2.add(save.getMoney());
                msgLists2.add(save.getCreateAt());
                if(content.length()>15){
                    content=content.substring(0,15)+"...";
                }
                msgLists2.add(content);
                AccountTokenServiceImpl.sendMsg(userInfo.getOpenId(),msgs2.getValue(),msgLists2,4);
            }
        }
        return ResultUtil.success();
    }


    /**
     * 瞄有券第三方转账
     *
     * @param alipay   支付宝账号
     * @param realname 真实姓名
     * @param money    支付金额
     * @param beizhu   备注
     */
    private void sendMoney(String alipay, String realname, String money, String beizhu) {
        CommonInfo three = commonRepository.findOne(30);
        String apkey = Config.MIAOYOUQUN_Key;
        if (three != null) {
            apkey = three.getValue();
        }
        long l = System.currentTimeMillis();
        String url = Config.MIAOYOUQUN_URL + "/platform/paymentToAlipay?apkey=" + apkey + "&thirdorder=" + l + "&beizhu=" + beizhu + "&realname=" + realname + "&money=" + money + "&alipay=" + alipay;
        String s = HttpClient.doGet(url);
    }

    /**
     * 查询某个用户的提现记录
     *
     * @param relationId
     * @return
     */
    @RequestMapping(value = "/list/{relationId}", method = RequestMethod.GET)
    @ApiOperation("用户端查询某个用户的提现记录")
    @ResponseBody
    public Result findOne(@PathVariable String relationId) {
        return iCashService.findOne(relationId);
    }

    /**
     * 前端获取已经提现了多少钱
     *
     * @param relationId
     * @return
     */
    @RequestMapping(value = "/countByRelationId/{relationId}", method = RequestMethod.GET)
    @ApiOperation("用户端获取已经提现了多少钱")
    @ResponseBody
    public Result countByRelationId(@PathVariable String relationId) {
        return ResultUtil.success(cashRepository.countByRelationId(relationId));
    }

    /**
     * 前端获取用户可以提现的金额
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/money/{userId}", method = RequestMethod.GET)
    @ApiOperation("用户端获取用户可以提现的金额")
    @ResponseBody
    public Result cashOutMoney(@PathVariable Long userId) {
        UserEntity one = userJpaRepository.findById(userId).orElse(null);
        if (one != null) {
            return ResultUtil.success(one.getBalance());
        }
        return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
    }

    /**
     * 用户的信息查询
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userinfo/{userId}", method = RequestMethod.GET)
    @ApiOperation("用户端用户的信息查询")
    @ResponseBody
    public Result cashUserInfo(@PathVariable Long userId) {
        UserEntity one = userJpaRepository.findById(userId).orElse(null);
        if (one != null) {
            return ResultUtil.success(one);
        }
        return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
    }

    /**
     * 前端修改支付宝账号
     *
     * @param userId
     * @param zhifubao
     * @param zhifubaoName
     * @return
     */
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    @ApiOperation("用户端修改支付宝账号")
    @ResponseBody
    public Result updateZhiFuBao(@RequestParam Long userId, @RequestParam String zhifubao, @RequestParam String zhifubaoName) {
        UserEntity one = userJpaRepository.findById(userId).orElse(null);
        if (one != null) {
//            one.setZhifubao(zhifubao);
//            one.setZhifubaoName(zhifubaoName);
            UserEntity userByPhone = userRepository.getUserByPhone(zhifubao);
            if (userByPhone == null) {
                if (one.getPhone() == null) {
                    one.setPhone(zhifubao);
                }
            }
            UserEntity save = userJpaRepository.save(one);
            return ResultUtil.success(save);
        }
        return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
    }

    /**
     * 用户发起提现
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/out/{userId}/{money}", method = RequestMethod.GET)
    @ApiOperation("用户端用户发起提现")
    @ResponseBody
    public Result cashOut(@PathVariable Long userId, @PathVariable BigDecimal money) {
        UserEntity one = userJpaRepository.findById(userId).orElse(null);
        CommonInfo url = commonRepository.findOne(19);
        if (one != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            if (one.getBalance() != null) {
                //计算零钱
                BigDecimal userMoney = new BigDecimal(String.valueOf(one.getBalance())); //用户零钱
                BigDecimal userJiFen = userMoney.subtract(money); //用户剩余零钱
                if (userJiFen.doubleValue() < 0){
                    return ResultUtil.error(-1,"零钱不足");
                }
                CashOut cashOut = new CashOut();
                cashOut.setOut(false);
//                cashOut.setZhifubao(one.getZhifubao());
//                cashOut.setZhifubaoName(one.getZhifubaoName());
//                cashOut.setRelationId(one.getRelationId());
                cashOut.setMoney(money.toString());
                cashOut.setCreateAt(sdf.format(now));
                cashOut.setUserId(String.valueOf(one.getUserId()));
                cashOut.setState(0);
                CashOut save = cashJpaRepository.save(cashOut);
                one.setBalance(userJiFen);
                //推送提现发起消息
                if (one.getOpenId() != null) {
                    cashOut(one.getOpenId(), one.getUserName(), save.getCreateAt(), save.getMoney(), save.getZhifubao(), url.getValue());
                }
                userJpaRepository.save(one);
                iMoneyDetailsService.saveBody(one.getUserId(),2,4,"用户提现","发起提现，提现金额：-"+save.getMoney(),Double.parseDouble(save.getMoney()));
                return ResultUtil.success(save);
            }
        }
        return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
    }


    /**
     * 申请提现成功通知
     *
     * @param openId
     * @param name
     * @param date
     * @param money
     * @param payWay
     * @param url
     */
    private void cashOut(String openId, String name, String date, String money, String payWay, String url) {
        UserEntity userByWxId = userRepository.getUserByWxId(openId);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(5));
        messageInfo.setContent("您好，您的提现申请成功，预计三个工作日到账！提现金额" + money + " 元！支付宝收款账号 " + payWay);
        messageInfo.setTitle("提现通知");
        messageInfo.setUserName(userByWxId.getUserName());
        messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
        iMessageService.saveBody(messageInfo);

        //提现通知管理员openid
        CommonInfo one = commonRepository.findOne(18);
        //后台管理平台域名配置
        CommonInfo two = commonRepository.findOne(20);

        CommonInfo three = commonRepository.findOne(39);
        String apkey = Config.CashNotification;
        if (three != null) {
            apkey = three.getValue();
        }
        if (userByWxId.getClientid() != null) {
            userService.pushToSingle("提现通知", "您好，您的提现申请成功，预计三个工作日到账！提现金额" + money + " 元！支付宝收款账号 " + payWay, userByWxId.getClientid());
        }
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        data.put("amount6", new TemplateMessageItem(money, "#d71345"));
        data.put("thing4", new TemplateMessageItem("发起提现", "#d71345"));
        data.put("time8", new TemplateMessageItem(date, "#d71345"));
        accountService.sendWxMessage(apkey, data, openId, url);

        /*LinkedHashMap<String, TemplateMessageItem> data1 = new LinkedHashMap<>();
        data1.put("first", new TemplateMessageItem("收到一个提现申请！ 请尽快处理！", "#d71345"));
        data1.put("keyword1", new TemplateMessageItem(name, "#d71345"));
        data1.put("keyword2", new TemplateMessageItem(date, "#d71345"));
        data1.put("keyword3", new TemplateMessageItem(money + " 元", "#d71345"));
        data1.put("keyword4", new TemplateMessageItem("支付宝收款账号 " + payWay, "#d71345"));
        data1.put("remark", new TemplateMessageItem("感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", null));
        accountService.sendWxMessage(apkey, data1, one.getValue() != null ? one.getValue() : Config.CashOpenId, two.getValue() != null ? two.getValue() : Config.CashAdmin);
*/
    }

    /**
     * 退款成功通知
     *
     * @param openId
     * @param date
     * @param money
     * @param url
     */
    private void refundSuccess(String openId, String date, String money, String url,String content) {
        UserEntity userByWxId = userRepository.getUserByWxId(openId);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(5));
        messageInfo.setContent(content);
        messageInfo.setTitle("提现失败提醒");
        messageInfo.setUserName(userByWxId.getUserName());
        messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
        iMessageService.saveBody(messageInfo);
        if (userByWxId.getClientid() != null) {
            userService.pushToSingle("提现失败提醒", content, userByWxId.getClientid());
        }
        CommonInfo three = commonRepository.findOne(39);
        String apkey = Config.TiXianSuccessNotification;
        if (three != null) {
            apkey = three.getValue();
        }
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        data.put("amount6", new TemplateMessageItem(money, "#d71345"));
        data.put("thing4", new TemplateMessageItem("提现失败", "#d71345"));
        data.put("time8", new TemplateMessageItem(date, "#d71345"));
        accountService.sendWxMessage(apkey, data, openId, url);
    }


    /**
     * 提现成功通知
     *
     * @param openId
     * @param date
     * @param money
     * @param payWay
     * @param url
     */
    private void cashOutSuccess(String openId, String date, String money, String payWay, String url) {
        UserEntity userByWxId = userRepository.getUserByWxId(openId);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(5));
        messageInfo.setContent("您好，您的提现转账成功，请注意查收！提现金额【" + money + "元】！支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服");
        messageInfo.setTitle("提现成功通知");
        messageInfo.setUserName(userByWxId.getUserName());
        messageInfo.setUserId(String.valueOf(userByWxId.getOpenId()));
        iMessageService.saveBody(messageInfo);
        if (userByWxId.getClientid() != null) {
            userService.pushToSingle("提现成功通知", "您好，您的提现转账成功，请注意查收！提现金额【" + money + "元】！支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", userByWxId.getClientid());
        }
        CommonInfo three = commonRepository.findOne(39);
        String apkey = Config.TiXianSuccessNotification;
        if (three != null) {
            apkey = three.getValue();
        }
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        data.put("amount6", new TemplateMessageItem(money, "#d71345"));
        data.put("thing4", new TemplateMessageItem("提现成功", "#d71345"));
        data.put("time8", new TemplateMessageItem(date, "#d71345"));
        accountService.sendWxMessage(apkey, data, openId, url);
    }
}
