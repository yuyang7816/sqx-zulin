package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.RentalGuidelines;
import com.sqx.domain.repository.mapper.sys.RentalGuidelinesDao;
import com.sqx.domain.repository.service.sys.RentalGuidelinesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Admin
* @description 针对表【rental_guidelines(租赁须知表)】的数据库操作Service实现
* @createDate 2024-09-19 16:02:07
*/
@Service
public class RentalGuidelinesServiceImpl extends ServiceImpl<RentalGuidelinesDao, RentalGuidelines>
    implements RentalGuidelinesService {

    @Override
    public Result addRentalGuidelines(RentalGuidelines rentalGuidelines) {
        if (rentalGuidelines.getId()!= null){
            rentalGuidelines.setCreateTime(new Date());
            baseMapper.updateById(rentalGuidelines);
            return Result.success();
        }else {
            rentalGuidelines.setCreateTime(new Date());
            baseMapper.insert(rentalGuidelines);
            return Result.success();
        }
    }

    @Override
    public Result deleteRentalGuidelines(Long id) {
        RentalGuidelines rentalGuidelines = baseMapper.selectById(id);
        if (rentalGuidelines != null){
            baseMapper.deleteById(id);
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @Override
    public IPage<RentalGuidelines> getRentalGuidelinesList(Integer page, Integer limit, RentalGuidelines rentalGuidelines) {
        IPage<RentalGuidelines> pages;
        if (page != null && limit != null){
            pages = new Page<>(page, limit);
        }else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<RentalGuidelines> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(rentalGuidelines.getTitle())){
            queryWrapper.like("title", "%" + rentalGuidelines.getTitle() + "%");
        }
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(pages,queryWrapper);
    }

    @Override
    public Result selectByRentalGuidelinesId(Long id) {
        RentalGuidelines rentalGuidelines = baseMapper.selectById(id);
        if (rentalGuidelines != null){
            return Result.success(rentalGuidelines);
        }
        return Result.failed("查看失败");
    }
}




