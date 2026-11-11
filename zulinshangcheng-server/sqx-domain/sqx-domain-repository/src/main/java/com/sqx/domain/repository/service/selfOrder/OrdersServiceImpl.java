package com.sqx.domain.repository.service.selfOrder;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.common.core.utils.excel.ExcelData;
import com.sqx.domain.repository.entity.app.*;
import com.sqx.domain.repository.entity.pay.PayDetails;
import com.sqx.domain.repository.entity.sys.*;
import com.sqx.domain.repository.mapper.app.AddressDao;
import com.sqx.domain.repository.mapper.app.SysEvaluateDao;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.mapper.app.UserMoneyDetailsDao;
import com.sqx.domain.repository.mapper.common.CommonRepository;
import com.sqx.domain.repository.mapper.money.MoneyDetailsJpaRepository;
import com.sqx.domain.repository.mapper.pay.PayDetailsDao;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponIssueMapper;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponMapper;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponUserJpaRepository;
import com.sqx.domain.repository.mapper.selfCoupon.SelfCouponUserMapper;
import com.sqx.domain.repository.mapper.selfGoods.GoodsJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsComment.SelfGoodsCommentDao;
import com.sqx.domain.repository.mapper.selfGoodsComment.SelfGoodsCommentJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsComment.SelfGoodsCommentRepository;
import com.sqx.domain.repository.mapper.selfGroup.SelfGroupJpaRepository;
import com.sqx.domain.repository.mapper.selfGroupPink.SelfGroupPinkJpaRepository;
import com.sqx.domain.repository.mapper.selfGroupSku.SelfGroupSkuJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.ContinuingOrderDao;
import com.sqx.domain.repository.mapper.selfOrder.OrdersDao;
import com.sqx.domain.repository.mapper.selfOrder.OrdersJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersRepository;
import com.sqx.domain.repository.mapper.selfOrderRelation.OrdersRelationJpaRepository;
import com.sqx.domain.repository.mapper.selfOrderRemind.SelfOrdersRemindJpaRepository;
import com.sqx.domain.repository.mapper.selfSeckill.SelfSeckillJpaRepository;
import com.sqx.domain.repository.mapper.sys.GoodsDao;
import com.sqx.domain.repository.mapper.sys.LeaseDaysDao;
import com.sqx.domain.repository.mapper.sys.MessageInfoDao;
import com.sqx.domain.repository.mapper.userinfo.UserJpaRepository;
import com.sqx.domain.repository.mapper.userinfo.UserRepository;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.jifen.IJiFenService;
import com.sqx.domain.repository.service.pay.WxService;
import com.sqx.domain.repository.service.selfAliPay.SelfAliPayService;
import com.sqx.domain.repository.service.selfGoods.GoodsService;
import com.sqx.domain.repository.service.selfGoodsSku.SelfGoodsSkuService;
import com.sqx.domain.repository.service.selfGoodsVirtual.SelfGoodsVirtualService;
import com.sqx.domain.repository.service.selfGroupPink.SelfGroupPinkService;
import com.sqx.domain.repository.service.selfOrderRelation.OrdersRelationService;
import com.sqx.domain.repository.service.selfWXPay.SelfWXService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.IUserService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.wx.AccountTokenServiceImpl;
import com.sqx.domain.repository.utils.BaiDuUtils;
import com.sqx.domain.repository.utils.SenInfoCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.sqx.common.core.utils.wx.WxPayUtils.getGeneralOrder;

@Service
@Slf4j
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrdersJpaRepository jpaRepository;
    @Autowired
    private OrdersRelationService ordersRelationService;
    @Autowired
    private OrdersRelationJpaRepository ordersRelationJpaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private MoneyDetailsJpaRepository moneyDetailsJpaRepository;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SelfAliPayService selfAliPayService;
    @Autowired
    private SelfWXService selfWXService;
    @Autowired
    private IJiFenService jiFenService;
    @Autowired
    private CommonRepository commonRepository;
    @Autowired
    private SelfOrdersRemindJpaRepository selfOrdersRemindJpaRepository;
    @Autowired
    private SelfGoodsSkuService skuService;
    @Autowired
    private SelfGroupPinkService groupPinkService;
    @Autowired
    private SelfGroupJpaRepository groupJpaRepository;
    @Autowired
    private SelfGroupSkuJpaRepository groupSkuJpaRepository;
    @Autowired
    private IUserService userService;
    @Autowired
    private MessageService iMessageService;
    @Autowired
    private SelfCouponUserJpaRepository couponUserJpaRepository;
    @Autowired
    private SelfGoodsVirtualService selfGoodsVirtualService;
    @Autowired
    private SelfGroupPinkJpaRepository selfGroupPinkJpaRepository;
    @Autowired
    private LeaseDaysDao leaseDaysDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private UserService userServices;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private SysEvaluateDao sysEvaluateDao;
    @Autowired
    private ContinuingOrderDao continuingOrderDao;
    @Autowired
    private SelfCouponMapper selfCouponMapper;
    @Autowired
    private SelfCouponUserMapper selfCouponUserMapper;
    @Autowired
    private SelfCouponIssueMapper selfCouponIssueMapper;
    @Autowired
    private SelfGoodsCommentJpaRepository goodsCommentJpaRepository;
    @Autowired
    private SelfGoodsCommentRepository selfGoodsCommentRepository;
    @Autowired
    private SelfGoodsCommentDao selfGoodsCommentDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WxService wxService;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);


    /**
     * 订单列表
     *
     * @param page
     * @param size
     * @param orderNum
     * @param status
     * @return
     */
    @Override
    public Result findAll(Integer page, Integer size, String orderNum, String status, String title, String mobile,
                          Long storeId, Integer isExpress, Integer orderType, String consignee, String sellerNumber, String expressNumber, String startTime, String endTime) {
        IPage<Orders> pages;
        if (page != null && size != null) {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        } else {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
            pages.setSize(-1);
        }
        IPage<Orders> page1 = ordersDao.selectPageByAll(pages, orderNum, status, title, mobile, storeId, isExpress, orderType, consignee,
                sellerNumber, expressNumber, startTime, endTime);
        if (page1.getRecords().isEmpty()) {
            return ResultUtil.success(Collections.emptyList());
        }
        for (Orders record : page1.getRecords()) {
            if (record != null) {
                BigDecimal totalMoney = BigDecimal.ZERO;
                List<ContinuingOrder> continuingOrders = continuingOrderDao.selectList(new QueryWrapper<ContinuingOrder>()
                        .eq("order_id", record.getId()));
                if (!continuingOrders.isEmpty()) {
                    for (ContinuingOrder continuingOrder : continuingOrders) {
                        record.setLeaseTerm(record.getLeaseTerm() + continuingOrder.getLeaseDay());
                        totalMoney = totalMoney.add(continuingOrder.getLeasePrice());
                    }
                }
                if (continuingOrders.isEmpty()) {
                    record.setPayMoney(record.getPayMoney());
                    record.setGrossRent(record.getGrossRent());
                } else {
                    Orders order = ordersDao.selectById(record.getId());
                    record.setPayMoney(totalMoney.add(record.getDeposit()).add(record.getPayMoney()));
                    record.setGrossRent(totalMoney.add(order.getPayMoney()));
                }
            }
        }
        return ResultUtil.success(page1);
    }


    @Override
    public ExcelData listExcel(String orderNum, String status, String title, String mobile, Long storeId, Integer isExpress, Integer orderType, String startTime, String endTime) {
        // 构造自定义查询条件
        Specification<Orders> queryCondition = new Specification<Orders>() {
            @Override
            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(orderNum)) {
                    predicateList.add(criteriaBuilder.equal(root.get("orderNum"), orderNum));
                }
                if (!"0".equals(status)) {
                    predicateList.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (StringUtils.isNotEmpty(title)) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
                }
                if (StringUtils.isNotEmpty(mobile)) {
                    predicateList.add(criteriaBuilder.equal(root.get("mobile"), mobile));
                }
                if (storeId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("storeId"), storeId));
                } else {
                    predicateList.add(criteriaBuilder.isNull(root.get("storeId")));
                }
                if (isExpress != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("isExpress"), isExpress));
                }
                if (orderType != 0) {
                    predicateList.add(criteriaBuilder.equal(root.get("orderType"), orderType));
                }
                if (StringUtils.isNotEmpty(startTime)) {
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createAt"), startTime));
                }
                if (StringUtils.isNotEmpty(endTime)) {
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createAt"), endTime));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<Orders> all = jpaRepository.findAll(queryCondition);
        ExcelData data = new ExcelData();
        data.setName("订单列表");
        List<String> titles = new ArrayList();
        titles.add("商品图片");
        titles.add("商品标题");
        titles.add("商品规格");
        titles.add("商品个数");
        titles.add("订单备注");
        titles.add("订单类型");
        titles.add("订单号");
        titles.add("收货人");
        titles.add("手机号");
        titles.add("详细地址");
        titles.add("商品价格");
        titles.add("支付金额");
        titles.add("佣金");
        titles.add("退款原因");
        titles.add("驳回原因");
        titles.add("支付方式");
        titles.add("创建时间");
        titles.add("付款时间");
        titles.add("收货时间");
        titles.add("订单状态");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (Orders orders : all) {
            List<Object> row = new ArrayList();
            row.add(orders.getImg());
            row.add(orders.getTitle());
            row.add(StringUtils.isEmpty(orders.getDetailJson()) ? "无" : orders.getDetailJson());
            row.add(orders.getNumber());
            row.add(StringUtils.isEmpty(orders.getDescrition()) ? "未填写" : orders.getDescrition());
            //1普通订单 2拼团订单 3秒杀订单 4积分订单
            if (orders.getOrderType() == 1) {
                row.add("普通订单");
            } else if (orders.getOrderType() == 2) {
                row.add("拼团订单");
            } else if (orders.getOrderType() == 3) {
                row.add("秒杀订单");
            } else if (orders.getOrderType() == 4) {
                row.add("积分订单");
            } else if (orders.getOrderType() == 5) {
                row.add("租赁订单");
            } else if (orders.getOrderType() == 6) {
                row.add("买断订单");
            } else {
                row.add("未知");
            }
            row.add(orders.getOrderNum());
            row.add(StringUtils.isEmpty(orders.getConsignee()) ? "" : orders.getConsignee());
            row.add(StringUtils.isEmpty(orders.getMobile()) ? "" : orders.getMobile());
            row.add(StringUtils.isEmpty(orders.getDetail()) ? "" : orders.getDetail());
            row.add(orders.getPrice());
            row.add(orders.getPayMoney());
            row.add(orders.getCommissionPrice());
            row.add(StringUtils.isEmpty(orders.getRefund()) ? "" : orders.getRefund());
            row.add(StringUtils.isEmpty(orders.getRefusedRefund()) ? "" : orders.getRefusedRefund());
            //1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分
            if (orders.getPayWay() == null) {
                row.add("未支付");
            } else if (orders.getPayWay() == 1) {
                row.add("app微信");
            } else if (orders.getPayWay() == 2) {
                row.add("微信公众号");
            } else if (orders.getPayWay() == 3) {
                row.add("微信小程序");
            } else if (orders.getPayWay() == 4) {
                row.add("app支付宝");
            } else if (orders.getPayWay() == 5) {
                row.add("H5支付宝");
            } else if (orders.getPayWay() == 6) {
                row.add("零钱");
            } else if (orders.getPayWay() == 7) {
                row.add("积分");
            } else {
                row.add("未知");
            }
            row.add(orders.getCreateAt());
            row.add(StringUtils.isEmpty(orders.getPayTime()) ? "" : orders.getPayTime());
            row.add(StringUtils.isEmpty(orders.getFinishTime()) ? "" : orders.getFinishTime());
            //订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价
            if (orders.getStatus() == 0) {
                row.add("待支付");
            } else if (orders.getStatus() == 1) {
                row.add("已支付待发货");
            } else if (orders.getStatus() == 2) {
                row.add("已发货待收货");
            } else if (orders.getStatus() == 3) {
                row.add("已收货");
            } else if (orders.getStatus() == 4) {
                row.add("待归还");
            } else if (orders.getStatus() == 5) {
                row.add("归还中");
            } else if (orders.getStatus() == 6) {
                row.add("已完成");
            } else if (orders.getStatus() == 7) {
                row.add("继续租赁");
            } else if (orders.getStatus() == 8) {
                row.add("买断");
            } else if (orders.getStatus() == 9) {
                row.add("已取消");
            } else if (orders.getStatus() == 10) {
                row.add("待评价");
            } else {
                row.add("未知");
            }
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }


    /**
     * 用户端订单列表
     *
     * @param page
     * @param size
     * @param userId
     * @param status
     * @return
     */
    @Override
    public Result findMyList(Integer page, Integer size, String userId, String status, String isExpress) {
        IPage<Orders> pages;
        if (page != null && size != null) {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        } else {
            pages = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
            pages.setSize(-1);
        }
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status", status);
        }
        if ("1".equals(isExpress)) {
            queryWrapper.in("is_express", Arrays.asList(1, 2));
        } else {
            queryWrapper.eq("is_express", isExpress);
        }
        queryWrapper.orderByDesc("create_at");
        IPage<Orders> all = ordersDao.selectPage(pages, queryWrapper);
        for (Orders record : all.getRecords()) {
            if (record != null) {
                BigDecimal totalMoney = BigDecimal.ZERO;
                List<ContinuingOrder> continuingOrders = continuingOrderDao.selectList(new QueryWrapper<ContinuingOrder>()
                        .eq("order_id", record.getId()));
                if (!continuingOrders.isEmpty()) {
                    for (ContinuingOrder continuingOrder : continuingOrders) {
                        record.setLeaseTerm(record.getLeaseTerm() + continuingOrder.getLeaseDay());
                        totalMoney = totalMoney.add(continuingOrder.getLeasePrice());
                    }
                }
                if (continuingOrders.isEmpty()) {
                    record.setPayMoney(record.getPayMoney());
                    record.setGrossRent(record.getGrossRent());
                } else {
                    Orders order = ordersDao.selectById(record.getId());
                    record.setPayMoney(totalMoney.add(record.getDeposit()).add(record.getPayMoney()));
                    record.setGrossRent(totalMoney.add(order.getPayMoney()));
                }
                LeaseDays leaseDays = leaseDaysDao.selectById(record.getDayId());
                if (leaseDays != null) {
                    record.setLeaseTerm(Integer.valueOf(leaseDays.getDayName()));
                }
                Goods goods = goodsDao.selectById(record.getGoodsId());
                if (goods != null) {
                    record.setGoods(goods);
                }
            }
        }
//        //根据时间排序
//        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createAt")));
//        // 构造自定义查询条件
//        Specification<Orders> queryCondition = new Specification<Orders>() {
//            @Override
//            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicateList = new ArrayList<>();
//                if (StringUtils.isNotEmpty(userId)) {
//                    predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
//                }
//                if (!"0".equals(status)) {
//                    predicateList.add(criteriaBuilder.equal(root.get("status"), status));
//                }
//                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
//            }
//        };
//        Page<Orders> all = jpaRepository.findAll(queryCondition, pageable);
//        //处理订单分销
//        List<SelfOrderRelation> all1 = ordersRelationJpaRepository.findAll();
//        //处理购物车订单bug
//        List<Orders> cartList = new ArrayList<>();
//        for (Orders o : all.getContent()) {
//            List<SelfOrderRelation> relationList = new ArrayList<>();
//            for (SelfOrderRelation r : all1) {
//                if (o.getId().toString().equals(r.getOrderId().toString())){
//                    relationList.add(r);
//                }
//            }
//            o.setRelationList(relationList);
//            //处理购物车订单bug
//            if (o.getIsCart() != null && o.getStatus()==1){ //购物车的待付款订单
//                cartList.add(o);
//            }
//        }
//        this.cancel(cartList); //处理购物车订单bug
        return ResultUtil.success(all);
    }

    /**
     * 我的售后订单
     *
     * @param page
     * @param size
     * @param userId
     * @return
     */
    @Override
    public Result findMyRefundList(Integer page, Integer size, String userId) {
        //根据时间排序
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createAt")));
        // 构造自定义查询条件
        Specification<Orders> queryCondition = new Specification<Orders>() {
            @Override
            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(userId)) {
                    predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                predicateList.add(criteriaBuilder.between(root.get("status"), 6, 8)); //6退款中 7已退款 8拒绝退款
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return ResultUtil.success(jpaRepository.findAll(queryCondition, pageable));
    }

    /**
     * 生成订单编号
     *
     * @return
     */
    private String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

    /**
     * 保存订单：普通下单
     *
     * @param entity
     * @return
     */
    @Override
    public Result saveBody(Orders entity) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            //校验下单金额
            Result check = this.checkOrderMoney(entity);
            if (check.getStatus() != 0) {
                return check;
            }
        /*
        前端传入：用户id、收货人、手机号、详细地址、商品类型、商品标题、商品图片、商品描述、商品价格、商品个数、支付金额、
        后台生成：创建时间、订单号、订单状态
         */
            entity.setCreateAt(DateUtils.createTime()); //创建时间
            entity.setOrderNum(this.getGeneralOrder()); //订单号
            entity.setPayNum(this.getGeneralOrder()); //订单号
            entity.setStatus(0); //订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消
            entity.setSupplementStatus(2);
            entity.setIsEvaluate(0);
            entity.setOverdueStatus(2);
            //判断库存
            Integer orderType = entity.getOrderType(); //订单类型
            if (orderType == 1) { //普通订单
                if (!skuService.checkStock(entity.getSkuId(), entity.getNumber())) {
                    return ResultUtil.error(-1, entity.getTitle() + "库存不足");
                }
                skuService.lessStock(entity.getSkuId(), entity.getNumber()); //sku库存减少对应数量
            } else if (orderType == 2) { //拼团
                if (!groupPinkService.checkPinkStock(entity.getSkuId(), entity.getNumber())) {
                    return ResultUtil.error(-1, entity.getTitle() + "库存不足");
                }
                groupSkuJpaRepository.lessPinkStock(entity.getSkuId(), entity.getNumber());
            } else if (orderType == 3) { //秒杀
                if (!groupPinkService.checkSkillStock(entity.getSkuId(), entity.getNumber())) {
                    return ResultUtil.error(-1, entity.getTitle() + "库存不足");
                }
                groupSkuJpaRepository.lessSecKillStock(entity.getSkuId(), entity.getNumber());
            } else if (orderType == 4) { // 积分订单
                if (!skuService.checkStock(entity.getSkuId(), entity.getNumber())) {
                    return ResultUtil.error(-1, entity.getTitle() + "库存不足");
                }
                skuService.lessStock(entity.getSkuId(), entity.getNumber()); //sku库存减少对应数量
            } else if (orderType == 5) { // 租赁订单
                if (!skuService.checkStock(entity.getSkuId(), entity.getNumber())) {
                    return ResultUtil.error(-1, entity.getTitle() + "库存不足");
                }
                skuService.lessStock(entity.getSkuId(), entity.getNumber());
            } else if (orderType == 6) { // 买断订单
                if (!skuService.checkStock(entity.getSkuId(), entity.getNumber())) {
                    return ResultUtil.error(-1, entity.getTitle() + "库存不足");
                }
            }
            //优惠券
//            if (entity.getUserCouponsId() != null) {
//                couponUserJpaRepository.usedCoupon(entity.getUserCouponsId());
//            }
            //检测复购
            entity.setIsRepeat(0);
            entity.setIsExpress(entity.getIsExpress());
            if (entity.getDayId() != null) {
                LeaseDays leaseDays = leaseDaysDao.selectById(entity.getDayId());
                if (leaseDays != null) {
                    entity.setLeaseTerm(Integer.valueOf(leaseDays.getDayName()));
                }
            }
            int count = ordersRepository.checkIsRepeat(entity.getUserId(), entity.getGoodsId());
            if (count > 0) {
                entity.setIsRepeat(1);
            }
            Orders save = jpaRepository.save(entity);
            skuService.lessStock(entity.getSkuId(), entity.getNumber());
            return ResultUtil.success(save.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存订单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return ResultUtil.error(-200, "系统繁忙，请稍后再试！");
    }

    @Autowired
    private GoodsJpaRepository goodsJpaRepository;
    @Autowired
    private SelfGroupJpaRepository selfGroupJpaRepository;
    @Autowired
    private SelfSeckillJpaRepository selfSeckillJpaRepository;

    /**
     * 校验下单金额
     *
     * @param entity
     * @return
     */
    public Result checkOrderMoney(Orders entity) {
        if (entity.getGroupId() != null) {
            SelfGroup selfGroup = selfGroupJpaRepository.findById(entity.getGroupId()).orElse(null);
            if (selfGroup == null || selfGroup.getStatus() != 1) {
                return ResultUtil.error(-1, "商品已被下架或移除，请选择其他商品！");
            }
        } else if (entity.getSecKillId() != null) {
            SelfSecKill selfSecKill = selfSeckillJpaRepository.findById(entity.getSecKillId()).orElse(null);
            if (selfSecKill == null || selfSecKill.getStatus() != 1) {
                return ResultUtil.error(-1, "商品已被下架或移除，请选择其他商品！");
            }
        } else {
            Goods g = goodsJpaRepository.findById(entity.getGoodsId()).orElse(null);
            if (g == null || g.getStatus() != 1) {
                return ResultUtil.error(-1, "商品已被下架或移除，请选择其他商品！");
            }
        }
        //校验优惠券
        if (entity.getCouponUserId() != null) {
            SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(entity.getCouponUserId());
            Goods g = goodsJpaRepository.findById(entity.getGoodsId()).orElse(null);
//            if (!entity.getUserCouponsId().equals(g.getBrandId())) { //品牌id:前端传值有误，排除商品品牌id
            if (couponUser == null) {
                return ResultUtil.error(-1, "优惠券不存在");
            }
            SelfCouponIssue selfCouponIssue = selfCouponIssueMapper.selectByCouponIssueId(couponUser.getCouponIssueId());
            if (selfCouponIssue.getStatus() != 1) {
                return ResultUtil.error(-1, "优惠券未启用，暂不能使用");
            }
            if (selfCouponIssue.getType() == 2) {
                boolean flag = false;
                if (selfCouponIssue.getGoodsIds() != null) {
                    String[] goodsIds = selfCouponIssue.getGoodsIds().split(",");
                    for (String goodsId : goodsIds) {
                        if (goodsId.trim().equals(entity.getGoodsId().toString().trim())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
                    }
                } else {
                    return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
                }
            }
            if (Double.parseDouble(couponUser.getLessMoney()) != entity.getCouponMoney().doubleValue()) {
                return ResultUtil.error(-1, "优惠券金额有误");
            }
//            }
        }
        //校验支付金额：商品金额：商品价格*个数+邮费=总价；支付金额：总价-优惠金额(单商户)
        //预估价格 = 日租金 * 天数 * 购买的数量 + 押金 + 运费
//        if (entity.getDayId() != null){
//
//        }else if (entity.getDayId() == null && entity.get){
//            return ResultUtil.error(-1, "商品金额有误");
//        }
        Goods g = goodsJpaRepository.findById(entity.getGoodsId()).orElse(null);
        if (g != null && g.getIsExpress() != null) {
            String[] split = g.getIsExpress().split(",");
            if (split.length > 0) {
                boolean condition1Met = false;
                boolean condition2Met = false;
                boolean condition3Met = false;
                for (String s : split) {
                    if ("1".equals(s.trim())) {
                        condition1Met = true;
                    } else if ("2".equals(s.trim())) {
                        condition2Met = true;
                    } else if ("3".equals(s.trim())) {
                        condition3Met = true;
                    }
                }
                if (condition2Met) {
                    entity.setPostagePrice(g.getPostagePrice() == null ? BigDecimal.ZERO : g.getPostagePrice());
                    Address address = addressDao.selectById(entity.getAddressId());
                    if (address != null) {
                        entity.setDetail(address.getProvince() + address.getCity() + address.getAddressDetail());
                    }
                } else if (condition1Met || condition3Met) {
                    entity.setPostagePrice(BigDecimal.ZERO);
                }
            }
        }
//        if (g != null && ("1".equals(g.getIsExpress()) || "2".equals(g.getIsExpress()))) {
//            if (g.getPostagePrice() == null) {
//                entity.setPostagePrice(BigDecimal.ZERO);
//            } else {
//                entity.setPostagePrice(g.getPostagePrice());
//            }
//            Address address = addressDao.selectById(entity.getAddressId());
//            if (address != null) {
//                entity.setDetail(address.getProvince() + address.getCity() + address.getAddressDetail());
//            }
//        } else if (g != null && "3".equals(g.getIsExpress())) {
//            entity.setPostagePrice(BigDecimal.ZERO);
//        }
        LeaseDays leaseDays = leaseDaysDao.selectById(entity.getDayId());
        if (leaseDays == null) {
            return ResultUtil.error(-1, "该租赁天数不存在，请刷新后重试");
        }
        String dayName = leaseDays.getDayName();
        long days = Long.parseLong(dayName);
        UserEntity user = userDao.selectById(entity.getUserId());
        if (user != null) {
            BigDecimal couponMoney = null;
            // 如果是租赁商品
            if (entity.getOrderType() == 5) {
                if (entity.getCouponMoney() == null) {
                    entity.setCouponMoney(BigDecimal.ZERO);
                    couponMoney = BigDecimal.ZERO;
                } else {
                    entity.setCouponMoney(entity.getCouponMoney());
                    couponMoney = entity.getCouponMoney();
                }
                // 预估价格 = 日租金 * 天数 * 购买的数量 + 押金 + 运费 - 优惠券
                entity.setEstimateMoney(
                        entity.getDailyRent()
                                .multiply(BigDecimal.valueOf(days))
                                .multiply(BigDecimal.valueOf(entity.getNumber()))
                                .add(entity.getDeposit())
                                .add(entity.getPostagePrice())
                                .subtract(couponMoney));
                // 总租金 = 日租金 * 天数 * 购买的数量 + 运费
                entity.setGrossRent(
                        entity.getDailyRent()
                                .multiply(BigDecimal.valueOf(days))
                                .multiply(BigDecimal.valueOf(entity.getNumber()))
                                .add(entity.getPostagePrice()));
                // 如果是买断商品
            } else if (entity.getOrderType() == 6) {
                if (entity.getCouponMoney() == null) {
                    entity.setCouponMoney(BigDecimal.ZERO);
                    couponMoney = BigDecimal.ZERO;
                } else {
                    entity.setCouponMoney(entity.getCouponMoney());
                    couponMoney = entity.getCouponMoney();
                }
                UserEntity userEntity = userDao.selectById(entity.getUserId());
                if (userEntity != null) {
                    if (userEntity.getIsVip() == 1) {
                        if (g.getMemberPrice() == null) {
                            g.setMemberPrice(BigDecimal.ZERO);
                        }
                        entity.setEstimateMoney(g.getMemberPrice()
                                .multiply(new BigDecimal(entity.getNumber()))
                                .add(entity.getPostagePrice())
                                .subtract(couponMoney));
                    } else {
                        // 预估价格 = 商品价格 * 购买数量 + 邮费 - 优惠券
                        if (g.getPrice() == null) {
                            g.setPrice(BigDecimal.ZERO);
                        }
                        entity.setEstimateMoney(
                                g.getPrice()
                                        .multiply(new BigDecimal(entity.getNumber()))
                                        .add(entity.getPostagePrice())
                                        .subtract(couponMoney));
                    }
                }
            }
//        BigDecimal sumMoney = entity.getPrice().multiply(new BigDecimal(entity.getNumber()));
//        if (entity.getPostagePrice() != null){ //邮费不为空
//            sumMoney = sumMoney.add(entity.getPostagePrice());
//        }
            BigDecimal payMoney = entity.getEstimateMoney();
            entity.setPayMoney(payMoney);
//            if (entity.getCouponMoney() != null) { //优惠券金额不为空
//                payMoney = payMoney.subtract(entity.getCouponMoney());
//            }
            if (payMoney.compareTo(BigDecimal.ZERO) < 0) {
                return ResultUtil.error(-1, "订单金额有误");
            }
            entity.setPayMoney(payMoney);
            if (payMoney.doubleValue() != entity.getPayMoney().doubleValue()) {
                return ResultUtil.error(-1, "订单金额有误");
            }
        } else {
            return ResultUtil.error(-1, "该用户未登录");
        }
        return ResultUtil.success();
    }

    /**
     * 购物车下单
     *
     * @param list
     * @return
     */
    @Override
    public Result saveAll(List<Orders> list) {
        //多订单时支付单号一致
        String payNum = this.getGeneralOrder();
        for (int i = 0; i < list.size(); i++) {
            Orders entity = list.get(i);
            //校验下单金额
            Result check = this.checkOrderMoney(entity);
            if (check.getStatus() != 0) {
                return check;
            }
            //判断库存
            Integer orderType = entity.getOrderType(); //订单类型
            if (!skuService.checkStock(entity.getSkuId(), entity.getNumber())) {
                return ResultUtil.error(-1, entity.getTitle() + "库存不足");
            }
            entity.setCreateAt(DateUtils.createTime()); //创建时间
            entity.setOrderNum(this.getGeneralOrder()); //订单号
            entity.setPayNum(payNum);
            entity.setStatus(1); //订单状态（1待付款 2已付款 3已发货 4已收货 5已取消 6退款中 7已退款 8拒绝退款 9拼团中）
            entity.setIsCart(1); //购物车订单标志
            Orders save = jpaRepository.saveAndFlush(entity);
            list.set(i, save);
            //sku库存减少对应数量
            skuService.lessStock(entity.getSkuId(), entity.getNumber());
            //优惠券
            if (entity.getCouponUserId() != null) {
                couponUserJpaRepository.usedCoupon(entity.getCouponUserId());
            }
        }
        return ResultUtil.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateBody(Orders entity, Long userId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (entity.getOrderType() == 6) {
                if (entity.getPayWay() == 6) {
                    PayDetails payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", entity.getBuyOrderNum()));
                    if (payDetails == null) {
                        return ResultUtil.success(entity.getId());
                    } else if (payDetails.getState() == 0 || payDetails.getState() == 2) {
                        return ResultUtil.success(entity.getId());
                    }
                }
                UserEntity user = userDao.selectById(userId);
                Orders orders = ordersDao.selectById(entity.getId());
                if (orders != null) {
                    Goods goods = goodsDao.selectById(entity.getGoodsId());
                    if (user != null) {
                        if (!user.getUserId().equals(orders.getUserId())) {
                            return ResultUtil.error(-1, "您无权操作！");
                        }
                    }
                    if (goods != null) {
                        LocalDate now = LocalDate.now();
                        LocalDate returnDate = orders.getReturnDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        if (returnDate.isBefore(now)) {
                            return ResultUtil.error(-1, "您已逾期，不能买断");
                        }
                        if (user != null && user.getIsVip() == 1) {
                            // 租期内买断补缴=买断价-支付的总租金-押金
                            // 买断价格 = (买断价格:会员价)-租赁价格-超出天数价格(-有优惠券减去优惠券价格)
                            if (entity.getCouponMoney() != null) {
                                entity.setPrice(
                                        entity.getBuyoutPrice().subtract(entity.getGrossRent())
                                                .subtract(orders.getDeposit())
                                                .subtract(entity.getCouponMoney()));
                            } else {
                                entity.setPrice(
                                        entity.getBuyoutPrice().subtract(entity.getGrossRent())
                                                .subtract(entity.getDeposit()));
                            }
                        } else {
                            // 买断价格 = (买断价格:非会员价)-租赁价格-超出天数价格(-有优惠券减去优惠券价格)
                            if (entity.getCouponMoney() != null) {
                                entity.setPrice(
                                        entity.getBuyoutPrice().subtract(entity.getGrossRent())
                                                .subtract(orders.getDeposit())
                                                .subtract(entity.getCouponMoney()));
                            } else {
                                entity.setPrice(
                                        entity.getBuyoutPrice().subtract(entity.getGrossRent())
                                                .subtract(orders.getDeposit()));
                            }
                        }
                    }
                }
                if (entity.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                    return ResultUtil.error(-1, "订单金额有误");
                }
                entity.setStatus(8);
                entity.setPurchaseTime(new Date());
                Address address = addressDao.selectById(entity.getAddressId());
                if (address != null) {
                    entity.setDetail(address.getProvince() + address.getCity() + address.getAddressDetail());
                }
                int result = ordersDao.updateById(entity);
                if (result <= 0) {
                    return ResultUtil.error(-1, "操作失败");
                }
                return ResultUtil.success(entity.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存订单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return ResultUtil.error(-1, "订单类型有误");
    }

    @Override
    public Result findOne(Long id) {
        Orders orders = jpaRepository.findById(id).orElse(null);
        if (orders == null) {
            return ResultUtil.error(-1, "订单已删除");
        }
        BigDecimal totalMoney = BigDecimal.ZERO;
        List<ContinuingOrder> continuingOrders = continuingOrderDao.selectList(new QueryWrapper<ContinuingOrder>()
                .eq("order_id", orders.getId()));
        if (!continuingOrders.isEmpty()) {
            for (ContinuingOrder continuingOrder : continuingOrders) {
                orders.setLeaseTerm(orders.getLeaseTerm() + continuingOrder.getLeaseDay());
                totalMoney = totalMoney.add(continuingOrder.getLeasePrice());
            }
        }
        if (continuingOrders.isEmpty()) {
            orders.setPayMoney(orders.getPayMoney());
            orders.setGrossRent(orders.getGrossRent());
        } else {
            Orders order = ordersDao.selectById(orders.getId());
            orders.setPayMoney(totalMoney.add(orders.getDeposit()).add(orders.getPayMoney()));
            orders.setGrossRent(totalMoney.add(order.getPayMoney()));
        }
        if (orders.getTotalContinueMoney() != null) {
            orders.setTotalContinueMoney(orders.getTotalContinueMoney().add(orders.getDeposit()));
        }
        return ResultUtil.success(orders);
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    /**
     * 收入统计
     *
     * @param data
     * @param way
     * @return
     */
    @Override
    public Result income(String data, int way, Long storeId) {
        List<Orders> list = ordersRepository.findByStatus();
        switch (way) {
            case 1:
                data = data.substring(0, 4);
                break; //年，截取前四个
            case 2:
                data = data.substring(0, 7);
                break; //月，前6
            default:
                break; //日，或者其他不做处理
        }
        BigDecimal sum0 = new BigDecimal("0.00"); //总收入
        BigDecimal sum1 = new BigDecimal("0.00"); //收入
        BigDecimal pay1 = new BigDecimal("0.00"); //微信
        BigDecimal pay2 = new BigDecimal("0.00"); //支付宝
        BigDecimal pay3 = new BigDecimal("0.00"); //余额
        int refundNum = 0;
        BigDecimal refundMoney = new BigDecimal("0.00"); //退款金额
        for (Orders o : list) {
            BigDecimal money = o.getPayMoney() == null ? new BigDecimal("0.00") : o.getPayMoney(); //订单金额
            sum0 = sum0.add(money);
            if (StringUtils.isNotEmpty(o.getPayTime()) && o.getPayTime().contains(data)) {
                sum1 = sum1.add(money);
                switch (o.getPayWay()) { //根据支付方式细分：支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
                    case 1:
                        pay1 = pay1.add(money);
                        break;
                    case 2:
                        pay1 = pay1.add(money);
                        break;
                    case 3:
                        pay1 = pay1.add(money);
                        break;
                    case 4:
                        pay2 = pay2.add(money);
                        break;
                    case 5:
                        pay2 = pay2.add(money);
                        break;
                    case 6:
                        pay3 = pay3.add(money);
                        break;
                    default:
                        break;
                }
                if (o.getStatus().equals(7)) {
                    refundNum++;
                    refundMoney = refundMoney.add(money);
                }
            }

        }
        BigDecimal refundIncome = sum1.subtract(refundMoney);
        Map<String, Object> map = new HashMap<>();
        map.put("sum0", sum0);
        map.put("sum1", sum1);
        map.put("pay1", pay1);
        map.put("pay2", pay2);
        map.put("pay3", pay3);
        map.put("refundMoney", refundMoney);
        map.put("refundIncome", refundIncome);
        map.put("refundNum", refundNum);
        return ResultUtil.success(map);
    }

    /**
     * 订单成交量统计
     *
     * @param data
     * @return
     */
    @Override
    public Result statistical(String data, Long storeId) {
        List<Orders> list = ordersRepository.findByStatus();
        int sum0 = 0; //年
        int sum1 = 0; //月
        int sum2 = 0; //日
        for (Orders o : list) {
            if (StringUtils.isNotEmpty(o.getPayTime()) && o.getPayTime().contains(data.substring(0, 4))) {
                String orderTime = o.getPayTime();
                if (orderTime.contains(data.substring(0, 4))) {
                    sum0++;
                }
                if (orderTime.contains(data.substring(0, 7))) {
                    sum1++;
                }
                if (orderTime.contains(data)) {
                    sum2++;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("sum0", sum0);
        map.put("sum1", sum1);
        map.put("sum2", sum2);
        return ResultUtil.success(map);
    }

    /**
     * 取消订单
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result orderCancel(Long id) {
        boolean refund = true;
        Orders orders = jpaRepository.findById(id).orElse(null);
        if (orders != null) {
            UserEntity userEntity = userDao.selectById(orders.getUserId());
            if (orders.getStatus() == 0){
                orders.setStatus(9);
                orders.setCancelTime(new Date());
                orders = jpaRepository.save(orders);
                if (orders.getCouponUserId() != null) {
                    SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(orders.getCouponUserId());
                    couponUser.setStatus(1);
                    selfCouponUserMapper.updateCouponUserId(couponUser);
                }
                if (userEntity != null) {
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setUserId(String.valueOf(orders.getUserId()));
                    messageInfo.setTitle("取消订单通知：" + orders.getOrderNum());
                    messageInfo.setContent("您的订单已取消");
                    messageInfo.setCreateAt(DateUtils.format(new Date()));
                    messageInfo.setIsSee("0");
                    messageInfo.setStatus(0);
                    messageInfo.setState(String.valueOf(4));
                    messageInfo.setUserName(userEntity.getUserName());
                    iMessageService.save(messageInfo);
                }
                return ResultUtil.success();
            }
            if (orders.getStatus() == 0 || (orders.getIsExpress() == 3 && orders.getStatus() == 2)) {
                if (orders.getPayWay() == 6) {
                    userDao.updateMoney(userEntity.getUserId(), orders.getPayMoney().doubleValue());
                } else if (orders.getPayWay() == 4 || orders.getPayWay() == 5) {
                    //3.支付宝退款
                    refund = selfAliPayService.refund(id);
                } else if (orders.getPayWay() == 1 || orders.getPayWay() == 2 || orders.getPayWay() == 3) {
                    //4.微信退款
                    refund = selfWXService.refund(id);
                }
                if (refund) {
                    orders.setStatus(9); //设置为已取消
                    orders.setCancelTime(new Date());
                    orders = jpaRepository.save(orders);
                    if (orders.getCouponUserId() != null) {
                        SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(orders.getCouponUserId());
                        couponUser.setStatus(1);
                        selfCouponUserMapper.updateCouponUserId(couponUser);
                    }
                    //2.3零钱增加明细
                    UserMoneyDetails moneyDetails = new UserMoneyDetails();
                    moneyDetails.setClassify(7);
                    moneyDetails.setUserId(userEntity.getUserId());
                    moneyDetails.setType(1); //增加
                    moneyDetails.setTitle("自营商城退款，订单号：" + orders.getOrderNum());
                    moneyDetails.setContent("您的订单已取消，退款已原路返回");
                    moneyDetails.setMoney(orders.getPayMoney());
                    moneyDetails.setCreateTime(DateUtils.createTime());
                    moneyDetails.setRelationId(orders.getId());
                    moneyDetails.setState(2);
                    userMoneyDetailsDao.insert(moneyDetails);
                    //公众号消息：取消订单
//            userService.orderCancel(orders.getUserId(), orders.getOrderNum(), DateUtils.createTime());
                    //sku库存+1
                    skuService.addStock(orders.getSkuId(), orders.getNumber());
                    //优惠券
                    if (orders.getCouponUserId() != null) {
                        couponUserJpaRepository.backCoupon(orders.getCouponUserId());
                    }
                    if (userEntity != null) {
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setUserId(String.valueOf(orders.getUserId()));
                        messageInfo.setTitle("取消订单通知：" + orders.getOrderNum());
                        messageInfo.setContent("您的订单已取消");
                        messageInfo.setCreateAt(DateUtils.format(new Date()));
                        messageInfo.setIsSee("0");
                        messageInfo.setStatus(0);
                        messageInfo.setState(String.valueOf(4));
                        messageInfo.setUserName(userEntity.getUserName());
                        iMessageService.save(messageInfo);
                    }
                    UserEntity user = userDao.selectById(orders.getUserId());
                    if (user != null && user.getOpenId() != null) {
                        List<String> msgList = new ArrayList<>();
                        msgList.add("您的订单已取消：" + orders.getOrderNum());
                        msgList.add("您的退款金额：" + orders.getPayMoney());
                        msgList.add("订单已取消,金额已原路返回,请注意查收！");
                        String commonInfo = commonInfoService.findOne(236).getValue();
                        SenInfoCheckUtil.sendMsg(user.getOpenId(), commonInfo, msgList, 2);
                    }
                    if (user != null && user.getClientid() != null) {
                        userServices.pushToSingle("您已取消订单", "订单号：" + orders.getOrderNum(), user.getClientid());
                    }
                    return ResultUtil.success(orders);
                }
            }
            if (orders.getStatus() == 0 || orders.getStatus() == 1 || orders.getStatus() == 2) {
                if (orders.getPayWay() == 6) {
                    userDao.updateMoney(userEntity.getUserId(), orders.getPayMoney().doubleValue());
                } else if (orders.getPayWay() == 4 || orders.getPayWay() == 5) {
                    //3.支付宝退款
                    refund = selfAliPayService.refund(id);
                } else if (orders.getPayWay() == 1 || orders.getPayWay() == 2 || orders.getPayWay() == 3) {
                    //4.微信退款
                    refund = selfWXService.refund(id);
                }
                if (refund) {
                    orders.setStatus(9); //设置为已取消
                    orders.setCancelTime(new Date());
                    orders = jpaRepository.save(orders);
                    if (orders.getCouponUserId() != null) {
                        SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(orders.getCouponUserId());
                        couponUser.setStatus(1);
                        selfCouponUserMapper.updateCouponUserId(couponUser);
                    }
                    //2.3零钱增加明细
                    UserMoneyDetails moneyDetails = new UserMoneyDetails();
                    moneyDetails.setClassify(7);
                    moneyDetails.setUserId(userEntity.getUserId());
                    moneyDetails.setType(1); //增加
                    moneyDetails.setTitle("自营商城退款，订单号：" + orders.getOrderNum());
                    moneyDetails.setContent("您的订单已取消，退款已原路返回");
                    moneyDetails.setMoney(orders.getPayMoney());
                    moneyDetails.setCreateTime(DateUtils.createTime());
                    moneyDetails.setRelationId(orders.getId());
                    moneyDetails.setState(2);
                    userMoneyDetailsDao.insert(moneyDetails);
                    //公众号消息：取消订单
//            userService.orderCancel(orders.getUserId(), orders.getOrderNum(), DateUtils.createTime());
                    //sku库存+1
                    skuService.addStock(orders.getSkuId(), orders.getNumber());
                    //优惠券
                    if (orders.getCouponUserId() != null) {
                        couponUserJpaRepository.backCoupon(orders.getCouponUserId());
                    }
                    if (userEntity != null) {
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setUserId(String.valueOf(orders.getUserId()));
                        messageInfo.setTitle("取消订单通知：" + orders.getOrderNum());
                        messageInfo.setContent("您的订单已取消");
                        messageInfo.setCreateAt(DateUtils.format(new Date()));
                        messageInfo.setIsSee("0");
                        messageInfo.setStatus(0);
                        messageInfo.setState(String.valueOf(4));
                        messageInfo.setUserName(userEntity.getUserName());
                        iMessageService.save(messageInfo);
                    }
                    UserEntity user = userDao.selectById(orders.getUserId());
                    if (user != null && user.getOpenId() != null) {
                        List<String> msgList = new ArrayList<>();
                        msgList.add("您的订单已取消：" + orders.getOrderNum());
                        msgList.add("您的退款金额：" + orders.getPayMoney());
                        msgList.add("订单已取消,金额已原路返回,请注意查收！");
                        String commonInfo = commonInfoService.findOne(236).getValue();
                        SenInfoCheckUtil.sendMsg(user.getOpenId(), commonInfo, msgList, 2);
                    }
                    if (user != null && user.getClientid() != null) {
                        userServices.pushToSingle("您已取消订单", "订单号：" + orders.getOrderNum(), user.getClientid());
                    }
                    return ResultUtil.success(orders);
                }
            } else {
                return ResultUtil.error(-1, "当前订单不可取消");
            }
        }
        return ResultUtil.error(-1, "订单取消失败");
    }

    /**
     * 发货
     *
     * @param id
     * @param expressName
     * @param sellerNumber
     * @return
     */
    @Override
    public Result express(Long id, String expressName, String sellerNumber) {
        Orders orders = jpaRepository.findById(id).orElse(null);
        orders.setExpressName(expressName);
        orders.setSellerNumber(sellerNumber);
        orders.setStatus(2); //设置为已发货
        orders.setExpressTime(DateUtils.createTime());
        //公众号消息：发货提醒
        String address = orders.getConsignee() + "——" + orders.getMobile() + "——" + orders.getProvinces() + orders.getDetail(); //收货信息
//        userService.orderExpress(orders.getUserId(), orders.getOrderNum(), DateUtils.createTime(), expressName, expressNumber, address);
//        UserEntity userInfo = userRepository.getUserById(orders.getUserId());
        UserEntity user = userDao.selectById(orders.getUserId());
        if (user != null) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setUserId(String.valueOf(user.getUserId()));
            messageInfo.setTitle("订单发货通知：" + orders.getOrderNum());
            messageInfo.setContent("您购买的商品已发货");
            messageInfo.setCreateAt(DateUtils.format(new Date()));
            messageInfo.setIsSee("0");
            messageInfo.setStatus(0);
            messageInfo.setState(String.valueOf(4));
            messageInfo.setUserName(user.getUserName());
            iMessageService.save(messageInfo);
        }
        if (user != null && user.getOpenId() != null) {
            List<String> msgList = new ArrayList<>();
            msgList.add("订单发货通知:" + orders.getOrderNum());
            Goods goods = goodsDao.selectById(orders.getGoodsId());
            msgList.add("您的商品:" + goods.getTitle() + "已发货，请注意查收");
            msgList.add("商品数量：" + orders.getNumber());
            msgList.add("发货时间：" + orders.getExpressTime());
            msgList.add("收货人：" + user.getUserName());
            String commonInfo = commonInfoService.findOne(522).getValue();
            SenInfoCheckUtil.sendMsg(user.getOpenId(), commonInfo, msgList, 1);
        }
        if (user != null && user.getClientid() != null) {
            userServices.pushToSingle("您的订单已发货", "订单号：" + orders.getOrderNum(), user.getClientid());
        }
        return ResultUtil.success(ordersDao.updateById(orders));
    }

    /**
     * 虚拟商品发货
     *
     * @param id
     * @return
     */
    @Override
    public Result expressVirtual(Long id) {
        Orders orders = jpaRepository.findById(id).orElse(null);
        if (orders.getIsExpress() != 2) { //是否需要发货(1需要发货 2无需发货)
            return ResultUtil.error(-1, "非虚拟商品,发货失败");
        }
        //虚拟商品发货，发货成功为确认收货，否则不做变化
        SelfGoodsVirtual v = this.sendExpress(orders);
        if (v != null) {
            orders.setVirtualId(v.getId()); //虚拟商品id
            orders.setExpressTime(DateUtils.createTime()); //发货时间
            orders.setFinishTime(DateUtils.createTime()); //收货时间
            orders.setStatus(4); //确认收货
            Orders save = jpaRepository.save(orders);
            //商品销量增加
            goodsService.salesAdd(orders.getGoodsId(), orders.getNumber());
            //sku销量增加
            skuService.addSales(orders.getSkuId(), orders.getNumber());
            return ResultUtil.success(save);
        } else {
            return ResultUtil.error(-1, "虚拟商品库存不足：" + orders.getTitle());
        }
    }

    /**
     * 确认收货
     *
     * @param id
     * @return
     */
    @Override
    public Result orderConfirm(Long id) {
        //1.订单设置已收货
        Orders orders = jpaRepository.findById(id).orElse(null);
        if (orders == null) {
            return ResultUtil.error(-1, "订单不存在");
        }
        //订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价
        if (orders.getStatus() != 2) {
            return ResultUtil.error(-200, "订单状态异常，请刷新页面后重试！");
        }
        orders.setStatus(4); //4待归还
        //商品销量增加
        goodsService.salesAdd(orders.getGoodsId(), orders.getNumber());
        //sku销量增加
        skuService.addSales(orders.getSkuId(), orders.getNumber());
        orders.setDeliveryTime(new Date()); //用户收货时间
        orders.setLeaseStartDate(new Date());
        if (orders.getDayId() != null) {
            LeaseDays leaseDays = leaseDaysDao.selectById(orders.getDayId());
            if (leaseDays != null && leaseDays.getDayName() != null) {
                LocalDate leaseStartDate = orders.getLeaseStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                int daysToAdd = Integer.parseInt(leaseDays.getDayName());
                // 计算归还日期
                LocalDate returnDate = leaseStartDate.plusDays(daysToAdd);
                // 设置归还日期
                orders.setReturnDate(Date.from(returnDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
            }
        }
        //积分商品确认收货
//        if (orders.getOrderType() == 4){
//            return ResultUtil.success(jpaRepository.save(orders));
//        }
//        //2.订单分销
//        List<SelfOrderRelation> list = ordersRelationService.findByOrdersId(id); //订单关联的分销列表
//        for (SelfOrderRelation s : list) {
//            //设置收货时间、收货七天后存入用户零钱
//            s.setFinishTime(DateUtils.createTime());
//            ordersRelationService.updateBody(s);
//        }
        return ResultUtil.success(jpaRepository.save(orders));
    }

    /**
     * 检测支付
     *
     * @param id
     * @return
     */
    @Override
    public Result checkPay(Long id) {
        Orders one = jpaRepository.findById(id).orElse(null);
        if (one.getStatus() == 2) {
            return ResultUtil.success(one);
        }
        return ResultUtil.error(-1, "未支付");
    }

    @Override
    public Orders findByOrderNum(String orderNum) {
        return jpaRepository.findByOrderNum(orderNum);
    }

    @Override
    public List<Orders> findByPayNum(String payNum) {
        return jpaRepository.findByPayNum(payNum);
    }


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 零钱支付
     *
     * @param orderId 订单号
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result changePay(String orderId,BigDecimal moneys, Integer classify,Integer orderType, Long userId) {

        reentrantReadWriteLock.writeLock().lock();
        try {
//            String[] ids = id.split(",");
//            for (String s : ids) {
            //1.获取订单、用户信息
            Orders orders = jpaRepository.findById(Long.valueOf(orderId)).orElse(null);
            if (orders == null){
                return ResultUtil.error(-1, "订单不存在");
            }

//            if (orders != null && (orders.getStatus() != 0 && orders.getOrderType() == 5) && orders.getStatus() != 7) {
//                return ResultUtil.error(-200, "订单已经支付过了！");
//            }
            Goods g = goodsJpaRepository.findById(orders.getGoodsId()).orElse(null);
            if (g == null || g.getStatus() != 1) {
                return ResultUtil.error(-1, "商品已被下架或移除，请选择其他商品！");
            }
            UserEntity user = userDao.selectById(orders.getUserId());
            double payMoney = moneys.doubleValue();
            double money = Double.parseDouble(user.getBalance().toString());
            if (payMoney > money) {
                return ResultUtil.error(-1, "零钱余额不足");
            }
            //2.用户零钱减少
            userDao.updateReduceMoney(user.getUserId(), orders.getPayMoney().doubleValue());
            PayDetails payDetails = null;
            //
            if (orderType == 5) {
                // 首次下单
                if (orders.getStatus() == 0) {
                    payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getOrderNum()));
                    if (payDetails == null) {
                        payDetails = new PayDetails();
                        payDetails.setState(1);
                        payDetails.setCreateTime(sdf.format(new Date()));
                        payDetails.setOrderId(orders.getOrderNum());
                        payDetails.setUserId(userId);
                        payDetails.setMoney(moneys.doubleValue());
                        payDetails.setClassify(6);
                        payDetails.setType(2);
                        payDetails.setPayTime(sdf.format(new Date()));
                        payDetailsDao.insert(payDetails);
                    } else {
                        payDetails.setState(1);
                        payDetails.setCreateTime(sdf.format(new Date()));
                        payDetails.setOrderId(orders.getOrderNum());
                        payDetails.setUserId(userId);
                        payDetails.setMoney(moneys.doubleValue());
                        payDetails.setClassify(6);
                        payDetails.setType(2);
                        payDetails.setClassify(6);
                        payDetails.setPayTime(sdf.format(new Date()));
                        payDetailsDao.updateById(payDetails);
                    }
                    // 继续租赁订单
                }else if (orders.getStatus() == 4){
                    orders.setPayWay(classify);
                    orders.setReOrderNum(getGeneralOrder());
                    ordersDao.updateById(orders);
                    payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getReOrderNum()));
                    if (payDetails == null) {
                        payDetails = new PayDetails();
                        payDetails.setState(1);
                        payDetails.setCreateTime(sdf.format(new Date()));
                        payDetails.setOrderId(orders.getReOrderNum());
                        payDetails.setUserId(userId);
                        payDetails.setMoney(moneys.doubleValue());
                        payDetails.setClassify(6);
                        payDetails.setType(5);
                        payDetails.setPayTime(sdf.format(new Date()));
                        payDetailsDao.insert(payDetails);
                    } else {
                        payDetails.setState(1);
                        payDetails.setCreateTime(sdf.format(new Date()));
                        payDetails.setOrderId(orders.getReOrderNum());
                        payDetails.setUserId(userId);
                        payDetails.setMoney(moneys.doubleValue());
                        payDetails.setClassify(6);
                        payDetails.setType(5);
                        payDetails.setPayTime(sdf.format(new Date()));
                        payDetailsDao.updateById(payDetails);
                    }
                }
                // 买断订单
            }else if (orderType == 6){
                orders.setPayWay(classify);
                orders.setBuyOrderNum(getGeneralOrder());
                ordersDao.updateById(orders);
                payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getBuyOrderNum()));
                if (payDetails == null) {
                    payDetails = new PayDetails();
                    payDetails.setState(1);
                    payDetails.setCreateTime(sdf.format(new Date()));
                    payDetails.setOrderId(orders.getBuyOrderNum());
                    payDetails.setUserId(userId);
                    payDetails.setMoney(moneys.doubleValue());
                    payDetails.setClassify(6);
                    payDetails.setType(6);
                    payDetails.setPayTime(sdf.format(new Date()));
                    payDetailsDao.insert(payDetails);
                } else {
                    payDetails.setState(1);
                    payDetails.setOrderId(orders.getBuyOrderNum());
                    payDetails.setClassify(6);
                    payDetails.setType(6);
                    payDetails.setPayTime(sdf.format(new Date()));
                    payDetailsDao.updateById(payDetails);
                }
            }
            //3.零钱减少明细
            UserMoneyDetails moneyDetails = new UserMoneyDetails();
            moneyDetails.setClassify(1);
            moneyDetails.setUserId(user.getUserId());
            moneyDetails.setType(2); //减少
            moneyDetails.setTitle("支付成功");
            moneyDetails.setContent("零钱支付，订单号：" + orders.getOrderNum());
            moneyDetails.setMoney(moneys);
            moneyDetails.setCreateTime(DateUtils.createTime());
            userMoneyDetailsDao.insert(moneyDetails);

            // 发货提醒
//            SelfOrderRemind r = new SelfOrderRemind();
//            r.setOrdersId(orders.getId());
//            r.setCreateTime(DateUtils.createTime());
//            r.setStatus(1);
//            selfOrdersRemindJpaRepository.save(r);

            //订单分销
//            this.fenXiao(orders.getId());
            //4.支付成功处理订单
            orders.setPayWay(6); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
            orders.setPayTime(DateUtils.createTime()); //设置支付时间
            //虚拟商品处理
            if (orders.getOrderType() == 1 || orders.getOrderType() == 5) { //普通订单
//                String[] split = g.getIsExpress().split(",");
//                if (split.length > 0) {
                // 初始化条件变量，默认为false
//                    boolean condition1Met = false;
//                    boolean condition2Met = false;
//                    boolean condition3Met = false;
//                    for (String s : split) {
//                        if ("1".equals(s.trim())) {
//                            condition1Met = true;
//                        } else if ("2".equals(s.trim())) {
//                            condition2Met = true;
//                        } else if ("3".equals(s.trim())) {
//                            condition3Met = true;
//                        }
//                    }
                if (orders.getIsExpress() == 1 || orders.getIsExpress() == 2) { //是否需要发货(1需要发货 2无需发货)
                    //虚拟商品发货，发货成功为确认收货，否则不做变化
//                        SelfGoodsVirtual v = this.sendExpress(orders);
//                        if (v != null){
//                            orders.setVirtualId(v.getId()); //虚拟商品id
//                            orders.setExpressTime(DateUtils.createTime()); //发货时间
//                            orders.setStatus(4); //确认收货
//                            //商品销量增加
//                            goodsService.salesAdd(orders.getGoodsId(), orders.getNumber());
//                            //sku销量增加
//                            skuService.addSales(orders.getSkuId(), orders.getNumber());
                    if (orders.getStatus() == 0) {
                        orders.setStatus(1);
                    }
                } else if (orders.getIsExpress() == 3) { //核销订单
                    // 订单状态：0 待支付 1 已支付待发货 2 已发货待收货 3 已收货 4 待归还 5 归还中 6 已完成 7 继续租赁 8 买断 9 已取消 10 待评价
                    if (orders.getStatus() == 0) {
                        //待收货、待使用
                        orders.setStatus(2);
                    } else {
                        // 已收货待归还
                        orders.setStatus(4);
                    }
                }
            } else {
                orders.setStatus(8);
            }
            if (orders.getCouponUserId() != null) {
                SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(orders.getCouponUserId());
                if (couponUser != null) {
                    couponUser.setStatus(2);
                    selfCouponUserMapper.updateCouponUserId(couponUser);
                }
            }
//            else if (orders.getOrderType() == 2) {
//                orders.setStatus(9);
            //拼团业务处理
//                    this.pink(orders);
//            }
            ordersDao.updateById(orders);
            UserEntity userEntity = userDao.selectById(orders.getUserId());
            if (userEntity != null) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setUserId(String.valueOf(orders.getUserId()));
                messageInfo.setTitle("订单下单通知：" + orders.getOrderNum());
                messageInfo.setContent("您已成功下单");
                messageInfo.setCreateAt(DateUtils.format(new Date()));
                messageInfo.setIsSee("0");
                messageInfo.setStatus(0);
                messageInfo.setState(String.valueOf(4));
                messageInfo.setUserName(userEntity.getUserName());
                iMessageService.save(messageInfo);
            }
            if (userEntity != null && userEntity.getOpenId() != null) {
                List<String> msgList = new ArrayList<>();
                msgList.add("订单下单通知:" + orders.getOrderNum());
                Goods goods = goodsDao.selectById(orders.getGoodsId());
                msgList.add("您的商品:" + goods.getTitle() + "已成功下单");
                msgList.add("商品数量：" + orders.getNumber());
                msgList.add("下单时间：" + orders.getCreateAt());
                msgList.add("下单人：" + userEntity.getUserName());
                String commonInfo = commonInfoService.findOne(522).getValue();
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), commonInfo, msgList, 1);
            }
            if (userEntity != null && userEntity.getClientid() != null) {
                userServices.pushToSingle("您已成功下单", "订单号：" + orders.getOrderNum(), userEntity.getClientid());
            }

            //公众号消息通知
//            userService.orderPay(orders.getUserId(), orders.getOrderNum(), orders.getPayMoney().toString(), orders.getCreateAt());
//            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("零钱支付异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return ResultUtil.success();
    }

    /**
     * 积分支付
     *
     * @param id
     * @return
     */
    @Override
    public Result jiFenPay(String id) {
        //1.获取订单、用户信息
        Orders orders = jpaRepository.findById(Long.valueOf(id)).orElse(null);
        UserEntity user = userJpaRepository.findById(orders.getUserId()).orElse(null);
        double payMoney = orders.getPayMoney().doubleValue();
        String orderJifen = String.valueOf(user.getOrderJifen());
        double money = Double.parseDouble(orderJifen);
        if (payMoney > money) {
            return ResultUtil.error(-1, "积分不足");
        }
        //2.用户积分减少
        userRepository.lessOrderJifen(orders.getPayMoney().toString(), user.getUserId());
//        //3.积分减少明细
//        JiFen jiFen = new JiFen();
//        jiFen.setCreateAt(DateUtil.createTime());
//        jiFen.setNumber(orders.getPayMoney().toString());
//        jiFen.setUserId(String.valueOf(user.getId()));
//        jiFen.setContent("积分兑换");
//        jiFen.setDes("积分兑换:【"+orders.getTitle()+"】-"+payMoney);
//        jiFenService.saveBody(jiFen);
        //4.支付成功处理订单
        orders.setPayWay(7); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分）
        orders.setPayTime(DateUtils.createTime()); //设置支付时间
        orders.setStatus(2); //订单状态（1待付款 2已付款 3已发货 4已收货 5已取消 6退款中 7已退款 8拒绝退款 9拼团中）
        //虚拟商品处理
        if (orders.getIsExpress() == 2) { //是否需要发货(1需要发货 2无需发货 3核销订单)
            //虚拟商品发货，发货成功为确认收货，否则不做变化
//            SelfGoodsVirtual v = this.sendExpress(orders);
//            if (v != null){
//                orders.setVirtualId(v.getId()); //虚拟商品id
//                orders.setExpressTime(DateUtils.createTime()); //发货时间
//                orders.setStatus(4); //确认收货
//            }
        } else if (orders.getIsExpress() == 3) { //核销订单
            orders.setStatus(3); //待收货、待使用
        }
        ordersDao.updateById(orders); //保存订单
        //公众号消息通知
//        userService.orderPay(orders.getUserId(), orders.getOrderNum(), orders.getPayMoney().toString(), orders.getCreateAt());
        return ResultUtil.success();
    }

    //处理虚拟商品
    @Override
    public SelfGoodsVirtual sendExpress(Orders orders) {
        //发送虚拟商品
        SelfGoodsVirtual v = selfGoodsVirtualService.sendGoods(orders.getGoodsId());
        if (v != null) { //有商品
            //订单消息
//            MessageInfo messageInfo = new MessageInfo();
//            messageInfo.setState("4");
//            messageInfo.setTitle("订单支付成功");
//            messageInfo.setContent("您购买的商品【" + orders.getTitle() + "】。请前往： " + v.getLinkUrl() + "中领取兑换，兑换码如下：" + v.getContent());
//            messageInfo.setUserId(orders.getUserId().toString());
//            iMessageService.saveBody(messageInfo);
            return v;
        } else { //无商品时,后台管理发送发货提醒
            SelfOrderRemind r = new SelfOrderRemind();
            r.setOrdersId(orders.getId());
            r.setCreateTime(DateUtils.createTime());
            r.setStatus(1);
            selfOrdersRemindJpaRepository.save(r);
            return null;
        }
    }

    /**
     * 拼团订单处理
     */
    @Override
    public void pink(Orders orders) {
        //拼团业务处理
        if (orders.getOrderType() == 2) {
            //发起拼团
            if (orders.getGroupPinkId() == null) {
                groupPinkService.createPink(orders);
            } else {
                groupPinkService.joinPink(orders);
            }
        }
    }

    /**
     * 发起退款
     *
     * @param id
     * @param refund
     * @return
     */
    @Override
    public Result refund(Long id, String refund) {
        Orders orders = jpaRepository.findById(id).orElse(null);
        orders.setRefund(refund);
        orders.setStatus(6); //6退款中
        return ResultUtil.success(jpaRepository.save(orders));
    }

    /**
     * 确认退款
     *
     * @param id
     * @return
     */
    @Override
    public Result refundMoney(Long id) {
        boolean refund = true;
        //1.获取订单
        Orders orders = jpaRepository.findById(id).orElse(null);
        if (orders == null) {
            return ResultUtil.error(-1, "订单不存在");
        }
        Integer payWay = orders.getPayWay();
        UserEntity user = userDao.selectById(orders.getUserId());
        if (user == null) {
            return ResultUtil.error(-1, "用户不存在或未登录");
        }
        //2.零钱支付退款
        if (payWay == 6) { //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
            //2.1用户信息
            //2.2用户零钱增加
            userDao.updateMoney(user.getUserId(), orders.getPayMoney().doubleValue());
        } else if (payWay == 4 || payWay == 5) {
            //3.支付宝退款
            refund = selfAliPayService.refund(id);
        } else if (payWay == 1 || payWay == 2 || payWay == 3) {
            //4.微信退款
            refund = selfWXService.refund(id);
        }
        //5.删除分销订单
//        List<SelfOrderRelation> list = ordersRelationService.findByOrdersId(id); //订单关联的分销列表
//        for (SelfOrderRelation s : list) {
//            ordersRelationService.delete(s.getId());
//        }
        //6.处理订单已退款
        if (refund) {
            orders.setStatus(9); //已退款
            jpaRepository.save(orders);
            if (orders.getCouponUserId() != null) {
                SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(orders.getCouponUserId());
                couponUser.setStatus(1);
                selfCouponUserMapper.updateCouponUserId(couponUser);
            }
            //2.3零钱增加明细
            UserMoneyDetails moneyDetails = new UserMoneyDetails();
            moneyDetails.setClassify(1);
            moneyDetails.setUserId(user.getUserId());
            moneyDetails.setType(1); //增加
            moneyDetails.setTitle("自营商城退款，订单号：" + orders.getOrderNum());
            moneyDetails.setContent("您的订单已取消，退款已原路返回");
            moneyDetails.setMoney(orders.getPayMoney());
            moneyDetails.setCreateTime(DateUtils.createTime());
            moneyDetails.setRelationId(orders.getId());
            moneyDetails.setState(2);
            userMoneyDetailsDao.insert(moneyDetails);
            //公众号消息：退款
//            userService.orderRefund(orders.getUserId(), orders.getPayMoney().toString(), orders.getOrderNum());
            if (StringUtils.isNotEmpty(user.getOpenId())) {
                CommonInfo msgs2 = commonRepository.findOne(511);
                List<String> msgLists2 = new ArrayList<>();
                msgLists2.add(orders.getOrderNum());
                String title = orders.getTitle();
                if (title.length() > 15) {
                    title = title.substring(0, 15) + "...";
                }
                msgLists2.add(title);
                msgLists2.add(String.valueOf(orders.getPayMoney()));
                AccountTokenServiceImpl.sendMsg(user.getOpenId(), msgs2.getValue(), msgLists2, 1);
            }
            return ResultUtil.success();
        } else {
            return ResultUtil.error(-1, "退款失败");
        }
    }

    /**
     * 拒绝退款
     *
     * @param ordersId
     * @param refusedRefund
     * @return
     */
    @Override
    public Result refusedRefund(Long ordersId, String refusedRefund) {
        Orders order = jpaRepository.findById(ordersId).orElse(null);
        //拒绝退款回到待发货状态
        order.setStatus(1); //订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价
        order.setRefusedRefund(refusedRefund);
        order = jpaRepository.save(order);
        //订单消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState("4");
        messageInfo.setTitle("退款失败通知");
        messageInfo.setContent("您发起的退款申请退款失败，订单编号：" + order.getOrderNum() + "退款失败原因：" + refusedRefund);
        messageInfo.setUserId(order.getUserId().toString());
        iMessageService.saveBody(messageInfo);
        return ResultUtil.success(order);
    }

    /**
     * 订单分销处理
     */
    @Override
    public void fenXiao(Long ordersId) {
        //1.查询订单
        Orders o = jpaRepository.getOne(ordersId);
        BigDecimal commissionPrice = o.getCommissionPrice(); //订单的佣金
        //2.查询订单用户
        UserEntity u = userJpaRepository.getOne(o.getUserId());
//        BigDecimal grade = new BigDecimal(u.getGrade()); //用户佣金等级
        //BigDecimal sum = commissionPrice.multiply(grade); //计算可以分配佣金
        BigDecimal sum = commissionPrice; //计算可以分配佣金
        //分销金额不能0
        if (sum == null || sum.doubleValue() < 0.01) {
            return;
        }
        //3.判断是否是会员
        if (StringUtils.isNotEmpty(u.getInvitationCode())) { //用户是会员
            //4.判断有无上级
            String invitation = u.getInviterCode();
            if (StringUtils.isNotEmpty(invitation)) { //用户有上级
                UserEntity shangJiUser = userRepository.getUserByRelationId(invitation);
                //5.计算上级分配佣金
                CommonInfo one = commonRepository.findOne(91);
                String value = commonRepository.findOne(43).getValue(); //上级佣金比例
                BigDecimal shangJiMoney = sum.multiply(new BigDecimal(value));
                //6.计算用户分配的佣金
                BigDecimal selfMoney = sum.subtract(shangJiMoney);
                //区分是二级佣金还是三级佣金  1  二级佣金  2  三级佣金
                if ("1".equals(one.getValue())) {
                    //7.保存团长的佣金明细
                    /*
                    相同部分：订单id、状态1未到账、订单实付金额、商品id、商品照片、商品标题
                    区分上下级：用户id、用户手机号、订单佣金、分销明细、来源（1下级 2自己）、下级用户id、下级用户名称
                     */
                    SelfOrderRelation s = new SelfOrderRelation();
                    s.setOrderId(o.getId());
                    s.setStatus(1);
                    s.setPayMoney(o.getPayMoney());
                    s.setGoodsId(o.getGoodsId());
                    s.setGoodsImg(o.getImg());
                    s.setGoodsTitle(o.getTitle());
                    //不同部分：佣金不同、代理佣金、团队
                    s.setUserId(shangJiUser.getUserId()); //团长id
                    s.setUserName(shangJiUser.getUserName()); //团长姓名
                    s.setPhone(shangJiUser.getPhone()); //团长手机号
                    s.setCommissionPrice(shangJiMoney); //佣金不同
                    s.setCommissionMoney(selfMoney); //代理佣金
                    s.setDetail("【直属购买】" + o.getTitle());
                    s.setMoneyFrom(1); //团队
                    s.setLowerUserId(u.getUserId());
                    s.setLowerUserName(u.getUserName());
                    ordersRelationService.saveBody(s);
                    //8.保存代理的佣金明细
                    SelfOrderRelation s1 = new SelfOrderRelation();
                    s1.setOrderId(o.getId());
                    s1.setStatus(1);
                    s1.setPayMoney(o.getPayMoney());
                    s1.setGoodsId(o.getGoodsId());
                    s1.setGoodsImg(o.getImg());
                    s1.setGoodsTitle(o.getTitle());
                    s1.setUserId(u.getUserId()); //用户id
                    s1.setUserName(u.getUserName()); //用户姓名
                    s1.setPhone(u.getPhone()); //用户手机号
                    s1.setCommissionPrice(selfMoney); //佣金不同
                    s1.setCommissionMoney(shangJiMoney); //团长佣金
                    s1.setDetail("【自己购买】" + o.getTitle());
                    s1.setMoneyFrom(2); //自己
                    ordersRelationService.saveBody(s1);
                } else {
                    //非直属佣金抽成
                    CommonInfo one1 = commonRepository.findOne(92);
                    BigDecimal feiMoney = sum.multiply(new BigDecimal(one1.getValue()));
                    //6.计算用户分配的佣金
                    //selfMoney = AmountCalUtils.sub(selfMoney, feiMoney);
                    selfMoney = selfMoney.subtract(feiMoney);
                    //7.保存团长的佣金明细
                    /*
                    相同部分：订单id、状态1未到账、订单实付金额、商品id、商品照片、商品标题
                    区分上下级：用户id、用户手机号、订单佣金、分销明细、来源（1下级 2自己）、下级用户id、下级用户名称
                     */
                    SelfOrderRelation s = new SelfOrderRelation();
                    s.setOrderId(o.getId());
                    s.setStatus(1);
                    s.setPayMoney(o.getPayMoney());
                    s.setGoodsId(o.getGoodsId());
                    s.setGoodsImg(o.getImg());
                    s.setGoodsTitle(o.getTitle());
                    //不同部分：佣金不同、代理佣金、团队
                    s.setUserId(shangJiUser.getUserId()); //团长id
                    s.setUserName(shangJiUser.getUserName()); //团长姓名
                    s.setPhone(shangJiUser.getPhone()); //团长手机号
                    s.setCommissionPrice(shangJiMoney); //佣金不同
                    s.setCommissionMoney(selfMoney); //代理佣金
                    s.setDetail("【直属购买】" + o.getTitle());
                    s.setMoneyFrom(1); //直属
                    s.setLowerUserId(u.getUserId());
                    s.setLowerUserName(u.getUserName());
                    ordersRelationService.saveBody(s);
                    //8.保存代理的佣金明细
                    SelfOrderRelation s1 = new SelfOrderRelation();
                    s1.setOrderId(o.getId());
                    s1.setStatus(1);
                    s1.setPayMoney(o.getPayMoney());
                    s1.setGoodsId(o.getGoodsId());
                    s1.setGoodsImg(o.getImg());
                    s1.setGoodsTitle(o.getTitle());
                    s1.setUserId(u.getUserId()); //用户id
                    s1.setUserName(u.getUserName()); //团长姓名
                    s1.setPhone(u.getPhone()); //用户手机号
                    s1.setCommissionPrice(selfMoney); //佣金不同
                    s1.setCommissionMoney(shangJiMoney); //团长佣金
                    s1.setDetail("【自己购买】" + o.getTitle());
                    s1.setMoneyFrom(2); //自己
                    ordersRelationService.saveBody(s1);
                    //非直属佣金
                    UserEntity feiUser = userRepository.getUserByRelationId(shangJiUser.getInviterCode());
                    if (feiUser != null) {
                        SelfOrderRelation s2 = new SelfOrderRelation();
                        s2.setOrderId(o.getId());
                        s2.setStatus(1);
                        s2.setPayMoney(o.getPayMoney());
                        s2.setGoodsId(o.getGoodsId());
                        s2.setGoodsImg(o.getImg());
                        s2.setGoodsTitle(o.getTitle());
                        s2.setUserId(feiUser.getUserId()); //团长id
                        s2.setUserName(feiUser.getUserName()); //团长姓名
                        s2.setPhone(feiUser.getPhone()); //团长手机号
                        s2.setCommissionPrice(feiMoney); //佣金不同
                        s2.setCommissionMoney(selfMoney); //代理佣金
                        s2.setDetail("【非直属购买】" + o.getTitle());
                        s2.setMoneyFrom(3); //非直属
                        s2.setLowerUserId(u.getUserId());
                        s2.setLowerUserName(u.getUserName());
                        ordersRelationService.saveBody(s2);
                    }
                }
            }
        }
    }


    @Override
    public Result pendingOrder() {
        return ResultUtil.success(ordersRepository.pendingOrder());
    }

    @Override
    public Result remind(Long orderId) {
        SelfOrderRemind remind = selfOrdersRemindJpaRepository.findByOrdersId(orderId);
        Orders orders = jpaRepository.findById(orderId).orElse(null);
        if (remind != null) {
            if (remind.getStatus() == 1) {
                return ResultUtil.error(0, "已经提醒发货");
            } else {
                remind.setStatus(1);
                selfOrdersRemindJpaRepository.save(remind);
            }
        } else {
            SelfOrderRemind r = new SelfOrderRemind();
            r.setOrdersId(orderId);
            r.setCreateTime(DateUtils.createTime());
            r.setStatus(1);
            selfOrdersRemindJpaRepository.save(r);
        }
        return ResultUtil.success();
    }

    @Override
    public Result remindOrder() {
        // 构造自定义查询条件
        Specification<SelfOrderRemind> queryCondition = new Specification<SelfOrderRemind>() {
            @Override
            public Predicate toPredicate(Root<SelfOrderRemind> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<SelfOrderRemind> all = selfOrdersRemindJpaRepository.findAll(queryCondition);
        for (SelfOrderRemind r : all) {
            r.setStatus(2);
            selfOrdersRemindJpaRepository.save(r);
        }
        return ResultUtil.success(all.size());
    }

    @Override
    public Result payCount(Long userId) {
        Map<String, Object> map = new HashMap<>();
        //今日付款数量
        String date = DateUtils.createDate(); //今日
        int count1 = ordersRepository.todayPayCount(userId, date);
        //本月付款数量
        String month = date.substring(0, 6); //本月
        int count2 = ordersRepository.monthPayCount(userId, month);
        //本月付款金额
        Double count3 = ordersRepository.monthPayNumber(userId, month);
        //上月付款金额
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1); //获取上个月的日期（上个月的今天）
        Date date3 = cal.getTime();
        String lastMonth = sdf.format(date3);
        Double count4 = ordersRepository.lastMonthPayNumber(userId, lastMonth);
        map.put("count1", count1);
        map.put("count2", count2);
        map.put("count3", count3 == null ? 0 : count3);
        map.put("count4", count4 == null ? 0 : count4);
        return ResultUtil.success(map);
    }

    /**
     * 订单数量统计
     *
     * @param userId
     * @return
     */
    @Override
    public Result count(Long userId) {
        Map<String, Object> map = new HashMap<>();
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        int count5 = 0;
        //订单状态（1待付款 2已付款 3已发货 4已收货 5已取消 6退款中 7已退款 8拒绝退款 9拼团中 10已评价）
        List<Orders> list = ordersRepository.findByUserId(userId);
        for (Orders o : list) {
            switch (o.getStatus()) {
                case 1:
                    count1++;
                    break;
                case 2:
                    count2++;
                    break;
                case 3:
                    count3++;
                    break;
                case 4:
                    count4++;
                    break;
                case 6:
                    count5++;
                    break;
            }
        }
        map.put("count1", count1); //待付款
        map.put("count2", count2); //代发货
        map.put("count3", count3); //待收货
        map.put("count4", count4); //待评价
        map.put("count5", count5); //退款
        return ResultUtil.success(map);
    }

    @Override
    public Result findExpress(String expressNumber) {
        String appCode = commonRepository.findOne(94).getValue();
        String host = "https://wuliu.market.alicloudapi.com";// 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/kdi";  // 【2】后缀
        String appcode = appCode; // 【3】开通服务后 买家中心-查看AppCode
        String no = expressNumber.trim();// 【4】请求参数，详见文档描述
        String type = ""; //  【4】请求参数，不知道可不填 95%能自动识别
        String urlSend = host + path + "?no=" + no + "&type=" + type;  // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLCon.getInputStream());
                JSONObject jsonObject = JSONObject.parseObject(json);
                JSONObject phone = jsonObject.getJSONObject("result");
                String phone1 = (String) phone.get("number");
                String deliverystatus = (String) phone.get("deliverystatus");
                if (phone1 != null) {
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(.*?):");
                    java.util.regex.Matcher matcher = pattern.matcher(phone1);
                    if (matcher.find()) {
                        phone1 = matcher.group(1); // 获取第一个捕获组，即冒号前面的部分
                    }
                    phone.put("number", phone1);
                }
                String statusCode = (String) jsonObject.get("status");
                if (statusCode != null && !statusCode.equals("0")) {
                    return Result.failed( "物流单号有误请联系发货人");
                }
                return Result.success(JSONObject.parseObject(json));
            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode `not exists`")) {
                    System.out.println("AppCode错误 ");
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    System.out.println("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    System.out.println("参数错误");
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    System.out.println("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    System.out.println("套餐包次数用完 ");
                } else {
                    System.out.println("参数名错误 或 其他错误");
                    System.out.println(error);
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("URL格式错误");
        } catch (UnknownHostException e) {
            System.out.println("URL地址错误");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failed("查询失败");
    }

    @Override
    public String findExpressByOrderNum(String expressNumber) {
        String appCode = commonRepository.findOne(94).getValue();
        String host = "https://wuliu.market.alicloudapi.com";// 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/kdi";  // 【2】后缀
        String appcode = appCode; // 【3】开通服务后 买家中心-查看AppCode
        String no = expressNumber.trim();// 【4】请求参数，详见文档描述
        String type = ""; //  【4】请求参数，不知道可不填 95%能自动识别
        String urlSend = host + path + "?no=" + no + "&type=" + type;  // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLCon.getInputStream());
                JSONObject jsonObject = JSONObject.parseObject(json);
                JSONObject phone = jsonObject.getJSONObject("result");
                String phone1 = (String) phone.get("number");
                String deliverystatus = (String) phone.get("deliverystatus");
                if (phone1 != null) {
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(.*?):");
                    java.util.regex.Matcher matcher = pattern.matcher(phone1);
                    if (matcher.find()) {
                        phone1 = matcher.group(1); // 获取第一个捕获组，即冒号前面的部分
                    }
                    phone.put("number", phone1);
                }
                return deliverystatus;
            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode `not exists`")) {
                    System.out.println("AppCode错误 ");
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    System.out.println("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    System.out.println("参数错误");
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    System.out.println("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    System.out.println("套餐包次数用完 ");
                } else {
                    System.out.println("参数名错误 或 其他错误");
                    System.out.println(error);
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("URL格式错误");
        } catch (UnknownHostException e) {
            System.out.println("URL地址错误");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "查询失败";
    }


    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 核销订单
     *
     * @return
     */
    @Override
    public Result isExpress(String ids) {
        String[] list = ids.split(",");
        for (int i = 0; i < list.length; i++) {
            Long id = Long.valueOf(list[i]);
            this.orderConfirm(id);
        }
        return ResultUtil.success();
    }

    /**
     * 购物车订单BUG:取消订单、返还优惠券
     *
     * @param list
     * @return
     */
    @Override
    public void cancel(List<Orders> list) {
        for (Orders orders : list) {
            if (orders.getStatus() == 0) {
                orders.setStatus(9);
                jpaRepository.save(orders);
                //sku库存+1
                skuService.addStock(orders.getSkuId(), orders.getNumber());
                //优惠券
                if (orders.getCouponUserId() != null) {
                    couponUserJpaRepository.backCoupon(orders.getCouponUserId());
                }
            }
        }
    }

    @Override
    public Result calculateTheNumberDay(Long id, Long userId) {
        Orders orders = ordersDao.selectById(id);
        if (orders == null) {
            return ResultUtil.error(-1, "订单不存在");
        }
        if (orders.getGoodsId() != null) {
            Goods goods = goodsDao.selectById(orders.getGoodsId());
            if (goods == null) {
                return ResultUtil.error(-1, "商品不存在");
            }
            // 归还日期
            Date date = orders.getReturnDate();
            // 当前日期
            LocalDate localDate = LocalDate.now();
            LocalDate localOrderDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long between = ChronoUnit.DAYS.between(localOrderDate, localDate);
            BigDecimal money;
            if (between > 0) {
                money = goods.getOverdueRent().multiply(BigDecimal.valueOf(between));
                return ResultUtil.success(money);
            } else {
                return ResultUtil.success(BigDecimal.ZERO);
            }
        }
        return ResultUtil.error(-1, "商品不存在");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payOrderBack(Long id, BigDecimal payMoney, Integer classify) {
        Orders orders = ordersDao.selectById(id);
        if (orders != null) {
            UserEntity user = userDao.selectById(orders.getUserId());
            PayDetails payDetails = payDetailsDao.selectByOrderId(orders.getOrderNum());
            if (payDetails != null) {
                payDetails.setState(1);
//                payDetails.setType(2);
                payDetails.setPayTime(DateUtils.createTime());
                payDetailsDao.updateById(payDetails);
            }
            if (orders.getOrderType() == 1 || orders.getOrderType() == 5) { //普通订单

                if (orders.getIsExpress() == 1 || orders.getIsExpress() == 2) { //是否需要发货(1需要发货 2无需发货)
                    if (orders.getStatus() == 0) {
                        orders.setStatus(1);
                    }
                } else if (orders.getIsExpress() == 3) { //核销订单
                    // 订单状态：0 待支付 1 已支付待发货 2 已发货待收货 3 已收货 4 待归还 5 归还中 6 已完成 7 继续租赁 8 买断 9 已取消 10 待评价
                    if (orders.getStatus() == 0) {
                        //待收货、待使用
                        orders.setStatus(2);
                    } else {
                        // 已收货待归还
                        orders.setStatus(4);
                    }
                }
            } else {
                orders.setStatus(8);
            }
            orders.setPayTime(DateUtils.createTime());
            ordersDao.updateById(orders);
            if (orders.getCouponUserId() != null) {
                SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(orders.getCouponUserId());
                if (couponUser != null) {
                    couponUser.setStatus(2);
                    selfCouponUserMapper.updateCouponUserId(couponUser);
                }
            }
            //3.零钱减少明细
            UserMoneyDetails moneyDetails = new UserMoneyDetails();
            moneyDetails.setClassify(1);
            moneyDetails.setUserId(user.getUserId());
            moneyDetails.setType(2); //减少
            moneyDetails.setTitle("支付成功");
            moneyDetails.setContent("支付成功，订单号：" + orders.getOrderNum());
            if (orders.getOrderType() == 6) {
                moneyDetails.setMoney(orders.getPrice());
            } else {
                moneyDetails.setMoney(orders.getPayMoney());
            }
            moneyDetails.setCreateTime(DateUtils.createTime());
            userMoneyDetailsDao.insert(moneyDetails);
//            userMoneyDetailsDao.insert(userMoneyDetails);
            UserEntity userEntity = userDao.selectById(orders.getUserId());
            if (userEntity != null) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setUserId(String.valueOf(orders.getUserId()));
                messageInfo.setTitle("订单下单通知：" + orders.getOrderNum());
                messageInfo.setContent("您已成功下单");
                messageInfo.setCreateAt(DateUtils.format(new Date()));
                messageInfo.setIsSee("0");
                messageInfo.setStatus(0);
                messageInfo.setState(String.valueOf(4));
                messageInfo.setUserName(userEntity.getUserName());
                iMessageService.save(messageInfo);
            }
            if (userEntity != null && userEntity.getOpenId() != null) {
                List<String> msgList = new ArrayList<>();
                msgList.add("订单下单通知:" + orders.getOrderNum());
                Goods goods = goodsDao.selectById(orders.getGoodsId());
                msgList.add("您的商品:" + goods.getTitle() + "已成功下单");
                msgList.add("商品数量：" + orders.getNumber());
                msgList.add("下单时间：" + orders.getCreateAt());
                msgList.add("下单人：" + userEntity.getUserName());
                String commonInfo = commonInfoService.findOne(522).getValue();
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), commonInfo, msgList, 1);
            }
//            if (userEntity != null && userEntity.getClientid() != null) {
//                userServices.pushToSingle("您已成功下单", "订单号：" + orders.getOrderNum(), userEntity.getClientid());
//            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void supplementMoneyBack(Long id, BigDecimal payMoney, Integer classify) {
        Orders orders = ordersDao.selectById(id);
        if (orders != null) {
            UserEntity user = userDao.selectById(orders.getUserId());
            PayDetails payDetails = payDetailsDao.selectByOrderId(orders.getSupplementNum());
            if (payDetails != null) {
                payDetails.setState(1);
                payDetails.setType(4);
                payDetails.setPayTime(DateUtils.createTime());
                payDetailsDao.updateById(payDetails);
            }
            if (orders.getSupplementStatus() == 2) {
                orders.setSupplementStatus(1);
                orders.setSupplementTime(new Date());
                orders.setReturnTime(new Date());
                if (orders.getExpressNumber() == null) {
                    orders.setStatus(4);
                } else {
                    orders.setStatus(5);
                }
                ordersDao.updateById(orders);
            }
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(user.getUserId());
            userMoneyDetails.setTitle("支付补缴租金订单：" + orders.getOrderNum());
            userMoneyDetails.setContent("补缴支付金额：" + payMoney);
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(6);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(new Date());
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsDao.insert(userMoneyDetails);
            UserEntity userEntity = userDao.selectById(orders.getUserId());
            if (userEntity != null) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setUserId(String.valueOf(orders.getUserId()));
                messageInfo.setTitle("订单补缴租金通知：" + orders.getOrderNum());
                messageInfo.setContent("您已成功补缴租金");
                messageInfo.setCreateAt(DateUtils.format(new Date()));
                messageInfo.setIsSee("0");
                messageInfo.setStatus(0);
                messageInfo.setState(String.valueOf(4));
                messageInfo.setUserName(userEntity.getUserName());
                iMessageService.save(messageInfo);
            }
            if (userEntity != null && userEntity.getOpenId() != null) {
                List<String> msgList = new ArrayList<>();
                msgList.add("订单补缴租金通知:" + orders.getOrderNum());
                Goods goods = goodsDao.selectById(orders.getGoodsId());
                msgList.add("您的商品:" + goods.getTitle() + "已成功补缴租金");
                msgList.add("商品数量：" + orders.getNumber());
                msgList.add("下单时间：" + orders.getCreateAt());
                msgList.add("下单人：" + userEntity.getUserName());
                String commonInfo = commonInfoService.findOne(522).getValue();
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), commonInfo, msgList, 1);
            }
            if (userEntity != null && userEntity.getClientid() != null) {
                userServices.pushToSingle("您已成功补缴租金", "订单号：" + orders.getOrderNum(), userEntity.getClientid());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result continuingLease(Orders entity, Long userId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            Orders entityOrders = ordersDao.selectById(entity.getId());
            if (entityOrders == null) {
                return ResultUtil.error(-1, "订单不存在");
            }
            if (entity.getPayWay() == 6) {
                PayDetails payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", entity.getReOrderNum()));
                if (payDetails == null) {
                    payDetails = new PayDetails();
                    payDetails.setOrderId(entity.getReOrderNum());
                    payDetails.setUserId(userId);
                    payDetails.setMoney(entity.getContinuingLeaseMoney().doubleValue());
                    payDetails.setClassify(entity.getPayWay());
                    payDetails.setType(5);
                    payDetailsDao.insert(payDetails);
                } else if (payDetails.getState() == 0 || payDetails.getState() == 2) {
                    return ResultUtil.error(-1,"您未支付成功");
                }
            }
            entity.setUserId(userId);
            //校验下单金额
//            Result check = this.checkOrderMoney(entity);
//            if (check.getStatus() != 0) {
//                return check;
//            }
            if (entity.getOrderType() != 5 && entity.getOrderType() != 7) {
                return ResultUtil.error(-1, "订单类型错误");
            }
            //检测复购
            entityOrders.setIsRepeat(2);
            entityOrders.setIsExpress(entity.getIsExpress());
            List<ContinuingOrder> continuingOrders = continuingOrderDao.selectList(new QueryWrapper<ContinuingOrder>()
                    .eq("order_id", entityOrders.getId()));
            if (continuingOrders.isEmpty()) {
                ContinuingOrder continuingOrder = new ContinuingOrder();
                continuingOrder.setOrderId(entityOrders.getId());
                continuingOrder.setStartDate(entityOrders.getLeaseStartDate());
                continuingOrder.setEndDate(entityOrders.getReturnDate());
                continuingOrder.setLeasePrice(entityOrders.getGrossRent());
                if (entityOrders.getCouponMoney() != null) {
                    continuingOrder.setDiscountMoney(entityOrders.getCouponMoney());
                }
                continuingOrder.setLeaseDay(entityOrders.getLeaseTerm());
                continuingOrder.setRemark(entityOrders.getDescrition());
                continuingOrder.setCreateTime(new Date());
                continuingOrder.setSort(1);
                int result = continuingOrderDao.insert(continuingOrder);
                if (result <= 0) {
                    return ResultUtil.error(-1, "继续租赁失败，请检查后重试");
                }
            } else {
                ContinuingOrder continuingOrder1 = continuingOrderDao.selectOne(new QueryWrapper<ContinuingOrder>()
                        .eq("order_id", entityOrders.getId())
                        .orderByDesc("sort")
                        .last("LIMIT 1"));
                ContinuingOrder continuingOrder = new ContinuingOrder();
                continuingOrder.setOrderId(entityOrders.getId());
                continuingOrder.setStartDate(entityOrders.getLeaseStartDate());
                continuingOrder.setEndDate(entityOrders.getReturnDate());
                continuingOrder.setLeasePrice(entityOrders.getGrossRent());
                if (entityOrders.getCouponMoney() != null) {
                    continuingOrder.setDiscountMoney(entityOrders.getCouponMoney());
                }
                continuingOrder.setLeaseDay(entityOrders.getLeaseTerm());
                continuingOrder.setRemark(entityOrders.getDescrition());
                continuingOrder.setCreateTime(new Date());
                continuingOrder.setSort(continuingOrder1 != null ? continuingOrder1.getSort() + 1 : 1);
                int result = continuingOrderDao.insert(continuingOrder);
                if (result <= 0) {
                    return ResultUtil.error(-1, "继续租赁失败，请检查后重试");
                }
            }
            int count = ordersRepository.checkIsRepeat(entity.getUserId(), entity.getGoodsId());
            if (count > 0) {
                entityOrders.setIsRepeat(1);
            }
            BigDecimal money = entityOrders.getGrossRent();
            if (entity.getDayId() != null) {
                LeaseDays leaseDays = leaseDaysDao.selectById(entity.getDayId());
                if (leaseDays != null) {
                    if (entityOrders.getReturnDate() != null) {
                        LocalDate leaseEndDate = entityOrders.getReturnDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        int daysToAdd = Integer.parseInt(leaseDays.getDayName());
                        // 计算归还日期
                        LocalDate returnDate = leaseEndDate.plusDays(daysToAdd);
                        // 设置归还日期
                        entityOrders.setReturnDate(Date.from(returnDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
                        entityOrders.setLeaseTerm(Integer.valueOf(leaseDays.getDayName()));
                    }
                    if (entity.getCouponMoney() != null) {
                        entityOrders.setGrossRent(entity.getDailyRent().multiply(new BigDecimal(leaseDays.getDayName()))
                                .subtract(entity.getCouponMoney())
                                .add(entity.getPostagePrice()));
                        entityOrders.setPayMoney(entityOrders.getGrossRent());
                    } else {
                        entityOrders.setGrossRent(entity.getDailyRent().multiply(new BigDecimal(leaseDays.getDayName()))
                                .add(entity.getPostagePrice()));
                        entityOrders.setPayMoney(entityOrders.getGrossRent());
                    }
                }
            }
            entityOrders.setTotalContinueMoney(entityOrders.getGrossRent().add(money));
            entityOrders.setStatus(4);
            entityOrders.setContinueDate(new Date());
            int save = ordersDao.updateByOrderId(entityOrders);
            if (save <= 0) {
                return ResultUtil.error(-1, "继续租赁失败，请检查后重试");
            }
            return ResultUtil.success(entityOrders.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存订单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return ResultUtil.error(-200, "系统繁忙，请稍后再试！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void continuingLeaseOrder(Orders entity, Long userId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            Orders entityOrders = ordersDao.selectById(entity.getId());
            if (entityOrders == null) {
                log.error("订单不存在");
            }
            entity.setUserId(userId);
            //校验下单金额
            Result check = this.checkOrderMoney(entity);
//            if (check.getStatus() != 0) {
//                return;
//            }
            if (entity.getOrderType() != 5 && entity.getOrderType() != 7) {
                 log.error("订单类型错误");
            }
            //检测复购
            entityOrders.setIsRepeat(2);
            entityOrders.setIsExpress(entity.getIsExpress());
            List<ContinuingOrder> continuingOrders = continuingOrderDao.selectList(new QueryWrapper<ContinuingOrder>()
                    .eq("order_id", entityOrders.getId()));
            if (continuingOrders.isEmpty()) {
                ContinuingOrder continuingOrder = new ContinuingOrder();
                continuingOrder.setOrderId(entityOrders.getId());
                continuingOrder.setStartDate(entityOrders.getLeaseStartDate());
                continuingOrder.setEndDate(entityOrders.getReturnDate());
                continuingOrder.setLeasePrice(entityOrders.getGrossRent());
                if (entityOrders.getCouponMoney() != null) {
                    continuingOrder.setDiscountMoney(entityOrders.getCouponMoney());
                }
                continuingOrder.setLeaseDay(entityOrders.getLeaseTerm());
                continuingOrder.setRemark(entityOrders.getDescrition());
                continuingOrder.setCreateTime(new Date());
                continuingOrder.setSort(1);
                int result = continuingOrderDao.insert(continuingOrder);
                if (result <= 0) {
                    log.error("继续租赁失败，请检查后重试");
                }
            } else {
                ContinuingOrder continuingOrder1 = continuingOrderDao.selectOne(new QueryWrapper<ContinuingOrder>()
                        .eq("order_id", entityOrders.getId())
                        .orderByDesc("sort")
                        .last("LIMIT 1"));
                ContinuingOrder continuingOrder = new ContinuingOrder();
                continuingOrder.setOrderId(entityOrders.getId());
                continuingOrder.setStartDate(entityOrders.getLeaseStartDate());
                continuingOrder.setEndDate(entityOrders.getReturnDate());
                continuingOrder.setLeasePrice(entityOrders.getGrossRent());
                if (entityOrders.getCouponMoney() != null) {
                    continuingOrder.setDiscountMoney(entityOrders.getCouponMoney());
                }
                continuingOrder.setLeaseDay(entityOrders.getLeaseTerm());
                continuingOrder.setRemark(entityOrders.getDescrition());
                continuingOrder.setCreateTime(new Date());
                continuingOrder.setSort(continuingOrder1 != null ? continuingOrder1.getSort() + 1 : 1);
                int result = continuingOrderDao.insert(continuingOrder);
                if (result <= 0) {
                    log.error("继续租赁失败，请检查后重试");
                }
            }
            int count = ordersRepository.checkIsRepeat(entity.getUserId(), entity.getGoodsId());
            if (count > 0) {
                entityOrders.setIsRepeat(1);
            }
            BigDecimal money = entityOrders.getGrossRent();
            if (entity.getDayId() != null) {
                LeaseDays leaseDays = leaseDaysDao.selectById(entity.getDayId());
                if (leaseDays != null) {
                    if (entityOrders.getReturnDate() != null) {
                        LocalDate leaseEndDate = entityOrders.getReturnDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        int daysToAdd = Integer.parseInt(leaseDays.getDayName());
                        // 计算归还日期
                        LocalDate returnDate = leaseEndDate.plusDays(daysToAdd);
                        // 设置归还日期
                        entityOrders.setReturnDate(Date.from(returnDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
                        entityOrders.setLeaseTerm(Integer.valueOf(leaseDays.getDayName()));
                    }
                    if (entity.getCouponMoney() != null) {
                        entityOrders.setGrossRent(entity.getDailyRent().multiply(new BigDecimal(leaseDays.getDayName()))
                                .subtract(entity.getCouponMoney())
                                .add(entity.getPostagePrice()));
                        entityOrders.setPayMoney(entityOrders.getGrossRent());
                    } else {
                        entityOrders.setGrossRent(entity.getDailyRent().multiply(new BigDecimal(leaseDays.getDayName()))
                                .add(entity.getPostagePrice()));
                        entityOrders.setPayMoney(entityOrders.getGrossRent());
                    }
                }
            }
            entityOrders.setTotalContinueMoney(entityOrders.getGrossRent().add(money));
            entityOrders.setStatus(4);
            entityOrders.setContinueDate(new Date());
            int save = ordersDao.updateByOrderId(entityOrders);
            if (save <= 0) {
                log.error("继续租赁失败，请检查后重试");
            }
             ResultUtil.success(entityOrders.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存订单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        log.error("系统繁忙，请稍后再试！");
    }

    @Override
    public Result returnGoods(Orders entity, Long userId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            Orders entityOrders = ordersDao.selectById(entity.getId());
            if (entityOrders == null) {
                return ResultUtil.error(-1, "订单不存在");
            }
            String[] status = new String[]{"0", "1", "2", "3", "6", "8", "9", "10"};
            List<String> statusList = Arrays.asList(status);
            // 检查订单状态是否在这个列表中
            if (statusList.contains(entityOrders.getStatus())) {
                return ResultUtil.error(-1, "订单状态错误");
            }
            UserEntity userEntity = userDao.selectById(userId);
            if (userEntity == null) {
                return ResultUtil.error(-1, "用户不存在");
            }

            if (entity.getExpressNumber() != null) {
                entityOrders.setExpressNumber(entity.getExpressNumber());
                entityOrders.setStatus(5);
            }
            entityOrders.setReturnTime(new Date());
            int save = ordersDao.updateById(entityOrders);
            if (save <= 0) {
                return ResultUtil.error(-1, "订单状态错误");
            }
            return ResultUtil.success(entityOrders.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存订单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return ResultUtil.error(-200, "系统繁忙，请稍后再试！");
    }

    @Override
    public Integer selectOrdersCountStatisticsByYear(Integer flag, String time, Integer status) {
        return ordersDao.selectOrdersCountStatisticsByYear(flag, time, status);
    }

    @Override
    public Result updateOrderStatus(Long id, Integer status) {
        Orders orders = ordersDao.selectById(id);
        if (orders == null) {
            return ResultUtil.error(-1, "订单不存在");
        }
        orders.setStatus(status);
        int result = ordersDao.updateById(orders);
        if (result <= 0) {
            return ResultUtil.error(-1, "订单状态修改失败");
        }
        return ResultUtil.success();
    }

    @Override
    public Result changePaySupplementMoney(Long userId, String orderId, BigDecimal supplementMoney) {
        Orders order = ordersDao.selectById(orderId);
        if (order == null) {
            return ResultUtil.error(-1, "订单不存在");
        }
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity != null && userEntity.getBalance().compareTo(supplementMoney) >= 0) {
            userEntity.setBalance(userEntity.getBalance().subtract(supplementMoney));
            if (order.getSupplementStatus() == null || order.getSupplementStatus() == 2) {
                order.setSupplementStatus(1);
                order.setSupplementMoney(supplementMoney);
                order.setSupplementTime(new Date());
                order.setSupplementNum(getGeneralOrder());
                order.setReturnTime(new Date());
                if (order.getExpressNumber() == null) {
                    order.setStatus(4);
                } else {
                    order.setStatus(5);
                }
                ordersDao.updateById(order);
                userDao.updateById(userEntity);
            }
            PayDetails payDetails = new PayDetails();
            payDetails.setState(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(order.getSupplementNum());
            payDetails.setUserId(userId);
            payDetails.setMoney(supplementMoney.doubleValue());
            payDetails.setClassify(6);
            payDetails.setType(4);
            payDetails.setParentId(order.getId());
            payDetails.setRemark("补缴租金：" + supplementMoney);
            payDetailsDao.insert(payDetails);
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setMoney(order.getSupplementMoney());
            userMoneyDetails.setContent("零钱补缴租金：" + order.getSupplementMoney());
            userMoneyDetails.setTitle("订单补缴租金成功");
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(6);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(new Date());
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsDao.insert(userMoneyDetails);
            return Result.success();
        } else {
            return ResultUtil.error(-1, "余额不足，请充值");
        }
    }

    @Override
    public Result updateOrder(Orders orders) {
        Orders entity = ordersDao.selectById(orders.getId());
        if (entity == null) {
            return ResultUtil.error(-1, "订单不存在");
        }
        int result = ordersDao.updateById(orders);
        if (result <= 0) {
            return ResultUtil.error(-1, "订单修改失败");
        }
        return ResultUtil.success();
    }

    @Override
    public Result appEvaluate(Long userId, Long orderId, Long byUserId, String evaluateMessage, Integer
            satisfactionFlag, String evaluateImg) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.failed("用户信息不存在,请重新登录");
        }
        Orders tbOrders = ordersDao.selectById(orderId);
        if (tbOrders.getStatus() != 6) {
            return Result.failed("订单状态不允许评价");
        }
        tbOrders.setStatus(6);
        tbOrders.setIsEvaluate(1);
        tbOrders.setEvaluationTime(new Date());
        ordersDao.updateById(tbOrders);
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity != null) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setUserId(String.valueOf(userId));
            messageInfo.setTitle("订单完成通知：" + tbOrders.getOrderNum());
            messageInfo.setContent("您的订单已完成");
            messageInfo.setCreateAt(DateUtils.format(new Date()));
            messageInfo.setIsSee("0");
            messageInfo.setState(String.valueOf(4));
            messageInfo.setStatus(0);
            messageInfo.setUserName(userEntity.getUserName());
            messageInfoDao.insert(messageInfo);
        }
        String value = commonInfoService.findOne(294).getValue();
        if ("是".equals(value)) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(evaluateMessage)) {
                if (BaiDuUtils.auditBaiDuText(evaluateMessage)) {
                    return Result.failed("您发布的内容包含敏感词汇");
                }
            }
            if (StringUtils.isNotBlank(evaluateImg)) {
                if (BaiDuUtils.auditBaiDuImg(evaluateImg)) {
                    return Result.failed("您发布的图片不合规");
                }
            }
        }
        SelfGoodsComment goodsComment = new SelfGoodsComment();
        goodsComment.setOrderId(tbOrders.getId());
        goodsComment.setUserId(user.getUserId());
        goodsComment.setContent(evaluateMessage);
        goodsComment.setScoreType(satisfactionFlag);
        if (evaluateImg != null) {
            goodsComment.setImg(evaluateImg);
        }
        goodsComment.setUserName(user.getUserName());
        goodsComment.setUserHeader(user.getAvatar());
        goodsComment.setSkuId(String.valueOf(tbOrders.getSkuId()));
        goodsComment.setGoodsId(tbOrders.getGoodsId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        goodsComment.setCreateTime(sdf.format(tbOrders.getEvaluationTime()));
        goodsCommentJpaRepository.save(goodsComment);
        return Result.success();
    }

    @Override
    public Result getEvaluateList(Integer page, Integer limit, SysEvaluate sysEvaluate) {
        Page<SysEvaluate> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        // 普通商品评论
        IPage<SysEvaluate> evaluateIPage = sysEvaluateDao.selectPageByEvaluateList(pages, sysEvaluate);
        return Result.success(evaluateIPage);
    }

    @Override
    public Result confirmOrder(Long id, Integer status) {
        //1.订单设置已收货
        Orders orders = jpaRepository.findById(id).orElse(null);
        if (orders == null) {
            return ResultUtil.error(-1, "订单不存在");
        }
        //订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价
        if (orders.getStatus() != 2) {
            return ResultUtil.error(-200, "订单状态异常，请刷新页面后重试！");
        }
        orders.setStatus(status); //4待归还
        //商品销量增加
        goodsService.salesAdd(orders.getGoodsId(), orders.getNumber());
        //sku销量增加
        skuService.addSales(orders.getSkuId(), orders.getNumber());
        orders.setDeliveryTime(new Date()); //设置完成时间
        orders.setLeaseStartDate(new Date());
        if (orders.getDayId() != null) {
            LeaseDays leaseDays = leaseDaysDao.selectById(orders.getDayId());
            if (leaseDays != null && leaseDays.getDayName() != null) {
                LocalDate leaseStartDate = orders.getLeaseStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                int daysToAdd = Integer.parseInt(leaseDays.getDayName());
                // 计算归还日期
                LocalDate returnDate = leaseStartDate.plusDays(daysToAdd);
                // 设置归还日期
                orders.setReturnDate(Date.from(returnDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
            }
        }
        return ResultUtil.success(ordersDao.updateById(orders));
    }

    @Override
    public Result orderComplete(Long id, Integer status) {
        Orders orders = jpaRepository.findById(id).orElse(null);
        if (orders == null) {
            return Result.failed("订单不存在");
        }
        if (orders.getStatus() != 6) {
            return Result.failed("订单状态异常，请刷新页面后重试！");
        }
        orders.setStatus(status); //6已完成
        orders.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        orders.setReturnDate(new Date());
        ordersDao.updateById(orders);
        //sku库存+1
        skuService.addStock(orders.getSkuId(), orders.getNumber());
        return Result.success("订单已完成");
    }

    @Override
    public Result checkCoupon(Long userId, Long id, Long couponUserId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return ResultUtil.error(-1, "用户不存在");
        }
        Goods goods = goodsDao.selectById(id);
        if (goods == null) {
            return ResultUtil.error(-1, "商品不存在");
        }
        if (couponUserId != null) {
            SelfCouponUser selfCouponUser = selfCouponUserMapper.selectByCouponUserId(couponUserId);
            if (selfCouponUser != null) {
                if (selfCouponUser.getType() == 2) {
                    boolean flag = false;
                    if (selfCouponUser.getGoodsIds() != null) {
                        String[] goodsIds = selfCouponUser.getGoodsIds().split(",");
                        for (String goodsId : goodsIds) {
                            if (goodsId.trim().equals(goods.getId().toString().trim())) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
                        }
                        return ResultUtil.success("优惠券匹配成功");
                    } else {
                        return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
                    }
                } else {
                    return ResultUtil.success("优惠券匹配成功");
                }
            }
        }
        return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
    }

    @Override
    public Result getOrderEvaluateList(Integer page, Integer limit, Long goodId) {
        Page<SelfGoodsComment> pageable = new Page<>(page, limit);
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
        Page<SelfGoodsComment> pageList = selfGoodsCommentDao.selectPage(pageable, new QueryWrapper<SelfGoodsComment>().eq("goods_id", goodId));
        List<SelfGoodsComment> list = pageList.getRecords();
        for (SelfGoodsComment selfGoodsComment : list) {
            Goods goods = goodsJpaRepository.findById(selfGoodsComment.getGoodsId()).orElse(null);
            selfGoodsComment.setGoods(goods);
        }
        return ResultUtil.success(pageList);
    }

    @Override
    public Result countMoney(Orders entity) {
        Goods g = goodsJpaRepository.findById(entity.getGoodsId()).orElse(null);
        if (g == null || g.getStatus() != 1) {
            return ResultUtil.error(-1, "商品已被下架或移除，请选择其他商品！");
        }
        if (entity.getCouponUserId() != null) {
            SelfCouponUser couponUser = selfCouponUserMapper.selectByCouponUserId(entity.getCouponUserId());
//            if (!entity.getUserCouponsId().equals(g.getBrandId())) { //品牌id:前端传值有误，排除商品品牌id
            if (couponUser == null) {
                return ResultUtil.error(-1, "优惠券不存在");
            }
            SelfCouponIssue selfCouponIssue = selfCouponIssueMapper.selectByCouponIssueId(couponUser.getCouponIssueId());
            if (selfCouponIssue.getStatus() != 1) {
                return ResultUtil.error(-1, "优惠券未启用，暂不能使用");
            }
            if (selfCouponIssue.getType() == 2) {
                boolean flag = false;
                if (selfCouponIssue.getGoodsIds() != null) {
                    String[] goodsIds = selfCouponIssue.getGoodsIds().split(",");
                    for (String goodsId : goodsIds) {
                        if (goodsId.trim().equals(entity.getGoodsId().toString().trim())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
                    }
                } else {
                    return ResultUtil.error(-1, "未找到与商品匹配的优惠券，暂不能使用");
                }
            }
            if (Double.parseDouble(couponUser.getLessMoney()) != entity.getCouponMoney().doubleValue()) {
                return ResultUtil.error(-1, "优惠券金额有误");
            }
//            }
        }
        //校验支付金额：商品金额：商品价格*个数+邮费=总价；支付金额：总价-优惠金额(单商户)
        //预估价格 = 日租金 * 天数 * 购买的数量 + 押金 + 运费
//        if (entity.getDayId() != null){
//
//        }else if (entity.getDayId() == null && entity.get){
//            return ResultUtil.error(-1, "商品金额有误");
//        }
        if (g.getIsExpress() != null) {
            String[] split = g.getIsExpress().split(",");
            if (split.length > 0) {
                boolean condition1Met = false;
                boolean condition2Met = false;
                boolean condition3Met = false;
                for (String s : split) {
                    if ("1".equals(s.trim())) {
                        condition1Met = true;
                    } else if ("2".equals(s.trim())) {
                        condition2Met = true;
                    } else if ("3".equals(s.trim())) {
                        condition3Met = true;
                    }
                }
                if (condition2Met) {
                    entity.setPostagePrice(g.getPostagePrice() == null ? BigDecimal.ZERO : g.getPostagePrice());
                    Address address = addressDao.selectById(entity.getAddressId());
                    if (address != null) {
                        entity.setDetail(address.getProvince() + address.getCity() + address.getAddressDetail());
                    }
                } else if (condition1Met || condition3Met) {
                    entity.setPostagePrice(BigDecimal.ZERO);
                }
            }
        }
//        if (g != null && ("1".equals(g.getIsExpress()) || "2".equals(g.getIsExpress()))) {
//            if (g.getPostagePrice() == null) {
//                entity.setPostagePrice(BigDecimal.ZERO);
//            } else {
//                entity.setPostagePrice(g.getPostagePrice());
//            }
//            Address address = addressDao.selectById(entity.getAddressId());
//            if (address != null) {
//                entity.setDetail(address.getProvince() + address.getCity() + address.getAddressDetail());
//            }
//        } else if (g != null && "3".equals(g.getIsExpress())) {
//            entity.setPostagePrice(BigDecimal.ZERO);
//        }
        LeaseDays leaseDays = leaseDaysDao.selectById(entity.getDayId());
        if (leaseDays == null) {
            return ResultUtil.error(-1, "该租赁天数不存在，请刷新后重试");
        }
        String dayName = leaseDays.getDayName();
        long days = Long.parseLong(dayName);
        UserEntity user = userDao.selectById(entity.getUserId());
        if (user != null) {
            // 如果是租赁商品
            if (entity.getOrderType() == 5) {
                if (entity.getCouponMoney() == null) {
                    entity.setCouponMoney(BigDecimal.ZERO);
                } else {
                    entity.setCouponMoney(entity.getCouponMoney());
                }
                // 预估价格 = 日租金 * 天数 * 购买的数量 + 押金 + 运费 - 优惠券
                entity.setEstimateMoney(
                        entity.getDailyRent()
                                .multiply(BigDecimal.valueOf(days))
                                .multiply(BigDecimal.valueOf(entity.getNumber()))
                                .add(entity.getDeposit())
                                .add(entity.getPostagePrice())
                                .subtract(entity.getCouponMoney()));
                // 总租金 = 日租金 * 天数 * 购买的数量 + 运费
                entity.setGrossRent(
                        entity.getDailyRent()
                                .multiply(BigDecimal.valueOf(days))
                                .multiply(BigDecimal.valueOf(entity.getNumber()))
                                .add(entity.getPostagePrice()));
                // 如果是买断商品
            } else if (entity.getOrderType() == 6) {
                UserEntity userEntity = userDao.selectById(entity.getUserId());
                if (userEntity != null) {
                    if (userEntity.getIsVip() == 1) {
                        entity.setEstimateMoney(g.getMemberPrice()
                                .multiply(new BigDecimal(entity.getNumber()))
                                .add(entity.getPostagePrice())
                                .subtract(entity.getCouponMoney()));
                    } else {
                        // 预估价格 = 商品价格 * 购买数量 + 邮费 - 优惠券
                        entity.setEstimateMoney(
                                g.getOriginalPrice()
                                        .multiply(new BigDecimal(entity.getNumber()))
                                        .add(entity.getPostagePrice())
                                        .subtract(entity.getCouponMoney()));
                    }
                }
            }
//        BigDecimal sumMoney = entity.getPrice().multiply(new BigDecimal(entity.getNumber()));
//        if (entity.getPostagePrice() != null){ //邮费不为空
//            sumMoney = sumMoney.add(entity.getPostagePrice());
//        }
            BigDecimal payMoney = entity.getEstimateMoney();
            entity.setPayMoney(payMoney);
            if (entity.getCouponMoney() != null) { //优惠券金额不为空
                payMoney = payMoney.subtract(entity.getCouponMoney());
            }
            if (payMoney.compareTo(BigDecimal.ZERO) < 0) {
                return ResultUtil.error(-1, "订单金额有误");
            }
            entity.setPayMoney(payMoney);
            if (payMoney.doubleValue() != entity.getPayMoney().doubleValue()) {
                return ResultUtil.error(-1, "订单金额有误");
            }
        } else {
            return ResultUtil.error(-1, "该用户未登录");
        }
        return ResultUtil.success(entity);
    }

    @Override
    public Result getContinuingLeaseList(Integer page, Integer limit, Long orderId) {
        Page<ContinuingOrder> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        if (orderId != null) {
            Page<ContinuingOrder> continuingOrderList = continuingOrderDao.selectPage(pages, new QueryWrapper<ContinuingOrder>()
                    .eq("order_id", orderId)
                    .orderByAsc("sort"));
            return ResultUtil.success(continuingOrderList);
        }
        return ResultUtil.success("暂无租赁历史记录");
    }
}
