package com.sqx.system.userCertification;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserCertification;
import com.sqx.domain.repository.service.userCertification.UserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实名认证-管理端
 * @author wuChang
 * @since 2024-03-22
 */
@RestController
@Api(value = "实名认证---管理端", tags = {"实名认证---管理端"})
@RequestMapping("/admin/userCertification/")
public class AdminUserCertificationController {
    @Autowired
    private UserCertificationService userCertificationService;

    /**
     * 新增或修改实名认证
     *@param userCertification 实名认证
     */
    @ApiOperation("新增或修改实名认证")
    @PostMapping(value = "saveUserCertification")
    public Result saveUserCertification(@ApiParam("实名认证实体") UserCertification userCertification) {
        return userCertificationService.saveUserCertification(userCertification);
    }

    @ApiOperation("修改实名认证(无需审核)")
    @PostMapping(value = "updateCertification")
    public Result updateCertification(@ApiParam("实名认证实体")UserCertification userCertification) {
        return userCertificationService.updateById(userCertification) ? Result.success() : Result.failed();
    }

    /**
     * 获取实名认证列表
     * @param userCertification 实名认证
     */
    @ApiOperation("获取实名认证列表")
    @GetMapping(value = "getUserCertificationList")
    public Result getUserCertificationList(@ApiParam("页码")Integer page,
                                           @ApiParam("条数")Integer limit,
                                           @ApiParam("实名认证实体")UserCertification userCertification) {

        return Result.success(userCertificationService.getUserCertificationList(page, limit, userCertification));
    }

    /**
     * 审核实名认证
     */
    @ApiOperation("审核实名认证")
    @PostMapping(value = "authCertification")
    public Result authCertification(@ApiParam("实名认证id")Long id,
                                    @ApiParam("0待审核 1已通过 2未通过")Integer status,
                                    @ApiParam("审核原因")String remark,
                                    @ApiParam("任务id")Long taskId,
                                    @ApiParam("是否可以再次提交：0可以  1不可以")Integer isSubmit) {
        return userCertificationService.authCertification(id, status, remark,taskId,isSubmit);

    }

    /**
     * 删除实名认证
     *@param id 实名认证id
     */
    @ApiOperation("删除实名认证")
    @PostMapping(value = "deleteUserCertificationById")
    public Result deleteUserCertificationById(@ApiParam("实名认证id")Long id) {

        return userCertificationService.deleteUserCertificationById(id);
    }

}