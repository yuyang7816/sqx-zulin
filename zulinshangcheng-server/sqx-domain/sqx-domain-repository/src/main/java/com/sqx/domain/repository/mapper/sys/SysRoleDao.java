package com.sqx.domain.repository.mapper.sys;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.sys.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色管理
 *
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
