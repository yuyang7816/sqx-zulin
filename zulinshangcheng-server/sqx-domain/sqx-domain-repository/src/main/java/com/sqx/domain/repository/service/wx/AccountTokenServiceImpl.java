package com.sqx.domain.repository.service.wx;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.core.utils.Config;
import com.sqx.common.core.utils.HttpClientUtil;
import com.sqx.common.core.utils.TemplateParam;
import com.sqx.common.core.utils.WxMssVo;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import weixin.popular.api.TicketAPI;
import weixin.popular.support.TokenManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangd
 */
@Slf4j
@Service
public class AccountTokenServiceImpl implements AccountTokenService {

    private static CommonRepository commonRepository;

    @Autowired
    public void setWxChatContentService(CommonRepository commonRepository) {
        AccountTokenServiceImpl.commonRepository = commonRepository;
    }


    @Override
    public String getWxToken() {
        try {
            //微信appid
            CommonInfo one = commonRepository.findOne(5);
            String token = TokenManager.getToken(one != null ? one.getValue() : Config.appId);
            return token;
        } catch (Exception e) {
            throw new RuntimeException("GET_ACCESS_TOKEN_FAIL");
        }
    }

    @Override
    public String getMpToken() {
        try {
            //微信appid
            CommonInfo one = commonRepository.findOne(45);
            return TokenManager.getToken(one != null ? one.getValue() : Config.appId);
        } catch (Exception e) {
            throw new RuntimeException("GET_ACCESS_TOKEN_FAIL");
        }
    }

    @Override
    public String getJsApiTicket(String accessToken) {
        return TicketAPI.ticketGetticket(accessToken).getTicket();
    }


    public static void sendMsg(String wxId, String templateId, List<String> msgList, Integer type){
        String mpAccessToken = getMpAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" +mpAccessToken;
        //拼接推送的模版
        RestTemplate restTemplate = new RestTemplate();
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(wxId);//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTemplate_id(templateId);//订阅消息模板id
        //wxMssVo.setPage("pages/index/index");
        wxMssVo.setData(getParam(type,msgList));
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        String body = responseEntity.getBody();
    }

    public static Map<String, TemplateParam> getParam(Integer type, List<String> msgList){
        if(type==1){
            Map<String, TemplateParam> paras = new HashMap<>();
            paras.put("character_string2",new TemplateParam( msgList.get(0)));
            paras.put("thing9",new TemplateParam( msgList.get(1)));
            paras.put("amount1",new TemplateParam( msgList.get(2)));
            return paras;
        }else if(type==2){
            Map<String, TemplateParam> paras = new HashMap<>();
            paras.put("character_string3",new TemplateParam( msgList.get(0)));
            paras.put("thing2",new TemplateParam( msgList.get(1)));
            paras.put("thing1",new TemplateParam( msgList.get(2)));
            paras.put("character_string5",new TemplateParam( msgList.get(2)));
            paras.put("thing9",new TemplateParam( msgList.get(2)));
            return paras;
        }else if(type==3){
            Map<String, TemplateParam> paras = new HashMap<>();
            paras.put("amount1",new TemplateParam( msgList.get(0)));
            paras.put("time8",new TemplateParam( msgList.get(1)));
            paras.put("thing10",new TemplateParam( msgList.get(2)));
            return paras;
        }else if(type==4){
            Map<String, TemplateParam> paras = new HashMap<>();
            paras.put("amount1",new TemplateParam( msgList.get(0)));
            paras.put("time2",new TemplateParam( msgList.get(1)));
            paras.put("thing3",new TemplateParam( msgList.get(2)));
            return paras;
        }
        return null;
    }

    public static  String getMpAccessToken(){
        String appid = commonRepository.findOne(45).getValue();
        String secret = commonRepository.findOne(46).getValue();
        String jsonResult = HttpClientUtil.doPost("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret);
        JSONObject parseObject = JSON.parseObject(jsonResult);
        log.info("=========accessTokenOut========="+parseObject.toJSONString());

        String errcode = parseObject.getString("errcode");
        String accessToken = parseObject.getString("access_token");
        String expiresIn = parseObject.getString("expires_in");
        return accessToken;
    }


}
