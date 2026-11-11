package com.sqx.domain.repository.mapper.admin;

import com.sqx.domain.repository.entity.sys.AdminInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface AdminRepository extends Repository<AdminInfo, Long> {

    @Query(value = "from AdminInfo s where s.phone=:phone")
    AdminInfo login(@Param("phone") String phone);


    @Query(value = "select a.id,a.phone,a.user_name as userName,a.pwd,a.create_at as createAt from admin_info a ",
            countQuery = "select count(*) from admin_info a ",nativeQuery = true)
    List<Map<String,Object>> selectAdminInfoList(Pageable pageable);

    @Query(value = "select a.id,a.phone,a.user_name as userName,a.pwd,a.create_at as createAt from admin_info a where a.id=:id", nativeQuery = true)
    Map<String,Object> selectAdminInfoDetails(@Param("id") Long id);

    @Query(value = "from AdminInfo s where s.id=:id")
    AdminInfo getUserByAdminId(@Param("id") Long id);


    @Modifying
    @Query(value = "update AdminInfo s set s.pwd=:pwd where s.phone=:phone")
    Integer updatePwd(@Param("pwd") String pwd, @Param("phone") String phone);

}