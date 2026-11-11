//package com.sqx.domain.repository.service.selfAddress;
//
//
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.app.Address;
//
//public interface AddressService {
//    //列表
//    Result findAll(Integer page, Integer size);
//
//    //查询
//    Result findOne(Long id);
//
//    //删除
//    Result delete(Long id);
//
//    //添加
//    Result saveBody(Address entity);
//
//    //修改
//    Result updateBody(Address entity);
//
//    //用户端我的收货列表
//    Result findByUserId(Long userId);
//
//    //用户端我的默认收货列表
//    Result findDefaultByUserId(Long userId);
//
//    /**
//     * 更改该用户下其他地址为非默认
//     * @param userId 用户id
//     * @param id 地址id
//     */
//    void changeIsDefault(Long userId, Long id);
//
//}
