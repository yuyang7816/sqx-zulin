package com.sqx.domain.repository.mapper.sysRole;

import com.sqx.domain.repository.entity.sys.SysMenu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


public interface SysMenuRepository extends Repository<SysMenu, Long> {


    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    @Query(value = "select * from sys_menu where parent_id = :parentId order by order_num asc",nativeQuery = true)
    List<SysMenu> queryListParentId(@Param("parentId") Long parentId);

    @Query(value = "select * from sys_menu  order by order_num asc",nativeQuery = true)
    List<SysMenu> selectSysMenuList();

    @Query(value = "select m.*,(select m2.name from sys_menu m2 where m1.parent_id=m2.menu_id) as  from sys_menu m1  order by order_num asc",nativeQuery = true)
    List<Map<String,Object>> selectSysMenuLists();

    @Query(value = "select * from sys_menu  where menu_id=:id",nativeQuery = true)
    SysMenu selectById(@Param("id")Long id);

    /**
     * 获取不包含按钮的菜单列表
     */
    @Query(value = "select * from sys_menu where `type` != 2 order by order_num asc",nativeQuery = true)
    List<SysMenu> queryNotButtonList();

    /**
     * 查询用户的所有菜单ID
     */
    @Query(value = "select distinct rm.menu_id from sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id " +
            "where ur.user_id = :userId",nativeQuery = true)
    List<Long> queryAllMenuId(@Param("userId") Long userId);


    @Transactional
    @Modifying
    @Query(value = "delete from sys_menu where menu_id=:menuId",nativeQuery = true)
    void deleteByMenuId(@Param("menuId") Long menuId);


    @Query(value = "select m.perms from sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m on rm.menu_id = m.menu_id " +
            "where ur.user_id =:userId",nativeQuery = true)
    List<String> queryAllPerms(@Param("userId") Long userId);


}