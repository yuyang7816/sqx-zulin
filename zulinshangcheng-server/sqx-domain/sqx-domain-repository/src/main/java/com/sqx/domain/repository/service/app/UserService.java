package com.sqx.domain.repository.service.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.qcloudsms.httpclient.HTTPException;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserEntity;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return
     */
    UserEntity queryByPhone(String phone);

    /**
     * 根据小程序微信openId查询用户
     *
     * @param openId 微信小程序openId
     * @return
     */
    UserEntity queryByOpenId(String openId);

    /**
     * 根据微信APP openId查询用户
     *
     * @param openId 微信APP openId
     * @return
     */
    UserEntity queryByWxOpenId(String openId);

    /**
     * 根据userId查询用户
     *
     * @param userId userId
     * @return
     */
    UserEntity queryByUserId(Long userId);

    UserEntity queryByInvitationCode(String invitationCode);

    /**
     * 根据用户appleId查询用户
     *
     * @param appleId
     * @return
     */
    UserEntity queryByAppleId(String appleId);


    Result wxLogin(String code);

    /**
     * 注册或更新用户信息
     *
     * @param userInfo1 用户信息
     * @return 用户信息
     */
    Result wxRegister(UserEntity userInfo1);

    /**
     * 注册或更新用户信息
     *
     * @param appleId 苹果账号id
     * @return 用户信息
     */
    Result iosRegister(String appleId);

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param state 验证码类型
     * @return
     */
    Result sendMsg(String phone, String state) throws HTTPException;

    Result sendMsg(String phone, String state, Integer code) throws HTTPException;

    /**
     * 绑定手机号
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone);

//    /**
//     * 绑定手机号
//     *
//     * @param phone 手机号
//     * @param code  验证码
//     * @return
//     */
//    Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone);

    /**
     * @param phone
     * @param code
     * @param appleId
     * @param platform
     * @param sysPhone
     * @return
     */
    Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone);

    /**
     * 换绑手机号
     *
     * @param phone  手机号
     * @param msg    验证码
     * @param userId 用户id
     * @return
     */
    Result updatePhone(String phone, String msg, Long userId);

    /**
     * 登录token
     *
     * @param user 用户信息
     * @return
     */
    Result getResult(UserEntity user);

    /**
     * app注册或h5注册
     *
     * @param phone    手机号
     * @param msg      验证按
     * @param password 密码
     * @param platform 来源 app  h5
     * @return
     */
    Result registerCode(String phone, String msg, String platform, Integer sysPhone, String openId, String inviterCode,String password,String userName);


    Result loginByOpenId(String openId);


    Result wxAppLogin(String wxOpenId, String token);


    /**
     * app或h5登录
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return
     */
    Result login(String phone, String pwd);


    /**
     * 根据 code 获取openId
     *
     * @param code
     * @param userId
     * @return
     */
    Result getOpenId(String code, Long userId);


    /**
     * 根据用户id查询用户
     *
     * @param userId 用户id
     * @return
     */
    UserEntity selectUserById(Long userId);

    void pushToSingle(String title, String content, String clientId);

    IPage<UserEntity> selectUserPage(Integer page, Integer limit, String startTime, String endTime,UserEntity userEntity);
    int queryInviterCount(String inviterCode);

    int queryUserCount(int type, String date);

    Double queryPayMoney(int type);


    int userMessage(String date, Integer type);

    Result loginApp(String phone, String password);

    Result registApp(String userName, String phone, String password, String msg, String platform, String inviterCode);


    Result forgetPwd(String pwd, String phone, String msg);

    Result selectUserByUserId(Long userId, Long byUserId);


    Result quickLogin(String phone, String verificationCode, String platform, Integer sysPhone, String openId, String inviterCode,String password);

    HashMap<String ,Object> getExtensionData(Long userId);

    IPage<UserEntity> getExtensionList(Long userId, Integer type, Integer page, Integer limit);

    IPage<HashMap<String, Object>> getProfitList(Long userId,Integer type, Integer page, Integer limit);

    void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid, String url);

    IPage<HashMap<String, Object>> getUserIncomeList(Long userId, Integer type, Integer page, Integer limit);

    void updateByUserId(Long userId,double money);

    Result orderMessageCount(String date, Integer type);

    Result getDataCenter(Long userId);

    int queryWxUserNumber(int type, String date);


    int queryAppUserNumber(int type, String date);

    int queryH5UserNumber(int type, String date);

    int queryPublicUserNumber(int type, String date);

    Result incomeStatistics(String date, Integer type);

    Result userList(Integer page, Integer limit, String userName, String phone, String platform,Integer isVip);

    Result updateUserMoneys(UserEntity userEntity);
}
