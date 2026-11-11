package com.sqx.system.selfGoodsVirtual;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsVirtual;
import com.sqx.domain.repository.service.selfGoodsVirtual.SelfGoodsVirtualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城虚拟商品",tags={"自营商城虚拟商品"})
@RequestMapping(value = "/selfGoodsVirtual")
public class SelfGoodsVirtualController {
    @Autowired
    private SelfGoodsVirtualService service;

    @GetMapping("/list")
    @ApiOperation("兑换码列表")
    public Result findAll(Integer page, Integer size, Long goodsId, @ApiParam("卡密内容")@RequestParam(required = false) String content) {
        return service.findAll(page, size, goodsId,content);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfGoodsVirtual entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfGoodsVirtual entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}
