package com.sqx.domain.repository.service.selfBanner;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.sys.SelfBanner;
import com.sqx.domain.repository.mapper.selfBanner.SelfBannerJpaRepository;
import com.sqx.domain.repository.mapper.selfBanner.SelfBannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SelfBannerServiceImpl implements SlefBannerService {
    @Autowired
    private SelfBannerJpaRepository jpaRepository;
    @Autowired
    private SelfBannerRepository selfBannerRepository;


    @Override
    public Result findAll(Integer type) {
        if(type==null){
            return ResultUtil.success(selfBannerRepository.selectSelfBanner());
        }else{
            return ResultUtil.success(selfBannerRepository.selectSelfBanner(type));
        }

    }

    @Override
    public Result saveBody(SelfBanner entity) {
        entity.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfBanner entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

}
