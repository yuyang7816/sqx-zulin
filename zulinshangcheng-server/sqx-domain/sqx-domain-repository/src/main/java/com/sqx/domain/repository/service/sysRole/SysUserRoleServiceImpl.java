package com.sqx.domain.repository.service.sysRole;

import com.sqx.domain.repository.entity.sys.SysUserRole;
import com.sqx.domain.repository.mapper.sysRole.SysUserRoleJpaRepository;
import com.sqx.domain.repository.mapper.sysRole.SysUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户与角色对应关系
 *
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService {

	@Autowired
	private SysUserRoleJpaRepository sysUserRoleJpaRepository;
	@Autowired
	private SysUserRoleRepository sysUserRoleRepository;

	@Override
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		//先删除用户与角色关系
		sysUserRoleRepository.deleteByUserId(userId);

		if(roleIdList == null || roleIdList.size() == 0){
			return ;
		}

		//保存用户与角色关系
		for(Long roleId : roleIdList){
			SysUserRole sysUserRoleEntity = new SysUserRole();
			sysUserRoleEntity.setUserId(userId);
			sysUserRoleEntity.setRoleId(roleId);

			sysUserRoleJpaRepository.save(sysUserRoleEntity);
		}
	}

	@Override
	public List<Long> queryRoleIdList(Long userId) {
		return sysUserRoleRepository.queryRoleIdList(userId);
	}

	@Override
	public int deleteBatch(Long[] roleIds){
		for(Long roleId:roleIds){
			sysUserRoleRepository.deleteBatch(roleId);
		}
		return 1;
	}
}
