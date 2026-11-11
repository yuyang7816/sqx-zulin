package com.sqx.system.activity;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.Activity;
import com.sqx.domain.repository.mapper.activity.ActivityJpaRepository;
import com.sqx.domain.repository.mapper.activity.ActivityRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Api(value="菜单和活动管理",tags={"菜单和活动管理"})
@RequestMapping(value = "/activity")
public class ActivityController {
    @Autowired
    private ActivityJpaRepository activityJpaRepository;

    @Autowired
    private ActivityRepository activityRepository;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台详情")
    @ResponseBody
    public Result getBanner(@PathVariable Integer id) {
        return ResultUtil.success(activityJpaRepository.findById(Long.valueOf(id)).orElse(null));
    }

    @RequestMapping(value = "/state/{state}", method = RequestMethod.GET)
    @ApiOperation("根据状态查询菜单列表")
    @IgnoreSecurity
    @ResponseBody
    public Result getBannerState(@PathVariable String state,Integer page,Integer size,Integer type) {
        if(page == null || size ==null){
            List<Activity> one = null;
            if(type==null){
                one = activityRepository.findOne(state);
            }else{
                one = activityRepository.findOne(state, type);
            }

            if("8".equals(state)){
                for(Activity activity:one){
                    if(StringUtils.isNotEmpty(activity.getType())){
                        String[] split = activity.getType().split(",");
                        StringBuilder stringBuilder=new StringBuilder();
                        if(split.length>0){
                            Activity activity1 = activityJpaRepository.findById(Long.parseLong(split[0])).orElse(null);
                            if(activity1!=null){
                                stringBuilder.append(activity1.getTitle());
                            }
                            int index=0;
                            for(String id:split){
                                if(index!=0){
                                    Activity activity2 = activityJpaRepository.findById(Long.parseLong(id)).orElse(null);
                                    if(activity2!=null){
                                        stringBuilder.append("，").append(activity2.getTitle());
                                    }
                                }
                                index++;
                            }
                            activity.setActivityName(stringBuilder.toString());
                        }
                    }
                }
            }
            return ResultUtil.success(one);
        }else{
            Pageable pageable= PageRequest.of(page,size);
            Page<Activity> one = null;
            if(type==null){
                one = activityRepository.findPage(pageable, state);
            }else{
                one = activityRepository.findPage(pageable, state, type);
            }
            if("8".equals(state)){
                for(Activity activity:one){
                    if(StringUtils.isNotEmpty(activity.getType())){
                        String[] split = activity.getType().split(",");
                        StringBuilder stringBuilder=new StringBuilder();
                        if(split.length>0){
                            Activity activity1 = activityJpaRepository.findById(Long.parseLong(split[0])).orElse(null);
                            if(activity1!=null){
                                stringBuilder.append(activity1.getTitle());
                            }
                            int index=0;
                            for(String id:split){
                                if(index!=0){
                                    Activity activity2 = activityJpaRepository.findById(Long.parseLong(id)).orElse(null);
                                    if(activity2!=null){
                                        stringBuilder.append("，").append(activity2.getTitle());
                                    }
                                }
                                index++;
                            }
                            activity.setActivityName(stringBuilder.toString());
                        }
                    }
                }
            }
            return ResultUtil.success(one);
        }
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ApiOperation("管理平台添加")
    @ResponseBody
    public Result addBanner(@PathVariable String id, @RequestBody Activity info) {
        Activity activity = new Activity();
        activity.setId(Long.valueOf(id));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        activity.setImage_url(info.getImage_url());
        activity.setTitle(info.getTitle());
        activity.setCreateAt(sdf.format(now));
        activity.setState(info.getState());
        activity.setUrl(info.getUrl());
        activity.setType(info.getType());
        activity.setSort(info.getSort());
        activity.setBackgroundImage(info.getBackgroundImage());
        return ResultUtil.success(activityJpaRepository.save(activity));
    }

    @PostMapping(value = "/save")
    @ApiOperation("管理平台添加")
    public Result saveBanner(@RequestBody Activity info) {
        info.setCreateAt(DateUtils.createTime());
        return ResultUtil.success(activityJpaRepository.save(info));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ApiOperation("管理平台删除")
    public Result deleteBanner(@PathVariable Long id) {
        activityJpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation("管理平台获取全部广告位")
    @ResponseBody
    public Result getBannerList() {
        return ResultUtil.success(activityJpaRepository.findAll());
    }


}
