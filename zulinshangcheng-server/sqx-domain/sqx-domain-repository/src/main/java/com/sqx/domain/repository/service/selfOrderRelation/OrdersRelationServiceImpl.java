package com.sqx.domain.repository.service.selfOrderRelation;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.AmountCalUtils;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.MoneyDetails;
import com.sqx.domain.repository.entity.sys.Orders;
import com.sqx.domain.repository.entity.sys.SelfOrderRelation;
import com.sqx.domain.repository.entity.sys.invitationTotal;
import com.sqx.domain.repository.mapper.money.MoneyDetailsJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersJpaRepository;
import com.sqx.domain.repository.mapper.selfOrderRelation.OrdersRelationJpaRepository;
import com.sqx.domain.repository.mapper.selfOrderRelation.OrdersRelationRepository;
import com.sqx.domain.repository.mapper.userinfo.UserJpaRepository;
import com.sqx.domain.repository.mapper.userinfo.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrdersRelationServiceImpl implements OrdersRelationService {
    @Autowired
    private OrdersRelationJpaRepository jpaRepository;
    @Autowired
    private OrdersRelationRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private MoneyDetailsJpaRepository moneyDetailsJpaRepository;
    @Autowired
    private OrdersJpaRepository ordersJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size, Long userId, String phone, Integer status, Integer moneyFrom) {
        //根据时间排序
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createAt")));
        // 构造自定义查询条件
        Specification<SelfOrderRelation> queryCondition = new Specification<SelfOrderRelation>() {
            @Override
            public Predicate toPredicate(Root<SelfOrderRelation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (userId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if (StringUtils.isNotEmpty(phone)) {
                    predicateList.add(criteriaBuilder.equal(root.get("phone"), phone));
                }
                if (status != 0) {
                    predicateList.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (moneyFrom != 0) {
                    predicateList.add(criteriaBuilder.equal(root.get("moneyFrom"), moneyFrom));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return ResultUtil.success(jpaRepository.findAll(queryCondition, pageable));
    }

    /**
     * 保存订单
     * @param entity
     * @return
     */
    @Override
    public Result saveBody(SelfOrderRelation entity) {
        entity.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); //创建时间
        SelfOrderRelation save = jpaRepository.save(entity);
        //虚拟商品、核销商品分销立即到账
        Orders o = ordersJpaRepository.findById(save.getOrderId()).orElse(null);
        if (o.getIsExpress() != 1){
            this.addMoney(save);
        }
        return ResultUtil.success(save);
    }

    @Override
    public Result updateBody(SelfOrderRelation entity) {
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
    public List<SelfOrderRelation> findByOrdersId(Long ordersId) {
        // 构造自定义查询条件
        Specification<SelfOrderRelation> queryCondition = new Specification<SelfOrderRelation>() {
            @Override
            public Predicate toPredicate(Root<SelfOrderRelation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (ordersId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("orderId"), ordersId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return jpaRepository.findAll(queryCondition);
    }

    @Override
    public List<SelfOrderRelation> findAllByStatus() {
        return repository.findAllByStatus();
    }

    /**
     * 佣金零钱到账
     * @param s
     */
    @Override
    public void addMoney(SelfOrderRelation s) {
        //1.用户零钱增加
        userRepository.updateJiFenAdd(s.getCommissionPrice().toString(), s.getUserId());
        //2.用户零钱明细增加
        MoneyDetails moneyDetails = new MoneyDetails();
        moneyDetails.setUserId(s.getUserId());
        moneyDetails.setClassify(1);
        moneyDetails.setType(1); //增加
        moneyDetails.setTitle("自营商城分销");
        moneyDetails.setContent(s.getGoodsTitle());
        moneyDetails.setMoney(s.getCommissionPrice().doubleValue());
        moneyDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        moneyDetailsJpaRepository.save(moneyDetails);
        //3.修改分销记录为已到账
        s.setStatus(2); //已到账
        this.updateBody(s);
        //4.订单设置未不可退款状态
        Long orderId = s.getOrderId();
        Orders order = ordersJpaRepository.findById(orderId).orElse(null);
        if(order!=null){
            order.setIsRefund(2); //不可退款
            ordersJpaRepository.save(order);
        }

    }

    /**
     * 我的佣金统计：已到账佣金、未到账佣金、本月佣金
     * @param userId
     * @return
     */
    @Override
    public Result statistaical(Long userId) {
        String month = new SimpleDateFormat("yyyy-MM").format(new Date());
        String sum1 = repository.sumByUserIdStatus1(userId, month);
        String sum2 = repository.sumByUserIdStatus2(userId, month);
        Map<String, Double> map = new HashMap<>();
        map.put("sum1", AmountCalUtils.moneySum(sum1)); //本月未到账
        map.put("sum2", AmountCalUtils.moneySum(sum2)); //本月已到账
        return ResultUtil.success(map);
    }

    /**
     * 团队佣金统计
     * @param userId
     * @return
     */
    @Override
    public Result tuanStatistaical(Long userId) {
        String month = new SimpleDateFormat("yyyy-MM").format(new Date());
        String sum1 = repository.sumByUserIdStatus1Tuan(userId, month);
        String sum2 = repository.sumByUserIdStatus2Tuan(userId, month);
        Map<String, Double> map = new HashMap<>();
        map.put("sum1", AmountCalUtils.moneySum(sum1)); //本月未到账
        map.put("sum2", AmountCalUtils.moneySum(sum2)); //本月已到账
        return ResultUtil.success(map);
    }

    @Override
    public Result tuanSum(Long userId) {
        return ResultUtil.success(AmountCalUtils.moneySum(repository.tuanSum(userId)));
    }

    /**
     * 我的团队
     * @param userId 用户id
     * @param type 1直属 2非直属
     * @return
     */
    @Override
    public Result getTeam(Long userId, Integer type) {
        List<invitationTotal> list = new ArrayList<>();
        UserEntity userById = userRepository.getUserById(userId);
        if (StringUtils.isEmpty(userById.getInvitationCode())){
            return ResultUtil.error(-1, "请先申请会员资格");
        }
        List<Long> usersByInvitations = new ArrayList<>();
        if(type == 1){
            usersByInvitations = userRepository.getUsersByRelationId(userById.getInvitationCode());
        }else {
            usersByInvitations = userRepository.getUsersByRelationIds(userById.getInvitationCode());
        }
        List<UserEntity> allUser = userJpaRepository.findAll();
        List<SelfOrderRelation> all = jpaRepository.findAll();
        for (Long usersByInvitation : usersByInvitations) {
            UserEntity byId = new UserEntity();
            for (UserEntity u : allUser) {
                if (usersByInvitation.equals(u.getUserId())){
                    byId = u;
                }
            }
            invitationTotal invitationTotal = new invitationTotal();
            assert byId != null;
            invitationTotal.setGrade(byId.getInvitationCode());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = simpleDateFormat.format(byId.getCreateTime());
            invitationTotal.setTime(date);
            if (byId.getUserName() != null) {
                invitationTotal.setUserName(byId.getUserName());
            } else {
                invitationTotal.setUserName(byId.getPhone());
            }
            if (StringUtils.isNotBlank(byId.getInvitationCode())) {
                //计算金额
                String money = "";
                if(type == 1){ //区分直属非直属
                    money = repository.sumByLowerUserId(byId.getUserId());
                }else {
                    money = repository.sumByLowerUserIds(byId.getUserId());
                }
                String s = AmountCalUtils.moneySum(money).toString();
                if (StringUtils.isNotBlank(s)) {
                    invitationTotal.setMoneySum(s);
                } else {
                    invitationTotal.setMoneySum("0.00");
                }
            } else {
                invitationTotal.setMoneySum("0.00");
            }
            if (invitationTotal.getUserName() == null) {
                invitationTotal.setUserName("直属："+byId.getUserId());
            }
            list.add(invitationTotal);
        }
        //排序
        Collections.sort(list, new Comparator<invitationTotal>() {
            @Override
            public int compare(invitationTotal o1, invitationTotal o2) {
                int i1 = Integer.valueOf(Math.round(Float.parseFloat(o1.getMoneySum())*100)).intValue();
                int i2 = Integer.valueOf(Math.round(Float.parseFloat(o2.getMoneySum())*100)).intValue();
                return i2 - i1;
            }
        });
        return ResultUtil.success(list);
    }

}
