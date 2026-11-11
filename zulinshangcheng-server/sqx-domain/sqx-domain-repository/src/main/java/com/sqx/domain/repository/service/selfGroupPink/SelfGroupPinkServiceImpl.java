package com.sqx.domain.repository.service.selfGroupPink;

import cn.hutool.system.UserInfo;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.TbOrders;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.selfGroup.SelfGroupJpaRepository;
import com.sqx.domain.repository.mapper.selfGroupPink.SelfGroupPinkJpaRepository;
import com.sqx.domain.repository.mapper.selfGroupPinkUser.SelfGroupPinkUserJpaRepository;
import com.sqx.domain.repository.mapper.selfGroupSku.SelfGroupSkuJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersJpaRepository;
import com.sqx.domain.repository.mapper.userinfo.UserJpaRepository;
import com.sqx.domain.repository.service.sys.MessageService;
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
import java.util.List;

@Service
public class SelfGroupPinkServiceImpl implements SelfGroupPinkService {
    @Autowired
    private SelfGroupPinkJpaRepository jpaRepository;
    @Autowired
    private SelfGroupJpaRepository selfGroupJpaRepository;
    @Autowired
    private OrdersJpaRepository ordersJpaRepository;
    @Autowired
    private SelfGroupPinkUserJpaRepository selfGroupPinkUserJpaRepository;
    @Autowired
    private SelfGroupSkuJpaRepository groupSkuJpaRepository;
    @Autowired
    private SelfGroupPinkUserJpaRepository pinkUserJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private SelfGroupJpaRepository groupJpaRepository;
    @Autowired
    private MessageService iMessageService;

    @Override
    public Result findAll(Integer page, Integer size, Integer status) {
        //根据时间排序
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        //构造自定义查询条件
        Specification<SelfGroupPink> queryCondition = new Specification<SelfGroupPink>() {
            @Override
            public Predicate toPredicate(Root<SelfGroupPink> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (status != 0) {
                    predicateList.add(criteriaBuilder.equal(root.get("status"), status));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfGroupPink> all = jpaRepository.findAll(queryCondition, pageable);
        List<SelfGroup> allGroup = selfGroupJpaRepository.findAll();
        List<SelfGroupPinkUser> allUser = selfGroupPinkUserJpaRepository.findAll();
        List<SelfGroupPinkUser> userList = new ArrayList<>();
        for (SelfGroupPink s : all.getContent()) {
            for (SelfGroup g : allGroup) {
                if (s.getGroupId().equals(g.getId())){
                    s.setSelfGroup(g); //拼团商品信息
                }
            }
            for (SelfGroupPinkUser u : allUser) {
                if (s.getId().equals(u.getGroupPinkId())){
                    userList.add(u);
                }
            }
            s.setPinkUserList(userList); //本团的用户列表
        }
        return ResultUtil.success(all);
    }

    @Override
    public Result findOne(Long id) {
        SelfGroupPink s = jpaRepository.findById(id).orElse(null);
        SelfGroup g = selfGroupJpaRepository.findById(s.getGroupId()).orElse(null);
        s.setSelfGroup(g);
        List<SelfGroupPinkUser> userList = selfGroupPinkUserJpaRepository.findAllByGroupPinkId(id);
        s.setPinkUserList(userList);
        return ResultUtil.success(s);
    }


    /**
     * 发起拼团
     * @param o
     * @return
     */
    @Override
    public Result createPink(Orders o) {
        SelfGroupPink p = new SelfGroupPink();
        p.setCreateTime(DateUtils.createTime()); //创建时间
        p.setStartTime(DateUtils.createTime()); //开始时间
        p.setGroupId(o.getGroupId()); //拼团规则
        //拼团规格
        SelfGroup group = selfGroupJpaRepository.getOne(o.getGroupId());
        p.setEndTime(DateUtils.endTime(group.getHours())); //结束时间
        p.setNeedNumber(group.getNumber()); //需要人数
        p.setCountNumber(1); //当前人数
        p.setStatus(1); //拼团状态（1拼团中 2已完成 3未完成）
        //保存发起的拼团
        SelfGroupPink save = jpaRepository.save(p);
        //保存订单中团队id
        o.setGroupPinkId(save.getId());
        ordersJpaRepository.save(o);
        //存储拼团用户信息
        SelfGroupPinkUser pinkUser = new SelfGroupPinkUser();
        UserEntity user = userJpaRepository.getOne(o.getUserId());
        pinkUser.setGroupPinkId(save.getId());
        pinkUser.setCreateTime(DateUtils.createTime());
        //用户信息存储：头像、昵称、用户id、手机号
        pinkUser.setHeaderImg(user.getAvatar());
        pinkUser.setNickName(user.getUserName());
        pinkUser.setUserId(user.getUserId());
        pinkUser.setMobile(user.getPhone());
        pinkUser.setOrderId(o.getId()); //订单id
        pinkUserJpaRepository.save(pinkUser);
        //判断拼团是否成功：满团；特殊情况一人团
        if (save.getNeedNumber() == 1){
            //1.修改拼团为已完成
            save.setStatus(2); //拼团状态（1拼团中 2已完成 3未完成）
            jpaRepository.save(save);
            //拼团商品销量+1
            SelfGroup g = groupJpaRepository.findById(save.getGroupId()).orElse(null);
            g.setGroupNumber(g.getGroupNumber()+1);
            groupJpaRepository.save(g);
            //2.修改对应订单为代发货
            List<SelfGroupPinkUser> userList = pinkUserJpaRepository.findAllByGroupPinkId(save.getId());
            for (SelfGroupPinkUser s : userList) {
                Orders order = ordersJpaRepository.getOne(s.getOrderId());
                order.setStatus(2); //订单状态为代发货
                ordersJpaRepository.save(order);
                //订单消息
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setState("4");
                messageInfo.setTitle("拼团成功提示");
                messageInfo.setContent("您购买的"+order.getTitle()+"已经拼团成功，订单编号："+order.getOrderNum());
                messageInfo.setUserName(s.getNickName());
                messageInfo.setUserId(s.toString());
                iMessageService.saveBody(messageInfo);
            }
        }
        return ResultUtil.success();
    }

    /**
     * 加入拼团
     * @param o
     * @return
     */
    @Override
    public Result joinPink(Orders o) {
        SelfGroupPink p = jpaRepository.getOne(o.getGroupPinkId());
        if (p.getStatus() != 1){
            return ResultUtil.error(-1, "团已拼成");
        }
        p.setCountNumber(p.getCountNumber()+1); //当前人数
        p.setStatus(1); //拼团状态（1拼团中 2已完成 3未完成）
        //保存发起的拼团
        SelfGroupPink save = jpaRepository.save(p);
        //存储拼团用户信息
        SelfGroupPinkUser pinkUser = new SelfGroupPinkUser();
        UserEntity user = userJpaRepository.getOne(o.getUserId());
        pinkUser.setGroupPinkId(save.getId());
        pinkUser.setCreateTime(DateUtils.createTime());
        //用户信息存储：头像、昵称、用户id、手机号
        pinkUser.setHeaderImg(user.getAvatar());
        pinkUser.setNickName(user.getUserName());
        pinkUser.setUserId(user.getUserId());
        pinkUser.setMobile(user.getPhone());
        pinkUser.setOrderId(o.getId()); //订单id
        pinkUserJpaRepository.save(pinkUser);
        //判断拼团是否成功：满团
        if (save.getNeedNumber().equals(save.getCountNumber())){
            //1.修改拼团为已完成
            save.setStatus(2); //拼团状态（1拼团中 2已完成 3未完成）
            jpaRepository.save(save);
            //拼团商品销量+1
            SelfGroup g = groupJpaRepository.findById(save.getGroupId()).orElse(null);
            g.setGroupNumber(g.getGroupNumber()+1);
            groupJpaRepository.save(g);
            //2.修改对应订单为代发货
            List<SelfGroupPinkUser> userList = pinkUserJpaRepository.findAllByGroupPinkId(save.getId());
            for (SelfGroupPinkUser s : userList) {
                Orders order = ordersJpaRepository.getOne(s.getOrderId());
                order.setStatus(2); //订单状态为代发货
                ordersJpaRepository.save(order);
                //订单消息
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setState(String.valueOf(5));
                messageInfo.setTitle("拼团成功提示");
                messageInfo.setContent("您购买的"+order.getTitle()+"已经拼团成功，订单编号："+order.getOrderNum());
                messageInfo.setUserName(s.getNickName());
                messageInfo.setUserId(s.getUserId().toString());
                iMessageService.saveBody(messageInfo);
            }
        }
        return ResultUtil.success();
    }

    @Override
    public Result findByGroupId(Long groupId) {
        //可拼的团
        List<SelfGroupPink> allPink = jpaRepository.findAllByGroupIdAndStatus(groupId, 1);
        //拼团用户
        List<SelfGroupPinkUser> allUser = selfGroupPinkUserJpaRepository.findAll();
        for (SelfGroupPink s : allPink) {
            List<SelfGroupPinkUser> userList = new ArrayList<>();
            for (SelfGroupPinkUser u : allUser) {
                if (s.getId().equals(u.getGroupPinkId())){
                    userList.add(u);
                }
            }
            s.setPinkUserList(userList); //本团的用户列表
        }
        return ResultUtil.success(allPink);
    }

    @Override
    public boolean checkPinkStock(Long skuId, Integer payNumber) {
        boolean flag = true;
        SelfGroupSku sku = groupSkuJpaRepository.findById(skuId).orElse(null);
        if (sku.getPinkStock().intValue() < payNumber){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean checkSkillStock(Long skuId, Integer payNumber) {
        boolean flag = true;
        SelfGroupSku sku = groupSkuJpaRepository.findById(skuId).orElse(null);
        if (sku!=null && sku.getSecKillStock() < payNumber){
            flag = false;
        }
        return flag;
    }

    @Override
    public Result updateBody(SelfGroupPink entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }
}
