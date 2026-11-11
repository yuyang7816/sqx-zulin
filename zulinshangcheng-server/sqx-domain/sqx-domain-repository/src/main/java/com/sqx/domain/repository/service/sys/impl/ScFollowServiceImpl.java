package com.sqx.domain.repository.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.ScFollow;
import com.sqx.domain.repository.entity.app.TbFootprint;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.DailyFootprint;
import com.sqx.domain.repository.entity.sys.Goods;
import com.sqx.domain.repository.entity.sys.SelfGoodsSku;
import com.sqx.domain.repository.mapper.app.ScFollowDao;
import com.sqx.domain.repository.mapper.app.TbFootprintDao;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.selfGoodsSku.SelfGoodsSkuJpaRepository;
import com.sqx.domain.repository.mapper.sys.GoodsDao;
import com.sqx.domain.repository.service.selfGoodsAttr.SelfGoodsAttrService;
import com.sqx.domain.repository.service.sys.ScFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ScFollowServiceImpl extends ServiceImpl<ScFollowDao, ScFollow> implements ScFollowService {

    @Autowired
    private TbFootprintDao footprintDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private SelfGoodsAttrService goodsAttrService;

    @Autowired
    private SelfGoodsSkuJpaRepository skuJpaRepository;

    @Override
    public Result updateFollow(ScFollow follow) {
        Long count = baseMapper.selectCount(new QueryWrapper<ScFollow>()
                .eq("user_id", follow.getUserId())
                .eq("by_id", follow.getById())
                .eq("classify", 1));
        if (count > 0) {
            baseMapper.delete(new QueryWrapper<ScFollow>().eq("user_id", follow.getUserId()).eq("by_id", follow.getById()).eq("classify", 1));
            if (follow.getClassify() == 1) {
                return Result.success("取消收藏成功");
            }
        } else {
            follow.setCreateTime(LocalDateTime.now());
            follow.setClassify(1);
            baseMapper.insert(follow);
            if (follow.getClassify() == 1) {
                return Result.success("收藏成功");
            }
        }
        return Result.failed("收藏失败");
    }


    @Override
    public Page<Goods> getFollowGoodsList(Long userId, Integer page, Integer limit, ScFollow follow) {
        Page<Goods> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getFollowGoodsList(pages, userId, follow);

    }


    @Override
    public int getMyFollowCount(Long userId) {

        return baseMapper.getMyFollowCount(userId);
    }

    @Override
    public int getMyFollowGoodsCount(Long userId) {
        return baseMapper.getMyFollowGoodsCount(userId);
    }

    @Override
    public Result selectMyFootprintList(Long userId, Integer pageNum, Integer pageSize) {
        Page<TbFootprint> pages;
        if (pageNum != null && pageSize != null) {
            pages = new Page<>(pageNum, pageSize);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.failed("用户信息不存在，请重新登录");
        }
        IPage<TbFootprint> list = footprintDao.selectByUserId(pages, userId);
        // 按照visitTime的年月日分组
        Map<LocalDate, List<TbFootprint>> groupDate = list.getRecords().stream()
                .collect(Collectors.groupingBy(tbFootprint ->
                        tbFootprint.getVisitTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                ));
         // 创建一个列表存储结果：DailyFootprint
        List<DailyFootprint> resultPairs = new ArrayList<>();
        groupDate.forEach((date, footprints) ->{
            Map<Long, TbFootprint> uniqueByGoodId = footprints.stream()
                    .filter(fp -> fp.getGoodId() != null)
                    .collect(Collectors.toMap(
                            TbFootprint::getGoodId,
                            fp -> fp,
                            (fp1, fp2) -> fp1.getVisitTime().compareTo(fp2.getVisitTime()) > 0 ? fp1 : fp2));
            resultPairs.add(new DailyFootprint(date,new ArrayList<>(uniqueByGoodId.values())));
        });
        resultPairs.sort(Comparator.comparing(DailyFootprint::getDate).reversed());
        resultPairs.forEach(dailyFootprint -> {
            dailyFootprint.getFootprints().forEach(tbFootprint -> {
                Long goodId = tbFootprint.getGoodId();
                Goods goods = goodsDao.selectById(goodId);
                if (goods != null) {
                    goods.setAttr(goodsAttrService.findByGoodsId(goods.getId()));
                    goods.setSku(skuJpaRepository.findAllByGoodsId(goods.getId()));
                    if (!goods.getSku().isEmpty()) {
                        SelfGoodsSku sku = goods.getSku().get(0);
                        goods.setDailyRent(sku.getDailyRent());
                        goods.setMemberRent(sku.getMemberRent());
                        goods.setOldDegree(sku.getOldDegree());
                    }
                    tbFootprint.setGoods(goods);
                }
            });
        });
        // 构建分页结果对象
        IPage<DailyFootprint> resultPage = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        resultPage.setRecords(resultPairs);
        return Result.success(resultPage);
    }

    @Override
    public Result getFollowList(Long userId, Integer page, Integer limit) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.failed("用户信息不存在，请重新登录");
        }
        List<ScFollow> list = baseMapper.selectByUserId(userId);
//        return Result.success(new PageInfo<>(list));
        return null;
    }

    @Override
    public Result getFanList(Long userId, Integer page, Integer limit) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.failed("用户信息不存在，请重新登录");
        }
        List<ScFollow> list = baseMapper.selectByFanId(userId);
//        return Result.success().put("data", new PageInfo<>(list));
        return null;
    }

}
