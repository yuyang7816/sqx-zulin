package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户与角色对应关系
 *
 */
@Data
@Entity
public class SysUserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 用户ID
	 */
	@Column
	private Long userId;

	/**
	 * 角色ID
	 */
	@Column
	private Long roleId;

	
}
