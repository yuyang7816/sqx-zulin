package com.sqx.domain.repository.mapper.wx;

import com.sqx.domain.repository.entity.wx.WordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordJpaRepository extends JpaRepository<WordInfo, Long> {



}
