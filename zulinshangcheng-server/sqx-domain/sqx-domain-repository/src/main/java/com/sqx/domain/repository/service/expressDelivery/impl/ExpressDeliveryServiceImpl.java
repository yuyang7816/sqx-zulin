package com.sqx.domain.repository.service.expressDelivery.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.ExpressDelivery;
import com.sqx.domain.repository.mapper.expressDelivery.ExpressDeliveryDao;
import com.sqx.domain.repository.service.expressDelivery.ExpressDeliveryService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Admin
* @description 针对表【express_delivery(快递公司表)】的数据库操作Service实现
* @createDate 2024-12-16 09:48:57
*/
@Service
public class ExpressDeliveryServiceImpl extends ServiceImpl<ExpressDeliveryDao, ExpressDelivery>
    implements ExpressDeliveryService {

    @Override
    public Result addExpressDelivery(ExpressDelivery expressDelivery) {
        QueryWrapper<ExpressDelivery> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("express_name",expressDelivery.getExpressName());
        if (expressDelivery.getId() != null){
            queryWrapper.ne("id",expressDelivery.getId());
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0){
                return Result.failed("快递公司已存在");
            }
            baseMapper.updateById(expressDelivery);
            return Result.success();
        }else {
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0){
                return Result.failed("快递公司已存在");
            }
            expressDelivery.setCreateTime(new Date());
            baseMapper.insert(expressDelivery);
            return Result.success();
        }
    }

    @Override
    public Result deleteExpressDeliveryById(Long id) {
        ExpressDelivery expressDelivery = baseMapper.selectById(id);
        if (expressDelivery != null){
            return Result.upStatus(baseMapper.deleteById(id));
        }
        return Result.failed("快递公司不存在");
    }

    @Override
    public Result selectExpressDeliveryList(Integer page, Integer limit, ExpressDelivery expressDelivery) {
        IPage<ExpressDelivery> pages;
        if (page != null && limit != null){
            pages = new Page<>(page, limit);
        }else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return Result.success(baseMapper.selectPage(pages, new QueryWrapper<>(expressDelivery)));
    }

    @Override
    public Result selectExpressDeliveryById(Long id) {
        ExpressDelivery expressDelivery = baseMapper.selectById(id);
        if (expressDelivery != null){
            return Result.success(expressDelivery);
        }
        return Result.failed("未找到快递公司");
    }
}




