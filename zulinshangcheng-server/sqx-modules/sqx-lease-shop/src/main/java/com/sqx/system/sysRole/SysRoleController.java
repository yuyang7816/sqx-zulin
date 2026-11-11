package com.sqx.system.sysRole;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.sys.SysRole;
import com.sqx.domain.repository.mapper.sysRole.SysRoleJpaRepository;
import com.sqx.domain.repository.mapper.sysRole.SysRoleRepository;
import com.sqx.domain.repository.service.sysRole.SysRoleMenuService;
import com.sqx.domain.repository.service.sysRole.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 *
 */
@Api(value = "角色管理", tags = {"角色管理"})
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleJpaRepository sysRoleJpaRepository;
	@Autowired
	private SysRoleRepository sysRoleRepository;

	/**
	 * 角色列表
	 */
	@ApiOperation("角色列表")
	@GetMapping("/list")
	public Result list(int page, int size, String roleName){
		Sort.Order order = new Sort.Order(Sort.Direction.DESC, "role_id");
		Pageable pageable = PageRequest.of(page, size, Sort.by(order));
		Page<SysRole> sysRoles = sysRoleService.queryPage(pageable, roleName);
		return ResultUtil.success(sysRoles);
	}

	/**
	 * 角色列表
	 */
	@ApiOperation("角色列表")
	@GetMapping("/select")
	public Result select(Long userId){
		if(userId==1){
			userId=null;
		}
		List<SysRole> list = sysRoleRepository.selectSysRoleList(userId);

		return ResultUtil.success(list);
	}

	/**
	 * 角色信息
	 */
	@ApiOperation("角色信息")
	@GetMapping("/info/{roleId}")
	public Result info(@PathVariable("roleId") Long roleId){
		SysRole role = sysRoleJpaRepository.findById(roleId).orElse(null);
		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		return ResultUtil.success(role);
	}

	/**
	 * 保存角色
	 */
	@ApiOperation("保存角色")
	@PostMapping("/save")
	public Result save(@RequestBody SysRole role){
//		ValidatorUtils.validateEntity(role);

		sysRoleService.saveRole(role);

		return ResultUtil.success();
	}

	/**
	 * 修改角色
	 */
	@ApiOperation("修改角色")
	@PostMapping("/update")
	public Result update(@RequestBody SysRole role){
//		ValidatorUtils.validateEntity(role);

		sysRoleService.update(role);

		return ResultUtil.success();
	}

	/**
	 * 删除角色
	 */
	@ApiOperation("删除角色")
	@PostMapping("/delete")
	public Result delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		return ResultUtil.success();
	}
}
