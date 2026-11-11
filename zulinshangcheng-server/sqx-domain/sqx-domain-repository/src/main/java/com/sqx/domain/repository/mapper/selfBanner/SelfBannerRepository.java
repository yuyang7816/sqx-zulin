package com.sqx.domain.repository.mapper.selfBanner;

import com.sqx.domain.repository.entity.sys.SelfBanner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface SelfBannerRepository extends Repository<SelfBanner, Long> {

    @Query(value = "select * from self_banner where `type`=:type order by sort",nativeQuery = true)
    List<SelfBanner> selectSelfBanner(Integer type);

    @Query(value = "select * from self_banner where `type` is null or `type`=1 order by sort",nativeQuery = true)
    List<SelfBanner> selectSelfBanner();


}