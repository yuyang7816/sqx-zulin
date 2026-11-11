package com.sqx.domain.repository.mapper.sysRole;

import com.sqx.domain.repository.entity.sys.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SysRoleRepository extends Repository<SysRole, Long> {

    /**
     * 查询用户创建的角色ID列表
     */
    @Query(value = "select role_id from sys_role where create_user_id =:createUserId ",nativeQuery = true)
    List<Long> queryRoleIdList(@Param("createUserId")Long createUserId);


    @Query(value = "select * from sys_role where if(:roleName !='',role_name like concat('%',:roleName,'%'),1=1) ",nativeQuery = true)
    Page<SysRole> queryPage(@Param("roleName") String roleName, Pageable pageable);

    @Query(value = "select * from sys_role where if(:userId !='',create_user_id=:userId,1=1)",nativeQuery = true)
    List<SysRole> selectSysRoleList(@Param("userId") Long userId);

    @Query(value = "select * from sys_role where role_id in (select role_id from sys_user_role where user_id=:userId)",nativeQuery = true)
    List<SysRole> selectSysRoleListByUserId(@Param("userId") Long userId);

}