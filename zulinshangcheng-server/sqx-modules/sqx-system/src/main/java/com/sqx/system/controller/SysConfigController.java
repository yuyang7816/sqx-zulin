package com.sqx.system.controller;

import com.sqx.common.core.annotation.SysLog;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.common.core.validator.ValidatorUtils;
import com.sqx.domain.repository.entity.sys.SysConfigEntity;
import com.sqx.system.domain.res.PageRes;
import com.sqx.system.domain.res.SysConfigRes;
import com.sqx.system.service.SysConfigService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统配置信息
 *
 */
@RestController
@RequestMapping("/sys/config")
@Api(value = "系统配置管理-管理端", tags = {"系统配置管理-管理端"})
public class SysConfigController extends AbstractController {
	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 所有配置列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:config:list")
	public Result list(@RequestParam Map<String, Object> params){
		PageUtils page = sysConfigService.queryPage(params);

		PageRes res = new PageRes();
		res.setPage(page);
		return Result.success(res);
	}


	/**
	 * 配置信息
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:config:info")
	public Result info(@PathVariable("id") Long id){
		SysConfigEntity config = sysConfigService.getById(id);

		SysConfigRes res = new SysConfigRes();
		res.setConfig(config);
		return Result.success(config);
	}

	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@PostMapping("/save")
	@RequiresPermissions("sys:config:save")
	public Result save(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);

		sysConfigService.saveConfig(config);

		return Result.success();
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@PostMapping("/update")
	@RequiresPermissions("sys:config:update")
	public Result update(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);

		sysConfigService.update(config);

		return Result.success();
	}

	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@PostMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public Result delete(@RequestBody Long[] ids){
		sysConfigService.deleteBatch(ids);

		return Result.success();
	}

}
