package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.Address;
import com.sqx.domain.repository.service.sys.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@RestController
@RequestMapping("/admin/address/")
@Api(value = "用户地址", tags = {"用户地址"})
public class AdminAddressController {

    @Autowired
    private AddressService userAddressService;

    @ApiOperation("保存地址")
    @PostMapping("saveAddress")
    public Result saveAddress(Long userId, Address address) {
        address.setUserId(userId);
        return userAddressService.addAddress(address);
    }

    @ApiOperation("获取地址详细信息")
    @GetMapping("getAddressInfo")
    public Result getAddressInfo(Long addressId) {

        return Result.success(userAddressService.getById(addressId));
    }

    @ApiOperation("删除地址")
    @PostMapping("deleteAddress")
    public Result deleteAddress(Long addressId) {
        return userAddressService.removeById(addressId) ? Result.success() : Result.failed();
    }

    @ApiOperation("获取地址列表")
    @GetMapping("getAddressList")
    public Result getAddressList(Long userId, Integer page, Integer limit, Address address) {
        address.setUserId(userId);
        return userAddressService.getAddressList(page, limit, address);
    }
}
