package com.sqx.app.controller.vipDetails;

import com.sqx.auth.app.annotation.Login;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.vo.PaymentFormVO;
import com.sqx.domain.repository.service.vipDetails.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/vipDetails/")
@Api(value = "app 会员详情信息---用户端",tags = {"app 会员详情信息---用户端"})
public class AppVipDetailsController {
    @Autowired
    private VipDetailsService vipDetailsService;

    @ApiOperation("获取会员分类列表")
    @GetMapping("getVipDetailsList")
    public Result getVipDetailsList(Integer page, Integer limit) {
        return Result.success(vipDetailsService.getVipDetailsList(page, limit, 1));
    }

    @ApiOperation("获取会员分类列表")
    @GetMapping("selectVipDetailsList")
    public Result selectVipDetailsList(Integer page, Integer limit) {
        return Result.success(vipDetailsService.selectVipDetailsList(page, limit, 1));
    }
    /**
     * @param  paymentFormVO
     * @return
     * @throws Exception
     */
    @Login
    @ApiOperation("用户支付")
    @PostMapping("userBuyVip")
    public Result userBuyVip(@RequestAttribute("userId") Long userId,
                             @RequestBody PaymentFormVO paymentFormVO) throws Exception {
        return vipDetailsService.userBuyVip(userId,paymentFormVO);
    }
}


