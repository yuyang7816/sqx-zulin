package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.domain.repository.entity.app.EnterpriseCertification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface EnterpriseCertificationDao extends BaseMapper<EnterpriseCertification> {
    IPage<Map<String, Object>> queryCertification(IPage iPage, @Param("status") String status, @Param("name") String name);
    IPage<Map<String, Object>> queryUserCertification(IPage iPage, @Param("name") String name,@Param("phone")String phone);
}
