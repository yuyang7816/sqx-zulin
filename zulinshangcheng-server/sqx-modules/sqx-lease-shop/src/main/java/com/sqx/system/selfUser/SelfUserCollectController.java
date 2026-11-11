package com.sqx.system.selfUser;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfUserCollect;
import com.sqx.domain.repository.service.selfUser.SelfUserCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城用户收藏",tags={"自营商城用户收藏"})
@RequestMapping(value = "/selfUserCollect")
public class SelfUserCollectController {
    @Autowired
    private SelfUserCollectService service;

    @GetMapping("/list")
    @ApiOperation("列表")
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
    public Result saveBody(@RequestBody SelfUserCollect entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfUserCollect entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

    @GetMapping("/check")
    @ApiOperation("判断是否收藏")
    public Result check(Long goodsId, Long userId) {
        return service.check(goodsId, userId);
    }

}
