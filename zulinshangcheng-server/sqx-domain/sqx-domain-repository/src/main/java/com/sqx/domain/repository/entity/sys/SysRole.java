package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 */
@Data
@Entity
public class SysRole implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;

	/**
	 * 角色名称
	 */
	@NotBlank(message="角色名称不能为空")
	@Column
	private String roleName;

	/**
	 * 备注
	 */
	@Column
	private String remark;
	
	/**
	 * 创建者ID
	 */
	@Column
	private Long createUserId;

	@Transient
	private List<Long> menuIdList;
	
	/**
	 * 创建时间
	 */
	@Column
	private Date createTime;

	
}
