package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色与菜单对应关系
 *
 */
@Data
@Entity
public class SysRoleMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 角色ID
	 */
	@Column
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@Column
	private Long menuId;
	
}
