package com.sqx.domain.repository.mapper.banner;

import com.sqx.domain.repository.entity.sys.Banner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BannerRepository extends Repository<Banner, Long> {
    @Query(value = "from Banner s where s.state=:state order by id desc")
    List<Banner> findOne( @Param("state") String state);

    @Modifying
    @Query(value = "update Banner s set s.state=:state where s.id=:id")
    Integer updateState(@Param("state") String state, @Param("id") long id);
}