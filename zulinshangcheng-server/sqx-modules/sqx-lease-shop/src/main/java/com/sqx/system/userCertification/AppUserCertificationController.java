package com.sqx.system.userCertification;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserCertification;
import com.sqx.domain.repository.service.userCertification.UserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实名认证-用户端
 * @author wuChang
 * @since 2024-03-22
 */
@RestController
@Api(value = "实名认证---用户端", tags = {"实名认证---用户端"})
@RequestMapping("/app/userCertification/")
public class AppUserCertificationController {
    @Autowired
    private UserCertificationService userCertificationService;

    /**
     * 新增或修改实名认证
     *@param userCertification 实名认证
     */
    @Login
    @ApiOperation("新增或修改实名认证")
    @PostMapping(value = "saveUserCertification")
    public Result saveUserCertification(@ApiParam("用户id")@RequestAttribute Long userId,
                                        @ApiParam("实名认证实体") UserCertification userCertification) {
        userCertification.setUserId(userId);
        userCertification.setStatus(0);
        return userCertificationService.saveUserCertification(userCertification);
    }

    /**
     * 获取实名认证详情
     *@param userId 用户id
     */
    @Login
    @ApiOperation("获取实名认证详情")
    @GetMapping(value = "getUserCertificationByUserId")
    public Result getUserCertificationById(@ApiParam("用户id")@RequestAttribute Long userId) {
        return Result.success(userCertificationService.getOne(new QueryWrapper<UserCertification>().eq("user_id", userId)));
    }

}