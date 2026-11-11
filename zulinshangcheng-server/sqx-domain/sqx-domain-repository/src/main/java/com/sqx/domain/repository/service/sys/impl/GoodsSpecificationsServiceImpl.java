package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsSpecifications;
import com.sqx.domain.repository.entity.sys.SysUserEntity;
import com.sqx.domain.repository.mapper.sys.GoodsSpecificationsDao;
import com.sqx.domain.repository.service.sys.GoodsSpecificationsService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Admin
* @description 针对表【goods_specifications(回收的二手物品的规格表)】的数据库操作Service实现
* @createDate 2024-09-14 14:38:18
*/
@Service
public class GoodsSpecificationsServiceImpl extends ServiceImpl<GoodsSpecificationsDao, GoodsSpecifications>
    implements GoodsSpecificationsService {

    @Override
    public Result addGoodsSpecifications(GoodsSpecifications goodsSpecifications) {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null){
            return Result.failed("用户未登录");
        }
        if (goodsSpecifications.getId() != null){
            GoodsSpecifications goodsSpecifications1 = baseMapper.selectById(goodsSpecifications.getId());
            if (goodsSpecifications1 != null){
                goodsSpecifications.setUpdateTime(new Date());
               baseMapper.updateById(goodsSpecifications);
            return Result.success();
            }
        }else{
            goodsSpecifications.setCreateTime(new Date());
            baseMapper.insert(goodsSpecifications);
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @Override
    public Result deleteGoodsSpecifications(Long id) {
        GoodsSpecifications goodsSpecifications = this.getById(id);
        if (goodsSpecifications != null){
            this.removeById(id);
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @Override
    public IPage<GoodsSpecifications> getGoodsSpecificationsList(Integer page, Integer limit, GoodsSpecifications goodsSpecifications) {
        IPage<GoodsSpecifications> pages;
        if (page != null && limit != null){
            pages = new Page<>(page, limit);
        }else{
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(goodsSpecifications));
    }

    @Override
    public Result selectBySpecId(Long id) {
        GoodsSpecifications goodsSpecifications = baseMapper.selectById(id);
        if (goodsSpecifications != null){
            return Result.success(goodsSpecifications);
        }
        return Result.failed("该数据不存在");
    }
}




