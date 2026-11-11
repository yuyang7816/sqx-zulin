package com.sqx.app.controller.appUser;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.qcloudsms.httpclient.HTTPException;
import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.annotation.SysLog;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.HttpClientUtil;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.service.app.IAppleService;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.utils.UserConstantInterface;
import com.sqx.domain.repository.utils.WxPhone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

import java.util.HashMap;
import java.util.Map;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/Login")
@Api("APP登录接口")
@Slf4j
public class AppLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private IAppleService appleService;
    @Autowired
    private ICommonInfoService commonInfoService;


    @ApiOperation("微信小程序登陆")
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    public Result wxLogin(@ApiParam("微信code码") String code) {
        return userService.wxLogin(code);
    }


    @ApiOperation("小程序登录新增或修改个人信息")
    @RequestMapping(value = "/insertWxUser", method = RequestMethod.POST)
    public Result insertWxUser(@RequestBody UserEntity userInfo) {
        return userService.wxRegister(userInfo);
    }


    @RequestMapping(value = "/appleLogin", method = RequestMethod.GET)
    @ApiOperation("苹果登陆获取appleUserId")
    public Result loginVerify(@ApiParam("苹果token")@RequestParam("identityToken") String identityToken) {
        try {
            log.info("苹果token：{}", identityToken);
            JSONObject jsonObject = JSON.parseObject(identityToken);
            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
            String identityTokens = userInfo.getString("identityToken");
            return appleService.getAppleUserInfo(identityTokens);
        } catch (Exception e) {
            log.error("苹果token校验失败：{}", identityToken, e);
            return Result.failed("苹果账号验证失败，请退出重试！");
        }
    }


    @ApiOperation("苹果登录")
    @RequestMapping(value = "/insertAppleUser", method = RequestMethod.GET)
    public Result insertAppleUser(@ApiParam("苹果id")@RequestParam String appleId) {
        return userService.iosRegister(appleId);
    }

    @RequestMapping(value = "/iosBindMobile", method = RequestMethod.POST)
    @ApiOperation("苹果登录绑定手机号")
    @ResponseBody
    public Result iosBindMobile(@ApiParam(value = "手机号")@RequestParam String phone,@ApiParam(value = "验证码") @RequestParam String code,
                                @ApiParam(value = "苹果id")@RequestParam String appleId,@ApiParam(value = "来源 APP 小程序  公众号")@RequestParam String platform,
                                @ApiParam(value = "手机类型 1安卓 2ios")@RequestParam Integer sysPhone) {
        return userService.iosBindMobile(phone, code, appleId, platform, sysPhone);
    }


    @RequestMapping(value = "/wxAppLogin", method = RequestMethod.POST)
    @ApiOperation("微信APP登录")
    @ResponseBody
    public Result wxAppLogin(@ApiParam(value = "微信openId")@RequestParam String wxOpenId,@ApiParam(value = "微信请求token") @RequestParam String token) {
        return userService.wxAppLogin(wxOpenId, token);
    }


    @RequestMapping(value = "/wxBindMobile", method = RequestMethod.POST)
    @ApiOperation("微信登录绑定手机号")
    @ResponseBody
    public Result wxBindMobile(@ApiParam(value = "手机号")@RequestParam String phone,
                               @ApiParam(value = "验证码") @RequestParam String code,
                               @ApiParam(value = "微信openId")@RequestParam String wxOpenId,
                               @ApiParam(value = "微信请求token")  @RequestParam String token,
                               @ApiParam(value = "来源 APP 小程序  公众号")@RequestParam String platform,
                               @ApiParam(value = "手机类型 1安卓 2ios")@RequestParam Integer sysPhone) {
        return userService.wxBindMobile(phone, code, wxOpenId, token, platform, sysPhone);
    }

    @ApiOperation("用户端openid登录")
    @RequestMapping(value = "/openid/login", method = RequestMethod.GET)
    @ResponseBody
    public Result loginByOpenId(@ApiParam(value = "微信openId")@RequestParam String openId) {
        return userService.loginByOpenId(openId);
    }


//    @SysLog(value = "注册或登录", type = "2")
    @RequestMapping(value = "/registerCode", method = RequestMethod.POST)
    @ApiOperation("app或h5注册或登录")
    @ResponseBody
    public Result registerCode(@ApiParam(value = "手机号")String phone,
                               @ApiParam(value = "验证码")String msg,
                               @ApiParam(value = "来源 APP 小程序  公众号")String platform,
                               @ApiParam(value = "手机类型 1安卓 2ios")Integer sysPhone,
                               @ApiParam(value = "微信openId")String openId,
                               @ApiParam(value = "上级邀请码")String inviterCode,
                               @ApiParam(value = "密码")String password,
                               @ApiParam(value = "用户名")String userName) {
        return userService.registerCode(phone, msg, platform, sysPhone, openId, inviterCode,password,userName);
    }

    @ApiOperation("用户端发送验证码")
    @RequestMapping(value = "/sendMsg/{phone}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendMsg(@ApiParam(value = "手机号")@PathVariable String phone,
                          @ApiParam(value = "验证码类型")@PathVariable String state) throws HTTPException {
        return userService.sendMsg(phone, state);
    }


    @ApiParam("快捷登录")
    @RequestMapping(value = "/quickLogin", method = RequestMethod.POST)
    public Result quickLogin(@ApiParam(value = "手机号")@RequestParam String phone,
                             @ApiParam(value = "验证码")@RequestParam String verificationCode,
                             @ApiParam(value = "来源 APP 小程序  公众号")String platform,
                             @ApiParam(value = "手机类型 1安卓 2ios")Integer sysPhone,
                             @ApiParam(value = "微信openId")String openId,
                             @ApiParam(value = "上级邀请码")String inviterCode,
                             @ApiParam(value = "密码")String password) {
        return userService.quickLogin(phone, verificationCode, platform, sysPhone, openId, inviterCode,password);
    }


    @ApiOperation("解密手机号")
    @RequestMapping(value = "/selectPhone", method = RequestMethod.POST)
    public Result getPhoneNumberBeanS5(@RequestBody WxPhone wxPhone) {
        return UserConstantInterface.decryptS5(wxPhone.getDecryptData(), wxPhone.getKey(), wxPhone.getIv());
    }

    @ApiParam("登录app")
    @RequestMapping(value = "/loginApp", method = RequestMethod.POST)
    public Result loginApp(@ApiParam(value = "手机号")@RequestParam String phone,
                           @ApiParam(value = "密码")@RequestParam String password) {
        return userService.loginApp(phone, password);
    }

    @CrossOrigin
    @ApiParam("注册app")
    @RequestMapping(value = "/registApp", method = RequestMethod.POST)
    public Result registApp(@ApiParam(value = "用户名")@RequestParam String userName,
                            @ApiParam(value = "手机号")@RequestParam String phone,
                            @ApiParam(value = "密码")@RequestParam String password,
                            @ApiParam(value = "验证码")String msg,
                            @ApiParam(value = "来源 APP 小程序  公众号")@RequestParam String platform,
                            @ApiParam(value = "上级邀请码")String invitation) {
        return userService.registApp(userName, phone, password, msg, platform, invitation);
    }

    @ApiOperation("用户端忘记密码")
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
    @ResponseBody
    public Result forgetPwd(@ApiParam(value = "密码")String pwd,
                            @ApiParam(value = "手机号")String phone,
                            @ApiParam(value = "验证码")String msg) {
        return userService.forgetPwd(pwd, phone, msg);
    }


    @GetMapping("/selectCity")
    @ApiOperation("根据经纬度获取城市")
    public Result selectCity(@ApiParam(value = "纬度") String lat,@ApiParam(value = "经度") String lng) {
        String way = commonInfoService.findOne(414).getValue();
        if("1".equals(way)){
            CommonInfo one = commonInfoService.findOne(217);
            String url = "https://apis.map.qq.com/ws/geocoder/v1/";
            Map<String, String> maps = new HashMap<>();
            maps.put("location", lat + "," + lng);
            maps.put("key", one.getValue());
            String data = HttpClientUtil.doGet(url, maps);
            JSONObject jsonObject = JSON.parseObject(data);
            String status = jsonObject.getString("status");
            if ("0".equals(status)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject adInfo = result.getJSONObject("ad_info");
                HashMap<String,Object> adInfoMap = new HashMap<>();
                adInfoMap.put("data",adInfo);
                return Result.success(adInfoMap);
//                return Result.success().put("data", adInfo);
            } else {
                log.error("转换失败！！！原因:" + jsonObject.getString("message"));
            }
            return Result.failed("获取定位失败！");
        }else{
            String value = commonInfoService.findOne(415).getValue();
            String url="http://api.tianditu.gov.cn/geocoder";
            Map<String,String> param=new HashMap<>();
            JSONObject postStr=new JSONObject();
            postStr.put("lon",lng);
            postStr.put("lat",lat);
            postStr.put("ver","1");
            param.put("postStr",postStr.toJSONString());
            param.put("type","geocode");
            param.put("tk",value);
            String s = HttpClientUtil.doGet(url,param);
            JSONObject jsonObject = JSONObject.parseObject(s);
            String status = jsonObject.getString("status");
            if("0".equals(status)){
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject addressComponent = result.getJSONObject("addressComponent");
                String province = addressComponent.getString("province");
                String city = addressComponent.getString("city");
                String county = addressComponent.getString("county");
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put("province",province);
                jsonObject1.put("city",city);
                jsonObject1.put("district",county);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("data",jsonObject1);
                return Result.success(hashMap);
//                return Result.success().put("data", jsonObject1);
            }
            return Result.failed("获取定位失败！");
        }
    }

    @GetMapping("/getOpenId")
    @ApiOperation("公众号根据code换取openId")
    public Result getOpenId(@ApiParam(value = "微信请求code")String code,
                            @ApiParam(value = "用户id")Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            HashMap<String, Object> map = new HashMap<>();
            map.put("data", openid);
            return Result.success(map);
//            return Result.success().put("data", openid);
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }

    }


    @GetMapping("/bindOpenId")
    @ApiOperation("用户绑定公众号openId")
    public Result bindOpenId(@ApiParam(value = "用户id")Long userId,
                             @ApiParam(value = "微信获取的openId")String openId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setOpenId(openId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @GetMapping("/getDataCenter")
    @ApiOperation("获取个人中心收藏数量、浏览、优惠券数量")
    public Result getDatCenter(@ApiParam(value = "用户id")Long userId) {
        return Result.success(userService.getDataCenter(userId));
    }



}
