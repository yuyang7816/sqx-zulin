package com.sqx.system.sysRole;


import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.exception.DescribeException;
import com.sqx.domain.repository.entity.sys.SysMenu;
import com.sqx.domain.repository.mapper.sysRole.SysMenuJpaRepository;
import com.sqx.domain.repository.mapper.sysRole.SysMenuRepository;
import com.sqx.domain.repository.service.sysRole.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统菜单
 *
 */
@Api(value = "系统菜单", tags = {"系统菜单"})
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysMenuJpaRepository sysMenuJpaRepository;
	@Autowired
	private SysMenuRepository sysMenuRepository;
	/**
	 * 导航菜单
	 */
	@ApiOperation("导航菜单")
	@GetMapping("/nav")
	public Result nav(Long userId){
		List<SysMenu> menuList = sysMenuService.getUserMenuList(userId);
		Set<String> permissions = sysMenuService.getUserPermissions(userId);
		Map<String,Object> map=new HashMap<>();
		map.put("menuList",menuList);
		map.put("permissions",permissions);
		return ResultUtil.success(map);
	}

	/**
	 * 所有菜单列表
	 */
	@ApiOperation("所有菜单列表")
	@GetMapping("/list")
	public List<SysMenu> list(){
		List<SysMenu> menuList = sysMenuRepository.selectSysMenuList();
		/*List<SysMenu> menuList = sysMenuService.list(new QueryWrapper<SysMenu>().orderByAsc("order_num"));*/
		for(SysMenu sysMenuEntity : menuList){
			SysMenu parentMenuEntity = sysMenuRepository.selectById(sysMenuEntity.getParentId());
			if(parentMenuEntity != null){
				sysMenuEntity.setParentName(parentMenuEntity.getName());
			}
		}

		return menuList;
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@ApiOperation("选择菜单(添加、修改菜单)")
	@GetMapping("/select")
	public Result select(){
		//查询列表数据
		List<SysMenu> menuList = sysMenuService.queryNotButtonList();

		//添加顶级菜单
		SysMenu root = new SysMenu();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);

		return ResultUtil.success(menuList);
	}

	/**
	 * 菜单信息
	 */
	@ApiOperation("菜单信息")
	@GetMapping("/info/{menuId}")
	public Result info(@PathVariable("menuId") Long menuId){
		SysMenu menu = sysMenuJpaRepository.findById(menuId).orElse(null);
		return ResultUtil.success(menu);
	}

	/**
	 * 保存
	 */
	@ApiOperation("保存菜单")
	@PostMapping("/save")
	public Result save(@RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);

		sysMenuJpaRepository.save(menu);

		return ResultUtil.success();
	}

	/**
	 * 修改
	 */
	@ApiOperation("修改")
	@PostMapping("/update")
	public Result update(@RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);

		sysMenuJpaRepository.save(menu);

		return ResultUtil.success();
	}

	/**
	 * 删除
	 */
	@ApiOperation("删除")
	@PostMapping("/delete/{menuId}")
	public Result delete(@PathVariable("menuId") long menuId){
		if(menuId <= 31){
			return ResultUtil.error(-100,"系统菜单，不能删除");
		}
		//判断是否有子菜单或按钮
		List<SysMenu> menuList = sysMenuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return ResultUtil.error(-100,"请先删除子菜单或按钮");
		}

		sysMenuService.delete(menuId);

		return ResultUtil.success();
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenu menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new DescribeException("菜单名称不能为空",-100);
		}

		if(menu.getParentId() == null){
			throw new DescribeException("上级菜单不能为空",-100);
		}

		//菜单
		if(menu.getType() == 1){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new DescribeException("菜单URL不能为空",-100);
			}
		}

		//上级菜单类型
		int parentType = 0;
		if(menu.getParentId() != 0){
			SysMenu parentMenu = sysMenuJpaRepository.findById(menu.getParentId()).orElse(null);
			parentType = parentMenu.getType();
		}

		//目录、菜单
		if(menu.getType() == 0 ||
				menu.getType() == 1){
			if(parentType != 0){
				throw new DescribeException("上级菜单只能为目录类型",-100);
			}
			return ;
		}

		//按钮
		if(menu.getType() == 2){
			if(parentType != 1){
				throw new DescribeException("上级菜单只能为菜单类型",-100);
			}
		}
	}
}
