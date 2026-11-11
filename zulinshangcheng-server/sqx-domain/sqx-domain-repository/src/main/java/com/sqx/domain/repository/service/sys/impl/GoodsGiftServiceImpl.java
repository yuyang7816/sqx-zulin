package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsGift;
import com.sqx.domain.repository.entity.sys.GoodsType;
import com.sqx.domain.repository.mapper.selfGoods.GoodsGiftDao;
import com.sqx.domain.repository.mapper.sys.GoodsTypeDao;
import com.sqx.domain.repository.service.sys.GoodsGiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Admin
* @description 针对表【goods_gift(赠品数量)】的数据库操作Service实现
* @createDate 2024-12-24 14:58:44
*/
@Service
public class GoodsGiftServiceImpl extends ServiceImpl<GoodsGiftDao, GoodsGift>
    implements GoodsGiftService {

    @Autowired
    private GoodsTypeDao goodsTypeDao;

    @Override
    public Result addGift(GoodsGift goodsGift) {
        QueryWrapper<GoodsGift> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gift_name",goodsGift.getGiftName());
        if (goodsGift.getId() != null){
            queryWrapper.ne("id",goodsGift.getId());
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0){
                return Result.failed("赠品名称已存在");
            }
            int result = baseMapper.updateById(goodsGift);
            if (result <= 0){
                return Result.failed("修改失败");
            }
            return Result.success();
        }else {
            Long count = baseMapper.selectCount(queryWrapper);
            if (count > 0){
                return Result.failed("赠品名称已存在");
            }
            goodsGift.setCreateTime(new Date());
            int result = baseMapper.insert(goodsGift);
            if (result <= 0){
                return Result.failed("添加失败");
            }
            return Result.success();
        }
    }

    @Override
    public Result deleteGift(Long id) {
        GoodsGift goodsGift = baseMapper.selectById(id);
        if (goodsGift != null){
            return Result.upStatus(baseMapper.deleteById(id));
        }
        return Result.failed("该赠品不存在");
    }

    @Override
    public Result selectGift(Long id) {
        GoodsGift goodsGift = baseMapper.selectById(id);
        if (goodsGift != null){
            return Result.success(goodsGift);
        }
        return Result.failed("该赠品不存在");
    }

    @Override
    public Result selectGiftList(Integer page, Integer limit, GoodsGift goodsGift) {
        IPage<GoodsGift> pages;
        if (page != null && limit != null){
            pages = new Page<>(page, limit);
        }else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<GoodsGift> queryWrapper = new QueryWrapper<>();
        if (goodsGift.getGiftName() != null){
        queryWrapper.like("gift_name",goodsGift.getGiftName());
        }
        queryWrapper.orderByDesc("create_time");
        IPage<GoodsGift> goodsGiftIPage = baseMapper.selectPage(pages,queryWrapper);
        goodsGiftIPage.getRecords().forEach(record -> {
            if (record.getTypeId() != null){
                GoodsType goodsType = goodsTypeDao.selectById(record.getTypeId());
                if (goodsType != null){
                    record.setTypeName(goodsType.getName());
                }
            }
        });
        return Result.success(goodsGiftIPage);
    }

    @Override
    public Result selectListByTypeId(Long typeId) {
        QueryWrapper<GoodsGift> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id",typeId);
        return Result.success(baseMapper.selectList(queryWrapper));
    }
}




