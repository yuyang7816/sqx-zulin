package com.sqx.app.controller.wx;

import com.alibaba.fastjson.JSON;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.app.UserMoney;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.entity.wx.WordInfo;
import com.sqx.domain.repository.mapper.wx.WordJpaRepository;
import com.sqx.domain.repository.mapper.wx.WordRepository;
import com.sqx.domain.repository.service.app.UserMoneyService;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.wx.AccountService;
import com.sqx.domain.repository.service.wx.AccountTokenService;
import com.sqx.domain.repository.utils.InvitationCodeUtil;
import com.sqx.domain.repository.utils.MessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.user.User;
import weixin.popular.bean.xmlmessage.XMLMessage;
import weixin.popular.bean.xmlmessage.XMLTextMessage;
import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


@Slf4j
@RestController
@Api(value = "微信账号管理", tags = {"微信账号管理"})
@RequestMapping("/app/wx")
public class AppWxController {

    @Autowired
    private WordJpaRepository wordJpaRepository;
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AccountTokenService accountTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private WordRepository wordRepository;


    //重复通知过滤
    private static ExpireKey expireKey = new DefaultExpireKey();


    @GetMapping("/createQr")
    @ApiOperation("用户端创建公众号免单商品二维码")
    public Result createQr(@RequestParam String relation) {
        return accountService.createQr(relation);
    }


    @RequestMapping(value = "/sqx/sendMsg", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("回复公众号消息")
    @ResponseBody
    public Result sendMsg(String content, Long id, HttpServletResponse response) throws IOException {
        MessageInfo messageInfo = messageService.getById(id);
        ServletOutputStream outputStream = response.getOutputStream();
        WXBizMsgCrypt wxBizMsgCrypt = (WXBizMsgCrypt) JSON.parse(messageInfo.getWxBizMsgCrypt());
        //创建回复
        XMLMessage xmlTextMessage = new XMLTextMessage(
                messageInfo.getFromUserName(),
                messageInfo.getToUserName(),
                content);
        //回复
        xmlTextMessage.outputStreamWrite(outputStream, wxBizMsgCrypt);
        return ResultUtil.success();
    }


    /**
     * 微信公众号消息管理
     *
     * @return openid
     */
    @RequestMapping(value = "/sqx/connect", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("在公众号平台配置 微信公众号消息")
    @ResponseBody
    public void connectWeixinsqx(HttpServletRequest request, HttpServletResponse response) {
        try {
            //公众号Token
            CommonInfo token = commonInfoService.findOne(16);
            //公众号EncodingAESKey
            CommonInfo EncodingAESKey = commonInfoService.findOne(17);
            //微信APPID
            CommonInfo appid = commonInfoService.findOne(5);

            ServletInputStream inputStream = request.getInputStream();
            ServletOutputStream outputStream = response.getOutputStream();
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            //加密模式
            String encrypt_type = request.getParameter("encrypt_type");
            String msg_signature = request.getParameter("msg_signature");
            WXBizMsgCrypt wxBizMsgCrypt = null;
            //加密方式
            boolean isAes = "aes".equals(encrypt_type);
            if (isAes) {
                try {
                    wxBizMsgCrypt = new WXBizMsgCrypt(accountTokenService.getWxToken(),  EncodingAESKey.getValue(),appid.getValue());
                } catch (AesException e) {
                    e.printStackTrace();
                }
            }

            //首次请求申请验证,返回echostr
            if (isAes && echostr != null) {
                try {
                    echostr = URLDecoder.decode(echostr, "utf-8");
                    assert wxBizMsgCrypt != null;
                    String echostr_decrypt = wxBizMsgCrypt.verifyUrl(msg_signature, timestamp, nonce, echostr);
                    outputStreamWrite(outputStream, echostr_decrypt);
                    return;
                } catch (AesException e) {
                    e.printStackTrace();
                }
            } else if (echostr != null) {
                outputStreamWrite(outputStream, echostr);
                return;
            }

            EventMessage eventMessage = null;
            if (isAes) {
                try {
                    //获取XML数据（含加密参数）
                    String postData = StreamUtils.copyToString(inputStream, Charset.forName("utf-8"));
                    //解密XML 数据
                    assert wxBizMsgCrypt != null;
                    String xmlData = wxBizMsgCrypt.decryptMsg(msg_signature, timestamp, nonce, postData);
                    //XML 转换为bean 对象
                    eventMessage = XMLConverUtil.convertToObject(EventMessage.class, xmlData);
                } catch (AesException e) {
                    e.printStackTrace();
                }
            } else {
                if (signature == null) {
                    System.out.println("The request signature is null");
                    return;
                }
                //验证请求签名
                if (!signature.equals(SignatureUtil.generateEventMessageSignature(token.getValue(), timestamp, nonce))) {
                    System.out.println("The request signature is invalid");
                    return;
                }

                if (inputStream != null) {
                    //XML 转换为bean 对象

                    eventMessage = XMLConverUtil.convertToObject(EventMessage.class, inputStream);
                }
            }

            String fromUserName = eventMessage.getFromUserName();
            String key = fromUserName + "__"
                    + eventMessage.getToUserName() + "__"
                    + eventMessage.getMsgId() + "__"
                    + eventMessage.getCreateTime();
            if (expireKey.exists(key)) {
                //重复通知不作处理
                return;
            } else {
                expireKey.add(key);
            }

            String event = eventMessage.getEvent();
            String msgType1 = eventMessage.getMsgType();
            String eventKey = eventMessage.getEventKey();
            log.error("getEvent----" + event);
            log.error("getMsgType----" + msgType1);
            log.error("eventKey----" + eventKey);
            //判断请求是否事件类型 event 用户关注公众号事件
            if (MessageUtil.EVENT_SUB.equals(event)) {
                if (eventKey != null && eventKey.contains("qrscene_")) {
                    String relation = eventKey.split("_")[1];
                    log.error("relation----" + relation);
                    log.error("fromUserName----" + fromUserName);
                    //下面处理用户关注成功自动注册用户
                    UserEntity userEntity = userService.queryByWxOpenId(fromUserName);
                    log.error("获取到的用户："+userEntity);
                    if (userEntity == null) {
                        log.error("userByWxId is null----" + relation);
//                        User user = SnsAPI.userinfo(accountTokenService.getWxToken(), fromUserName, "zh_CN");
                        UserEntity userInfo = new UserEntity();
                        //添加默认头像
                        userInfo.setUserName("公众号用户");
                        userInfo.setPlatform("公众号");
                        userInfo.setWxOpenId(fromUserName);
                        userInfo.setStatus(1);
//
//                        userInfo.setUserRate(new BigDecimal(commonInfoService.findOne(295).getValue()));
//                        userInfo.setShopRate(new BigDecimal(commonInfoService.findOne(296).getValue()));
                        if(StringUtils.isNotEmpty(relation)){
                            userInfo.setInviterCode(relation);
                        }else{
                            userInfo.setInviterCode(commonInfoService.findOne(88).getValue());
                        }
                        userInfo.setCreateTime(LocalDateTime.now());
                        userInfo.setStatus(1);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        userInfo.setUpdateTime(LocalDateTime.now());
                        userService.save(userInfo);

                        userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
                        userService.updateById(userInfo);

                        //给用户创建钱包
                        UserMoney userMoney = new UserMoney();
                        userMoney.setUserId(userInfo.getUserId());
                        userMoney.setMoney(BigDecimal.valueOf(0));
//                        userMoney.setCashDeposit(BigDecimal.valueOf(0));
                        userMoneyService.save(userMoney);
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setContent("恭喜您，账号注册成功！");
                        messageInfo.setTitle("系统通知");
                        messageInfo.setState(String.valueOf(5));
                        messageInfo.setUserName(userInfo.getUserName());
                        messageInfo.setUserId(String.valueOf(userInfo.getUserId()));
                        messageInfo.setCreateAt(sdf.format(new Date()));
                        messageInfo.setIsSee("0");
                        messageService.saveBody(messageInfo);
                    }else{
                        log.error("重复关注："+userEntity.getUserId());
                    }
                    String value = commonInfoService.findOne(172).getValue();
                    //创建回复
                    XMLMessage xmlTextMessage = new XMLTextMessage(
                            fromUserName,
                            eventMessage.getToUserName(),
                            value);
                    //回复
                    xmlTextMessage.outputStreamWrite(outputStream, wxBizMsgCrypt);
                } else {
                    String value = commonInfoService.findOne(172).getValue();
                    //创建回复
                    XMLMessage xmlTextMessage = new XMLTextMessage(
                            fromUserName,
                            eventMessage.getToUserName(),
                            value);
                    //回复
                    xmlTextMessage.outputStreamWrite(outputStream, wxBizMsgCrypt);
                }
            }else if(MessageUtil.EVENT_SCAN.equals(event)){
                String relation = eventKey;
                UserEntity userEntity = userService.queryByWxOpenId(fromUserName);
                log.error("获取到的用户："+userEntity);
                if (userEntity == null) {
                    UserEntity userInfo = new UserEntity();
                    //添加默认头像
                    userInfo.setUserName("公众号用户");
                    userInfo.setPlatform("公众号");
                    userInfo.setWxOpenId(fromUserName);
                    userInfo.setStatus(1);

//                    userInfo.setUserRate(new BigDecimal(commonInfoService.findOne(295).getValue()));
//                    userInfo.setShopRate(new BigDecimal(commonInfoService.findOne(296).getValue()));
                    if(StringUtils.isNotEmpty(relation)){
                        userInfo.setInviterCode(relation);
                    }else{
                        userInfo.setInviterCode(commonInfoService.findOne(88).getValue());
                    }
                    userInfo.setCreateTime(LocalDateTime.now());
                    userInfo.setStatus(1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    userInfo.setUpdateTime(LocalDateTime.now());
                    userService.save(userInfo);

                    userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
                    userService.updateById(userInfo);

                    //给用户创建钱包
                    UserMoney userMoney = new UserMoney();
                    userMoney.setUserId(userInfo.getUserId());
                    userMoney.setMoney(BigDecimal.valueOf(0));
//                    userMoney.setCashDeposit(BigDecimal.valueOf(0));
                    userMoneyService.save(userMoney);
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setContent("恭喜您，账号注册成功！");
                    messageInfo.setTitle("系统通知");
                    messageInfo.setState(String.valueOf(5));
                    messageInfo.setUserName(userInfo.getUserName());
                    messageInfo.setUserId(String.valueOf(userInfo.getUserId()));
                    messageInfo.setCreateAt(sdf.format(new Date()));
                    messageInfo.setIsSee("0");
                    messageService.saveBody(messageInfo);
                }else{
                    log.error("重复关注："+userEntity.getUserId());
                }
            }else {
                if (StringUtils.isNotBlank(eventMessage.getContent())) {
                    String content = eventMessage.getContent().replaceAll("\\s*", "");
                    String contents = eventMessage.getContent();
                    log.error("receiver content is ----" + content);
                    log.error("receiver content is ----" + contents);
                    log.error("getToUserName" + eventMessage.getToUserName());
                    log.error("getFromUserName" + fromUserName);
                    //关键字回复
                    WordInfo one1 = wordRepository.findOne(content);
                    if (one1 != null) {
                        //创建回复
                        XMLMessage xmlTextMessage = new XMLTextMessage(
                                fromUserName,
                                eventMessage.getToUserName(),
                                one1.getValue());
                        //回复
                        xmlTextMessage.outputStreamWrite(outputStream, wxBizMsgCrypt);
                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error("关注公众号出错："+e.getMessage(),e);
        }

    }


    /**
     * 数据流输出
     */
    private void outputStreamWrite(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes("utf-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 获取openid
     * 获取用户信息
     *
     * @param openId openId
     * @return openid
     */
    @GetMapping("/userInfo/{openId:.+}")
    @ApiOperation("用户端通过openid获取微信用户信息")
    public User getUserInfo(@PathVariable("openId") String openId) {
        return accountService.getUserInfo(openId);
    }

    /**
     * 获取签名
     * 监测js签名
     *
     * @param url request的url
     * @return String
     */
    @GetMapping("/js/sign")
    @ApiOperation("用户端获取js签名")
    public String getJSSign(@RequestParam String url) {
        return accountService.getJSSign(url);
    }




}
