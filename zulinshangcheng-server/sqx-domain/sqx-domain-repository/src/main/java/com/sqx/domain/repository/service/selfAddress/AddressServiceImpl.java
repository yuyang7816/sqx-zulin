//package com.sqx.domain.repository.service.selfAddress;
//
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.app.Address;
//import com.sqx.domain.repository.mapper.selfAddress.AddressJpaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class AddressServiceImpl implements AddressService {
//    @Autowired
//    private AddressJpaRepository jpaRepository;
//
//    @Override
//    public Result findAll(Integer page, Integer size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResultUtil.success(jpaRepository.findAll(pageable));
//    }
//
//    @Override
//    public Result saveBody(Address entity) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date now = new Date();
//        entity.setCreateAt(sdf.format(now));
//        if (entity.getIsDefault() == 1){
//            this.changeIsDefault(entity.getUserId(), entity.getId());
//        }
//        return ResultUtil.success(jpaRepository.save(entity));
//    }
//
//    @Override
//    public Result updateBody(Address entity) {
//        if (entity.getIsDefault() == 1){
//            this.changeIsDefault(entity.getUserId(), entity.getId());
//        }
//        return ResultUtil.success(jpaRepository.save(entity));
//    }
//
//    @Override
//    public Result findOne(Long id) {
//        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
//    }
//
//    @Override
//    public Result delete(Long id) {
//        jpaRepository.deleteById(id);
//        return ResultUtil.success();
//    }
//
//    @Override
//    public Result findByUserId(Long userId) {
//        return ResultUtil.success(jpaRepository.findByUserId(userId));
//    }
//
//    @Override
//    public Result findDefaultByUserId(Long userId) {
//        List<Address> list = jpaRepository.findByUserId(userId);
//        if (list != null && list.size() >0){ //判断是否有地址
//            return ResultUtil.success(list.get(0));
//        }
//        return ResultUtil.success();
//    }
//
//    @Override
//    public void changeIsDefault(Long userId, Long id) {
//        List<Address> list = jpaRepository.findByUserId(userId);
//        for (Address address : list) {
//            if (address.getId() != id && address.getIsDefault() == 1){
//                address.setIsDefault(0);
//                jpaRepository.save(address);
//            }
//        }
//    }
//
//}
