package com.sqx.domain.repository.mapper.common;

import com.sqx.domain.repository.entity.sys.CommonInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommonRepository extends Repository<CommonInfo, Long> {
    @Query(value = "from CommonInfo s where s.conditionFrom=:condition")
    List<CommonInfo> findByCondition(@Param("condition") String condition);

    /**
     * <Option value='1'>客服二维码</Option>
     * <Option value="2">公众号二维码</Option>
     * <Option value="3">佣金开启</Option>
     * <Option value="4">注册邀请码</Option>
     * <Option value="5">微信APPID</Option>
     * <Option value="21">微信秘钥</Option>
     * <Option value="6">淘宝APPID</Option>
     * <Option value="7">淘宝秘钥</Option>
     * <Option value="8">淘宝授权地址</Option>
     * <Option value="9">淘宝PID</Option>
     * <Option value="10">好单库key</Option>
     * <Option value="11">淘宝名</Option>
     * <Option value="12">后台服务名称</Option>
     * <Option value="13">京东APPID</Option>
     * <Option value="14">京东秘钥</Option>
     * <Option value="15">私域邀请码（唯一不变）</Option>
     * <Option value="16">公众号Token</Option>
     * <Option value="17">公众号EncodingAESKey</Option>
     * <Option value="18">提现通知管理员openid</Option>
     * <Option value="19">后台服务域名配置</Option>
     * <Option value="20">后台管理平台域名配置</Option>
     * <Option value="22">拼多多优惠券地址</Option>
     * <Option value="23">拼多多APPID</Option>
     * <Option value="24">拼多多秘钥</Option>
     * <Option value="26">拼多多默认PID</Option>
     * <Option value="25">APP下载地址</Option>
     * <Option value="27">京东默认推广位pid</Option>
     *
     * <Option value="28">京东网站siteId</Option>
     * <Option value="29">瞄有券京东授权的key</Option>
     * <Option value="30">瞄有券秘钥</Option>
     * <Option value="31">腾讯云短信clientId</Option>
     * <Option value="32">腾讯云短信clientSecret</Option>
     *
     * <Option value="33">邀请注册成功通知</Option>
     * <Option value="34">会员申请成功通知</Option>
     * <Option value="35">新订单通知</Option>
     * <Option value="36">成为会员通知</Option>
     * <Option value="37">结算完成通知</Option>
     * <Option value="38">提现申请通知</Option>
     * <Option value="39">提现成功通知</Option>
     *
     * <Option value="40">最低佣金</Option>
     * <Option value="41">最高佣金</Option>
     * <Option value="42">用户邀请升级人数</Option>
     * <Option value="43">二级佣金比例</Option>
     *
     * @param type
     * @return
     */
    @Query(value = "from CommonInfo s where s.type=:type")
    CommonInfo findOne(@Param("type") Integer type);
}