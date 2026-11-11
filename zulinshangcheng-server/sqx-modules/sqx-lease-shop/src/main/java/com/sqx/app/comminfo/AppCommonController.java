package com.sqx.app.comminfo;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Api(value = "通用配置管理", tags = {"通用配置管理"})
@RequestMapping(value = "/app/common")
public class AppCommonController {
    @Autowired
    private ICommonInfoService commonService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台通用配置详情")
    @ResponseBody
    public Result getCommon(@ApiParam("通用配置类型")@PathVariable Integer id) {
       CommonInfo commonInfo = commonService.findOne(id);
        return Result.success(commonInfo);
    }


    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    @ApiOperation("用户端根据type获取对象  1客服二维码\n" +
            "                   2公众号二维码\n" +
            "                   3佣金开启\n" +
            "                   4注册邀请码\n" +
            "                    5微信APPID\n" +
            "                    21微信秘钥\n" +
            "                    6淘宝APPID\n" +
            "                   7淘宝秘钥\n" +
            "                   8淘宝授权地址\n" +
            "                    9淘宝PID\n" +
            "                    10好单库key\n" +
            "                    11淘宝名\n" +
            "                    12后台服务名称\n" +
            "                    13京东APPID\n" +
            "                    14京东秘钥\n" +
            "                    15私域邀请码（唯一不变）\n" +
            "                    16公众号Token\n" +
            "                    17公众号EncodingAESKey\n" +
            "                    18提现通知管理员openid\n" +
            "                   19后台服务域名配置\n" +
            "                    20后台管理平台域名配置\n" +
            "                    22拼多多优惠券地址")
    @ResponseBody
    public Result getCommonList(@ApiParam("通用配置类型")@PathVariable Integer type) {
        return commonService.findByType(type);
    }


    @RequestMapping(value = "/type/condition/{condition}", method = RequestMethod.GET)
    @ApiOperation("根据condition去查询  xitong  xitongs  shouye")
    @ResponseBody
    public Result findByTypeAndCondition(@ApiParam("通用配置类型分类")@PathVariable String condition) {
        return commonService.findByTypeAndCondition(condition);
    }




}
