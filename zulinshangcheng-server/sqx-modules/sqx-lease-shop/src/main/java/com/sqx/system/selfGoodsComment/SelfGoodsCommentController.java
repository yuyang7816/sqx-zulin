package com.sqx.system.selfGoodsComment;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.SelfGoodsComment;
import com.sqx.domain.repository.service.selfGoodsComment.SelfGoodsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城商品评论",tags={"自营商城商品评论"})
@RequestMapping(value = "/selfGoodsComment")
public class SelfGoodsCommentController {
    @Autowired
    private SelfGoodsCommentService service;

    @IgnoreSecurity
    @GetMapping("/list")
    @ApiOperation("商品评价列表")
    public Result findAll(Integer page, Integer size, Long goodsId, Integer scoreType, String title, String userName, String phone, String content) {
        return service.findAll(page, size, goodsId,scoreType,title,userName,phone,content);
    }

    @IgnoreSecurity
    @GetMapping("/count")
    @ApiOperation("商品评价统计")
    public Result count(Long goodsId) {
        return service.count(goodsId);
    }

    @IgnoreSecurity
    @GetMapping("/findByOrderId")
    @ApiOperation("订单评价")
    public Result findByOrderId(Long orderId) {
        return service.findByOrderId(orderId);
    }

    @GetMapping("/userList")
    @ApiOperation("用户评论列表")
    public Result userList(Integer page, Integer size, Long userId) {
        return service.userList(page, size, userId);
    }

    @IgnoreSecurity
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfGoodsComment entity) {
        return service.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfGoodsComment entity) {
        return service.updateBody(entity);
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

    @GetMapping("/reply")
    @ApiOperation("回复评论")
    public Result reply(Long commentId, String reply) {
        return service.reply(commentId, reply);
    }

}
