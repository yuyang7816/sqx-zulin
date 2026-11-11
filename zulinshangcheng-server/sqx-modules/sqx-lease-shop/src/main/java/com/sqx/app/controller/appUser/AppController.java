package com.sqx.app.controller.appUser;

import com.sqx.auth.app.annotation.Login;
import com.sqx.auth.app.annotation.LoginUser;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.service.app.AppService;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/user")
@Api(value = "APP管理", tags = {"APP管理"})
public class AppController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;
    @Autowired
    private ICommonInfoService commonInfoService;

    @Login
    @PostMapping("/updateSendMsg")
    @ApiOperation("修改是否接受消息推送")
    public Result updateSendMsg(@LoginUser UserEntity user, @ApiParam(value = "是否推送消息") Integer isSendMsg) {
        user.setIsSendMsg(isSendMsg);
        userService.updateById(user);
        return Result.success();
    }


    @Login
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户端修改密码")
    public Result updatePwd(@LoginUser UserEntity user, @ApiParam(value = "新密码") String pwd, @ApiParam(value = "旧密码") String oldPwd) {
        if (!user.getPassword().equals(DigestUtils.sha256Hex(oldPwd))) {
            return Result.failed("原始密码不正确！");
        }
        if (pwd.equals(oldPwd)) {
            return Result.failed("新密码不能与旧密码相同！");
        }
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(new Sha256Hash(pwd, salt).toHex());
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @ApiOperation("用户端换绑手机号")
    @ResponseBody
    public Result updatePhone(@ApiParam(value = "用户id") @RequestAttribute("userId") Long userId,
                              @ApiParam(value = "手机号") @RequestParam String phone,
                              @ApiParam(value = "验证码") @RequestParam String msg) {
        return userService.updatePhone(phone, msg, userId);
    }

    @Login
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUser(@RequestBody UserEntity userEntity, @ApiParam(value = "用户id") @RequestAttribute("userId") Long userId) {
        userEntity.setUserId(userId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserImageUrl", method = RequestMethod.POST)
    @ApiOperation("用户修改头像")
    @ResponseBody
    public Result updateUserImageUrl(@LoginUser UserEntity user, @ApiParam(value = "头像") String avatar) {
        user.setAvatar(avatar);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @ApiOperation("用户修改昵称")
    @ResponseBody
    public Result updateUserName(@LoginUser UserEntity user, @ApiParam(value = "用户修改昵称") String userName) {
        user.setUserName(userName);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@LoginUser UserEntity user) {

        userService.updateById(user);
        return Result.success(user);
    }


    @RequestMapping(value = "/selectNewApp", method = RequestMethod.GET)
    @ApiOperation("升级检测")
    @ResponseBody
    public Result selectNewApp() {
        return Result.success(appService.selectNewApp());

    }

    @GetMapping(value = "/updateClientId")
    @ApiOperation("绑定ClientId")
    @ResponseBody
    @Login
    public Result updateClientId(@ApiParam(value = "app推送id") String clientId, @ApiParam(value = "用户id") @RequestAttribute("userId") Long userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setClientid(clientId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @GetMapping("/selectUserByUserId")
    @ApiOperation("查看用户信息")
    @ResponseBody
    @Login
    public Result selectUserByUserId(@LoginUser UserEntity user, @ApiParam(value = "用户id") @RequestAttribute("userId") Long userId) {
        return userService.selectUserByUserId(userId, user.getUserId());
    }

    @Login
    @ApiOperation("查看用户头像，用户名")
    @GetMapping("/selectUserInfoByUserId")
    public Result selectUserInfoByUserId(@ApiParam(value = "用户id") @RequestAttribute Long userId) {
        UserEntity userEntity = userService.getById(userId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", userEntity.getUserId());
        hashMap.put("userName", userEntity.getUserName());
        hashMap.put("avatar", userEntity.getAvatar());
        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("data", hashMap);
        return Result.success(hashMap1);
//        return Result.success().put("data", hashMap);
    }

}
