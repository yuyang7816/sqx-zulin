package com.sqx.domain.repository.service.sys.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.exception.SqxException;
import com.sqx.common.redis.utils.RedisKeys;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.mapper.sys.CommonInfoDao;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author fang
 * @date 2020/7/8
 */
@Service
public class CommonInfoServiceImpl extends ServiceImpl<CommonInfoDao, CommonInfo> implements ICommonInfoService {


    private final CommonInfoDao commonInfoDao;

    @Autowired
    public CommonInfoServiceImpl(CommonInfoDao commonInfoDao) {
        this.commonInfoDao = commonInfoDao;
    }

    @Override
    public Result update(CommonInfo commonInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        commonInfo.setCreateAt(sdf.format(now));
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }


    @Override
    public CommonInfo findOne(int id) {
        return commonInfoDao.findOne(id);
    }

    @Override
    public Result delete(long id) {
        commonInfoDao.deleteById(id);
        return Result.success();
    }


    @Override
    public Result updateBody(CommonInfo commonInfo) {
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }

    @Override
    public Result findByType(Integer type) {
        return Result.success(commonInfoDao.findOne(type));
    }

    @Override
    public Result findByTypeAndCondition(String condition) {
        return Result.success(commonInfoDao.findByCondition(condition));
    }

//    @Override
//    public void loadCommonInfo() {
//        List<CommonInfo> commonInfoList = this.list();
//        if (CollectionUtils.isNotEmpty(commonInfoList)) {
//            commonInfoList.parallelStream()
//                    .forEach(item -> RedisUtil.setCacheObject(RedisKeys.COMMON_INFO_KEY + item.getType(), item));
//        }
//    }

//    @Override
//    public CommonInfo getCommonInfo(Integer type) {
//        // 先从缓存中获取
//        String key = RedisKeys.COMMON_INFO_KEY + type;
//        CommonInfo cacheCommonInfo = RedisUtil.getCacheObject(key);
//        if (Objects.nonNull(cacheCommonInfo)) {
//            return cacheCommonInfo;
//        }
//
//        // 缓存中没有再从数据库获取
//        CommonInfo dbCommonInfo = this.getOne(new QueryWrapper<CommonInfo>().lambda().eq(CommonInfo::getType, type));
//        if (Objects.nonNull(dbCommonInfo)) {
//            RedisUtil.setCacheObject(key, dbCommonInfo);
//            return dbCommonInfo;
//        }
//
//        // 数据库也没有，配置不存在
//        throw new SqxException("type: " + type + "配置不存在");
//    }

//    @Override
//    public String getCommonValue(Integer type) {
//        // 先从缓存中获取
//        String key = RedisKeys.COMMON_INFO_KEY + type;
//        CommonInfo cacheCommonInfo = RedisUtil.getCacheObject(key);
//        if (Objects.nonNull(cacheCommonInfo)) {
//            return cacheCommonInfo.getValue();
//        }
//
//        // 缓存中没有再从数据库获取
//        CommonInfo dbCommonInfo = this.getOne(new QueryWrapper<CommonInfo>().lambda().eq(CommonInfo::getType, type));
//        if (Objects.nonNull(dbCommonInfo)) {
//            RedisUtil.setCacheObject(key, dbCommonInfo);
//            return dbCommonInfo.getValue();
//        }
//
//        // 数据库也没有，配置不存在
//        throw new SqxException("type: " + type + "配置不存在");
//    }

//    @Override
//    public Result<Void> refreshCache() {
//        String keyPrefix = RedisKeys.COMMON_INFO_KEY;
//        RedisUtil.deleteKeys(keyPrefix);
//
//        this.loadCommonInfo();
//        return Result.success();
//    }

//    @Override
//    public Result<Void> refreshCacheKey(Integer type) {
//        String key = RedisKeys.COMMON_INFO_KEY + type;
//        RedisUtil.deleteObject(key);
//
//        CommonInfo dbCommonInfo = this.getOne(new QueryWrapper<CommonInfo>().lambda().eq(CommonInfo::getType, type));
//        if (Objects.isNull(dbCommonInfo)) {
//            throw new SqxException(String.format("系统配置: %s不存在", type));
//        }
//
//        RedisUtil.setCacheObject(key, dbCommonInfo);
//        return Result.success();
//    }
}