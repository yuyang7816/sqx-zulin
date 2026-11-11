package com.sqx.system.vipDetails;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.VipDetails;
import com.sqx.domain.repository.service.vipDetails.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "会员管理---管理端",tags = {"会员管理---管理端"})
@RequestMapping(value = "/admin/vipDetails/")
public class VipDetailsController {

    @Autowired
    private VipDetailsService vipDetailsService;

    @ApiOperation("获取会员分类列表")
    @GetMapping("getVipDetailsList")
    public Result getVipDetailsList(Integer page, Integer limit) {
        return Result.success(vipDetailsService.getVipDetailsList(page, limit, null));
    }

    @ApiOperation("新增会员分类")
    @PostMapping("insetVipDetails")
    public Result insetVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.insetVipDetails(vipDetails);
    }

    @ApiOperation("修改会员分类")
    @PostMapping("updateVipDetails")
    public Result updateVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.updateVipDetails(vipDetails);
    }

    @ApiOperation("删除会员分类")
    @GetMapping("deleteVipDetails")
    public Result deleteVipDetails(Long id) {
        return vipDetailsService.deleteVipDetails(id);
    }

    @ApiOperation("赠送用户会员")
    @PostMapping("giveUserVip")
    public Result giveUserVip(Long userId, Integer num) {
        return vipDetailsService.giveUserVip(userId, num);
    }

    @ApiOperation("取消用户会员")
    @PostMapping("cancelUserVip")
    public Result cancelUserVip(Long userId) {
        return vipDetailsService.cancelUserVip(userId);
    }
}
