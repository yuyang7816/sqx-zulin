package com.sqx.system.message.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.mapper.sys.MessageInfoDao;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.utils.BaiDuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ICommonInfoService commonInfoService;

    @Login
    @GetMapping("/getUserMessageInfoList")
    @ApiOperation("消息列表")
    public Result getUserMessageInfoList(@ApiParam("页码") Integer page,
                                         @ApiParam("条数") Integer limit,
                                         @ApiParam("用户id") @RequestAttribute("userId") Long userId,
                                         @ApiParam("消息实体") MessageInfo messageInfo) {
        messageInfo.setUserId(userId.toString());
        messageService.updateSendState(userId);
        return Result.success(messageService.selectMessageList(page, limit, messageInfo));
    }

    @Login
    @GetMapping("/getUserMessageInfoListLimit1")
    @ApiOperation("查看最新消息")
    public Result getUserMessageInfoListLimit1(@ApiParam("用户id") @RequestAttribute("userId") Long userId,
                                               @ApiParam("消息实体") MessageInfo messageInfo) {
        messageInfo.setUserId(userId.toString());
        return Result.success(messageService.getUserMessageInfoListLimit1(messageInfo));
    }

    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("意见反馈或注销")
    public Result insertMessage(@ApiParam("用户id") @RequestAttribute("userId") Long userId,
                                @ApiParam("消息实体") @RequestBody MessageInfo messageInfo) {
        String value = commonInfoService.findOne(294).getValue();
        if ("是".equals(value)) {
            if (StringUtils.isNotBlank(messageInfo.getTitle())) {
                if (BaiDuUtils.auditBaiDuText(messageInfo.getTitle())) {
                    return Result.failed("您发布的标题包含敏感词汇");
                }
            }
            if (StringUtils.isNotBlank(messageInfo.getContent())) {
                if (BaiDuUtils.auditBaiDuText(messageInfo.getContent())) {
                    return Result.failed("您发布的内容包含敏感词汇");
                }
            }
            if (StringUtils.isNotBlank(messageInfo.getImage())) {
                if (BaiDuUtils.auditBaiDuImg(messageInfo.getImage())) {
                    return Result.failed("您发布的图片不合规");
                }
            }
        }
        if ("8".equals(messageInfo.getState())) {
            UserEntity userEntity = userService.getById(userId);
            messageInfo.setTitle(userEntity.getPhone());
            messageInfo.setContent("注销");
            Long count = messageService.count(new QueryWrapper<MessageInfo>()
                    .eq("user_id", userId)
                    .eq("state", 8));
            if (count > 0) {
                return Result.failed("注销申请已提交,请勿重复提交");
            }
        }
        if ("2".equals(messageInfo.getState())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
            messageInfo.setTitle("意见反馈");
            messageInfo.setState("2");
            messageInfo.setUserId(String.valueOf(userId));
            messageInfo.setIsSee("2");
            String emailPattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
            if (messageInfo.getPhone() != null && (Pattern.matches("^[0-9]*$", messageInfo.getPhone()) || Pattern.matches(emailPattern, messageInfo.getPhone()))) {
                messageInfo.setPhone(messageInfo.getPhone());
            }else {
                return ResultUtil.error(-1,"输入的内容有误，请检查");
            }
            int result = messageService.saveBody(messageInfo);
            if (result <= 0) {
                return Result.failed("意见反馈失败");
            } else {
                return Result.success();
            }
        }
        messageInfo.setUserId(String.valueOf(userId));
        messageInfo.setIsSee("2");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/updateMessage")
    @ApiOperation("修改")
    public Result updateMessage(@ApiParam("消息实体") @RequestBody MessageInfo messageInfo) {
        messageService.update(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/deleteMessage")
    @ApiOperation("删除")
    public Result deleteMessage(@ApiParam("消息id") Long id) {
        messageService.delete(id);
        return Result.success();
    }

    @Login
    @GetMapping("/getMessageSign")
    @ApiOperation("获取未读消息数量")
    public Result getMessageSign(@ApiParam("用户id") @RequestAttribute("userId") Long userId) {
        return Result.success(messageService.getMessageSign(userId));
    }

}