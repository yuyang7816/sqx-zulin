package com.sqx.domain.repository.service.admin;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.enums.ExceptionEnum;
import com.sqx.common.core.utils.MD5Util;
import com.sqx.common.core.utils.UpdateJPAUtil;
import com.sqx.domain.repository.entity.sys.AdminInfo;
import com.sqx.domain.repository.entity.sys.Token;
import com.sqx.domain.repository.mapper.admin.AdminJpaRepository;
import com.sqx.domain.repository.mapper.admin.AdminRepository;
import com.sqx.domain.repository.mapper.token.TokenJpaRepository;
import com.sqx.domain.repository.mapper.token.TokenRepository;
import com.sqx.domain.repository.service.captcha.SysCaptchaService;
import com.sqx.domain.repository.service.sysRole.SysUserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 管理平台用户登录的service实现类
 */
@Service
public class IAdminSellingServiceImpl implements IAdminSellingService {
    @Autowired
    private AdminJpaRepository couponJpaRepository;
    @Autowired
    private AdminRepository couponRepository;
    @Autowired
    private TokenJpaRepository tokenJpaRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysCaptchaService sysCaptchaService;

    @Override
    public Result findAll() {
        List<AdminInfo> all = couponJpaRepository.findAll();
        return ResultUtil.success(all);
    }

    @Override
    public List<Map<String, Object>> selectAdminInfoList(Pageable pageable) {
        return couponRepository.selectAdminInfoList(pageable);
    }

    @Override
    public Map<String, Object> selectAdminInfoDetails(Long id) {
        return couponRepository.selectAdminInfoDetails(id);
    }

    @Override
    public Result updateAdminInfo(AdminInfo adminInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AdminInfo adminInfo1 = couponJpaRepository.findById(adminInfo.getId()).orElse(null);
        System.out.println("saveBody-------------------------" + adminInfo.getId());
            assert adminInfo1 != null;
            if (adminInfo.getPwd().equals(adminInfo1.getPwd())) {
                adminInfo.setPwd(adminInfo.getPwd());
            } else {
                adminInfo.setPwd(MD5Util.encoderByMd5(adminInfo.getPwd()));
            }
            adminInfo.setCreateAt(sdf.format(new Date()));
            //修改排除空字段
            UpdateJPAUtil.copyNullProperties(adminInfo1, adminInfo);
            AdminInfo adminInfo2=couponJpaRepository.save(adminInfo);
            sysUserRoleService.saveOrUpdate(adminInfo2.getId(),adminInfo.getRoleIdList());
            return ResultUtil.success();
    }



    @Override
    public Result saveBody(AdminInfo adminInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String phone = adminInfo.getPhone();
        AdminInfo login = couponRepository.login(phone);
        AdminInfo adminInfo1 = couponJpaRepository.findById(adminInfo.getId()).orElse(null);
        System.out.println("saveBody-------------------------" + adminInfo.getId());
        if (adminInfo.getId() == 0) {
            if (login != null) {
                return ResultUtil.error(999, "用户名已注册");
            } else {
                adminInfo.setPwd(MD5Util.encoderByMd5(adminInfo.getPwd()));
                adminInfo.setCreateAt(sdf.format(new Date()));
                AdminInfo adminInfo2=couponJpaRepository.save(adminInfo);
                sysUserRoleService.saveOrUpdate(adminInfo2.getId(),adminInfo.getRoleIdList());
                return ResultUtil.success();
            }
        } else {
            if (login != null) {
                if (login.getId() != adminInfo.getId()) {
                    return ResultUtil.error(999, "用户名已注册");
                }
            }
            assert adminInfo1 != null;
            if (adminInfo.getPwd().equals(adminInfo1.getPwd())) {
                adminInfo.setPwd(adminInfo.getPwd());
            } else {
                adminInfo.setPwd(MD5Util.encoderByMd5(adminInfo.getPwd()));
            }
            adminInfo.setCreateAt(sdf.format(new Date()));
            AdminInfo adminInfo2=couponJpaRepository.save(adminInfo);
            sysUserRoleService.saveOrUpdate(adminInfo2.getId(),adminInfo.getRoleIdList());
            return ResultUtil.success();
        }
    }

    @Override
    public Result findOne(long id) {
        AdminInfo a = couponJpaRepository.findById(id).orElse(null);
        return ResultUtil.success(a);
    }

    @Override
    public Result Login(String pwd, String name,String captchaUuid,String captcha) {
        try {
            boolean captchas = sysCaptchaService.validate(captchaUuid, captcha);
            if(!captchas){
                return ResultUtil.error(500,"验证码不正确");
            }

            if (pwd != null && name != null) {
                AdminInfo login = couponRepository.login(name);

                if (login == null) {
                    return ResultUtil.error(ExceptionEnum.USER_PWD_ERROR);
                }
                if (!MD5Util.checkMD5(pwd, login.getPwd())) {
                    return ResultUtil.error(ExceptionEnum.USER_PWD_ERROR);
                }
                long id = login.getId();
                List<Token> byUserId = tokenRepository.findByAdminId(String.valueOf(id));
                if (byUserId != null && byUserId.size()>0) {
                    tokenJpaRepository.deleteAll(byUserId);
                }
                String uuid = UUID.randomUUID().toString();
                Token token = new Token();
                token.setAdminId(String.valueOf(login.getId()));
                token.setUuid(uuid);
                Token save = tokenJpaRepository.save(token);
                return ResultUtil.success(save);
            } else {
                return ResultUtil.error(ExceptionEnum.USER_PWD_EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ResultUtil.error(ExceptionEnum.USER_NOT_FIND);

    }

    @Override
    public Result delete(long id) {
        couponJpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public Result findByMerchant(String state) {
        return null;
    }

    @Override
    public Result find(String state, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public Result updateBody(AdminInfo foodBanner) {
        BeanUtils.copyProperties(foodBanner, Objects.requireNonNull(couponJpaRepository.findById(foodBanner.getId()).orElse(null)));
        return ResultUtil.success();
    }

    @Override
    public AdminInfo getUserByPhone(String userName) {
        return null;
    }

    @Override
    public Result updateState(String state, String id) {
        return null;
    }

    @Override
    @Transactional
    public Result updatePwd(String phone, String pwd) {
        Integer adminInfo = couponRepository.updatePwd(MD5Util.encoderByMd5(pwd), phone);
        if (adminInfo == 1) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ExceptionEnum.UPDATE_PWD_ERROR);
        }
    }
}
