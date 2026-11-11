package com.sqx.common.core.utils;
import org.springframework.stereotype.Component;

@Component
public class Config {
    //全局的名字 支持动态
    public static String BASE_NAME = "菲汛购";
    //京东的url 静态死的
    public static String JD_URL = "https://router.jd.com/api";
    //第三方的京东链接  http://openapi.weimohe.cn/index/index/openapi/id/57.shtml?ptype=2 不支持动态
    public static String MIAOYOUQUN_URL = "https://api.web.21ds.cn";
    //第三方 京东商品秘钥 支持动态
    public static String MIAOYOUQUN_Key = "";
    //淘宝的key 网站的淘宝appkey 私域授权 淘口令获取 必须和移动端一致

    //以下为公众号的消息推送配置 支持动态
    public static String appId = "";                //appid 通过微信后台获取
    public static String appSecret = ""; //微信公证号秘钥
    public static String token = "";            //通过微信后台获取
    public static String encodingAesKey = "";        //EncodingAESKey(消息加解密密钥) 通过微信后台获取
    //邀请好友注册推送 邀请成功通知 支持动态配置
    public static String InvitaionNotification = "-8r5IaVlAko";
    //提现通知模板ID
    public static String CashNotification = "";
    //会员等级升级消息
    public static String ShengjiNotification = "";
    //结算通知模板ID
    public static String JieSuanNotification = "";
    //提现成功通知
    public static String TiXianSuccessNotification = "";
    //提现成功通知
    public static String TiXianRefundSuccessNotification = "";
    //成为会员通知模板
    public static String RelationNotification = "";
    //新订单通知模板
    public static String orderNotification = "";
    //提现通知管理员的openid 支持动态
    public static String CashOpenId = "";
    //管理平台配置 支持动态
    public static String CashAdmin = "http://www..cn:8080";
    //淘宝授权的私域id 支持动态
    public static String publicRelation = "";            //淘宝授权后获取 可在淘宝联盟查看
    //腾讯云短信发送 需要申请验证码模板 支持动态
    public static String DXAppId = ""; // 1400开头
    public static String DXAppKey = "";


    public static String menuText(String url, String one, String name) {

        String s = "欢迎来到<a href=\"" + url + "/?invitation=" + one + "\">☞ " + name + "</a>\n";
        return s;
    }

    public static String menuText2(String from) {
        String s = "@" + from + "\n" +
                "☞ 红包福利\n" +
                "-------------------------------\n" +
                "<a href=\"$Fzv2YDOggqL$\">☞ 长按我【复制链接】到【手机淘宝】即可领取</a>" +
                "-------------------------------\n";
        return s;
    }

    public static String menuText1(String from, String link) {
        return "@" + from + "\n" +
                "已为您找到相关商品\n" +
                "-------------------------------\n" +
                "<a href=\"" + link + "\">☞ 点我马上购买 >></a>\n" +
                "-------------------------------\n" +
                "人工客服，请点右下角";
    }

    public static String menuText2(String from, String link) {
        return "未能找到您需要的商品\n" +
                "-------------------------------\n" +
                "☞ 请发送【商品标题】至公众号查询 >>\n" +
                "-------------------------------\n" +
                "人工客服，请点右下角";
    }

    public static String sendCustomer(String openid, String title, String description, String url, String image) {
        return "{\n" +
                " \"touser\":\"" + openid + "\",\n" +
                " \"msgtype\":\"news\",\n" +
                " \"news\":{\n" +
                "  \"articles\": [\n" +
                "   {\n" +
                "    \"title\":\"" + title + "\",\n" +
                "    \"description\":\"" + description + "\",\n" +
                "    \"url\":\"" + url + "\",\n" +
                "    \"picurl\":\"" + image + "\"\n" +
                "   }\n" +
                "   ]\n" +
                " }\n" +
                "}";
    }


}
