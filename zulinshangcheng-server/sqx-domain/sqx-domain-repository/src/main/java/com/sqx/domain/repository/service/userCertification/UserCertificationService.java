package com.sqx.domain.repository.service.userCertification;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserCertification;

/**
 * 实名认证 服务类
 * @author wuChang
 * @since 2024-03-22
 */

public interface UserCertificationService extends IService<UserCertification> {
    /**
     * 新增或修改实名认证
     */
    Result saveUserCertification (UserCertification userCertification);

    /**
     * 获取改实名认证列表
     */
    IPage<UserCertification> getUserCertificationList(Integer page, Integer limit, UserCertification userCertification);

    Result deleteUserCertificationById(Long id);

    Result authCertification(Long id, Integer status, String remark,Long taskId,Integer isSubmit);
}
