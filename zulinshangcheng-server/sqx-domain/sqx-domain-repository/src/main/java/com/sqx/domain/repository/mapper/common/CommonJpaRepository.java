package com.sqx.domain.repository.mapper.common;

import com.sqx.domain.repository.entity.sys.CommonInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonJpaRepository extends JpaRepository<CommonInfo, Long> {



}
