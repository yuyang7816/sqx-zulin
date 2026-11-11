//package com.sqx.domain.repository.service.common.impl;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.sys.CommonInfo;
//import com.sqx.domain.repository.mapper.sys.CommonInfoDao;
//import com.sqx.domain.repository.service.common.CommonInfoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @author fang
// * @date 2020/7/8
// */
//@Service
//public class CommonInfoServiceImpl extends ServiceImpl<CommonInfoDao, CommonInfo> implements CommonInfoService {
//
//
//    private final CommonInfoDao commonInfoDao;
//
//    @Autowired
//    public CommonInfoServiceImpl(CommonInfoDao commonInfoDao) {
//        this.commonInfoDao = commonInfoDao;
//    }
//
//    @Override
//    public Result update(CommonInfo commonInfo) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date now = new Date();
//        commonInfo.setCreateAt(sdf.format(now));
//        commonInfoDao.updateById(commonInfo);
//        return Result.success();
//    }
//
//
//    @Override
//    public CommonInfo findOne(int id) {
//        return commonInfoDao.findOne(id);
//    }
//
//    @Override
//    public Result delete(long id) {
//        commonInfoDao.deleteById(id);
//        return Result.success();
//    }
//
//
//    @Override
//    public Result updateBody(CommonInfo commonInfo) {
//        commonInfoDao.updateById(commonInfo);
//        return Result.success();
//    }
//
//    @Override
//    public Result findByType(Integer type) {
//        CommonInfo commonInfo = commonInfoDao.findOne(type);
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("data",commonInfo);
//        return Result.success(hashMap);
//    }
//
//    @Override
//    public Result findByTypeAndCondition(String condition) {
//        List<CommonInfo> list = commonInfoDao.findByCondition(condition);
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("data",list);
//        return Result.success(hashMap);
//    }
//
//
//}