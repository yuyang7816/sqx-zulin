//package com.sqx.system.selfOrder;
//
//import com.sqx.domain.repository.entity.sys.Orders;
//import com.sqx.domain.repository.entity.sys.SelfGroupPink;
//import com.sqx.domain.repository.entity.sys.SelfOrderRelation;
//import com.sqx.domain.repository.mapper.selfOrder.OrdersRepository;
//import com.sqx.domain.repository.service.selfGroupPink.SelfGroupPinkService;
//import com.sqx.domain.repository.service.selfOrder.OrdersService;
//import com.sqx.domain.repository.service.selfOrderRelation.OrdersRelationService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//@Slf4j
//@Component
//public class OrdersTask {
//    @Autowired
//    private OrdersRelationService service;
//    @Autowired
//    private OrdersRepository ordersRepository;
//    @Autowired
//    private OrdersService orderService;
//    @Autowired
//    private SelfGroupPinkService groupPinkService;
//
//
//    /**
//     * 每天凌晨执行一次
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void scheduledTask(){
//        //分销
//        List<SelfOrderRelation> list = service.findAllByStatus();
//        for (SelfOrderRelation s : list) {
//            try{
//                String finishTime = s.getFinishTime();
//                boolean timeOut = this.isTimeOut(finishTime, 7);
//                if (timeOut){
//                    service.addMoney(s);
//                }
//            }catch (Exception e){
//                log.error("定时分销失败！！！"+s.getId(),e);
//            }
//
//        }
//    }
//
//    /**
//     * 每分钟检测拼团超时订单进行退款
//     */
//    @Scheduled(cron = "0 */1 * * * ?")
//    public void scheduledTask1(){
//        //拼团失败:退款
//        List<Orders> pinkList = ordersRepository.findPinkList();
//        for (Orders o : pinkList) {
//            try{
//                SelfGroupPink p = (SelfGroupPink)groupPinkService.findOne(o.getGroupPinkId()).getData();
//                String endTime = p.getEndTime();
//                if (isPinkRefund(endTime)){
//                    //退款操作
//                    orderService.refundMoney(o.getId());
//                    //删除团
//                    p.setStatus(3); //拼团状态未完成
//                    groupPinkService.updateBody(p);
//                }
//            }catch (Exception e){
//                log.error("自动拼团退款失败！！！"+o.getId(),e);
//            }
//
//        }
//        //检测未发货自动退款
//        List<Orders> statusList = ordersRepository.findAllByStatus(2); //查询所有待发货
//        for (Orders s : statusList) {
//            try{
//                String time = StringUtils.isEmpty(s.getPayTime()) ? s.getCreateAt():s.getPayTime();
//                boolean timeOut = this.isTimeOut(time, 15);
//                if (timeOut){ //自动退款
//                    orderService.refundMoney(s.getId());
//                }
//            }catch (Exception e){
//                log.error("未发货自动退款,退款失败！！！"+s.getId(),e);
//            }
//
//        }
//        //检测自动收货
//        List<Orders> status3List = ordersRepository.findAllByStatus(3); //查询所有待收货
//        for (Orders s : status3List) {
//            try{
//                String time = StringUtils.isEmpty(s.getExpressTime()) ? s.getPayTime():s.getExpressTime();
//                boolean timeOut = this.isTimeOut(time, 15);
//                if (timeOut){ //自动收货
//                    orderService.orderConfirm(s.getId());
//                }
//            }catch (Exception e){
//                log.error("自动收货失败！！！"+s.getId(),e);
//            }
//        }
//    }
//
//
//    /**
//     * 判断传入时间是否超时
//     * @param finishTime 完成时间
//     * @param days 天数
//     * @return  是否超时 true超过时间，可以执行
//     */
//    public boolean isTimeOut(String finishTime, int days){
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try{
//            Date finish = sf.parse(finishTime);
//            Calendar c = Calendar.getInstance();
//            c.setTime(finish);
//            c.add(Calendar.DATE, + days);
//            finish = c.getTime(); //完成时间七天后的时间，得到过期时间
//            //过期时间和现在时间比
//            return finish.before(new Date());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean isPinkRefund(String finishTime){
//        boolean flag = false;
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try{
//            long time = sf.parse(finishTime).getTime();
//            long now = sf.parse(sf.format(new Date())).getTime();
//            if (time <= now){
//                flag = true;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return flag;
//    }
//}
