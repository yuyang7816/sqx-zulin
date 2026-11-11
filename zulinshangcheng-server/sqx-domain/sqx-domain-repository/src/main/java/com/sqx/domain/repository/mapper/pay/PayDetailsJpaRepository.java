package com.sqx.domain.repository.mapper.pay;

import com.sqx.domain.repository.entity.pay.PayDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayDetailsJpaRepository extends JpaRepository<PayDetails, Long> {


}
