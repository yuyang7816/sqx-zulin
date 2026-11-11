package com.sqx.domain.repository.mapper.sys;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.sys.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 *
 */
@Mapper
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

    SysUserTokenEntity queryByToken(String token);
	
}
