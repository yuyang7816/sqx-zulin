package com.sqx.domain.repository.mapper.admin;

import com.sqx.domain.repository.entity.sys.AdminInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminJpaRepository extends JpaRepository<AdminInfo, Long> {



}
