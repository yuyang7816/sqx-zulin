package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsAccessory;
import com.sqx.domain.repository.entity.sys.GoodsType;
import com.sqx.domain.repository.mapper.selfGoods.GoodsAccessoryDao;
import com.sqx.domain.repository.mapper.sys.GoodsTypeDao;
import com.sqx.domain.repository.service.sys.GoodsAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Admin
 * @description 针对表【goods_accessory(商品配件表)】的数据库操作Service实现
 * @createDate 2024-12-24 14:58:39
 */
@Service
public class GoodsAccessoryServiceImpl extends ServiceImpl<GoodsAccessoryDao, GoodsAccessory>
        implements GoodsAccessoryService {

    @Autowired
    private GoodsTypeDao goodsTypeDao;

    @Override
    public Result addGoodsAccessory(GoodsAccessory goodsAccessory) {
        QueryWrapper<GoodsAccessory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessory_name", goodsAccessory.getAccessoryName());
        if (goodsAccessory.getId() != null) {
            queryWrapper.ne("id", goodsAccessory.getId());
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                return Result.failed("商品配件名称已存在");
            }
            int result = baseMapper.updateById(goodsAccessory);
            if (result <= 0) {
                return Result.failed("修改失败");
            }
            return Result.success();
        } else {
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                return Result.failed("商品配件名称已存在");
            }
            goodsAccessory.setCreateTime(new Date());
            int result = baseMapper.insert(goodsAccessory);
            if (result <= 0) {
                return Result.failed("添加失败");
            }
            return Result.success();
        }
    }

    @Override
    public Result deleteGoodsAccessory(Long id) {
        GoodsAccessory goodsAccessory = baseMapper.selectById(id);
        if (goodsAccessory != null) {
            return Result.upStatus(baseMapper.deleteById(id));
        }
        return Result.failed("商品配件不存在");
    }

    @Override
    public Result selectGoodsAccessory(Long id) {
        GoodsAccessory goodsAccessory = baseMapper.selectById(id);
        if (goodsAccessory != null) {
            if (goodsAccessory.getTypeId() != null) {
                GoodsType goodsType = goodsTypeDao.selectById(goodsAccessory.getTypeId());
                if (goodsAccessory.getTypeId() != null) {
                    goodsAccessory.setTypeName(goodsType.getName());
                }
            }
            return Result.success(goodsAccessory);
        }
        return Result.failed("商品配件不存在");
    }

    @Override
    public Result selectGoodsAccessoryList(Integer page, Integer limit, GoodsAccessory goodsAccessory) {
        IPage<GoodsAccessory> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<GoodsAccessory> queryWrapper = new QueryWrapper<>();
        if (goodsAccessory.getAccessoryName() != null) {
            queryWrapper.like("accessory_name", goodsAccessory.getAccessoryName());
        }
        queryWrapper.orderByDesc("create_time");
        IPage<GoodsAccessory> goodsAccessoryIPage = baseMapper.selectPage(pages, queryWrapper);
        goodsAccessoryIPage.getRecords().forEach(record -> {
            if (record.getTypeId() != null) {
                GoodsType goodsType = goodsTypeDao.selectById(record.getTypeId());
                if (record.getTypeId() != null) {
                    record.setTypeName(goodsType.getName());
                }
            }
        });
        return Result.success(goodsAccessoryIPage);
    }

    @Override
    public Result selectListByTypeId(Long typeId) {
        QueryWrapper<GoodsAccessory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        List<GoodsAccessory> goodsAccessoryList = baseMapper.selectList(queryWrapper);
        goodsAccessoryList.forEach(goodsAccessory -> {
            if (goodsAccessory.getTypeId() != null) {
                GoodsType goodsType = goodsTypeDao.selectById(goodsAccessory.getTypeId());
                if (goodsAccessory.getTypeId() != null) {
                    goodsAccessory.setTypeName(goodsType.getName());
                }
            }
        });
        return Result.success(goodsAccessoryList);
    }
}




