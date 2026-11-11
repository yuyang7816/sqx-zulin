package com.sqx.system.selfUser;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfUserBrowse;
import com.sqx.domain.repository.service.selfUser.SelfUserBrowseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城用户浏览",tags={"自营商城用户浏览"})
@RequestMapping(value = "/selfUserBrowse")
public class SelfUserBrowseController {
    @Autowired
    private SelfUserBrowseService service;

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size, Long userId, @RequestParam(required = false) String createTime) {
        return service.findAll(page, size, userId, createTime);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfUserBrowse entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfUserBrowse entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}
