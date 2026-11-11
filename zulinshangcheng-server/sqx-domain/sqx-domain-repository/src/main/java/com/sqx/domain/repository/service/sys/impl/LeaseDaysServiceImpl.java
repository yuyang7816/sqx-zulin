package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.LeaseDays;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.sys.LeaseDaysDao;
import com.sqx.domain.repository.service.sys.LeaseDaysService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Admin
* @description 针对表【lease_days(租赁天数配置表)】的数据库操作Service实现
* @createDate 2024-09-14 14:41:55
*/
@Service
public class LeaseDaysServiceImpl extends ServiceImpl<LeaseDaysDao, LeaseDays>
    implements LeaseDaysService {

    @Autowired
    private UserDao userDao;
    @Override
    public Result addLeaseDays(LeaseDays leaseDays) {
        QueryWrapper<LeaseDays> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("day_name",leaseDays.getDayName());
        if (leaseDays.getId()!=null){
            queryWrapper.ne("id",leaseDays.getId());
            Long count = this.count(queryWrapper);
            if (count>0){
                return Result.failed("该天数已存在");
            }
            baseMapper.updateById(leaseDays);
            return Result.success();
        }else {
            Long count = this.count(queryWrapper);
            if (count>0){
                return Result.failed("该天数已存在");
            }
            leaseDays.setCreateTime(new Date());
            baseMapper.insert(leaseDays);
            return Result.success();
        }
    }

    @Override
    public Result deleteLeaseDays(Long id) {
        LeaseDays leaseDays = baseMapper.selectById(id);
        if (leaseDays == null){
            return Result.failed("该天数不存在");
        }
        baseMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result selectDaysById(Long id) {
        LeaseDays leaseDays = baseMapper.selectById(id);
        if (leaseDays != null){
            return Result.success(leaseDays);
        }
        return Result.failed("该天数不存在");
    }

    @Override
    public IPage<LeaseDays> getLeaseDaysList(Integer page, Integer limit, LeaseDays leaseDays) {
        IPage<LeaseDays> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<LeaseDays> queryWrapper = new QueryWrapper<>();
        if (leaseDays.getDayName() != null) {
            queryWrapper.like("day_name", "%" + leaseDays.getDayName() + "%");
        }
        queryWrapper.orderByAsc("sort");
        IPage<LeaseDays> page1 = baseMapper.selectPage(pages, queryWrapper);
        page1.getRecords().forEach(record ->{
            record.setDayName(record.getDayName()+"天");
        });
        return baseMapper.selectPage(pages,queryWrapper);
    }
}




