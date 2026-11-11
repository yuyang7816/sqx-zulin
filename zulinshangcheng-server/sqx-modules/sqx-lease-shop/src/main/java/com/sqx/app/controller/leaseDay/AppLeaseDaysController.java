package com.sqx.app.controller.leaseDay;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.LeaseDays;
import com.sqx.domain.repository.service.sys.LeaseDaysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/26 16:32
 */
@RestController
@RequestMapping("/app/leaseDays/")
@Api(value = "租赁天数相关-用户端", tags = {"租赁天数相关-用户端"})
public class AppLeaseDaysController {

    @Autowired
    private LeaseDaysService leaseDaysService;

    @GetMapping("selectDaysById")
    @ApiOperation("根据id查询租赁天数")
    public Result selectDaysById(Long id) {
        return leaseDaysService.selectDaysById(id);
    }

    @GetMapping("getLeaseDaysList")
    @ApiOperation("查询租赁天数列表")
    public Result getLeaseDaysList(Integer page, Integer limit, LeaseDays leaseDays) {
        return Result.success(leaseDaysService.getLeaseDaysList(page, limit, leaseDays));
    }
}
