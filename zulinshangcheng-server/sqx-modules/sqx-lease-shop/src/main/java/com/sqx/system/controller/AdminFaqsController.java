package com.sqx.system.controller;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Faqs;
import com.sqx.domain.repository.service.sys.FaqsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/19 16:34
 */
@RestController
@RequestMapping("/admin/faqs/")
@Api(value = "常见问题-管理端", tags = {"常见问题-管理端"})
public class AdminFaqsController {

    @Autowired
    private FaqsService faqsService;

    @PostMapping("addFaqs")
    @ApiOperation("修改或新增常见问题")
    public Result addFaqs(Faqs faqs){
        return faqsService.addFaqs(faqs);
    }

    @PostMapping("deleteFaqs")
    @ApiOperation("删除常见问题")
    public Result deleteFaqs(Long id){
        return faqsService.deleteFaqs(id);
    }

    @GetMapping("getFaqsList")
    @ApiOperation("获取常见问题列表")
    public Result getFaqsList(Integer page, Integer limit, Faqs faqs){
        return Result.success(faqsService.getFaqsList(page,limit,faqs));
    }

    @GetMapping("selectByFaqsId")
    @ApiOperation("根据id查询常见问题")
    public Result selectByFaqsId(Long id){
        return faqsService.selectByFaqsId(id);
    }

}
