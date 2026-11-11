package com.sqx.domain.repository.mapper.selfAdvert;

import com.sqx.domain.repository.entity.sys.Adverti;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface AdvertRepository extends Repository<Adverti, Long> {


    @Query(value = "select * from adverti where `type`=:type order by create_at desc ",nativeQuery = true)
    List<Adverti> selectSelfBanner(Integer type);

    @Query(value = "select * from adverti where `type` is null or `type`=1 order by create_at desc",nativeQuery = true)
    List<Adverti> selectSelfBanner();

}