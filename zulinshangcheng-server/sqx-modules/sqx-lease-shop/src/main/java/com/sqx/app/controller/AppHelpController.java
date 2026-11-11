package com.sqx.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.HelpClassify;
import com.sqx.domain.repository.entity.sys.HelpWord;
import com.sqx.domain.repository.service.helpCenter.HelpClassifyService;
import com.sqx.domain.repository.service.helpCenter.HelpWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "商品详情的常见问题---用户端", tags = {"商品详情的常见问题---用户端"})
@RequestMapping(value = "/app/help")
public class AppHelpController {


    @Autowired
    private HelpClassifyService helpClassifyService;
    @Autowired
    private HelpWordService helpWordService;

    @GetMapping("/selectHelpList")
    @ApiOperation("查询常见问题列表")
    public Result selectHelpList() {
        List<HelpClassify> helpClassifyList = helpClassifyService.list(new QueryWrapper<HelpClassify>()
                .eq("parent_id", 0)
                .orderByAsc("sort"));
        // 创建一个 Map 以便快速查找子分类
        Map<Long, List<HelpClassify>> childrenMap = new HashMap<>();
        for (HelpClassify helpClassify : helpClassifyList) {
            List<HelpClassify> helpWordList = helpClassifyService.list(new QueryWrapper<HelpClassify>()
                    .eq("Parent_id", helpClassify.getHelpClassifyId())
                    .orderByAsc("sort"));
            // 将子分类列表放入 map 中
            childrenMap.put(helpClassify.getHelpClassifyId(), helpWordList);
            // 设置父分类的 children 属性
            helpClassify.setChildren(helpWordList);
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("data", helpClassifyList);
        return Result.success(data);
    }

    @GetMapping("/selectHelpCenter")
    @ApiOperation("查询帮助中心")
    public Result selectHelpClassifyList() {
        List<HelpClassify> helpClassifyList = helpClassifyService.list(new QueryWrapper<HelpClassify>()
                .ne( "parent_id", 0));
            for (HelpClassify helpClassify : helpClassifyList) {
                List<HelpWord> helpWordList = helpWordService.list(new QueryWrapper<HelpWord>()
                        .eq("help_classify_id", helpClassify.getHelpClassifyId())
                        .orderByAsc("sort"));
                helpClassify.setHelpWordList(helpWordList);
            }
        return Result.success(helpClassifyList);
    }
    @GetMapping("/selectHelpDetail")
    @ApiOperation("查询帮助中心详情")
    public Result selectHelpDetail(@ApiParam("分类id") Long helpClassifyId) {
      List<HelpWord> helpClassifyList = helpWordService.list(new QueryWrapper<HelpWord>()
                .eq("help_classify_id", helpClassifyId)
                .orderByAsc("sort"));
        return Result.success(helpClassifyList);
    }

    @GetMapping("/selectHelpWordList")
    @ApiOperation("查询订单问题等列表")
    public Result selectHelpWordList(@ApiParam("页码") Integer page,
                                     @ApiParam("条数") Integer limit,
                                     @ApiParam("分类id") Long helpClassifyId,
                                     @ApiParam("标题") String helpWordTitle) {
        List<HelpWord> helpWordList = helpWordService.list(new QueryWrapper<HelpWord>()
                .eq(helpClassifyId != null, "help_classify_id", helpClassifyId)
                .like(StringUtils.isNotBlank(helpWordTitle), "help_word_title", '%' + helpWordTitle + '%')
                .orderByAsc("sort"));
        return Result.success(helpWordList);
    }

    @GetMapping("/selectHelpWordDetails")
    @ApiOperation("查询详情")
    public Result selectHelpWordDetails(@ApiParam("主键id") Long helpWordId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("data", helpWordService.getById(helpWordId));
        return Result.success(data);
    }


}
