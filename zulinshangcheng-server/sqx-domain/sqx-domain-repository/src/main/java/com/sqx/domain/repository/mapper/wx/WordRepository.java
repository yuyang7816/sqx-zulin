package com.sqx.domain.repository.mapper.wx;

import com.sqx.domain.repository.entity.wx.WordInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface WordRepository extends Repository<WordInfo, Long> {
    @Query(value = "from WordInfo s where s.lable=:lable")
    WordInfo findOne(@Param("lable") String lable);
}