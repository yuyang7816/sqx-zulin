package com.sqx.system.expressDelivery;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.ExpressDelivery;
import com.sqx.domain.repository.service.expressDelivery.ExpressDeliveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/12/16 9:56
 */
@RestController
@RequestMapping("/admin/expressDelivery/")
@Api(value = "快递相关---管理端",tags = {"快递相关---管理端"})
public class AdminExpressDeliveryController {

    @Autowired
    private ExpressDeliveryService expressDeliveryService;

    @PostMapping("addExpressDelivery")
    @ApiOperation("添加或修改快递公司")
    public Result addExpressDelivery(ExpressDelivery expressDelivery) {
        return expressDeliveryService.addExpressDelivery(expressDelivery);
    }

    @PostMapping("deleteExpressDelivery")
    @ApiOperation("删除快递公司")
    public Result deleteExpressDeliveryById(Long id) {
        return expressDeliveryService.deleteExpressDeliveryById(id);
    }

    @GetMapping("selectExpressDelivery")
    @ApiOperation("查询快递公司")
    public Result selectExpressDeliveryById(Long id) {
        return expressDeliveryService.selectExpressDeliveryById(id);
    }

    @GetMapping("selectExpressDeliveryList")
    @ApiOperation("查询快递公司列表")
    public Result selectExpressDeliveryList(Integer page, Integer limit, ExpressDelivery expressDelivery) {
        return expressDeliveryService.selectExpressDeliveryList(page, limit, expressDelivery);
    }


}
