package com.sqx.domain.repository.mapper.sysRole;

import com.sqx.domain.repository.entity.sys.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleJpaRepository extends JpaRepository<SysRole, Long> {



}
