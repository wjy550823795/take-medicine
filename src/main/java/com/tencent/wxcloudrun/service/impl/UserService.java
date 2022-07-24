package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSON;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.dto.WechatAppToken;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserHelperService;
import com.tencent.wxcloudrun.util.RestTemplateUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserHelperService userHelperService;

    public List<User> getUser() {
        return userHelperService.list();
    }

    public void setCode(String code) {
        log.info("setCode:{}", code);
        WechatAppToken openIdInMiniApp = getOpenIdInMiniApp(code);
        if (openIdInMiniApp != null && openIdInMiniApp.getOpenid() != null) {
            User user = new User();
            user.setOpenid(openIdInMiniApp.getOpenid());
            user.setUnionid(openIdInMiniApp.getUnionId());
            userHelperService.saveOrUpdate(user);
        }

    }

    /**
     * 解析code，获取openId
     *
     * @param code      前端传过来的code
     * @return WechatAppToken
     */
    public WechatAppToken getOpenIdInMiniApp(String code) {
        //小程序的appId
        String appId = "wx45657c6db53f14c5";
        //小程序的appSecret
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
            log.info("wechat Result:{}", result);
            if (result == null || result.errcode != null) {
                log.info("getUnionIdInMiniApp result error:{}", JSON.toJSONString(result));
                // 抛个异常
            }
            return result;
        } catch (Exception ex) {
            log.info("getUnionIdInMiniApp error:", ex);
        }
        return null;
    }

}
