package com.sqx.domain.repository.service.wx;

import com.sqx.common.core.domain.Result;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.user.User;

import java.util.LinkedHashMap;

/**
 * @author zhangd
 */
public interface AccountService {


    /**
     * 根据微信获取凭证
     *
     * @param openId 微信
     * @return BingingResultView
     */
    String getAccountTokenWeixin(String openId);

    /**
     * 根据微信获取凭证
     *
     * @param openId 微信
     * @return BingingResultView
     */
    User getUserInfo(String openId);

    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    Result getOpenId(String code, Long userId);

    /**
     * 获取openid
     *
     * @param
     * @return openid
     */
    Result sendMsg(String openid);


    /**
     * 获取openid
     *
     * @param
     * @return openid
     */
    Result sendCustomerMsg(String json, String openid);

    /**
     * 获取openid
     *
     * @param
     * @return openid
     */
    Result createMenu(String json);

    Result getMenu();

    Result createQr(String relation);

    Result mpCreateQr(String value, String width);

    Result showQrcode();

    Result jscode2session(String code);

    /**
     * 检查是否关注
     *
     * @param openid openid
     * @return boolean
     */
    boolean checkIsFollow(String openid);

    /**
     * 获取分享json
     *
     * @param url 分享的url
     * @return String
     */
    String getJSSign(String url);

    void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid, String url);
}
