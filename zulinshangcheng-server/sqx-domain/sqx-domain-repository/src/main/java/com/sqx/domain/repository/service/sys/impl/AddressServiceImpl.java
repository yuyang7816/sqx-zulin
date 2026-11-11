package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.Address;
import com.sqx.domain.repository.mapper.app.AddressDao;
import com.sqx.domain.repository.service.sys.AddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/20 16:17
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressDao, Address> implements AddressService {

    @Override
    public Result addAddress(Address address) {
        if (address.getAddressId() != null) {
            Address address1 = baseMapper.selectById(address.getAddressId());
            if (address1 != null) {
                if (address.getAddressDefault() == 1) {
                    address.setAddressDefault(1);
                    setNonDefaultForOthers(address1.getUserId(), address1.getAddressId());
                } else {
                    address.setAddressDefault(0);
                }
                baseMapper.updateById(address);
            }
        } else {
            List<Address> list = baseMapper.selectList(new QueryWrapper<Address>().eq("user_id", address.getUserId()));
            if (!list.isEmpty()) {
                address.setAddressDefault(0);
            } else {
                address.setAddressDefault(1);
            }
            baseMapper.insert(address);
            if (address.getAddressDefault() == 1) {
                setNonDefaultForOthers(address.getUserId(), address.getAddressId());
            }
        }
        return Result.success();
    }

    private void setNonDefaultForOthers(Long userId, Long addressId) {
        // 将用户的所有其他地址设置为非默认
        Address nonDefaultAddress = new Address();
        nonDefaultAddress.setAddressDefault(0);
        baseMapper.update(nonDefaultAddress,
                new QueryWrapper<Address>().eq("user_id", userId).ne(addressId != null, "address_id", addressId));
    }


    @Override
    public Result deleteAddress(Long addressId) {
        Address address = baseMapper.selectById(addressId);
        if (address == null){
            return Result.failed("该地址不存在");
        }
//            if (address.getAddressDefault() == 0){
                baseMapper.deleteById(addressId);
                return Result.success("删除成功");
//            } else if (address.getAddressDefault() == 1) {
//                List<Address> address1 = baseMapper.selectList(new QueryWrapper<Address>().eq("user_id", address.getUserId()));
//                boolean hasNonDefaultAddress = false;
//                if (address1 != null){
//                    for (Address address2 : address1) {
//                        if (address2.getAddressId().equals(addressId)){
//                            continue;
//                        }
//                        if (address2.getAddressDefault() == 0){
//                            address2.setAddressDefault(1);
//                            baseMapper.update(address2, new QueryWrapper<Address>().eq("address_id", address2.getAddressId()));
//                            hasNonDefaultAddress = true;
//                            break;
//                        }
//                    }
//                }
//                if (!hasNonDefaultAddress){
//                    return Result.failed("该地址为默认地址，无法删除");
//                }else {
//                    baseMapper.deleteById(addressId);
//                    return Result.success();
//                }
//            }
//        return Result.success("删除失败");
    }

    @Override
    public Result getAddressList(Integer page, Integer limit, Address address) {
        IPage<Address> pages;
        if (page != null && limit != null){
            pages = new Page<>(page,limit);
        }else{
            pages = new Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(address.getUserName()),"user_name","%"+ address.getUserName()+"%");
        queryWrapper.like(StringUtils.isNotBlank(address.getUserPhone()),"user_phone","%"+ address.getUserPhone()+"%");
        queryWrapper.like(StringUtils.isNotBlank(address.getAddressDetail()),"address_detail","%"+ address.getAddressDetail()+"%");
        if (address.getUserId() != null){
            queryWrapper.eq("user_id", address.getUserId());
        }
        return Result.success(baseMapper.selectPage(pages,queryWrapper));
    }

    @Override
    public Result selectByAddressId(Long addressId) {
        Address address = baseMapper.selectById(addressId);
        if (address != null){
            return Result.success(address);
        }
        return Result.failed("查询失败");
    }
}
