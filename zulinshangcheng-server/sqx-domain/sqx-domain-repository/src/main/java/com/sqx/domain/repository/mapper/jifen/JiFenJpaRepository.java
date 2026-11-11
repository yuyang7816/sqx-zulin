package com.sqx.domain.repository.mapper.jifen;

import com.sqx.domain.repository.entity.sys.JiFen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JiFenJpaRepository extends JpaRepository<JiFen, Long> {

    List<JiFen> findAllByUserIdOrderByCreateAtDesc(String userId);
}
