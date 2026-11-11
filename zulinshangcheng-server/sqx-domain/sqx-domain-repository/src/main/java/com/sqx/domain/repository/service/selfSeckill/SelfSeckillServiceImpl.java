package com.sqx.domain.repository.service.selfSeckill;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.common.core.utils.SkuUtil;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.selfGroupSku.SelfGroupSkuJpaRepository;
import com.sqx.domain.repository.mapper.selfSeckill.SelfSeckillJpaRepository;
import com.sqx.domain.repository.service.selfGoods.GoodsService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SelfSeckillServiceImpl implements SelfSeckillService {
    @Autowired
    private SelfSeckillJpaRepository jpaRepository;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SelfGroupSkuJpaRepository selfGroupSkuJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size, String title) {
        //根据时间排序
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        //构造自定义查询条件
        Specification<SelfSecKill> queryCondition = new Specification<SelfSecKill>() {
            @Override
            public Predicate toPredicate(Root<SelfSecKill> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(title)) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+title+"%"));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfSecKill> all = jpaRepository.findAll(queryCondition, pageable);
        for (SelfSecKill s : all.getContent()) {
            Goods one = (Goods)goodsService.findOne(s.getGoodsId()).getData();
            s.setGoods(one);
            if (s.getStatus() == 1){
                String string = DateUtils.secKillTime(s.getStartTime(), s.getEndTime());
                s.setStatusString(string);
            }else {
                s.setStatusString("已关闭");
            }

        }
        return ResultUtil.success(all);
    }

    @Override
    public Result userList(Integer page, Integer size, Long secKillTimeId) {
        Pageable pageable = PageRequest.of(page, size);
        //构造自定义查询条件
        Specification<SelfSecKill> queryCondition = new Specification<SelfSecKill>() {
            @Override
            public Predicate toPredicate(Root<SelfSecKill> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //上架
                predicateList.add(criteriaBuilder.equal(root.get("secKillTimeId"), secKillTimeId)); //时段
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfSecKill> all = jpaRepository.findAll(queryCondition, pageable);
        for (SelfSecKill s : all.getContent()) {
            Goods one = (Goods)goodsService.findOne(s.getGoodsId()).getData();
            s.setGoods(one);
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result saveBody(SelfSecKill entity) {
        entity.setCreateTime(DateUtils.createTime());
        SelfSecKill save = jpaRepository.save(entity);
        //保存拼团sku
        List<SelfGroupSku> sku = entity.getSku();
        for (SelfGroupSku s : sku) {
            s.setGoodsId(entity.getGoodsId());
            s.setSecKillId(save.getSecKillId());
        }
        selfGroupSkuJpaRepository.saveAll(sku);
        return ResultUtil.success(save);
    }

    @Override
    public Result updateBody(SelfSecKill entity) {
        selfGroupSkuJpaRepository.saveAll(entity.getSku());
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        SelfSecKill o = jpaRepository.findById(id).orElse(null);
        o.setGoods((Goods)goodsService.findOne(o.getGoodsId()).getData());
        o.setSku(selfGroupSkuJpaRepository.findAllBySecKillId(o.getSecKillId()));
        return ResultUtil.success(o);
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        List<SelfGroupSku> sku = selfGroupSkuJpaRepository.findAllByGroupId(id);
        selfGroupSkuJpaRepository.deleteAll(sku);
        return ResultUtil.success();
    }

    @Override
    public Result updateStatus(Long id) {
        SelfSecKill o = jpaRepository.findById(id).orElse(null);
        if (o.getStatus() == 1){
            o.setStatus(2);
        }else {
            o.setStatus(1);
        }
        return ResultUtil.success(jpaRepository.save(o));
    }

    @Override
    public Result isFormatSku(Long goodsId) {
        //1.获取商品规格值
        Goods goods = (Goods)goodsService.findOne(goodsId).getData();
        List<SelfGoodsAttrValue> attr = goods.getAttr().get(0).getAttrValue();
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
        List<SelfGoodsSku> goodsSkuList = goods.getSku();
        for (int i = 0; i < goodsSkuList.size(); i++) {
            Map<String, Object> sku = new HashMap<>();
            List<String> strings = skuList.get(i);
            String[] arr = strings.toArray(new String[strings.size()]);
            String json = "";
            for (int j = 0; j < attrSize; j++) {
                String arrString = arr[j];
                sku.put("value"+j, arrString);
                json += arrString+",";
            }
            SelfGoodsSku goodsSku = goodsSkuList.get(i);
            json = json.substring(0, json.length()-1);
            sku.put("json", json);
            sku.put("detailJson", json);
            sku.put("skuImg", goodsSku.getSkuImg());
            sku.put("skuOriginalPrice", goodsSku.getSkuOriginalPrice());
            sku.put("skuPrice", goodsSku.getSkuPrice());
            sku.put("memberPrice", goodsSku.getMemberPrice());
            sku.put("stock", goodsSku.getStock());
            sku.put("sales", goodsSku.getSales());
            sku.put("secKillPrice", goodsSku.getSkuPrice());
            sku.put("secKillStock", goodsSku.getStock());
            value.add(sku);
        }
        //表头
        header.add("图片");
        header.add("原价");
        header.add("售价");
        header.add("会员价");
        header.add("库存");
        header.add("销量");
        header.add("秒杀价格");
        header.add("秒杀库存");
        header.add("操作");
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return ResultUtil.success(map);
    }

    @Override
    public Result onlyFormatSku(Long goodsId) {
        Goods goods = (Goods)goodsService.findOne(goodsId).getData();
        SelfGoodsSku goodsSku = goods.getSku().get(0);
        Map<String, Object> map = new HashMap<>();
        List<String> header = new ArrayList<>(); //表头
        //表头
        header.add("图片");
        header.add("原价");
        header.add("售价");
        header.add("会员价");
        header.add("库存");
        header.add("销量");
        header.add("秒杀价格");
        header.add("秒杀库存");
        List<Map<String, Object>> value = new ArrayList<>(); //规格排列组合
        Map<String, Object> sku = new HashMap<>();
        sku.put("json", null);
        sku.put("detailJson", null);
        sku.put("skuImg", goodsSku.getSkuImg());
        sku.put("skuOriginalPrice", goodsSku.getSkuOriginalPrice());
        sku.put("skuPrice", goodsSku.getSkuPrice());
        sku.put("memberPrice", goodsSku.getMemberPrice());
        sku.put("stock", goodsSku.getStock());
        sku.put("sales", goodsSku.getSales());
        sku.put("secKillPrice", goodsSku.getSkuPrice());
        sku.put("secKillStock", goodsSku.getStock());
        value.add(sku);
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return ResultUtil.success(map);
    }

    @Override
    public Result showSku(Long secKillId) {
        //1.准备返回值
        Map<String, Object> map = new HashMap<>();
        List<String> header = new ArrayList<>(); //表头
        List<Map<String, Object>> value = new ArrayList<>(); //规格排列组合
        //2.sku集合
        List<SelfGroupSku> skuList = selfGroupSkuJpaRepository.findAllBySecKillId(secKillId);
        SelfGroupSku selfGroupSku = skuList.get(0);
        if (StringUtils.isNotEmpty(selfGroupSku.getDetailJson())){
            String[] attr = selfGroupSku.getDetailJson().split(",");
            int attrSize = attr.length;
            if (attrSize > 0){
                //4.循环规格值
                for (int i = 0; i < attrSize; i++) {
                    //5.放入表头数据
                    header.add("属性"+(i+1));
                }
            }
        }
        //表头
        header.add("图片");
        header.add("原价");
        header.add("售价");
        header.add("会员价");
        header.add("库存");
        header.add("销量");
        header.add("秒杀价格");
        header.add("秒杀库存");
        //5.sku数据放入
        for (SelfGroupSku s : skuList) {
            Map<String, Object> sku = new HashMap<>();
            String detailJson = s.getDetailJson();
            if (detailJson != null){
                String[] split = detailJson.split(",");
                for (int i = 0; i < split.length; i++) {
                    sku.put("value"+i, split[i]);
                }
            }
            sku.put("detailJson", detailJson);
            sku.put("skuOriginalPrice", s.getSkuOriginalPrice());
            sku.put("skuPrice", s.getSkuPrice());
            sku.put("memberPrice", s.getMemberPrice());
            sku.put("stock", s.getStock());
            sku.put("sales", s.getSales());
            sku.put("skuImg", s.getSkuImg());
            sku.put("secKillPrice", s.getSecKillPrice());
            sku.put("secKillStock", s.getSecKillStock());
            sku.put("id", s.getId());
            sku.put("secKillId", s.getSecKillId());
            sku.put("goodsId", s.getGoodsId());
            value.add(sku);
        }
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return ResultUtil.success(map);
    }
}
