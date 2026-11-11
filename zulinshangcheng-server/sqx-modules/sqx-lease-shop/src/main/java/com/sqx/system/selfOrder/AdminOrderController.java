package com.sqx.system.selfOrder;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.selfOrder.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/24 10:40
 */
@RestController
@Api(value = "自营商城订单", tags = {"自营商城订单管理"})
@RequestMapping(value = "/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrdersService service;

    @GetMapping("/selectOrdersCountStatisticsByYear")
    @ApiOperation("订单数量统计")
    public Result selectOrdersCountStatisticsByYear(String startTime, String endTime) {
        List<Integer> allOrdersCounts = new ArrayList<>();
        List<Integer> ordinaryOrders = new ArrayList<>();
        List<Integer> retrieveCountList = new ArrayList<>();
//        List<Integer> ordersDaiFuKuanCountList=new ArrayList<>();
//        List<Integer> ordersYiZhiFuCountList=new ArrayList<>();
//        List<Integer> ordersYiTuiKuanLunCountList=new ArrayList<>();
        List<String> year = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parse);
        while (true) {
            String dateTime = simpleDateFormat.format(calendar.getTime());
            // 总订单数
            Integer allOrdersCount = service.selectOrdersCountStatisticsByYear(1, dateTime, 3);
            allOrdersCounts.add(allOrdersCount);
//            //普通总订单数
//            Integer ordinaryOrdersCount = service.selectOrdersCountStatisticsByYear(1, dateTime, 1);
//            ordinaryOrders.add(ordinaryOrdersCount);
//            // 回收订单总数
//            Integer retrieveCount = service.selectOrdersCountStatisticsByYear(1, dateTime, 2);
//            retrieveCountList.add(retrieveCount);
//            //0待支付
//            Integer ordersDaiFuKuanCount = tbOrdersService.selectOrdersCountStatisticsByYear(1, dateTime, 0);
//            ordersDaiFuKuanCountList.add(ordersDaiFuKuanCount);
//            //1已支付
//            Integer ordersJinXinCount = tbOrdersService.selectOrdersCountStatisticsByYear(1, dateTime, 1);
//            ordersYiZhiFuCountList.add(ordersJinXinCount);
//            //2已退款
//            Integer ordersQuXiaoCount = tbOrdersService.selectOrdersCountStatisticsByYear(1, dateTime, 2);
//            ordersYiTuiKuanLunCountList.add(ordersQuXiaoCount);
            year.add(dateTime);
            if (dateTime.equals(endTime)) {
                break;
            }
            calendar.add(Calendar.DATE, 1);
        }
        Map<String, Object> result = new HashMap<>();
//        //普通订单总数
//        result.put("ordersCountList", ordinaryOrders);
//        // 回收订单总数
//        result.put("retrieveCountList", retrieveCountList);
        // 总订单数
        result.put("allOrdersCounts", allOrdersCounts);
//        result.put("ordersDaiFuKuanCountList",ordersDaiFuKuanCountList);
//        result.put("ordersYiZhiFuCountList",ordersYiZhiFuCountList);
//        result.put("ordersYiTuiKuanLunCountList",ordersYiTuiKuanLunCountList);
        result.put("year", year);
        return Result.success(result);
    }

}
