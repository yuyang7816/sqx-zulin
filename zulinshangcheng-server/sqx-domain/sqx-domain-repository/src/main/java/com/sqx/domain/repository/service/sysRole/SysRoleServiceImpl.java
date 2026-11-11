package com.sqx.domain.repository.service.sysRole;

import com.sqx.domain.repository.entity.sys.SysRole;
import com.sqx.domain.repository.mapper.sysRole.SysMenuRepository;
import com.sqx.domain.repository.mapper.sysRole.SysRoleJpaRepository;
import com.sqx.domain.repository.mapper.sysRole.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 */
@Service("sysRoleService")
public class SysRoleServiceImpl  implements SysRoleService {

	@Autowired
	private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
	private SysRoleJpaRepository sysRoleJpaRepository;
    @Autowired
	private SysRoleRepository sysRoleRepository;
    @Autowired
	private SysMenuRepository sysMenuRepository;


	@Override
	public Page<SysRole> queryPage(Pageable pageable, String roleName ) {
		return sysRoleRepository.queryPage(roleName,pageable);
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRole role) {
        role.setCreateTime(new Date());
		sysRoleJpaRepository.save(role);

       /* //检查权限是否越权
        checkPrems(role);*/

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

	@Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRole role) {
		role.setCreateTime(new Date());
		sysRoleJpaRepository.save(role);

        /*//检查权限是否越权
        checkPrems(role);*/

        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
		for(Long roleId:roleIds){
			sysRoleJpaRepository.deleteById(roleId);
		}
        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }


    @Override
	public List<Long> queryRoleIdList(Long createUserId) {
		return sysRoleRepository.queryRoleIdList(createUserId);
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRole role){
		//如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		/*if(role.getCreateUserId() ==1){
			return ResultUtil;
		}*/
		
		//查询用户所拥有的菜单列表
		/*List<Long> menuIdList = sysMenuRepository.queryAllMenuId(role.getCreateUserId());
		
		//判断是否越权
		if(!menuIdList.containsAll(role.getMenuIdList())){
			throw new DescribeException("新增角色的权限，已超出你的权限范围",-100);
		}*/
	}
}
