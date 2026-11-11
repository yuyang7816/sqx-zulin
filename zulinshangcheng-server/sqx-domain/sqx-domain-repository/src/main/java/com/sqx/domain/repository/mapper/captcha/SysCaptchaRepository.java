package com.sqx.domain.repository.mapper.captcha;

import com.sqx.domain.repository.entity.sys.SysCaptcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SysCaptchaRepository extends JpaRepository<SysCaptcha, Long> {

    @Query(value = "select * from sys_captcha s where s.uuid=:uuid limit 1",nativeQuery = true)
    SysCaptcha findByUuid(@Param("uuid") String uuid);

    @Transactional
    @Modifying
    @Query(value = "delete from sys_captcha where uuid=:uuid",nativeQuery = true)
    Integer deleteSysCaptchaByUuid(@Param("uuid")String uuid);

}
