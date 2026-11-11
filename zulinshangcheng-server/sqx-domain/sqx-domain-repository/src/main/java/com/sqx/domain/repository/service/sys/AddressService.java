package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.Address;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/20 16:16
 */
public interface AddressService extends IService<Address> {

    Result addAddress(Address address);

    Result deleteAddress(Long addressId);

    Result getAddressList(Integer page, Integer limit, Address address);

    Result selectByAddressId(Long addressId);

}
