package com.sqx.domain.repository.mapper.money;

import com.sqx.domain.repository.entity.sys.MoneyDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


public interface MoneyDetailsRepository extends Repository<MoneyDetails, Long> {


    @Query(value = "select * from money_details where user_id=:userId",
            countQuery = "select count(*) from money_details where user_id=:userId",
            nativeQuery = true)
    Page<MoneyDetails> selectMoneyDetailsByUserId(Pageable pageable,@Param("userId") Long userId);

}