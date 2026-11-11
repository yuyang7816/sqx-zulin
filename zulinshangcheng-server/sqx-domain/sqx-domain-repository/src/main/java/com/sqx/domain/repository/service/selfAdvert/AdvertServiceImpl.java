package com.sqx.domain.repository.service.selfAdvert;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.sys.Adverti;
import com.sqx.domain.repository.mapper.selfAdvert.AdvertJpaRepository;
import com.sqx.domain.repository.mapper.selfAdvert.AdvertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AdvertServiceImpl implements AdvertService {
    @Autowired
    private AdvertJpaRepository jpaRepository;
    @Autowired
    private AdvertRepository advertRepository;

    @Override
    public Result findAll(Integer type) {
        if(type==null){
            return Result.success(advertRepository.selectSelfBanner());
        }else{
            return Result.success(advertRepository.selectSelfBanner(type));
        }
    }

    @Override
    public Result saveBody(Adverti entity) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        entity.setCreateAt(sdf.format(now));
        return Result.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(Adverti entity) {
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
