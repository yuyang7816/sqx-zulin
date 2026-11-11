package com.sqx.domain.repository.mapper.selfAdvert;


import com.sqx.domain.repository.entity.sys.Adverti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertJpaRepository extends JpaRepository<Adverti, Long> {

}
