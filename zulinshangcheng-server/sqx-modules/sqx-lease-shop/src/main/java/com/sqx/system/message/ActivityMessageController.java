//package com.sqx.system.message;
//
//import com.sqx.common.core.domain.Result;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * @author fang
// * @date 2020/7/13
// */
//@RestController
//@Api(value = "消息管理", tags = {"消息管理"})
//@RequestMapping(value = "/ActivityMessage")
//public class ActivityMessageController {
//
//    @Autowired
//    private ActivityMessageService activityMessageService;
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    @ApiOperation("管理平台公告详情")
//    @ResponseBody
//    public Result getMessage(@ApiParam("消息公告id") @PathVariable Integer id) {
//        return Result.success(activityMessageService.findOne(Long.valueOf(id)));
//    }
//
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("管理平台和用户端通用接口添加公告")
//    @ResponseBody
//    public Result addMessage(@ApiParam("消息公告的实体类")@RequestBody ActivityMessageInfo messageInfo) {
//        activityMessageService.saveBody(messageInfo);
//        return Result.success();
//    }
//
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    @ApiOperation("管理平台修改公告接口")
//    @ResponseBody
//    public Result uUpdate(@ApiParam("消息公告的实体类")@RequestBody ActivityMessageInfo messageInfo) {
//        activityMessageService.updateBody(messageInfo);
//        return Result.success();
//    }
//
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
//    @ApiOperation("管理平台删除公告接口")
//    public Result deleteMessage(@ApiParam("消息公告id")@PathVariable int id) {
//        activityMessageService.delete(id);
//        return Result.success();
//    }
//
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    @ApiOperation("管理平台获取全部公告接口")
//    @ResponseBody
//    public Result getMessageList() {
//        List<ActivityMessageInfo> all = activityMessageService.findAll();
//        return Result.success().put("data",all);
//    }
//
//    @RequestMapping(value = "/page/{state}/{page}/{limit}", method = RequestMethod.GET)
//    @ApiOperation("管理平台分页查询公告接口")
//    @ResponseBody
//    public Result getBodyPage(@ApiParam("消息发送状态")@PathVariable String state,
//                              @ApiParam("页码")@PathVariable Integer page,
//                              @ApiParam("条数")@PathVariable int limit) {
//        return Result.success().put("data",activityMessageService.find(state, page,limit));
//    }
//
//    @RequestMapping(value = "/type/{type}/{page}/{limit}", method = RequestMethod.GET)
//    @ApiOperation("管理平台通过类型获取接口 type1为公告2位用户反馈 3为系统消息 4为订单信息 5为用户消息 6客服消息")
//    @ResponseBody
//    public Result findType(@ApiParam("消息类型")@PathVariable Integer type,
//                           @ApiParam("页码")@PathVariable Integer page,
//                           @ApiParam("条数")@PathVariable int limit) {
//        return Result.success().put("data",activityMessageService.findType(type, page,limit));
//    }
//
//    @RequestMapping(value = "/findType/{userId}/{type}/{page}/{limit}", method = RequestMethod.GET)
//    @ApiOperation("用户端获取消息列表 type 4为订单信息 5为用户消息")
//    @ResponseBody
//    public Result findType(@ApiParam("用户id")@PathVariable String userId,
//                           @ApiParam("消息类型")@PathVariable String type,
//                           @ApiParam("页码")@PathVariable Integer page,
//                           @ApiParam("条数")@PathVariable int limit) {
//        return Result.success().put("data",activityMessageService.findTypeByUserId(type, userId,page,limit));
//    }
//
//}