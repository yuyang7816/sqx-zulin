package com.sqx.domain.repository.mapper.selfActivity;

import com.sqx.domain.repository.entity.sys.SelfActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelfActivityRepository extends Repository<SelfActivity, Long> {
    @Query(value = "from SelfActivity s where s.state=:state")
    List<SelfActivity> findOne(@Param("state") String state);

}