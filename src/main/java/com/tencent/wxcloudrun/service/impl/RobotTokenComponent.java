package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.util.RestTemplateUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RobotTokenComponent {

    /**
     * 缓存的access_token
     */
    private static String accessToken;
    /**
     * access_token的失效时间
     */
    private static long expiresTime;


    public String getAccessToken() {
        long nowMillions = System.currentTimeMillis();
        boolean flag = expiresTime < nowMillions;
        log.info("expiresTime:{},nowMillions:{},flag:{}", expiresTime, nowMillions, flag);
        // 判断accessToken是否已经过期，如果过期需要重新获取
        if (accessToken == null || flag) {
            forceRefreshToken(nowMillions);
        }
        return accessToken;
    }

    public String forceRefreshToken(long nowMillions) {
        RestTemplate restTemplate = RestTemplateUtil.getInstance();
        Map<String, String> params = new HashMap<>(2);
        String appId = "wx45657c6db53f14c5";
        String appSecret = "841f5d9f0418ee4da9a007f123effaec";
        params.put("APPID", appId);
        params.put("APPSECRET", appSecret);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}",
            String.class, params);
        String body = responseEntity.getBody();
        JSONObject object = JSON.parseObject(body);
        log.info("getAccessToken object:{}", object.toJSONString());
        Integer errcode = object.getInteger("errcode");
        if (errcode != null && errcode != 0) {
            String errmsg = object.getString("errmsg");
            log.info("请求accessToken失败，返回码：" + errcode + "，错误信息：" + errmsg);
            // 抛个异常
        }
        // 缓存accessToken
        accessToken = object.getString("access_token");
        // 设置accessToken的失效时间
        long expiresIn = object.getLong("expires_in");
        // 失效时间 = 当前时间 + 有效期(提前一分钟，也可不提前，这里只是稳妥一下)
        expiresTime = nowMillions + (expiresIn - 60) * 1000;
        return accessToken;
    }
}