package com.sqx.domain.repository.mapper.activity;

import com.sqx.domain.repository.entity.sys.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityJpaRepository extends JpaRepository<Activity, Long> {



}
