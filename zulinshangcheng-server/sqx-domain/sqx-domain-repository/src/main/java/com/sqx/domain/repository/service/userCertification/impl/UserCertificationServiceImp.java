package com.sqx.domain.repository.service.userCertification.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.app.UserCertification;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.CommonInfo;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.mapper.userCertification.UserCertificationDao;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.userCertification.UserCertificationService;
import com.sqx.domain.repository.utils.IdentityCardUtil;
import com.sqx.domain.repository.utils.VerifyIdCardUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实名认证 服务实现类
 *
 * @author wuChang
 * @since 2024-03-22
 */
@Service
public class UserCertificationServiceImp extends ServiceImpl<UserCertificationDao, UserCertification> implements UserCertificationService {
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @Override
    public Result saveUserCertification(UserCertification userCertification) {
        UserEntity userEntity = userService.getById(userCertification.getUserId());
        if (userEntity == null) {
            return Result.failed("用户信息已失效,请重新登录");
        }
        if (userCertification.getId() != null) {
            Long count = baseMapper.selectCount(new QueryWrapper<UserCertification>()
                    .eq("user_id", userCertification.getUserId())
                    .ne("id", userCertification.getId()));
            if (count > 0) {
                return Result.failed("您已上传实名认证，请勿重复提交");
            }
            UserCertification certificate = baseMapper.selectById(userCertification.getId());
            String value = commonInfoService.findOne(303).getValue();
            if ("1".equals(value)) {
                userCertification.setStatus(0);
                userEntity.setSex(IdentityCardUtil.getSex(userCertification.getIdNumber()));
                userEntity.setAge(IdentityCardUtil.getAge(userCertification.getIdNumber()));
                userEntity.setIsAuth(1);
                userService.updateById(userEntity);
            } else {
                String result = VerifyIdCardUtils.verifyIdCard(userCertification.getIdNumber(), userCertification.getRealName());
                if ("ok".equals(result)) {
                    userCertification.setAuditTime(LocalDateTime.now());
                    userCertification.setStatus(1);
//                    if (StringUtils.isNotBlank(userEntity.getOpenId())) {
//                        List<String> msgList = new ArrayList<>();
//                        msgList.add("实名认证");
//                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                        msgList.add(fmt.format(LocalDateTime.now()));
//                        msgList.add("审核已通过");
//                        msgList.add("审核已通过,如有问题请联系管理员");
//                        CommonInfo one = commonInfoService.findOne(292);
//                        msgList.add(fmt.format(LocalDateTime.now()));
//                        SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
//                    }
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setContent("您的实名认证审核已通过");
                    messageInfo.setTitle("审核通知");
                    messageInfo.setState(String.valueOf(6));
                    messageInfo.setUserName(userEntity.getUserName());
                    messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                    messageInfo.setCreateAt(DateUtils.format(new Date()));
                    messageInfo.setIsSee("0");
                    messageService.saveBody(messageInfo);
                    if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                        userService.pushToSingle("审核通知", "您的实名认证审核已通过", userEntity.getClientid());
                    }
                } else {
                    return Result.failed("您填写的信息有误，请检查后重试！");
                }
            }
            return Result.upStatus(baseMapper.updateById(userCertification));
        } else {
            Long count = baseMapper.selectCount(new QueryWrapper<UserCertification>()
                    .eq("user_id", userCertification.getUserId()));
            if (count > 0) {
                return Result.failed("您已上传实名认证，请查看认证信息状态");
            }
            UserCertification oldUserCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>()
                    .eq("id_number", userCertification.getIdNumber()));
            if (oldUserCertification != null) {
                return Result.failed("您已上传实名认证，请查看认证信息状态");
            } else {
                String value = commonInfoService.findOne(303).getValue();
                if ("1".equals(value)) {
                    userCertification.setStatus(0);
                    userEntity.setSex(IdentityCardUtil.getSex(userCertification.getIdNumber()));
                    userEntity.setAge(IdentityCardUtil.getAge(userCertification.getIdNumber()));
                    userEntity.setIsAuth(1);
                    userService.updateById(userEntity);
                } else {
                    String result = VerifyIdCardUtils.verifyIdCard(userCertification.getIdNumber(), userCertification.getRealName());
                    if ("ok".equals(result)) {
                        userCertification.setAuditTime(LocalDateTime.now());
                        userCertification.setStatus(1);
//                        if (StringUtils.isNotBlank(userEntity.getOpenId())) {
//                            List<String> msgList = new ArrayList<>();
//                            msgList.add("实名认证");
//                            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                            msgList.add(fmt.format(LocalDateTime.now()));
//                            msgList.add("审核已通过");
//                            msgList.add("审核已通过,如有问题请联系管理员");
//                            CommonInfo one = commonInfoService.findOne(292);
//                            msgList.add(fmt.format(LocalDateTime.now()));
//                            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
//                        }
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setContent("您的实名认证审核已通过");
                        messageInfo.setTitle("审核通知");
                        messageInfo.setState(String.valueOf(6));
                        messageInfo.setUserName(userEntity.getUserName());
                        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                        messageInfo.setCreateAt(DateUtils.format(new Date()));
                        messageInfo.setIsSee("0");
                        messageService.saveBody(messageInfo);

                        if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                            userService.pushToSingle("审核通知", "您的实名认证审核已通过", userEntity.getClientid());
                        }
                    } else {
                        return Result.failed("您填写的信息有误，请检查后重试！");
                    }
                }
                userCertification.setCreateTime(LocalDateTime.now());
                return Result.upStatus(baseMapper.insert(userCertification));
            }
        }
    }

    @Override
    public IPage<UserCertification> getUserCertificationList(Integer page, Integer limit, UserCertification userCertification) {
        Page<UserCertification> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getUserCertificationList(pages, userCertification);
    }

    @Override
    public Result deleteUserCertificationById(Long id) {
        UserCertification userCertification = baseMapper.selectById(id);
        if (userCertification == null) {
            return Result.failed("所选实名认证不存在");
        } else {
            UserEntity userEntity = userService.getById(userCertification.getUserId());
            if (userEntity != null) {
                userEntity.setIsAuth(0);
                userService.updateById(userEntity);
            }
        }
        return Result.upStatus(baseMapper.deleteById(id));

    }

    @Override
    public Result authCertification(Long id, Integer status, String remark, Long taskId, Integer isSubmit) {
        UserCertification userCertification = baseMapper.selectById(id);
        if (userCertification == null) {
            return Result.failed("实名认证不存在");
        }
        if (status == 1) {
            UserEntity userEntity = userService.selectUserById(userCertification.getUserId());
//            userEntity.setIsAuth(1);
            userService.updateById(userEntity);
            userCertification.setStatus(1);
            userCertification.setRemark(" ");
        } else {
            userCertification.setStatus(2);
            userCertification.setRemark(remark);
            UserEntity userEntity = userService.selectUserById(userCertification.getUserId());
            if (userEntity == null) {
                return Result.failed("用户不存在");
            }
            userEntity.setIsAuth(2);
            userService.updateById(userEntity);
            setUserBaseInfo(userCertification.getIdNumber(), userCertification.getUserId());
        }
        // 新增实名认证通过后增加金币的记录
//        GoldRecords goldRecords = new GoldRecords();
//        goldRecords.setUserId(userCertification.getUserId());
//        goldRecords.setGoldChange(1);
//        goldRecords.setRemark("实名认证奖励");
//        goldRecords.setCreateTime(new Date());
//        goldRecords.setRelatedId(userCertification.getId());
//        goldRecords.setGoldCoins(Integer.valueOf(commonInfoService.findOne(509).getValue()));
//        goldRecordsDao.insert(goldRecords);
//        UserEntity userEntity = userService.getById(userCertification.getUserId());
//        userEntity.setTotalGold(userEntity.getTotalGold() + Integer.valueOf(commonInfoService.findOne(509).getValue()));
//        userService.updateById(userEntity);
        userCertification.setAuditTime(LocalDateTime.now());
        return Result.upStatus(baseMapper.updateById(userCertification));
    }

    public void setUserBaseInfo(String idNumber, Long userId) {
        UserEntity userEntity = userService.getById(userId);
        //年龄
        int genderNum = Integer.parseInt(idNumber.substring(idNumber.length() - 2, idNumber.length() - 1));
        if (genderNum % 2 == 0) {
            //女
            userEntity.setSex(2);
        } else {
            //男
            userEntity.setSex(1);
        }
        userService.updateById(userEntity);
    }

}
