package com.sqx.domain.repository.mapper.sysRole;

import com.sqx.domain.repository.entity.sys.SysUserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface SysUserRoleRepository extends Repository<SysUserRole, Long> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    @Query(value = "select role_id from sys_user_role where user_id = :userId",nativeQuery = true)
    List<Long> queryRoleIdList(@Param("userId") Long userId);


   /**
     * 根据角色ID数组，批量删除
     */
    @Transactional
    @Modifying
    @Query(value = "delete from sys_user_role where role_id=:roleId",nativeQuery = true)
    int deleteBatch(@Param("roleId") Long roleId);

    @Transactional
    @Modifying
    @Query(value = "delete from sys_user_role where user_id=:userId",nativeQuery = true)
    int deleteByUserId(@Param("userId") Long userId);

}