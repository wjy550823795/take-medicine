package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dto.WechatAppTokenDto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class UserCode {


    public static final Set<WechatAppTokenDto> wechatAppTokenDtoList = new HashSet<>();


    public Set<WechatAppTokenDto> getWechatAppTokenDtoList(){
        return wechatAppTokenDtoList;
    }

    public void setCode(String code) {
        WechatAppTokenDto openIdInMiniApp = getOpenIdInMiniApp(code);
        wechatAppTokenDtoList.add(openIdInMiniApp);
    }

    /**
     * 解析code，获取openId
     *
     * @param appId     小程序的appId
     * @param appSecret 小程序的appSecret
     * @param code      前端传过来的code
     * @return WechatAppToken
     */
    public WechatAppTokenDto getOpenIdInMiniApp(String code) {
        String appId = "wx45657c6db53f14c5";
        String appSecret = "841f5d9f0418ee4da9a007f123effaec";
        if (code.isEmpty()) {
            log.info("code不能为空");
        }
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = UriComponentsBuilder.fromHttpUrl(
                "https://api.weixin.qq.com/sns/jscode2session")
            .queryParam("appid", appId)
            .queryParam("secret", appSecret)
            .queryParam("js_code", code)
            .queryParam("grant_type", "authorization_code")
            .build().encode().toUriString();
        try {
            WechatAppTokenDto result = restTemplate.getForObject(requestUrl, WechatAppTokenDto.class);
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
