package com.sqx.system.selfCoupon;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCoupon;
import com.sqx.domain.repository.service.selfCoupon.SelfCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城优惠券制作",tags={"自营商城优惠券制作"})
@RequestMapping(value = "/selfCoupon")
public class SelfCouponController {
    @Autowired
    private SelfCouponService service;

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size) {
        return service.findAll(page, size);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCoupon entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCoupon entity) {
        return service.updateBody(entity);
    }


    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}
