package com.sqx.domain.repository.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sqx.common.core.utils.HttpClientUtil;
import com.sqx.common.core.utils.TemplateParam;
import com.sqx.common.core.utils.WxMssVo;
import com.sqx.domain.repository.entity.app.WxTemplateMsgVo;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SenInfoCheckUtil {

    private static Logger logger = LoggerFactory.getLogger(SenInfoCheckUtil.class);

    private static String MpAccessToken;

    // 这里使用静态，让 service 属于类
    private static ICommonInfoService commonInfoService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setWxChatContentService(ICommonInfoService commonInfoService) {
        SenInfoCheckUtil.commonInfoService = commonInfoService;
    }


    /**
     * 获取Token  小程序
     *
     * @param
     * @param
     * @return AccessToken
     */
    public static String getMpToken() {
        if (StringUtils.isEmpty(MpAccessToken)) {
            getMpAccessToken();
        }
        return MpAccessToken;
    }


    public static void getImg(String invitationCode, String page, HttpServletResponse response) {
        String mpToken = getMpToken();
        //获取二维码数据
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + mpToken;
        Map<String, Object> map = Maps.newHashMap();
//        map.put("scene",relation+"&"+goodsId+"&"+type);
        map.put("scene", invitationCode);
        // 此开关只有小程序上线了才打开，下面的page才生效
        String value = commonInfoService.findOne(105).getValue();
        if ("是".equals(value)) {
            map.put("page", page);
        }
        map.put("width", 280);
        String jsonString = JSON.toJSONString(map);
        InputStream inputStream = sendPostBackStream(url, jsonString);
        //生成二维码图片
        response.setContentType("image/png");
        try {
            BufferedImage bi = ImageIO.read(inputStream);
            ImageIO.write(bi, "JPG", response.getOutputStream());
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取二维码图片
     */
    public static void getPoster(String invitationCode, HttpServletResponse response) {
        String mpToken = getMpToken();
        //获取二维码数据
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + mpToken;
        Map<String, Object> map = Maps.newHashMap();
        map.put("scene", invitationCode);
        map.put("width", 280);
        String jsonString = JSON.toJSONString(map);
        InputStream inputStream = sendPostBackStream(url, jsonString);
        //生成二维码图片
        response.setContentType("image/png");
        try {
            BufferedImage bi = ImageIO.read(inputStream);
            ImageIO.write(bi, "JPG", response.getOutputStream());
            inputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static InputStream sendPostBackStream(String url, String param) {
        PrintWriter out = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //解决乱码问题
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            out = new PrintWriter(outWriter);
            // 发送请求参数
            if (StringUtils.isNotBlank(param)) {
                out.print(param);
            }
            // flush输出流的缓冲
            out.flush();
            out.close();
            return conn.getInputStream();
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！" + e);
        } finally {
            IOUtils.closeQuietly(out);
        }
        return null;
    }


    /**
     * 获取access_token
     * 每个两个小时自动刷新AcessTocken
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public static void getMpAccessToken() {
        String appid = commonInfoService.findOne(45).getValue();
        String secret = commonInfoService.findOne(46).getValue();
        String jsonResult = HttpClientUtil.doPost("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret);
        JSONObject parseObject = JSON.parseObject(jsonResult);
        logger.info("=========accessTokenOut=========" + parseObject.toJSONString());

        String errcode = parseObject.getString("errcode");
        String accessToken = parseObject.getString("access_token");
        String expiresIn = parseObject.getString("expires_in");
        MpAccessToken = accessToken;
    }


    // 发送小程序消息
    public static void sendMsg(String wxId, String templateId, List<String> msgList, Integer type) {
        String mpToken = getMpToken();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + mpToken;
        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(wxId);//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTemplate_id(templateId);//订阅消息模板id
        wxMssVo.setPage("/pages/index/index");
        wxMssVo.setData(getParam(type, msgList));
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        String body = responseEntity.getBody();
        System.err.println("--------------微信小程序推送：" + body);

    }

    // 发送公众号消息
    public static void sendOfficialAccountMsg(String wxId, String templateId, List<String> msgList, Integer type) {
        String mpToken = getMpToken();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + mpToken;

        WxTemplateMsgVo wxTemplateMsgVo = new WxTemplateMsgVo();
        wxTemplateMsgVo.setTouser(wxId);
        wxTemplateMsgVo.setTemplate_id(templateId);
        wxTemplateMsgVo.setUrl("https://xianyu.xianmaxiong.com"); // 跳转链接
        wxTemplateMsgVo.setData(getParam(type, msgList));

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxTemplateMsgVo, String.class);
        String body = responseEntity.getBody();
        System.err.println("--------------微信公众号推送："+body);
    }


    public static Map<String, TemplateParam> getParam(Integer type, List<String> msgList) {
        Map<String, TemplateParam> paras = new HashMap<>();
        // 1.订单通知
        if (type == 1) {
            paras.put("character_string13", new TemplateParam(msgList.get(0)));
            paras.put("thing14", new TemplateParam(msgList.get(1)));
            paras.put("number16", new TemplateParam(msgList.get(2)));
            paras.put("time55", new TemplateParam(msgList.get(3)));
            paras.put("thing45", new TemplateParam(msgList.get(4)));
            // 退款通知
        } else if (type == 2) {
            paras.put("character_string3", new TemplateParam(msgList.get(0)));
            paras.put("amount2", new TemplateParam(msgList.get(1)));
            paras.put("thing4", new TemplateParam(msgList.get(1)));
            // 商品、私聊通知
        } else if (type == 3) {
            paras.put("thing1", new TemplateParam(msgList.get(0)));
            paras.put("thing2", new TemplateParam(msgList.get(1)));
//            paras.put("thing3",new TemplateParam( msgList.get(2)));
        }
        return paras;
    }


    /**
     * 图片违规检测
     *
     * @param
     * @param file
     * @return
     */
    public static Boolean imgFilter(MultipartFile file) {
        String contentType = file.getContentType();
        if (StringUtils.isEmpty(MpAccessToken)) {
            getMpAccessToken();
        }
        return checkPic(file, MpAccessToken, contentType);
    }

    /**
     * 文本违规检测
     *
     * @param
     * @param content
     * @return
     */
    public static Boolean contentFilter(String content) {
        if (StringUtils.isEmpty(MpAccessToken)) {
            getMpAccessToken();
        }
        return checkContent(MpAccessToken, content);
    }

    /**
     * 恶意图片过滤
     *
     * @param multipartFile
     * @return
     */
    private static Boolean checkPic(MultipartFile multipartFile, String accessToken, String contentType) {
        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();

            CloseableHttpResponse response = null;

            HttpPost request = new HttpPost("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken);
            request.addHeader("Content-Type", "application/octet-stream");

            InputStream inputStream = multipartFile.getInputStream();

            byte[] byt = new byte[inputStream.available()];
            inputStream.read(byt);
            request.setEntity(new ByteArrayEntity(byt, ContentType.create(contentType)));

            response = httpclient.execute(request);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");// 转成string
            JSONObject jso = JSONObject.parseObject(result);
            return getResult(jso);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("----------------调用腾讯内容过滤系统出错------------------");
            return true;
        }
    }

    private static Boolean checkContent(String accessToken, String content) {
        try {
            System.err.println(accessToken);
            System.err.println(content);
            CloseableHttpClient httpclient = HttpClients.createDefault();

            CloseableHttpResponse response = null;

            HttpPost request = new HttpPost("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken);
            request.addHeader("Content-Type", "application/json");
            Map<String, String> map = new HashMap<>();
            map.put("content", content);
            String body = JSONObject.toJSONString(map);
            request.setEntity(new StringEntity(body, ContentType.create("text/json", "UTF-8")));
            response = httpclient.execute(request);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");// 转成string
            JSONObject jso = JSONObject.parseObject(result);
            System.err.println(jso);
            return getResult(jso);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("----------------调用腾讯内容过滤系统出错------------------");
            return true;
        }
    }

    private static Boolean getResult(JSONObject jso) {
        Object errcode = jso.get("errcode");
        int errCode = (int) errcode;
        if (errCode == 0) {
            return true;
        } else if (errCode == 87014) {
            System.err.println("图片内容违规-----------");
            return false;
        }
        return true;
    }

    public static MultipartFile createFileItem(String url, String fileName) {
        FileItem item = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            //设置应用程序要从网络连接读取数据
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();

                FileItemFactory factory = new DiskFileItemFactory(16, null);
                String textFieldName = "uploadfile";
                item = factory.createItem(textFieldName, ContentType.APPLICATION_OCTET_STREAM.toString(), false, fileName);
                OutputStream os = item.getOutputStream();

                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }

        return new CommonsMultipartFile(item);
    }

    //调用该类直接跳过ssl证书
    static {
        try {
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier
                    (
                            (urlHostName, session) -> true
                    );
        } catch (Exception e) {
        }
    }

    /**
     * 跳过ssl证书
     *
     * @throws
     * @throws
     */
    private static void trustAllHttpsCertificates()
            throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = (TrustManager) new TrustAllManager();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
    }
}

