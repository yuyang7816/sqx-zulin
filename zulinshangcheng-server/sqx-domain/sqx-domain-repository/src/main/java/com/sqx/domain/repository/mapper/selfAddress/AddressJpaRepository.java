package com.sqx.domain.repository.mapper.selfAddress;


import com.sqx.domain.repository.entity.app.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressJpaRepository extends JpaRepository<Address, Long> {

    @Query(value = "from Address s where s.userId=:userId order by s.addressDefault desc")
    List<Address> findByUserId(@Param("userId") Long userId);

}
