package com.sqx.domain.repository.service.selfGoods;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.SkuUtil;
import com.sqx.domain.repository.entity.app.ScFollow;
import com.sqx.domain.repository.entity.app.TbFootprint;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.app.ScFollowDao;
import com.sqx.domain.repository.mapper.app.TbFootprintDao;
import com.sqx.domain.repository.mapper.labelType.LabelTypeDao;
import com.sqx.domain.repository.mapper.selfGoods.GoodsAccessoryDao;
import com.sqx.domain.repository.mapper.selfGoods.GoodsGiftDao;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfGoods.GoodsRepository;
import com.sqx.domain.repository.mapper.selfGoodsAttr.SelfGoodsAttrJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsAttrValue.SelfGoodsAttrValueJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsBrand.SelfGoodsBrandDao;
import com.sqx.domain.repository.mapper.selfGoodsBrand.SelfGoodsBrandJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsSku.SelfGoodsSkuJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsType.GoodsTypeJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsVirtual.SelfGoodsVirtualJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersRepository;
import com.sqx.domain.repository.mapper.sys.GoodsDao;
import com.sqx.domain.repository.service.selfGoodsAttr.SelfGoodsAttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsJpaRepository jpaRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsTypeJpaRepository goodsJpaTypeRepository;
    @Autowired
    private SelfGoodsAttrValueJpaRepository attrValueJpaRepository;
    @Autowired
    private SelfGoodsAttrJpaRepository attrJpaRepository;
    @Autowired
    private SelfGoodsAttrService goodsAttrService;
    @Autowired
    private SelfGoodsSkuJpaRepository skuJpaRepository;
    @Autowired
    private SelfGoodsBrandJpaRepository brandJpaRepository;
    @Autowired
    private SelfGoodsBrandDao selfGoodsBrandDao;
    @Autowired
    private SelfGoodsVirtualJpaRepository goodsVirtualJpaRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private LabelTypeDao labelTypeDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private TbFootprintDao tbFootprintDao;
    @Autowired
    private GoodsAccessoryDao goodsAccessoryDao;
    @Autowired
    private GoodsGiftDao goodsGiftDao;
    @Autowired
    private ScFollowDao scFollowDao;

    /**
     * 后台管理商品列表、创建时间排序
     *
     * @param page   页数
     * @param size   条数
     * @param title  标题：模糊查询
     * @param type   类型
     * @param status 状态
     * @return
     */
    @Override
    public Result findAll(Integer page, Integer size, String title, String type, Long twoTypeId, Long threeTypeId, String status, String isJiFenGoods, String activityId, String notActivityId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Goods> all = goodsRepository.findAll(pageable, title, type, twoTypeId, threeTypeId, status, isJiFenGoods, activityId, notActivityId);
        List<Goods> list = all.getContent();
        for (Goods g : list) {
            if (g.getLabelId() != null) {
                LabelType lt = labelTypeDao.selectById(g.getLabelId());
                g.setLabel(lt.getLabel());
            }
            if (g.getLeaseStart() != null) {
                g.setLeaseStart(g.getLeaseStart() + "天起租");
            }
            if (g.getTypeId() != null) {
                g.setType(goodsJpaTypeRepository.findById(g.getTypeId()).orElse(null));
            }
            if (g.getTwoTypeId() != null) {
                GoodsType gt = goodsJpaTypeRepository.findById(g.getTwoTypeId()).orElse(null);
                if (gt != null) {
                    g.setTwoTypeName(gt.getName());
                }
            }
            if (g.getThreeTypeId() != null) {
                GoodsType gt = goodsJpaTypeRepository.findById(g.getThreeTypeId()).orElse(null);
                if (gt != null) {
                    g.setThreeTypeName(gt.getName());
                }
            }
        }
        return ResultUtil.success(all);
    }

    //虚拟商品列表
    @Override
    public Result goodsVirtualList(Integer page, Integer size, String title, String type, Integer status, Integer isExpress, Integer isJiFenGoods) {
        //按照时间排序
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createAt")));
        //构造自定义查询条件
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(title)) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
                }
                if (StringUtils.isNotEmpty(type)) {
                    predicateList.add(criteriaBuilder.equal(root.get("typeId"), type));
                }
                if (status != 0) {
                    predicateList.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (isExpress != 0) {
                    predicateList.add(criteriaBuilder.equal(root.get("isExpress"), isExpress));
                }
                if (isJiFenGoods != null) {
                    if (isJiFenGoods != 0) { //虚拟商品列表包含积分虚拟商品
                        predicateList.add(criteriaBuilder.equal(root.get("isJiFenGoods"), isJiFenGoods));
                    }
                } else {
                    predicateList.add(criteriaBuilder.isNull(root.get("isJiFenGoods")));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        //处理数据：商品分类
        Page<Goods> all = jpaRepository.findAll(queryCondition, pageable);
        List<SelfGoodsVirtual> virtualList = goodsVirtualJpaRepository.findAll();
        List<Goods> list = all.getContent();
        for (Goods g : list) {
            int sum = 0;
            int count = 0;
            for (SelfGoodsVirtual v : virtualList) {
                if (g.getId().equals(v.getGoodsId())) { //虚拟商品总数库存
                    sum++;
                    if (v.getStatus() == 1) { //未使用的库存
                        count++;
                    }
                }
            }
//            g.setVirtualSum(sum);
//            g.setVirtualCount(count);
        }
        return ResultUtil.success(all);
    }

    /**
     * 用户端商品列表、销量排序
     *
     * @param page  页数
     * @param size  条数
     * @param title 标题：模糊查询
     * @return
     */
    @Override
    public Result findAllByUser(Integer page, Integer size, String type, String brandId, String title, String sort, Integer isJiFenGoods) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, sort);
        if (sort.equals("price")) {
            order = new Sort.Order(Sort.Direction.ASC, sort);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        //构造自定义查询条件
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(type)) {
                    predicateList.add(criteriaBuilder.equal(root.get("typeId"), type));
                }
                if (StringUtils.isNotEmpty(brandId)) {
                    predicateList.add(criteriaBuilder.equal(root.get("brandId"), brandId));
                }
                if (StringUtils.isNotEmpty(title)) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
                }
                if (isJiFenGoods != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("isJiFenGoods"), isJiFenGoods));
                } else {
                    predicateList.add(criteriaBuilder.isNull(root.get("isJiFenGoods")));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        //处理数据：商品分类
        Page<Goods> all = jpaRepository.findAll(queryCondition, pageable);
        List<Goods> list = all.getContent();
        for (Goods g : list) {
            Long id = g.getId();
            /*if (g.getTypeId() != null){
                g.setType(goodsJpaTypeRepository.findById(Long.valueOf(g.getTypeId())).orElse(null));
            }*/
            g.setAttr(goodsAttrService.findByGoodsId(id));
            g.setSku(skuJpaRepository.findAllByGoodsId(id));
            /*if (g.getBrandId() != null){
                g.setBrand(brandJpaRepository.findById(g.getBrandId()).orElse(null));
            }*/
//            if (g.getIsExpress() == 2){ //虚拟商品库存
//                g.setVirtualCount(goodsVirtualJpaRepository.findByGoodsId(g.getId()).size());
//            }
        }
        return ResultUtil.success(all);
    }


    @Override
    public Result updateGoodsActivity(Integer type, Long goodsId, String activityId) {
        Goods goods = jpaRepository.findById(goodsId).orElse(null);
        if (goods != null) {
            if (StringUtils.isNotEmpty(goods.getActivityIds())) {
                String[] split = goods.getActivityIds().split(",");
                List<String> oldStringList = Arrays.asList(split);
                List<String> stringList = new ArrayList<>(oldStringList);
                if (type == 1) {
                    if (!stringList.contains(activityId)) {
                        stringList.add(activityId);
                    }
                } else {
                    if (stringList.contains(activityId)) {
                        stringList.removeIf(activityId::equals);
                    }
                }
                String join = "";
                if (stringList.size() > 0) {
                    join = StringUtils.join(stringList, ",");
                }
                goods.setActivityIds(join);
            } else {
                if (type == 1) {
                    goods.setActivityIds(activityId);
                }
            }
            jpaRepository.save(goods);
        }
        return ResultUtil.success();
    }


    @Override
    public Result searchBrandList(String title) {
        //构造自定义查询条件
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(title)) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<Goods> goodsList = jpaRepository.findAll(queryCondition);
        List<SelfGoodsBrand> brandList = brandJpaRepository.findAll();
        List<SelfGoodsBrand> result = new ArrayList<>();
        for (Goods g : goodsList) {
            if (g.getBrandId() != null) {
                Long brandId = g.getBrandId();
                for (SelfGoodsBrand b : brandList) {
                    if (brandId.equals(b.getBrandId())) {
                        result.add(b);
                        break;
                    }
                }
            }
        }
        //利用list中的元素创建HashSet集合，此时set中进行了去重操作
        HashSet set = new HashSet(result);
        result.clear();
        result.addAll(set);
        return ResultUtil.success(result);
    }

    @Override
    public Result saveBody(Goods entity) {
        entity.setCreateTime(new Date()); //创建时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createAt = sdf.format(new Date());
        entity.setCreateAt(createAt);
        Goods g = jpaRepository.save(entity);
        //处理规格
        List<SelfGoodsAttr> attr = entity.getAttr();
        for (SelfGoodsAttr a : attr) {
            a.setGoodsId(g.getId());
            goodsAttrService.saveBody(a);
        }
        List<SelfGoodsSku> sku = entity.getSku();
        for (SelfGoodsSku v : sku) {
            v.setGoodsId(g.getId());
            skuJpaRepository.save(v);
        }
        return ResultUtil.success(g);
    }

    @Override
    public Result updateBody(Goods entity) {
        //1.修改attr
        if (entity.getAttr() != null && entity.getAttr().size() > 0) {
            SelfGoodsAttr attr = entity.getAttr().get(0);
            attr.setGoodsId(entity.getId());
            goodsAttrService.updateBody(attr);
        } else { //修改为单规格，删除所有attr
            goodsAttrService.deleteAttrAndValue(entity.getId());
        }
        //2.修改所有sku
        List<SelfGoodsSku> allSku = skuJpaRepository.findAllByGoodsId(entity.getId());
        skuJpaRepository.deleteAll(allSku);
        List<SelfGoodsSku> sku = entity.getSku();
        for (SelfGoodsSku v : sku) {
            v.setGoodsId(entity.getId());
            skuJpaRepository.save(v);
        }
        //3.修改商品信息
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        Goods g = jpaRepository.findById(id).orElse(null);
        if (g != null) {
            if (g.getTypeId() != null) {
                g.setType(goodsJpaTypeRepository.findById(g.getTypeId()).orElse(null));
            }
            g.setAttr(goodsAttrService.findByGoodsId(id));
            g.setSku(skuJpaRepository.findAllByGoodsId(id));
            if (g.getBrandId() != null) {
                g.setBrand(brandJpaRepository.findById(g.getBrandId()).orElse(null));
            }
//        if (g.getIsExpress() == 2){ //虚拟商品库存
//            g.setVirtualCount(goodsVirtualJpaRepository.findByGoodsId(g.getId()).size());
//        }
            //复购率
            int sum = ordersRepository.repeatSum(id);
            int count = ordersRepository.repeat(id);
            if (sum != 0) {
                g.setRepeat(String.valueOf(count / sum));
            }
        }
        return ResultUtil.success(g);
    }

    @Override
    public Result findById(Long id, Long userId) {
        Goods g = jpaRepository.findById(id).orElse(null);
        if (g != null && userId == null) {
            if (g.getThreeTypeId() != null) {
                g.setType(goodsJpaTypeRepository.findById(g.getThreeTypeId()).orElse(null));
            }
            if (g.getIsExpress() != null) {
                String[] split = g.getIsExpress().split(",");
                if (split.length > 0) {
                    g.setIsExpressList(Arrays.asList(split));
                }
            }
            g.setAttr(goodsAttrService.findByGoodsId(id));
            g.setSku(skuJpaRepository.findAllByGoodsId(id));
            if (!g.getSku().isEmpty()) {
                SelfGoodsSku sku = g.getSku().get(0);
                g.setDailyRent(sku.getDailyRent());
                g.setMemberRent(sku.getMemberRent());
                g.setOldDegree(sku.getOldDegree());
            }
            if (g.getBrandId() != null) {
                g.setBrand(brandJpaRepository.findById(g.getBrandId()).orElse(null));
            }
            List<GoodsAccessory> goodsAccessoryList = new ArrayList<>();
            List<GoodsGift> goodsGiftList = new ArrayList<>();
            if (g.getActivityIds() != null) {
                String[] accessory = g.getAccessoryIds().split(",");
                for (String s : accessory) {
                    GoodsAccessory goodsAccessory = goodsAccessoryDao.selectById(Long.valueOf(s));
                    GoodsAccessory goodsAccessory1 = new GoodsAccessory();
                    goodsAccessory1.setId(goodsAccessory.getId());
                    goodsAccessory1.setAccessoryName(goodsAccessory.getAccessoryName());
                    goodsAccessory1.setAccessoryNum(goodsAccessory.getAccessoryNum());
                    goodsAccessoryList.add(goodsAccessory1);
                }
                g.setAccessoryList(goodsAccessoryList);
            }
            if (g.getGiftIds() != null) {
                String[] gift = g.getGiftIds().split(",");
                for (String s : gift) {
                    GoodsGift goodsGift = goodsGiftDao.selectById(Long.valueOf(s));
                    GoodsGift goodsGift1 = new GoodsGift();
                    goodsGift1.setId(goodsGift.getId());
                    goodsGift1.setGiftName(goodsGift.getGiftName());
                    goodsGift1.setGiftNum(goodsGift.getGiftNum());
                    goodsGiftList.add(goodsGift1);
                }
                g.setGiftList(goodsGiftList);
            }
            //复购率
            int sum = ordersRepository.repeatSum(id);
            int count = ordersRepository.repeat(id);
            if (sum != 0) {
                g.setRepeat(String.valueOf(count / sum));
            }
            if (g.getDailyRent() == null) {
                g.setDailyRent(0.00);
            }
            if (g.getMemberRent() == null) {
                g.setMemberRent(0.00);
            }
            if (g.getOriginalPrice() == null) {
                g.setOriginalPrice(BigDecimal.ZERO);
            }
            if (g.getPostagePrice() == null) {
                g.setPostagePrice(BigDecimal.ZERO);
            }
            if (g.getDepositMoney() == null) {
                g.setDepositMoney(BigDecimal.ZERO);
            }
            return ResultUtil.success(g);
        } else if (g != null) {
            ScFollow scFollow = scFollowDao.selectOne(new QueryWrapper<ScFollow>().eq("user_id", userId).eq("by_id", id));
            if (scFollow != null) {
                g.setIsCollect(1);
            } else {
                g.setIsCollect(0);
            }
            if (g.getThreeTypeId() != null) {
                g.setType(goodsJpaTypeRepository.findById(g.getThreeTypeId()).orElse(null));
            }
            if (g.getIsExpress() != null) {
                String[] split = g.getIsExpress().split(",");
                if (split.length > 0) {
                    g.setIsExpressList(Arrays.asList(split));
                }
            }
            g.setAttr(goodsAttrService.findByGoodsId(id));
            g.setSku(skuJpaRepository.findAllByGoodsId(id));
            if (!g.getSku().isEmpty()) {
                SelfGoodsSku sku = g.getSku().get(0);
                g.setDailyRent(sku.getDailyRent());
                g.setMemberRent(sku.getMemberRent());
                g.setOldDegree(sku.getOldDegree());
            }
            if (g.getBrandId() != null) {
                g.setBrand(brandJpaRepository.findById(g.getBrandId()).orElse(null));
            }
            List<GoodsAccessory> goodsAccessoryList = new ArrayList<>();
            List<GoodsGift> goodsGiftList = new ArrayList<>();
            if (g.getActivityIds() != null) {
                String[] accessory = g.getAccessoryIds().split(",");
                for (String s : accessory) {
                    GoodsAccessory goodsAccessory = goodsAccessoryDao.selectById(Long.valueOf(s));
                    GoodsAccessory goodsAccessory1 = new GoodsAccessory();
                    goodsAccessory1.setId(goodsAccessory.getId());
                    goodsAccessory1.setAccessoryName(goodsAccessory.getAccessoryName());
                    goodsAccessory1.setAccessoryNum(goodsAccessory.getAccessoryNum());
                    goodsAccessoryList.add(goodsAccessory1);
                }
                g.setAccessoryList(goodsAccessoryList);
            }
            if (g.getGiftIds() != null) {
                String[] gift = g.getGiftIds().split(",");
                for (String s : gift) {
                    GoodsGift goodsGift = goodsGiftDao.selectById(Long.valueOf(s));
                    GoodsGift goodsGift1 = new GoodsGift();
                    goodsGift1.setId(goodsGift.getId());
                    goodsGift1.setGiftName(goodsGift.getGiftName());
                    goodsGift1.setGiftNum(goodsGift.getGiftNum());
                    goodsGiftList.add(goodsGift1);
                }
                g.setGiftList(goodsGiftList);
            }
                TbFootprint footprint = new TbFootprint();
                footprint.setGoodId(id);
                footprint.setUserId(userId);
                footprint.setVisitTime(new Date());
                tbFootprintDao.insert(footprint);
            //复购率
            int sum = ordersRepository.repeatSum(id);
            int count = ordersRepository.repeat(id);
            if (sum != 0) {
                g.setRepeat(String.valueOf(count / sum));
            }
            if (g.getDailyRent() == null) {
                g.setDailyRent(0.00);
            }
            if (g.getMemberRent() == null) {
                g.setMemberRent(0.00);
            }
            if (g.getOriginalPrice() == null) {
                g.setOriginalPrice(BigDecimal.ZERO);
            }
            if (g.getPostagePrice() == null) {
                g.setPostagePrice(BigDecimal.ZERO);
            }
            if (g.getDepositMoney() == null) {
                g.setDepositMoney(BigDecimal.ZERO);
            }
            return ResultUtil.success(g);
        }
        return ResultUtil.error(-1, "该商品不存在，请刷新后重试");
    }


    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public Result selectGoods(Integer page, Integer size, String sort) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, sort);
        if (sort.equals("price")) {
            order = new Sort.Order(Sort.Direction.ASC, sort);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        //构造自定义查询条件
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("isSelect"), 1));
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //上架
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return ResultUtil.success(jpaRepository.findAll(queryCondition, pageable));
    }

    @Override
    public Result selling(Integer page, Integer size, String sort) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, sort);
        if ("price".equals(sort)) {
            order = new Sort.Order(Sort.Direction.ASC, sort);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        //构造自定义查询条件
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.isNull(root.get("isJiFenGoods")));
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //上架
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return ResultUtil.success(jpaRepository.findAll(queryCondition, pageable));
    }

    @Override
    public Result news(Integer page, Integer size, String sort) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, sort);
        if (sort.equals("price")) {
            order = new Sort.Order(Sort.Direction.ASC, sort);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        //构造自定义查询条件
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //上架
                predicateList.add(criteriaBuilder.isNull(root.get("isJiFenGoods"))); //非积分商品
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return ResultUtil.success(jpaRepository.findAll(queryCondition, pageable));
    }

    @Override
    public Result recommend(Integer page, Integer size, String sort) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, sort);
        if (sort.equals("price")) {
            order = new Sort.Order(Sort.Direction.ASC, sort);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        //构造自定义查询条件
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("isRecommend"), 1)); //热爆租品
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //上架
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return ResultUtil.success(jpaRepository.findAll(queryCondition, pageable));
    }

    @Override
    public Result homeGoods(Integer page, Integer size) {
        //按照时间排序
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "sales")));
        //构造自定义查询条件
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("homeGoods"), 1));
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //上架
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return ResultUtil.success(jpaRepository.findAll(queryCondition, pageable));
    }

    @Override
    public Result addSelectGoods(Long id) {
        Goods one = jpaRepository.getOne(id);
        one.setIsSelect(1); //设置为精选好物
        return ResultUtil.success(jpaRepository.save(one));
    }

    @Override
    public Result deleteSelectGoods(String ids) {
        String[] split = ids.split(",");
        for (String id : split) {
            Goods one = jpaRepository.getOne(Long.valueOf(id));
            one.setIsSelect(0); //设置为否
            jpaRepository.save(one);
        }
        return ResultUtil.success();
    }

    @Override
    public Result addRecommend(Long id) {
        Goods one = jpaRepository.getOne(id);
        one.setIsRecommend(1); //设置为每日推荐
        return ResultUtil.success(jpaRepository.save(one));
    }

    @Override
    public Result deleteRecommend(String ids) {
        String[] split = ids.split(",");
        for (String id : split) {
            Goods one = jpaRepository.getOne(Long.valueOf(id));
            one.setIsRecommend(0); //设置为否
            jpaRepository.save(one);
        }
        return ResultUtil.success();
    }

    @Override
    public void salesAdd(Long id, Integer number) {
        Goods one = jpaRepository.findById(id).orElse(null);
        if (one != null) {
            one.setSales(one.getSales() + number);
            jpaRepository.save(one);
        }
    }

    @Override
    public Result isFormatAttr(SelfGoodsAttr goodsAttr, String coverImg, String originalPrice, String price, String memberPrice, String dailyRent, String memberRent, Integer stock, String oldDegree) {
        //1.获取商品规格值
        List<SelfGoodsAttrValue> attr = goodsAttr.getAttrValue();
        int attrSize = attr.size();
        //2.准备返回值
        Map<String, Object> map = new HashMap<>();
        List<String> header = new ArrayList<>(); //表头
        List<Map<String, Object>> value = new ArrayList<>(); //规格排列组合
        //3.sku规格处理集合
        List<List<String>> skuInputList = new ArrayList<>();
        //4.循环规格值
        for (int i = 0; i < attrSize; i++) {
            //5.放入表头数据
            header.add(attr.get(i).getValue());
            //6.sku规格计算
            String detail = attr.get(i).getDetail();
            String[] split = detail.split(",");
            List<String> skuList = new ArrayList<>();
            for (int j = 0; j < split.length; j++) {
                skuList.add(split[j]);
            }
            skuInputList.add(skuList);
        }
        //8.sku规格种类
        List<List<String>> skuList = SkuUtil.skuSort(skuInputList); //计算规格种类
        for (int i = 0; i < skuList.size(); i++) {
            Map<String, Object> sku = new HashMap<>();
            List<String> strings = skuList.get(i);
            String[] arr = strings.toArray(new String[strings.size()]);
            String json = "";
            for (int j = 0; j < attrSize; j++) {
                String arrString = arr[j];
                sku.put("value" + j, arrString);
                json += arrString + ",";
            }
            json = json.substring(0, json.length() - 1);
            sku.put("json", json);
            sku.put("detailJson", json);
            sku.put("skuImg", coverImg);
            sku.put("skuOriginalPrice", originalPrice);
            sku.put("skuPrice", price);
            sku.put("memberPrice", memberPrice);
            sku.put("dailyRent", dailyRent);
            sku.put("memberRent", memberRent);
            sku.put("stock", 999);
            sku.put("oldDegree", oldDegree);
            sku.put("sales", 0);
            value.add(sku);
        }
        //表头
        header.add("图片");
        if (StringUtils.isNotEmpty(originalPrice)) {
            header.add("原价");
        }
        header.add("售价");
        if (StringUtils.isNotEmpty(memberPrice)) {
            header.add("会员价");
        }
        header.add("日租价");
        header.add("会员日租价");
        header.add("库存");
        header.add("新旧程度");
        header.add("销量");
        header.add("操作");
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return ResultUtil.success(map);
    }

    @Override
    public Result onlyFormatAttr(String coverImg, String originalPrice, String price, String memberPrice,
                                 String dailyRent, String memberRent, Integer stock, String oldDegree) {
        Map<String, Object> map = new HashMap<>();
        List<String> header = new ArrayList<>(); //表头
        //表头
        header.add("图片");
        if (StringUtils.isNotEmpty(originalPrice)) {
            header.add("原价");
        }
        header.add("售价");
        header.add("会员价");
        header.add("日租价");
        header.add("会员日租价");
        header.add("库存");
        header.add("新旧程度");
        header.add("销量");
        List<Map<String, Object>> value = new ArrayList<>(); //规格排列组合
        Map<String, Object> sku = new HashMap<>();
        sku.put("json", null);
        sku.put("detailJson", null);
        sku.put("skuImg", coverImg);
        sku.put("skuOriginalPrice", originalPrice);
        sku.put("skuPrice", price);
        sku.put("memberPrice", memberPrice);
        sku.put("dailyRent", dailyRent);
        sku.put("memberRent", memberRent);
        sku.put("stock", 999);
        sku.put("oldDegree", oldDegree);
        sku.put("sales", 0);
        value.add(sku);
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return ResultUtil.success(map);
    }

    /**
     * 回显sku
     *
     * @param goodsId
     * @return
     */
    @Override
    public Result formatAttr(Long goodsId) {
        //1.获取商品规格值
        List<SelfGoodsAttrValue> attr = attrValueJpaRepository.findAllByGoodsId(goodsId);
        //sku集合
        List<SelfGoodsSku> goodsSkuList = skuJpaRepository.findAllByGoodsId(goodsId);
        int attrSize = attr.size();
        //2.准备返回值
        Map<String, Object> map = new HashMap<>();
        List<String> header = new ArrayList<>(); //表头
        List<Map<String, Object>> value = new ArrayList<>(); //规格排列组合
        if (attrSize > 0) {
            //4.循环规格值
            for (int i = 0; i < attrSize; i++) {
                //5.放入表头数据
                header.add(attr.get(i).getValue());
            }
        }
        Goods goods = jpaRepository.findById(goodsId).orElse(null);
        if (goods.getIsJiFenGoods() == null) { //普通商品回显表头
            //表头
            header.add("图片");
            header.add("原价");
            header.add("售价");
            header.add("会员价");
            header.add("日租价");
            header.add("会员日租价");
            header.add("库存");
            header.add("新旧程度");
            header.add("销量");
            header.add("操作");
        } else { //积分商品回显表头
            //表头
            header.add("图片");
            header.add("原价");
            header.add("售价");
            header.add("会员价");
            header.add("日租价");
            header.add("会员日租价");
            header.add("库存");
            header.add("新旧程度");
            header.add("销量");
            header.add("操作");
        }
        //5.sku数据放入
        for (SelfGoodsSku s : goodsSkuList) {
            Map<String, Object> sku = new HashMap<>();
            String detailJson = s.getDetailJson();
            if (detailJson != null) {
                String[] split = detailJson.split(",");
                for (int i = 0; i < split.length; i++) {
                    sku.put("value" + i, split[i]);
                }
            }
            if (goods.getIsJiFenGoods() == null) {
                sku.put("detailJson", detailJson);
                sku.put("skuImg", s.getSkuImg());
                sku.put("skuOriginalPrice", s.getSkuOriginalPrice());
                sku.put("skuPrice", s.getSkuPrice());
                sku.put("memberPrice", s.getMemberPrice());
                sku.put("dailyRent", s.getDailyRent());
                sku.put("memberRent", s.getMemberRent());
                sku.put("stock", s.getStock());
                sku.put("oldDegree", s.getOldDegree());
                sku.put("sales", s.getSales());

            } else {
                sku.put("detailJson", detailJson);
                sku.put("skuImg", s.getSkuImg());
                sku.put("skuOriginalPrice", s.getSkuOriginalPrice());
                sku.put("skuPrice", s.getSkuPrice());
                sku.put("memberPrice", s.getMemberPrice());
                sku.put("dailyRent", s.getDailyRent());
                sku.put("memberRent", s.getMemberRent());
                sku.put("stock", s.getStock());
                sku.put("oldDegree", s.getOldDegree());
                sku.put("sales", s.getSales());

            }
            value.add(sku);
        }
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return ResultUtil.success(map);
    }

    @Override
    public Result findAttrValue(Long goodsId) {
        List<SelfGoodsAttr> attrList = attrJpaRepository.findAllByGoodsId(goodsId);
        SelfGoodsAttr s = new SelfGoodsAttr();
        if (attrList.size() > 0 && attrList != null) {
            s = attrList.get(0);
            s.setAttrValue(attrValueJpaRepository.findAllByGoodsId(goodsId));
        }
        return ResultUtil.success(s);
    }

    @Override
    public Result updateStatus(Long goodsId) {
        Goods g = jpaRepository.findById(goodsId).orElse(null);
        if (g.getStatus() == 1) {
            g.setStatus(2);
        } else {
            g.setStatus(1);
        }
        return ResultUtil.success(jpaRepository.save(g));
    }

    @Override
    public Result goodsInfoByBrand(Integer page, Integer size, Long typeId, Long brandId, Integer type, String title) {
        IPage<Goods> pages;
        if (page != null && size != null) {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        } else {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>()
                .orderByDesc("create_at");
        if (typeId != null) {
            queryWrapper.eq("type_id", typeId);
        }
        if (brandId != null) {
            queryWrapper.eq("brand_id", brandId);
        }
        IPage<Goods> all = goodsDao.selectGoodsByBrand(pages, typeId, brandId, title);
        for (Goods goods : all.getRecords()) {
            Long id = goods.getId();
            goods.setAttr(goodsAttrService.findByGoodsId(id));
            goods.setSku(skuJpaRepository.findAllByGoodsId(id));
            if (!goods.getSku().isEmpty()) {
                SelfGoodsSku sku = goods.getSku().get(0);
                goods.setDailyRent(sku.getDailyRent());
                goods.setMemberRent(sku.getMemberRent());
                goods.setOldDegree(sku.getOldDegree());
            }
        }
        if (type != null) {
            Comparator<Goods> comparator = null;
            if (type == 1) {
                comparator = Comparator.comparing(Goods::getDailyRent, Comparator.nullsLast(Double::compareTo));
            } else if (type == 2) {
                comparator = Comparator.comparing(Goods::getDailyRent, Comparator.nullsLast(Double::compareTo)).reversed();
            }
            Collections.sort(all.getRecords(), comparator);
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result selectByLabelType(Integer page, Integer size) {
        IPage<LabelType> pages;
        if (page != null && size != null) {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        } else {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
            pages.setSize(-1);
        }
        IPage<LabelType> labelTypeList = labelTypeDao.selectPage(pages, new QueryWrapper<>());
        if (labelTypeList != null) {
            return ResultUtil.success(labelTypeList);
        }
        return ResultUtil.error(-1, "暂无标签");
    }

    @Override
    public Result goodListByLabelId(Integer page, Integer size, Long labelId) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Goods> queryCondition = new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (labelId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("labelId"), labelId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        //处理数据：商品分类
        Page<Goods> all = jpaRepository.findAll(queryCondition, pageable);
        for (Goods goods : all.getContent()) {
            Long id = goods.getId();
            goods.setAttr(goodsAttrService.findByGoodsId(id));
            goods.setSku(skuJpaRepository.findAllByGoodsId(id));
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result selectedGood(Integer page, Integer size, Long labelId) {
        IPage<Goods> pages;
        if (page != null && size != null) {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        } else {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>()
                .eq("is_recommend", 1)
                .orderByDesc("create_at");
        if (labelId != null) {
            queryWrapper.eq("label_id", labelId);
        }
        IPage<Goods> all = goodsDao.selectPage(pages, queryWrapper);
        //处理数据：商品分类
        for (Goods goods : all.getRecords()) {
            Long id = goods.getId();
            goods.setAttr(goodsAttrService.findByGoodsId(id));
            goods.setSku(skuJpaRepository.findAllByGoodsId(id));
            SelfGoodsSku sku = goods.getSku().get(0);
            goods.setDailyRent(sku.getDailyRent());
            goods.setMemberRent(sku.getMemberRent());
            goods.setOldDegree(sku.getOldDegree());
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result selectedRecommendations(Integer page, Integer size, Long typeId, Integer type, String title) {
        IPage<Goods> pages;
        if (page != null && size != null) {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        } else {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>()
                .eq("is_select", 1)
                .orderByDesc("create_at");
        if (typeId != null) {
            queryWrapper.eq("type_id", typeId);
        }
        IPage<Goods> all = goodsDao.selectGoodsList(pages, typeId, title);
        for (Goods goods : all.getRecords()) {
            Long id = goods.getId();
            goods.setAttr(goodsAttrService.findByGoodsId(id));
            goods.setSku(skuJpaRepository.findAllByGoodsId(id));
            if (!goods.getSku().isEmpty()) {
                SelfGoodsSku sku = goods.getSku().get(0);
                goods.setDailyRent(sku.getDailyRent());
                goods.setMemberRent(sku.getMemberRent());
                goods.setOldDegree(sku.getOldDegree());
            }
        }
        if (type != null) {
            Comparator<Goods> comparator = null;
            if (type == 1) {
                comparator = Comparator.comparing(Goods::getDailyRent, Comparator.nullsLast(Double::compareTo));
            } else if (type == 2) {
                comparator = Comparator.comparing(Goods::getDailyRent, Comparator.nullsLast(Double::compareTo)).reversed();
            }
            Collections.sort(all.getRecords(), comparator);
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result goodsListByBrandId(Integer page, Integer size, String title, Long brandId, Integer type, Long threeTypeId) {
        IPage<Goods> pages;
        if (page != null && size != null) {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        } else {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
            pages.setSize(-1);
        }

        IPage<Goods> all = goodsDao.selectPageByList(pages, threeTypeId, title, brandId, type);

        // 设置合并后的记录到分页对象中
//        all.setRecords(finalGoodsList);
        for (Goods goods : all.getRecords()) {
            Long id = goods.getId();
            goods.setAttr(goodsAttrService.findByGoodsId(id));
            goods.setSku(skuJpaRepository.findAllByGoodsId(id));
            if (!goods.getSku().isEmpty()) {
                SelfGoodsSku sku = goods.getSku().get(0);
                goods.setDailyRent(sku.getDailyRent());
                goods.setMemberRent(sku.getMemberRent());
                goods.setOldDegree(sku.getOldDegree());
            }
        }
        return ResultUtil.success(all);
    }
}
