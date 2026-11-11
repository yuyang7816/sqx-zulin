package com.sqx.domain.repository.service.selfGoodsComment;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.sys.Goods;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.entity.sys.SelfGoodsComment;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsComment.SelfGoodsCommentJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsComment.SelfGoodsCommentRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersJpaRepository;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SelfGoodsCommentServiceImpl implements SelfGoodsCommentService {
    @Autowired
    private SelfGoodsCommentJpaRepository jpaRepository;
    @Autowired
    private OrdersJpaRepository ordersJpaRepository;
    @Autowired
    private GoodsJpaRepository goodsJpaRepository;
    @Autowired
    private SelfGoodsCommentRepository selfGoodsCommentRepository;

    @Override
    public Result findAll(Integer page, Integer size, Long goodsId, Integer scoreType, String title, String userName, String phone, String content) {
        Pageable pageable = PageRequest.of(page, size);
        /*//构造自定义查询条件
        Specification<SelfGoodsComment> queryCondition = new Specification<SelfGoodsComment>() {
            @Override
            public Predicate toPredicate(Root<SelfGoodsComment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("goodsId"), goodsId));
                if (scoreType != 0){
                    predicateList.add(criteriaBuilder.equal(root.get("scoreType"), scoreType));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfGoodsComment> pageList = jpaRepository.findAll(queryCondition, pageable);*/
        Page<SelfGoodsComment> pageList = selfGoodsCommentRepository.selectSelfGoodsCommentList(pageable, goodsId, scoreType, title, userName,phone,content);
        List<SelfGoodsComment> list = pageList.getContent();
        for(SelfGoodsComment selfGoodsComment:list){
            Goods goods = goodsJpaRepository.findById(selfGoodsComment.getGoodsId()).orElse(null);
            selfGoodsComment.setGoods(goods);
        }
        return ResultUtil.success(pageList);
    }

    @Override
    public Result count(Long goodsId) {
        List<SelfGoodsComment> list = jpaRepository.findAllByGoodsId(goodsId);
        Map<String, Object> map = new HashMap<>();
        int count = list.size(); //个数
        int sum = 0; //总分数
        String count1 = ""; //平均分
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        for (SelfGoodsComment s : list) {
            Integer score = s.getScoreType();
            sum += s.getScore(); //总分数
            switch (score){
                case 1 : count2++; break;
                case 2 : count3++; break;
                case 3 : count4++; break;
            }
        }
        DecimalFormat dF = new DecimalFormat("0.0");
        count1 = dF.format((float)sum/count); //计算平均分
        map.put("count", count); //总条数
        map.put("count1", count1); //平均分
        map.put("count2", count2); //好评个数
        map.put("count3", count3); //中评个数
        map.put("count4", count4); //差评个数
        return ResultUtil.success(map);
    }

    @Override
    public Result userList(Integer page, Integer size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        //构造自定义查询条件
        Specification<SelfGoodsComment> queryCondition = new Specification<SelfGoodsComment>() {
            @Override
            public Predicate toPredicate(Root<SelfGoodsComment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfGoodsComment> pageList = jpaRepository.findAll(queryCondition, pageable);
        return ResultUtil.success(pageList);
    }

    @Override
    public Result findByOrderId(Long orderId) {
        return ResultUtil.success(jpaRepository.findByOrderId(orderId));
    }

    @Override
    public Result saveBody(SelfGoodsComment entity) {
        SelfGoodsComment c = jpaRepository.findByOrderId(entity.getOrderId());
        if (c != null && c.getCommentId()!=null){
            ResultUtil.error(-1, "已经评价了");
        }
        //更新订单状态
        Orders o = ordersJpaRepository.findById(entity.getOrderId()).orElse(null);
//        o.setPayStatus(10); //10已评价
        ordersJpaRepository.save(o);
        //保存评论
        entity.setCreateTime(DateUtils.createTime()); //创建时间
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfGoodsComment entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public Result reply(Long commentId, String reply) {
        SelfGoodsComment s = jpaRepository.findById(commentId).orElse(null);
        s.setReply(reply);
        return ResultUtil.success(jpaRepository.save(s));
    }
}
