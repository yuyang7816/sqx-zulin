package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.app.Msg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户
 *
 */
@Mapper
public interface MsgDao extends BaseMapper<Msg> {

    Msg findByPhone(String phone);

    Msg findByPhoneAndCode(@Param("phone") String phone, @Param("msg") String msg);



}
