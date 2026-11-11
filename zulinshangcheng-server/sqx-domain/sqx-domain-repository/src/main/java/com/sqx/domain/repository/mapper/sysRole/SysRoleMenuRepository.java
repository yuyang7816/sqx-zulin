package com.sqx.domain.repository.mapper.sysRole;

import com.sqx.domain.repository.entity.sys.SysRoleMenu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface SysRoleMenuRepository extends Repository<SysRoleMenu, Long> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    @Query(value = "select menu_id from sys_role_menu where role_id =:roleId order by id",nativeQuery = true)
    List<Long> queryMenuIdList(@Param("roleId") Long roleId);

    /**
     * 根据角色ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from sys_role_menu where role_id=:roleId",nativeQuery = true)
    int deleteBatch(@Param("roleId")Long roleId);

}