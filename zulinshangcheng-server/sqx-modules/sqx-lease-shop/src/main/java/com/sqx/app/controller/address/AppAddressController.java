package com.sqx.app.controller.address;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.Address;
import com.sqx.domain.repository.service.sys.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@RestController
@RequestMapping("/app/address/")
@Api(value = "用户地址", tags = {"用户地址"})
public class AppAddressController {

    @Autowired
    private AddressService userAddressService;

    @Login
    @ApiOperation("保存地址")
    @PostMapping("saveAddress")
    public Result saveAddress(@RequestAttribute("userId") Long userId, Address address) {
        address.setUserId(userId);
        return userAddressService.addAddress(address);
    }

//    @ApiOperation("保存地址")
//    @PostMapping("saveAddressByUser")
//    public Result saveAddress(SysUserAddress sysUserAddress) {
//        return userAddressService.saveAddress(sysUserAddress);
//    }
    @ApiOperation("获取地址详细信息")
    @GetMapping("getAddressInfo")
    public Result getAddressInfo(Long addressId) {

        return Result.success(userAddressService.getById(addressId));
    }

    @Login
    @ApiOperation("删除地址")
    @PostMapping("deleteAddress")
    public Result deleteAddress(@RequestAttribute("userId") Long userId, Long addressId) {
        return userAddressService.deleteAddress(addressId);
    }

    @Login
    @ApiOperation("获取地址列表")
    @GetMapping("getAddressList")
    public Result getAddressList(@RequestAttribute("userId") Long userId, Integer page, Integer limit,Address address) {
        address.setUserId(userId);
        return Result.success(userAddressService.getAddressList(page, limit, address));
    }
}
