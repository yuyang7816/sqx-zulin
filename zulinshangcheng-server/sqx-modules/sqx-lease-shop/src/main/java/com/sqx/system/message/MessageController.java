package com.sqx.system.message;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.service.sys.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/auditMessage")
    @ApiOperation("审核投诉")
    public Result auditMessage(@ApiParam("消息id")Long messageId,
                               @ApiParam("审核状态 1不做处理 2封号 3下架内容")Integer status,
                               @ApiParam("审核内容")String auditContent){
        return messageService.auditMessage(messageId,status,auditContent);
    }


    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(@ApiParam("页码") Integer page,
                                     @ApiParam("条数") Integer limit,
                                     @ApiParam("消息实体") MessageInfo messageInfo){

        return Result.success(messageService.selectMessageList(page,limit,messageInfo));
    }

    @RequestMapping(value = "/selectMessageByType", method = RequestMethod.GET)
    @ApiOperation("获取消息 type1为公告2位用户反馈 3为投诉服务 4为投诉店铺 5为用户消息 6客服消息")
    @ResponseBody
    public Result selectMessageByType(@ApiParam("页码")Integer page,
                                      @ApiParam("条数")Integer limit,
                                      @ApiParam("消息实体")MessageInfo messageInfo){
        return Result.success(messageService.selectMessageList(page,limit,messageInfo));
    }

    @RequestMapping(value = "/selectMessageDetails", method = RequestMethod.GET)
    @ApiOperation("获取消息详细信息")
    @ResponseBody
    public Result selectMessageDetails(@ApiParam("消息id")Long id){
        return Result.success(messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    @ApiOperation("修改消息")
    @ResponseBody
    public Result updateMessage(@ApiParam("消息实体")@RequestBody MessageInfo messageInfo){
        return Result.success(messageService.update(messageInfo));
    }


    @RequestMapping(value = "/deleteMessageById", method = RequestMethod.POST)
    @ApiOperation("删除消息")
    @ResponseBody
    public Result deleteMessageById(@ApiParam("消息id")Long id){
        return Result.success(messageService.delete(id));
    }

    @RequestMapping(value = "/insertMessage", method = RequestMethod.POST)
    @ApiOperation("添加消息")
    @ResponseBody
    public Result insertMessage(@ApiParam("消息实体")MessageInfo messageInfo){
        return Result.success(messageService.saveBody(messageInfo));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台公告详情")
    @ResponseBody
    public Result getMessage(@ApiParam("消息id")@PathVariable Long id) {
        return Result.success(messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("管理平台和用户端通用接口添加公告")
    @ResponseBody
    public Result addMessage(@ApiParam("消息实体")@RequestBody MessageInfo messageInfo) {
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("管理平台修改公告接口")
    @ResponseBody
    public Result uUpdate(@ApiParam("消息实体")@RequestBody MessageInfo messageInfo) {
        messageService.update(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除公告接口")
    public Result deleteMessage(@ApiParam("消息id")@PathVariable Long id) {
        messageService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "/page/{state}/{page}/{size}", method = RequestMethod.GET)
    @IgnoreSecurity
    @ApiOperation("管理平台分页查询公告接口")
    @ResponseBody
    public Result getBodyPage(@PathVariable String state, @PathVariable Integer page, @PathVariable int size) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return messageService.find(state, pageable);
    }


}