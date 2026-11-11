package com.sqx.system.user;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.domain.app.HomeMessageResponse;
import com.sqx.domain.repository.domain.app.UserMessageResponse;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.mapper.app.UserDao;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.pay.PayDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/23 13:28
 */
@RestController
@Api(value = "用户管理", tags = {"用户管理"})
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PayDetailsService payDetailsService;

    @Autowired
    private UserDao userDao;

    /**
     * 信息分析
     *
     * @return
     */
    @GetMapping("/homeMessage")
    @ApiOperation("信息分析")
    public Result homeMessage() {
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null));

        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));

        return Result.success( homeMessageResponse);
    }


    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @ApiOperation("用户分析")
    public Result userMessage(@ApiParam(value = "日期") String date,
                              @ApiParam(value = "type筛选：1年月日 2年月 3年") int type) {
        UserMessageResponse userMessageResponse = new UserMessageResponse();
        // 总用户
        userMessageResponse.setTotalNumber(userService.queryUserCount(type, date));
//        userMessageResponse.setVipUserNumber(userService.userMessage(date, type));
//        userMessageResponse.setUserNumber(userService.queryUserCount(type, date) - userService.userMessage(date, type));
        // 小程序用户
        userMessageResponse.setWxUserNumber(userService.queryWxUserNumber(type, date));
        // app用户
        userMessageResponse.setAppUserNumber(userService.queryAppUserNumber(type, date));
        // h5用户
        userMessageResponse.setH5UserNumber(userService.queryH5UserNumber(type, date));
        // 公众号用户
        userMessageResponse.setPublicUserNumber(userService.queryPublicUserNumber(type, date));
        return Result.success(userMessageResponse);
    }


    /**
     * 用户分析的支付统计
     */
    @GetMapping("/payMessage")
    @ApiOperation("充值统计")
    public Result payMessage(@ApiParam(value = "日期") String date,
                             @ApiParam(value = "type筛选：1年月日 2年月 3年") Integer type) {
        return Result.success(payDetailsService.payMessage(date, type));
    }

    @ApiOperation("订单统计")
    @GetMapping("/orderMessageCount")
    public Result orderMessageCount(@ApiParam(value = "日期") String date,
                                    @ApiParam(value = "type筛选：1年月日 2年月 3年") Integer type) {
        return userService.orderMessageCount(date, type);
    }

    @GetMapping("/selectUserCountStatisticsByTime")
    @ApiOperation("用户统计")
    public Result selectUserCountStatisticsByTime(String startTime, String endTime) {
        List<Integer> userCountList = new ArrayList<>();
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
//            int i = userService.queryUserCount(1, dateTime,null,null);
            int i = userService.queryUserCount(1, dateTime);
            userCountList.add(i);
            year.add(dateTime);
            if (dateTime.equals(endTime)) {
                break;
            }
            calendar.add(Calendar.DATE, 1);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("userCountList", userCountList);
        result.put("year", year);
        return Result.success(result);
    }

    @ApiOperation("收入分析")
    @GetMapping("/incomeStatistics")
    public Result incomeStatistics(@ApiParam(value = "日期") String date,
                                   @ApiParam(value = "type筛选：1年月日 2年月 3年") Integer type) {
        return userService.incomeStatistics(date, type);
    }

    @ApiOperation("用户中心")
    @GetMapping("/userList")
    public Result userList(@ApiParam(value = "当前页") Integer page,
                           @ApiParam(value = "每页显示条数") Integer limit,
                           @ApiParam(value = "用户名") String nickName,
                           @ApiParam(value = "手机号") String phone,
                           @ApiParam(value = "用户来源") String platform,
                           @ApiParam(value = "是否会员：1是 2否")Integer isVip){
        return userService.userList(page,limit,nickName,phone,platform,isVip);
    }

    @ApiOperation("用户修改钱包")
    @PostMapping("/updateUserMoneys")
    public Result updateUserMoneys(UserEntity userEntity){
        return userService.updateUserMoneys(userEntity);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@ApiParam(value = "用户id") @PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByUserId(userId);
//        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Double money = 0.0;
//        if (userMoney != null) {
//            money = userMoney.getMoney().doubleValue();
//        }
        //查询用户钱包
        Double m = money;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
//        Double income = userMoneyDetailsService.monthIncome(date, userId);

        map.put("userEntity", userEntity);
//        map.put("money", m);
        map.put("consume", consume);
//        map.put("income", income);
       /* map.put("takeOrdersCount", takeOrdersCount);
        map.put("myOrdersCount", myOrdersCount);*/
        return Result.success(map);
    }

    @ApiOperation("修改用户状态")
    @PostMapping("/updateUser")
    public Result updateUser(UserEntity userEntity){
       return ResultUtil.success(userDao.updateById(userEntity));
    }

}
