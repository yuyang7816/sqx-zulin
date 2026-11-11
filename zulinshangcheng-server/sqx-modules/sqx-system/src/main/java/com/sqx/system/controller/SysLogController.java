package com.sqx.system.controller;


import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.system.domain.res.PageRes;
import com.sqx.system.service.SysLogService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 系统日志
 *
 */
@Controller
@RequestMapping("/sys/log")
@Api(value = "日志管理-管理端", tags ={"日志管理-管理端"} )
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:log:list")
	public Result list(@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);

		PageRes res = new PageRes();
		res.setPage(page);
		return Result.success(res);
	}
	
}
