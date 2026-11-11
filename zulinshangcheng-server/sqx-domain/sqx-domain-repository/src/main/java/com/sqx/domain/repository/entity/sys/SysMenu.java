package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 *
 */
@Data
@Entity
public class SysMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long menuId;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	@Column
	private Long parentId;
	
	/**
	 * 父菜单名称
	 */
	@Transient
	private String parentName;

	/**
	 * 菜单名称
	 */
	@Column
	private String name;

	/**
	 * 菜单URL
	 */
	@Column
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	@Column
	private String perms;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	@Column
	private Integer type;

	/**
	 * 菜单图标
	 */
	@Column
	private String icon;

	/**
	 * 排序
	 */
	@Column
	private Integer orderNum;
	
	/**
	 * ztree属性
	 */
	@Transient
	private Boolean open;

	@Transient
	private List<?> list;

}
