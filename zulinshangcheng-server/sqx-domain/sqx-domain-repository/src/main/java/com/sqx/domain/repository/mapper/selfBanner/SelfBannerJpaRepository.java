package com.sqx.domain.repository.mapper.selfBanner;


import com.sqx.domain.repository.entity.sys.SelfBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfBannerJpaRepository extends JpaRepository<SelfBanner, Long> {

}
