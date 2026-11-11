package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.sqx.common.core.utils.Config;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import com.sqx.domain.repository.mapper.userinfo.UserRepository;
import com.sqx.domain.repository.service.sys.IUserService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.wx.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import java.util.LinkedHashMap;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/24 18:27
 */
@Service("authUserServiceImpl")
@Slf4j
public class IUserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService iMessageService;

    @Autowired
    private UserDao userDao;
    @Override
    public void orderCancel(Long userId, String orderNum, String time) {
        UserEntity user = baseMapper.selectById(userId);
        String openId = user.getOpenId()==null?user.getWxOpenId():user.getOpenId();
        if (user != null && user.getOpenId() != null) {
            CommonInfo urls = commonRepository.findOne(19); //域名
            String templateId = commonRepository.findOne(86).getValue(); //消息模板
            LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
            data.put("character_string1", new TemplateMessageItem(orderNum, "#d71345"));
            data.put("thing8", new TemplateMessageItem(user.getUserName(), "#d71345"));
            accountService.sendWxMessage(templateId, data, openId, urls.getValue() + "/pages/zysc/my/myList#/pages/zysc/my/myList");
        }
    }

    @Override
    public void orderExpress(Long userId, String orderNum, String time, String expressName, String expressNumber, String address) {
        UserEntity user = baseMapper.selectById(userId);
        String openId = user.getOpenId()==null?user.getWxOpenId():user.getOpenId();
        if (user != null && user.getOpenId() != null) {
            CommonInfo urls = commonRepository.findOne(19); //域名
            String templateId = commonRepository.findOne(87).getValue(); //消息模板
            LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
            data.put("character_string2", new TemplateMessageItem(orderNum, "#d71345"));
            data.put("thing13", new TemplateMessageItem(expressName, "#d71345"));
            data.put("character_string14", new TemplateMessageItem(expressNumber, "#d71345"));
            data.put("thing19", new TemplateMessageItem(address, "#d71345"));
            accountService.sendWxMessage(templateId, data, openId, urls.getValue() + "/pages/zysc/my/myList#/pages/zysc/my/myList");
        }
        //订单消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState("4");
        messageInfo.setTitle("订单发货成功");
        messageInfo.setContent("订单编号："+orderNum+"，发货时间："+time+"，快递公司："+expressName+"，快递单号:"+expressNumber+"，收货地址："+address);
        messageInfo.setUserName(user.getUserName());
        messageInfo.setUserId(userId.toString());
        iMessageService.saveBody(messageInfo);
        if (user.getClientid() != null) {
            pushToSingle("订单发货成功", "订单编号："+orderNum+"，发货时间："+time+"，快递公司："+expressName+"，快递单号:"+expressNumber+"，收货地址："+address, user.getClientid());
        }
    }

    /**
     * 对单个用户推送消息
     * <p>
     * 场景1：某用户发生了一笔交易，银行及时下发一条推送消息给该用户。
     * <p>
     * 场景2：用户定制了某本书的预订更新，当本书有更新时，需要向该用户及时下发一条更新提醒信息。
     * 这些需要向指定某个用户推送消息的场景，即需要使用对单个用户推送消息的接口。
     */
    private int number = 1;
    @Override
    public void pushToSingle(String title, String content, String clientId) {

        IGtPush push = new IGtPush(commonRepository.findOne(60).getValue(), commonRepository.findOne(62).getValue());

        NotificationTemplate template = new NotificationTemplate();

        // 设置APPID与APPKEY
        template.setAppId(commonRepository.findOne(61).getValue());
        template.setAppkey(commonRepository.findOne(60).getValue());

        //设置展示样式
        Style0 style = new Style0();
        // 设置通知栏标题与内容

        style.setTitle(title);
        style.setText(content);
        // 配置通知栏图标
        style.setLogo("icon.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
        // 配置通知栏网络图标
        style.setLogoUrl(commonRepository.findOne(19).getValue() + "/logo.png");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        style.setChannel(String.valueOf(number));
        style.setChannelName(Config.BASE_NAME);
        style.setChannelLevel(4); //设置通知渠道重要性
        template.setStyle(style);
        template.setTransmissionType(1);  // 透传消息设置，收到消息是否立即启动应用： 1为立即启动，2则广播等待客户端自启动
        template.setTransmissionContent(content);
        template.setNotifyid(number); // 在消息推送的时候设置notifyid。如果需要覆盖此条消息，则下次使用相同的notifyid发一条新的消息。客户端sdk会根据notifyid进行覆盖。
        number = number + 1;
        // 单推消息类型
        SingleMessage message = new SingleMessage();

        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(72 * 3600 * 1000);
        message.setPushNetWorkType(0); // 判断客户端是否wifi环境下推送。1为仅在wifi环境下推送，0为不限制网络环境，默认不限
        // APNs下发策略；1: 个推通道优先，在线经个推通道下发，离线经APNs下发(默认);2: 在离线只经APNs下发;3: 在离线只经个推通道下发;4: 优先经APNs下发，失败后经个推通道下发;
        message.setStrategyJson("{\"ios\":4}");
        Target target = new Target();

        target.setAppId(commonRepository.findOne(61).getValue());
        target.setClientId(clientId);
        IPushResult ret = null;
        try {

            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
            log.info("app消息推送"+ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }

    }


    /**
     * 自营商城订单通知
     * @param userId 用户id
     * @param orderNum 订单号
     * @param payMoney 支付金额
     * @param createTime 下单时间
     */
    @Override
    public void orderPay(Long userId, String orderNum, String payMoney, String createTime) {
        UserEntity user = baseMapper.selectById(userId);
        String openId = user.getOpenId()==null?user.getWxOpenId():user.getOpenId()==null?user.getWxOpenId():user.getOpenId();
        if (user != null && user.getOpenId() != null) {
            CommonInfo urls = commonRepository.findOne(19); //域名
            String templateId = commonRepository.findOne(35).getValue(); //消息模板
            LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
            data.put("thing4", new TemplateMessageItem("收到一个佣金新订单", "#d71345"));
            data.put("thing3", new TemplateMessageItem("获得" + payMoney + "元佣金", "#d71345"));
            accountService.sendWxMessage(templateId, data, openId, urls.getValue() + "/pages/zysc/my/myList#/pages/zysc/my/myList");
        }
        //订单消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState("4");
        messageInfo.setTitle("订单支付成功");
        messageInfo.setContent("订单编号："+orderNum+"，支付金额："+payMoney);
        messageInfo.setUserName(user.getUserName());
        messageInfo.setUserId(userId.toString());
        iMessageService.saveBody(messageInfo);
        if (user.getClientid() != null) {
            pushToSingle("订单支付成功", "订单编号："+orderNum+"，支付金额："+payMoney, user.getClientid());
        }
    }

    @Override
    public void orderRefund(Long userId, String payMoney,String ordersNum) {
        UserEntity user = baseMapper.selectById(userId);
        String openId = user.getOpenId()==null?user.getWxOpenId():user.getOpenId();
        if (user != null && user.getOpenId() != null) {
            CommonInfo urls = commonRepository.findOne(19); //域名
            String templateId = commonRepository.findOne(88).getValue(); //消息模板
            LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
            data.put("character_string5", new TemplateMessageItem(ordersNum, "#d71345"));
            data.put("amount2", new TemplateMessageItem(payMoney, "#d71345"));
            data.put("thing3", new TemplateMessageItem("原路退回", "#d71345"));
            accountService.sendWxMessage(templateId, data, openId, urls.getValue() + "/pages/zysc/my/myList#/pages/zysc/my/myList");
        }
        //订单消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState("4");
        messageInfo.setTitle("订单退款成功");
        messageInfo.setContent("退款金额："+payMoney+"退款方式：原路退回，退款时间：具体到账时间以收到时间为准");
        messageInfo.setUserName(user.getUserName());
        messageInfo.setUserId(userId.toString());
        iMessageService.saveBody(messageInfo);
        if (user.getClientid() != null) {
            pushToSingle("订单退款成功", "退款金额："+payMoney+"退款方式：原路退回，退款时间：具体到账时间以收到时间为准", user.getClientid());
        }
    }
}
