package com.sqx.domain.repository.mapper.selfActivity;


import com.sqx.domain.repository.entity.sys.SelfActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfActivityJpaRepository extends JpaRepository<SelfActivity, Long> {



}
