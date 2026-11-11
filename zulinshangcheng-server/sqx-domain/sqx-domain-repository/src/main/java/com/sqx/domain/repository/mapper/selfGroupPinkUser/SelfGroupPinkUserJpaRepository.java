package com.sqx.domain.repository.mapper.selfGroupPinkUser;

import com.sqx.domain.repository.entity.sys.SelfGroupPinkUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfGroupPinkUserJpaRepository extends JpaRepository<SelfGroupPinkUser, Long> {

    List<SelfGroupPinkUser> findAllByGroupPinkId(Long groupPinkId);

}
