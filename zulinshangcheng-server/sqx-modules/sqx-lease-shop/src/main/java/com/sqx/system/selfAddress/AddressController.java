//package com.sqx.system.selfAddress;
//
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.app.Address;
//import com.sqx.domain.repository.service.sys.AddressService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@Api(value="自营商城我的地址",tags={"自营商城我的地址管理"})
//@RequestMapping(value = "/address")
//public class AddressController {
//    @Autowired
//    private AddressService service;
//
//    @GetMapping("/list")
//    @ApiOperation("列表")
//    public Result findAll(Integer page, Integer size) {
//        return service.findAll(page, size);
//    }
//
//    @GetMapping("/find")
//    @ApiOperation("查询")
//    public Result findOne(Long id) {
//        return service.findOne(id);
//    }
//
//    @PostMapping("/save")
//    @ApiOperation("添加")
//    public Result saveBody(@RequestBody Address entity) {
//        return service.saveBody(entity);
//    }
//
//    @PostMapping("/update")
//    @ApiOperation("修改")
//    public Result updateBody(@RequestBody Address entity) {
//        return service.updateBody(entity);
//    }
//
//
//    @GetMapping("/delete")
//    @ApiOperation("删除")
//    public Result delete(Long id) {
//        return service.delete(id);
//    }
//
//    @GetMapping("/findByUserId")
//    @ApiOperation("用户端我的收货列表")
//    public Result findByUserId(Long userId) {
//        return service.findByUserId(userId);
//    }
//
//    @GetMapping("/findDefaultByUserId")
//    @ApiOperation("用户端获取用户默认地址")
//    public Result findDefaultByUserId(Long userId) {
//        return service.findDefaultByUserId(userId);
//    }
//
//}
