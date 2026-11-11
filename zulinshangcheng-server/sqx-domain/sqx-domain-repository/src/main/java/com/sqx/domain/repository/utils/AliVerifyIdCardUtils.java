package com.sqx.domain.repository.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Slf4j
public class AliVerifyIdCardUtils {

    public static String verifyIdCard(String idcard, String mobile, String name) throws IOException {
        String url = "https://mobile3elements.shumaidata.com/mobile/verify_real_name";
        String appCode = "9c7bb96d80c14b2382704040e82dd7ff";
        Map<String, String> params = new HashMap<>();
        params.put("idcard", idcard);
        params.put("mobile", mobile);
        params.put("name", name);
        return postForm(appCode, url, params);
    }

    /**
     * 用到的HTTP工具包：okhttp 3.13.1
     * <dependency>
     * <groupId>com.squareup.okhttp3</groupId>
     * <artifactId>okhttp</artifactId>
     * <version>3.13.1</version>
     * </dependency>
     */
    public static String postForm(String appCode, String url, Map<String, String> params) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        FormBody.Builder formbuilder = new FormBody.Builder();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            formbuilder.add(key, params.get(key));
        }
        FormBody body = formbuilder.build();
        Request request = new Request.Builder().url(url).addHeader("Authorization", "APPCODE " + appCode).post(body).build();
        Response response = client.newCall(request).execute();
        if (response.code() == 0) {
            return "ok";
        }else {
            return response.message();
        }
    }


}
