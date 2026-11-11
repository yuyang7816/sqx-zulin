package com.sqx.system.selfCart;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfCart;
import com.sqx.domain.repository.service.selfCart.SlefCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城购物车",tags={"自营商城购物车"})
@RequestMapping(value = "/selfCart")
public class SelfCartController {
    @Autowired
    private SlefCartService service;

    @GetMapping("/list")
    @ApiOperation("用户端购物车列表")
    public Result findAll(Integer page, Integer size, Long userId) {
        return service.findAll(page, size, userId);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCart entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCart entity) {
        return service.updateBody(entity);
    }


    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

    @GetMapping("/deleteAll")
    @ApiOperation("批量删除")
    public Result delete(String ids) {
        return service.deleteAll(ids);
    }

    @GetMapping("/changeNumber")
    @ApiOperation("加减数量")
    public Result changeNumber(Long id, Integer number) {
        return service.changeNumber(id, number);
    }

}
