package com.sqx.domain.repository.service.wx;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.Config;
import com.sqx.common.core.utils.HttpClient;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import com.sqx.domain.repository.mapper.userinfo.UserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.*;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.MessageSendResult;
import weixin.popular.bean.message.massmessage.MassTextMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.user.User;
import weixin.popular.util.JsUtil;
import weixin.popular.util.JsonUtil;

import javax.websocket.SendResult;
import java.util.*;

/**
 * @author maxd
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private CommonRepository commonRepository;
    @Autowired
    private AccountTokenService accountTokenService;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public String getAccountTokenWeixin(String openId) {
        return accountTokenService.getWxToken();
    }

    @Override
    public User getUserInfo(String openId) {
        return UserAPI.userInfo(accountTokenService.getWxToken(), openId);
    }


    @Override
    public Result getOpenId(String code, Long userId) {
        try {
            //微信appid
            CommonInfo one = commonRepository.findOne(5);
            //微信秘钥
            CommonInfo two = commonRepository.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            if (StringUtils.isNotEmpty(openid) && userId != 0){
                UserEntity u = userJpaRepository.getOne(userId);
                u.setOpenId(openid);
                userJpaRepository.save(u);
            }
            Result result = new Result();
            result.setData(openid);
            result.setMsg(snsToken.getUnionid());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }
    }

    @Override
    public Result sendMsg(String openid) {
        MassTextMessage textMessage2 = new MassTextMessage("openId 列表群发文本消息");
        HashSet<String> strings = new HashSet<>();
        strings.add(openid);
        textMessage2.setTouser(strings);
        MessageSendResult messageSendResult = MessageAPI.messageMassSend(accountTokenService.getWxToken(), textMessage2);
        return ResultUtil.success(messageSendResult);
    }

    @Override
    public Result sendCustomerMsg(String json, String openid) {
        BaseResult baseResult = MessageAPI.messageCustomSend(accountTokenService.getWxToken(), json);
        return ResultUtil.success(baseResult);
    }

    @Override
    public Result createMenu(String json) {
        BaseResult baseResult = MenuAPI.menuCreate(accountTokenService.getWxToken(), json);
        return ResultUtil.success(baseResult);
    }

    @Override
    public Result getMenu() {
        BaseResult baseResult = MenuAPI.menuGet(accountTokenService.getWxToken());
        return ResultUtil.success(baseResult);
    }

    @Override
    public Result createQr(String relation) {
        return ResultUtil.success(QrcodeAPI.qrcodeCreateFinal(accountTokenService.getWxToken(), relation));
    }

    @Override
    public Result mpCreateQr(String value, String width) {
        String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode";
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("access_token", accountTokenService.getMpToken());
        stringMap.put("path", value);
        stringMap.put("width", width);
        String s = JsonUtil.toJSONString(stringMap);
        return ResultUtil.success(HttpClient.doPost(url, s));
    }

    @Override
    public Result showQrcode() {
        String jsApiTicket = accountTokenService.getJsApiTicket(accountTokenService.getWxToken());
        return ResultUtil.success(QrcodeAPI.showqrcode(jsApiTicket));
    }

    @Override
    public Result jscode2session(String code) {
        //微信小程序appid
        CommonInfo three = commonRepository.findOne(45);
        //微信小程序秘钥
        CommonInfo four = commonRepository.findOne(46);
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+three.getValue()+"&secret="+four.getValue()+"&js_code="+code+"&grant_type=authorization_code";
        return ResultUtil.success(HttpClient.doGet(url));
    }

    @Override
    public boolean checkIsFollow(String openid) {
        try {
            return 1 == UserAPI.userInfo(accountTokenService.getWxToken(), openid).getSubscribe();
        } catch (Exception e) {
            throw new RuntimeException("INVALID_OPENID");
        }
    }

    @Override
    public void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid, String url) {
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(openid);
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(data);
        templateMessage.setUrl(url);
        TemplateMessageResult templateMessageResult = MessageAPI.messageTemplateSend(accountTokenService.getWxToken(), templateMessage);
        if (templateMessageResult.isSuccess()) {
            new SendResult();
        } else {
            new SendResult();
        }
    }

    @Override
    public String getJSSign(String url) {
        CommonInfo one = commonRepository.findOne(5);
        String nonceStr = UUID.randomUUID().toString();
        String jsApiTicket = accountTokenService.getJsApiTicket(accountTokenService.getWxToken());
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String signature = JsUtil.generateConfigSignature(nonceStr, jsApiTicket, timestamp, url);
        Map<String, String> map = new HashMap<>(16);
        map.put("signature", signature);
        map.put("noncestr", nonceStr);
        map.put("appid", one != null ? one.getValue() : Config.appId);
        map.put("timestamp", timestamp);
        return JSONObject.toJSONString(map);
    }
}
