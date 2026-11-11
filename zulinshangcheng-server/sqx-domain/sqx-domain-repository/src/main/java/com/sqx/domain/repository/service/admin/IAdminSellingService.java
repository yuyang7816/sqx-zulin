package com.sqx.domain.repository.service.admin;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.AdminInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IAdminSellingService {
    /**
     * 获取全部
     *
     * @return
     */
    Result findAll();

    List<Map<String,Object>> selectAdminInfoList(Pageable pageable);

    Map<String,Object> selectAdminInfoDetails(Long id);

    Result updateAdminInfo(AdminInfo adminInfo);
    /**
     * 保存对象
     *
     * @param coupon
     */
    Result saveBody(AdminInfo coupon);

    /**
     * 获取一个对象
     */
    Result findOne(long id);

    /**
     * 获取一个对象
     */
    Result Login(String pwd, String name,String captchaUuid,String captcha);

    /**
     * 删除一个
     */
    Result delete(long id);

    Result findByMerchant(String state);

    /**
     * 条件查询
     * <p>
     * 分页
     */
    Result find(String state, Pageable pageable);

    /**
     * 修改
     *
     * @param coupon 对象
     */
    Result updateBody(AdminInfo coupon);

    AdminInfo getUserByPhone(String userName);
    /**
     * 修改管理员状态
     *
     *
     */
    Result updateState(String state, String id);
    /**
     * 修改密码
     *
     *
     */
    Result updatePwd(String phone, String pwd);
}
