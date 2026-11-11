package com.sqx.domain.repository.mapper.admin;

import com.sqx.domain.repository.entity.sys.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterJpaRepository extends JpaRepository<CharacterInfo, Long> {


}
