package com.sqx.domain.repository.service.wx;

/**
 * @author zhangd
 */
public interface AccountTokenService {

    /**
     * 获取微信token
     *
     * @return wxtoken
     */
    String getWxToken();

    /**
     * 获取小程序token
     *
     * @return wxtoken
     */
    String getMpToken();


    /**
     * 获取微信jsapi_ticket
     *
     * @param accessToken access_token
     * @return JsApiTicket
     */
    String getJsApiTicket(String accessToken);


}
