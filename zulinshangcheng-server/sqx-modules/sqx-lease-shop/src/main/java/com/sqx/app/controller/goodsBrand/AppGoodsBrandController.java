package com.sqx.app.controller.goodsBrand;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.service.selfGoodsBrand.SelfGoodsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/23 18:05
 */
@RestController
@Api(value="商城商品品牌---用户端",tags={"商城商品品牌---用户端"})
@RequestMapping(value = "/app/selfGoodsBrand")
public class AppGoodsBrandController {

    @Autowired
    private SelfGoodsBrandService service;
    @GetMapping("/result")
    @ApiOperation("品牌封装列表")
    public Result result() {
        return service.result();
    }


}
