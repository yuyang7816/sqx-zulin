package com.sqx.system.selfRuleController;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsRule;
import com.sqx.domain.repository.service.selfGoodsRule.SelfGoodsRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城商品规格",tags={"自营商城商品规格"})
@RequestMapping(value = "/selfGoodsRule")
public class SelfGoodsRuleController {
    @Autowired
    private SelfGoodsRuleService service;

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
    public Result saveBody(@RequestBody SelfGoodsRule entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfGoodsRule entity) {
        return service.updateBody(entity);
    }


    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}
