package com.sqx.domain.repository.mapper.userCertification;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.domain.repository.entity.app.UserCertification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 实名认证 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2024-03-22
 */
@Mapper
public interface UserCertificationDao extends BaseMapper<UserCertification> {

    IPage<UserCertification> getUserCertificationList(@Param("pages") Page<UserCertification> pages, @Param("userCertification") UserCertification userCertification);

    UserCertification selectByUserId(@Param("userId") Long userId, @Param("taskId") Long taskId);
}
