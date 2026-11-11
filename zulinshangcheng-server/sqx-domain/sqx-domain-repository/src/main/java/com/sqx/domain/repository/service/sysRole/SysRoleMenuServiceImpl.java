package com.sqx.domain.repository.service.sysRole;

import com.sqx.domain.repository.entity.sys.SysRoleMenu;
import com.sqx.domain.repository.mapper.sysRole.SysRoleMenuJpaRepository;
import com.sqx.domain.repository.mapper.sysRole.SysRoleMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色与菜单对应关系
 *
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

	@Autowired
	private SysRoleMenuJpaRepository sysRoleMenuJpaRepository;
	@Autowired
	private SysRoleMenuRepository sysRoleMenuRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
		//先删除角色与菜单关系
		deleteBatch(new Long[]{roleId});

		if(menuIdList.size() == 0){
			return ;
		}

		//保存角色与菜单关系
		for(Long menuId : menuIdList){
			SysRoleMenu sysRoleMenuEntity = new SysRoleMenu();
			sysRoleMenuEntity.setMenuId(menuId);
			sysRoleMenuEntity.setRoleId(roleId);

			sysRoleMenuJpaRepository.save(sysRoleMenuEntity);
		}
	}

	@Override
	public List<Long> queryMenuIdList(Long roleId) {
		return sysRoleMenuRepository.queryMenuIdList(roleId);
	}

	@Override
	public int deleteBatch(Long[] roleIds){
		for(Long roleId:roleIds){
			sysRoleMenuRepository.deleteBatch(roleId);
		}
		return 1;
	}

}
