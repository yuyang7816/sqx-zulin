package com.sqx.domain.repository.mapper.banner;

import com.sqx.domain.repository.entity.sys.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerJpaRepository extends JpaRepository<Banner, Long> {



}
