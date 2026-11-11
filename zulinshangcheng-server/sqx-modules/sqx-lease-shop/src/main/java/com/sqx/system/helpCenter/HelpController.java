package com.sqx.system.helpCenter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.domain.repository.entity.sys.HelpClassify;
import com.sqx.domain.repository.entity.sys.HelpWord;
import com.sqx.domain.repository.service.helpCenter.HelpClassifyService;
import com.sqx.domain.repository.service.helpCenter.HelpWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(value = "帮助中心---管理端", tags = {"帮助中心---管理端"})
@RequestMapping(value = "/help")
public class HelpController {

    @Autowired
    private HelpClassifyService helpClassifyService;
    @Autowired
    private HelpWordService helpWordService;


    @PostMapping("/insertHelpClassify")
    @ApiOperation("添加帮助分类")
    public Result insertHelpClassify(@ApiParam("帮助中心分类实体") @RequestBody HelpClassify helpClassify){
        helpClassify.setCreateTime(DateUtils.format(new Date()));
        helpClassifyService.save(helpClassify);
        return Result.success();
    }

    @PostMapping("/updateHelpClassify")
    @ApiOperation("修改帮助分类")
    public Result updateHelpClassify(@ApiParam("帮助中心分类实体")@RequestBody HelpClassify helpClassify){
        helpClassifyService.updateById(helpClassify);
        return Result.success();
    }

    @PostMapping("/deleteHelpClassify")
    @ApiOperation("删除帮助分类")
    public Result deleteHelpClassify(@ApiParam("帮助中心分类id")Long helpClassifyId){
        helpClassifyService.removeById(helpClassifyId);
        return Result.success();
    }


    @GetMapping("/selectHelpClassifyList")
    @ApiOperation("查询帮助分类")
    public Result selectHelpClassifyList(@ApiParam("页码")Integer page,
                                         @ApiParam("条数")Integer limit,
                                         @ApiParam("上级id")Long parentId,
                                         @ApiParam("分类名称")String helpClassifyName){
        if(page==null || limit==null){
            List<HelpClassify> page1 = helpClassifyService.list(
                    new QueryWrapper<HelpClassify>().
                            eq(StringUtils.isNotBlank(helpClassifyName), "help_classify_name", helpClassifyName)
                            .eq(parentId != null, "parent_id", parentId).orderByAsc("sort"));
            return Result.success(page1);
        }
        IPage<HelpClassify> page1 = helpClassifyService.page(new Page<>(page, limit),
                new QueryWrapper<HelpClassify>().
                        eq(StringUtils.isNotBlank(helpClassifyName), "help_classify_name", helpClassifyName)
                        .eq(parentId != null, "parent_id", parentId).orderByAsc("sort"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("data",page1.getRecords());
        return Result.success(map);
    }


    @PostMapping("/insertHelpWord")
    @ApiOperation("添加帮助文档")
    public Result insertHelpWord(@ApiParam("帮助文档实体")@RequestBody HelpWord helpWord){
        helpWord.setCreateTime(DateUtils.format(new Date()));
        helpWordService.save(helpWord);
        return Result.success();
    }

    @PostMapping("/updateHelpWord")
    @ApiOperation("修改帮助文档")
    public Result updateHelpWord(@ApiParam("帮助文档实体")@RequestBody HelpWord helpWord){
        helpWordService.updateById(helpWord);
        return Result.success();
    }

    @PostMapping("/deleteHelpWord")
    @ApiOperation("删除帮助文档")
    public Result deleteHelpWord(@ApiParam("帮助文档id")Long helpWordId){
        helpWordService.removeById(helpWordId);
        return Result.success();
    }


    @GetMapping("/selectHelpWordList")
    @ApiOperation("查询帮助文档")
    public Result selectHelpWordList(@ApiParam("页码")Integer page,
                                     @ApiParam("条数")Integer limit,
                                     @ApiParam("帮助文档分类id")Long helpClassifyId,
                                     @ApiParam("帮助标题")String helpWordTitle){
        IPage<HelpWord> page1 = helpWordService.page(new Page<>(page, limit), new QueryWrapper<HelpWord>()
                .eq(helpClassifyId != null, "help_classify_id", helpClassifyId)
                .eq(StringUtils.isNotBlank(helpWordTitle), "help_word_title", helpWordTitle).orderByAsc("sort"));
        return Result.success(page1);
    }





}
