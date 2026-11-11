//package com.sqx.domain.repository.service.sys.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.app.Business;
//import com.sqx.domain.repository.entity.app.TbFootprint;
//import com.sqx.domain.repository.entity.app.UserEntity;
//import com.sqx.domain.repository.entity.sys.*;
//import com.sqx.domain.repository.mapper.app.TbFootprintDao;
//import com.sqx.domain.repository.mapper.app.UserDao;
//import com.sqx.domain.repository.mapper.sys.*;
//import com.sqx.domain.repository.service.sys.GoodsService;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.SecurityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author Admin
// * @description 针对表【goods(商品表)】的数据库操作Service实现
// * @createDate 2024-09-14 14:38:18
// */
//@Service
//public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods>
//        implements GoodsService {
//
//    @Autowired
//    private BrandTypeDao brandTypeDao;
//    @Autowired
//    private BusinessDao businessDao;
//    @Autowired
//    private SectionDao sectionDao;
//    @Autowired
//    private LabelTypeDao labelTypeDao;
//    @Autowired
//    private GoodsSpecificationsDao goodsSpecificationsDao;
//    @Autowired
//    private UserDao userDao;
//    @Autowired
//    private TbFootprintDao tbFootprintDao;
//
//    @Override
//    public Result addGoodsByAdmin(Goods goods) {
//        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
//        if (user == null) {
//            return Result.failed("用户未登录");
//        }
//        if (goods.getId() != null) {
//            goods.setUpdateTime(new Date());
//            Goods goods1 = baseMapper.selectById(goods.getId());
//            if (!goods.getPrice().equals(goods1.getPrice())) {
//                goods.setPrice(goods.getPrice());
//                if (!goods.getPurchaseMoney().equals(goods1.getPurchaseMoney())) {
//                    goods.setPurchaseMoney(goods.getPurchaseMoney());
//                }
//                int result = baseMapper.updateById(goods);
//                if (result <= 0) {
//                    return Result.failed("修改失败");
//                }
//            }
//            return Result.success();
//        } else {
//            goods.setStatus(2);
//            goods.setCreateTime(new Date());
//            int result = baseMapper.insert(goods);
//            if (result <= 0) {
//                return Result.failed("添加失败");
//            }
//            return Result.success();
//        }
//    }
//
//    @Override
//    public Result addGoods(Goods goods) {
//        UserEntity user = userDao.selectById(goods.getUserId());
//        if (user == null) {
//            return Result.failed("用户未登录");
//        }
//        if (goods.getId() != null) {
//            goods.setUpdateTime(new Date());
//            Goods goods1 = baseMapper.selectById(goods.getId());
//            if (!goods.getPrice().equals(goods1.getPrice())) {
//                goods.setPrice(goods.getPrice());
//                if (!goods.getPurchaseMoney().equals(goods1.getPurchaseMoney())) {
//                    goods.setPurchaseMoney(goods.getPurchaseMoney());
//                }
//                int result = baseMapper.updateById(goods);
//                if (result <= 0) {
//                    return Result.failed("修改失败");
//                }
//            }
//            return Result.success();
//        } else {
//            goods.setStatus(2);
//            goods.setCreateTime(new Date());
//            int result = baseMapper.insert(goods);
//            if (result <= 0) {
//                return Result.failed("添加失败");
//            }
//            return Result.success();
//
//        }
//    }
//
//    @Override
//    public Result examineGoods(Long id, Integer status) {
//        Goods goods = baseMapper.selectById(id);
//        if (goods != null) {
//            goods.setStatus(status);
//            baseMapper.updateById(goods);
//            return Result.success();
//        }
//        return Result.failed("商品不存在");
//    }
//
//    @Override
//    public Result deleteGoods(Long id) {
//        Goods goods = baseMapper.selectById(id);
//        if (goods != null) {
//            baseMapper.deleteById(goods.getId());
//            return Result.success();
//        }
//        return Result.failed("商品不存在");
//    }
//
//    @Override
//    public IPage<Goods> getGoodsList(Integer page, Integer limit, Goods goods) {
//        IPage<Goods> pages;
//        if (page != null && limit != null) {
//            pages = new Page<>(page, limit);
//        } else {
//            pages = new Page<>();
//            pages.setSize(-1);
//        }
//        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
//        if (StringUtils.isNotBlank(goods.getGoodName())) {
//            queryWrapper.like("good_name", "%" + goods.getGoodName() + "%");
//        }
//        queryWrapper.orderByDesc("create_time");
//        return baseMapper.selectPage(pages, queryWrapper);
//    }
//
//    @Override
//    public Result selectByGoodsId(Long id, Long userId) {
////        UserEntity userEntity = userDao.selectById(userId);
////        if (userEntity == null) {
////            return Result.failed("用户未登录");
////        }
//        Goods goods = baseMapper.selectById(id);
//        if (goods != null) {
//            Section section = sectionDao.selectById(goods.getSectionId());
//            if (section != null) {
//                goods.setSectionName(section.getSectionName());
//            }
//            BrandType brandType = brandTypeDao.selectById(goods.getBrandId());
//            if (brandType != null) {
//                goods.setBrandTypeName(brandType.getBrandTypeName());
//            }
//            Business business = businessDao.selectById(goods.getBusinessId());
//            if (business != null) {
//                goods.setBusinessName(business.getBusinessName());
//            }
//            LabelType labelType = labelTypeDao.selectById(goods.getLabelId());
//            if (labelType != null) {
//                goods.setLabel(labelType.getLabel());
//            }
//            GoodsSpecifications goodsSpecifications = goodsSpecificationsDao.selectOne(new QueryWrapper<GoodsSpecifications>().eq("good_id", goods.getId()));
//            if (goodsSpecifications != null) {
//                goods.setGoodsSpecifications(goodsSpecifications);
//            }
//            TbFootprint tbFootprint = new TbFootprint();
//            tbFootprint.setGoodId(goods.getId());
//            tbFootprint.setUserId(userId);
//            tbFootprint.setVisitTime(new Date());
//            tbFootprintDao.insert(tbFootprint);
//            return Result.success(goods);
//        }
//        return Result.failed("该商品不存在");
//    }
//
//    @Override
//    public IPage<Goods> selectBySectionId(Integer page, Integer limit, Long sectionId, Long brandId, Integer priceSort) {
//        Page<Goods> pages;
//        if (page != null && limit != null) {
//            pages = new Page<>(page, limit);
//        } else {
//            pages = new Page<>();
//            pages.setSize(-1);
//        }
//        if (sectionId != null && brandId == null && priceSort == null) {
//            Page<Goods> goodsList = baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .apply("section_id is not null")
//                    .eq("section_id", sectionId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("create_time")
//            );
//            return goodsList.setRecords(goodsList.getRecords());
//        }
//        if (sectionId != null && brandId != null && priceSort == null) {
//            Page<Goods> goodsList = baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .apply("section_id is not null")
//                    .eq("section_id", sectionId)
//                    .apply("brand_id is not null")
//                    .eq("brand_id", brandId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("create_time"));
//            return goodsList.setRecords(goodsList.getRecords());
//        }
//        if (sectionId != null && brandId != null && priceSort == 1) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .apply("section_id is not null")
//                    .eq("section_id", sectionId)
//                    .apply("brand_id is not null")
//                    .eq("brand_id", brandId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByAsc("price"));
//        } else if (sectionId != null && brandId != null && priceSort == 2) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .apply("section_id is not null")
//                    .eq("section_id", sectionId)
//                    .apply("brand_id is not null")
//                    .eq("brand_id", brandId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("price"));
//        } else if (sectionId != null && brandId == null && priceSort == 1) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .apply("section_id is not null")
//                    .eq("section_id", sectionId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByAsc("price"));
//        } else if (sectionId != null && brandId == null && priceSort == 2) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .apply("section_id is not null")
//                    .eq("section_id", sectionId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("price"));
//        }
//        return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                .eq("section_id", sectionId)
//                .eq("status", 1)
//                .groupBy("business_id")
//                .orderByDesc("create_time"));
//    }
//
//    @Override
//    public IPage<Goods> selectHotGoods(Integer page, Integer limit, Long labelId, Integer typeState) {
//        Page<Goods> pages;
//        if (page != null && limit != null) {
//            pages = new Page<>(page, limit);
//        } else {
//            pages = new Page<>();
//            pages.setSize(-1);
//        }
//        if (typeState == 1) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .apply("label_id is not null")
//                    .eq(labelId != null, "label_id", labelId)
//                    .eq("type_state", 1)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("create_time"));
//        }
//        return baseMapper.selectPage(pages, new QueryWrapper<>((Goods) Collections.emptyList()));
//    }
//
//    @Override
//    public IPage<Goods> selectedRecommendations(Integer page, Integer limit, Long brandId, Integer priceSort, Integer typeState) {
//        Page<Goods> pages;
//        if (page != null && limit != null) {
//            pages = new Page<>(page, limit);
//        } else {
//            pages = new Page<>();
//            pages.setSize(-1);
//        }
//        if (typeState == 2 && brandId == null && priceSort == null) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .eq("type_state", 2)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("create_time"));
//        } else if (typeState == 2 && brandId != null && priceSort == null) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .eq("type_state", 2)
//                    .apply("brand_id is not null")
//                    .eq("brand_id", brandId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("create_time"));
//        } else if (typeState == 2 && brandId != null && priceSort == 1) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .eq("type_state", 2)
//                    .apply("brand_id is not null")
//                    .eq("brand_id", brandId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByAsc("price"));
//        } else if (typeState == 2 && brandId != null && priceSort == 2) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .eq("type_state", 2)
//                    .apply("brand_id is not null")
//                    .eq("brand_id", brandId)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("price"));
//        } else if (typeState == 2 && brandId == null && priceSort == 1) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .eq("type_state", 2)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByAsc("price"));
//        } else if (typeState == 2 && brandId == null && priceSort == 2) {
//            return baseMapper.selectPage(pages, new QueryWrapper<Goods>()
//                    .eq("type_state", 2)
//                    .eq("status", 1)
//                    .groupBy("business_id")
//                    .orderByDesc("price"));
//        }
//        return baseMapper.selectPage(pages, new QueryWrapper<>((Goods) Collections.emptyList()));
//    }
//
//
//}
//
//
//
//
