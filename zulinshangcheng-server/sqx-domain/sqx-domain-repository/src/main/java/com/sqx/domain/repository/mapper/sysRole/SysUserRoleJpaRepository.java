package com.sqx.domain.repository.mapper.sysRole;

import com.sqx.domain.repository.entity.sys.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRoleJpaRepository extends JpaRepository<SysUserRole, Long> {



}
