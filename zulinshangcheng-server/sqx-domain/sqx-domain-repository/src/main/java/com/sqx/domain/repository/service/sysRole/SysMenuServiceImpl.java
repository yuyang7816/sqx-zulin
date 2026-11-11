package com.sqx.domain.repository.service.sysRole;

import com.sqx.domain.repository.entity.sys.SysMenu;
import com.sqx.domain.repository.mapper.sysRole.SysMenuJpaRepository;
import com.sqx.domain.repository.mapper.sysRole.SysMenuRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuJpaRepository sysMenuJpaRepository;
	@Autowired
	private SysMenuRepository sysMenuRepository;
	
	@Override
	public List<SysMenu> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenu> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenu> userMenuList = new ArrayList<>();
		for(SysMenu menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenu> queryListParentId(Long parentId) {
		return sysMenuRepository.queryListParentId(parentId);
	}

	@Override
	public List<SysMenu> queryNotButtonList() {
		return sysMenuRepository.queryNotButtonList();
	}

	@Override
	public List<SysMenu> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		/*if(userId == 1){
			return getAllMenuList(null);
		}*/
		
		//用户菜单列表
		List<Long> menuIdList = sysMenuRepository.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public Set<String> getUserPermissions(long userId) {
		List<String> permsList;

		//系统管理员，拥有最高权限
		if(userId == 1){
			List<SysMenu> menuList = sysMenuJpaRepository.findAll();
			permsList = new ArrayList<>(menuList.size());
			for(SysMenu menu : menuList){
				permsList.add(menu.getPerms());
			}
		}else{
			permsList = sysMenuRepository.queryAllPerms(userId);
		}
		//用户权限列表
		Set<String> permsSet = new HashSet<>();
		for(String perms : permsList){
			if(StringUtils.isBlank(perms)){
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}
		return permsSet;
	}



	@Override
	public void delete(Long menuId){
		//删除菜单
		sysMenuJpaRepository.deleteById(menuId);
		//删除菜单与角色关联
		sysMenuRepository.deleteByMenuId(menuId);
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenu> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenu> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Long> menuIdList){
		List<SysMenu> subMenuList = new ArrayList<SysMenu>();
		
		for(SysMenu entity : menuList){
			//目录
			if(entity.getType() == 0){
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}
}
