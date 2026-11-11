package com.sqx.domain.repository.service.sysRole;

import com.sqx.domain.repository.entity.sys.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * 角色
 *
 */
public interface SysRoleService {

	Page<SysRole> queryPage(Pageable pageable, String roleName);

	void saveRole(SysRole role);

	void update(SysRole role);

	void deleteBatch(Long[] roleIds);

	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
