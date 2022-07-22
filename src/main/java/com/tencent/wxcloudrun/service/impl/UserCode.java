package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dto.WechatAppToken;
import com.tencent.wxcloudrun.util.RestTemplateUtil;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class UserCode {


    protected static final Set<WechatAppToken> WECHAT_APP_TOKEN_LIST = new HashSet<>();


    public Set<WechatAppToken> getWechatAppTokenDtoList() {
        return WECHAT_APP_TOKEN_LIST;
    }

    public void setCode(String code) {
        WechatAppToken openIdInMiniApp = getOpenIdInMiniApp(code);
        if (openIdInMiniApp.getOpenid() != null) {
            WECHAT_APP_TOKEN_LIST.add(openIdInMiniApp);
        }

    }

    /**
     * 解析code，获取openId
     *
     * @param appId     小程序的appId
     * @param appSecret 小程序的appSecret
     * @param code      前端传过来的code
     * @return WechatAppToken
     */
    public WechatAppToken getOpenIdInMiniApp(String code) {
        String appId = "wx45657c6db53f14c5";
        String appSecret = "841f5d9f0418ee4da9a007f123effaec";
        if (code.isEmpty()) {
            log.info("code不能为空");
        }
        RestTemplate restTemplate = RestTemplateUtil.getInstance();
        String requestUrl = UriComponentsBuilder.fromHttpUrl(
                "https://api.weixin.qq.com/sns/jscode2session")
            .queryParam("appid", appId)
            .queryParam("secret", appSecret)
            .queryParam("js_code", code)
            .queryParam("grant_type", "authorization_code")
            .build().encode().toUriString();
        try {
            WechatAppToken result = restTemplate.getForObject(requestUrl, WechatAppToken.class);
            log.info("wxchat Result:{}", result);
            if (result == null || result.errcode != null) {
                log.info("getUnionIdInMiniApp error");
                // 抛个异常
            }
            return result;
        } catch (RestClientResponseException ex) {
            log.info("getUnionIdInMiniApp error");
        }
        return null;
    }

}
