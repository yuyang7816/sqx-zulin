//package com.sqx.system.controller;
//
//
//import com.sqx.common.core.annotation.SysLog;
//import com.sqx.common.core.domain.Result;
//import com.sqx.common.core.utils.PageUtils;
//import com.sqx.common.core.validator.ValidatorUtils;
//import com.sqx.domain.repository.entity.sys.SysRoleEntity;
//import com.sqx.system.domain.res.PageRes;
//import com.sqx.system.domain.res.SysRoleRes;
//import com.sqx.system.service.SysRoleMenuService;
//import com.sqx.system.service.SysRoleService;
//import io.swagger.annotations.Api;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 角色管理
// *
// */
//@RestController
//@RequestMapping("/sys/role")
//@Api(value = "角色管理-管理端", tags = {"角色管理-管理端"})
//public class SysRoleController extends AbstractController {
//	@Autowired
//	private SysRoleService sysRoleService;
//	@Autowired
//	private SysRoleMenuService sysRoleMenuService;
//
//	/**
//	 * 角色列表
//	 */
//	@GetMapping("/list")
//	@RequiresPermissions("sys:role:list")
//	public Result list(@RequestParam Map<String, Object> params){
//		PageRes pageRes = new PageRes();
//
//		//如果不是超级管理员，则只查询自己创建的角色列表
//		/*if(getUserId() != Constant.SUPER_ADMIN){
//			params.put("createUserId", getUserId());
//		}*/
//		PageUtils page = sysRoleService.queryPage(params);
//		pageRes.setPage(page);
//		return Result.success(pageRes);
//	}
//
//	/**
//	 * 角色列表
//	 */
//	@GetMapping("/select")
//	@RequiresPermissions("sys:role:select")
//	public Result select(){
//		SysRoleRes sysRoleRes = new SysRoleRes();
//		Map<String, Object> map = new HashMap<>();
//
//		//如果不是超级管理员，则只查询自己所拥有的角色列表
//		/*if(getUserId() != Constant.SUPER_ADMIN){
//			map.put("create_user_id", getUserId());
//		}*/
//		List<SysRoleEntity> list = (List<SysRoleEntity>) sysRoleService.listByMap(map);
//		sysRoleRes.setList(list);
//		return Result.success(sysRoleRes);
//	}
//
//	/**
//	 * 角色信息
//	 */
//	@GetMapping("/info/{roleId}")
//	@RequiresPermissions("sys:role:info")
//	public Result info(@PathVariable("roleId") Long roleId){
//		SysRoleRes sysRoleRes = new SysRoleRes();
//
//		SysRoleEntity role = sysRoleService.getById(roleId);
//
//		//查询角色对应的菜单
//		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
//		role.setMenuIdList(menuIdList);
//
//		sysRoleRes.setRole(role);
//		return Result.success(sysRoleRes);
//	}
//
//	/**
//	 * 保存角色
//	 */
//	@SysLog("保存角色")
//	@PostMapping("/save")
//	@RequiresPermissions("sys:role:save")
//	public Result save(@RequestBody SysRoleEntity role){
//		ValidatorUtils.validateEntity(role);
//
//		role.setCreateUserId(getUserId());
//		sysRoleService.saveRole(role);
//
//		return Result.success();
//	}
//
//	/**
//	 * 修改角色
//	 */
//	@SysLog("修改角色")
//	@PostMapping("/update")
//	@RequiresPermissions("sys:role:update")
//	public Result update(@RequestBody SysRoleEntity role){
//		ValidatorUtils.validateEntity(role);
//
//		role.setCreateUserId(getUserId());
//		sysRoleService.update(role);
//
//		return Result.success();
//	}
//
//	/**
//	 * 删除角色
//	 */
//	@SysLog("删除角色")
//	@PostMapping("/delete")
//	@RequiresPermissions("sys:role:delete")
//	public Result delete(@RequestBody Long[] roleIds){
//		sysRoleService.deleteBatch(roleIds);
//
//		return Result.success();
//	}
//}
