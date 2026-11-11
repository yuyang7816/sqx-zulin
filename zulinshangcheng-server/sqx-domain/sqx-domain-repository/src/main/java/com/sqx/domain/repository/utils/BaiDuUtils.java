package com.sqx.domain.repository.utils;

import com.alibaba.fastjson.JSONObject;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BaiDuUtils {

    private static ICommonInfoService commonInfoService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setBaiDuUtils(ICommonInfoService commonInfoService) {
        BaiDuUtils.commonInfoService = commonInfoService;
    }


    public static boolean auditBaiDuText(String content) {
        boolean result = false;
        String apiKey = commonInfoService.findOne(420).getValue();
        String secretKey = commonInfoService.findOne(507).getValue();
        String accessToken = getAccessToken(apiKey, secretKey);

        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "text=" + content);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = null;
        try {
            response = HTTP_CLIENT.newCall(request).execute();
            String string = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(string);
            String conclusionType = jsonObject.getString("conclusionType");
            if (!"1".equals(conclusionType)) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("百度审核内容失败：" + e.getMessage(), e);
        }
        return result;
    }

    public static boolean auditBaiDuImg(String content) {

        String apiKey = commonInfoService.findOne(420).getValue();
        String secretKey = commonInfoService.findOne(507).getValue();
        String accessToken = getAccessToken(apiKey, secretKey);
        String[] split = content.split(",");
        for (String s : split) {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "imgUrl=" + s);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
            Response response = null;
            try {
                response = HTTP_CLIENT.newCall(request).execute();
                String string = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(string);
                String conclusionType = jsonObject.getString("conclusionType");
                if (!"1".equals(conclusionType)) {
                   return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return false;
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    public static String getAccessToken(String apiKey, String secretKey) {
        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + apiKey
                + "&client_secret=" + secretKey);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = null;
        try {
            response = HTTP_CLIENT.newCall(request).execute();
            return new org.json.JSONObject(response.body().string()).getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取百度token失败：" + e.getMessage(), e);
        }
        return null;
    }

}
