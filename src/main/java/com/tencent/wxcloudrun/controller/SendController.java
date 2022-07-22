package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.WechatAppTokenDto;
import com.tencent.wxcloudrun.dto.WxMsgDto;
import com.tencent.wxcloudrun.dto.WxTemplateDataDto;
import com.tencent.wxcloudrun.service.impl.RobotToken;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
public class SendController {

    @PostMapping("send")
    public void send(String code) {
        WechatAppTokenDto openIdInMiniApp = getOpenIdInMiniApp(code);
        sendVlogCompleteTemplateMsg(openIdInMiniApp.getOpenid());
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


    /**
     * 发送视频完成模板消息
     *
     * @param openId 用户的openId
     * @return 结果。发送成功，返回值实例：{"errcode":0,"errmsg":"ok","msgid":11111}
     */
    public String sendVlogCompleteTemplateMsg(String openId) {
        RobotToken robotToken = new RobotToken();
        RestTemplate restTemplate = new RestTemplate();
        //发送订阅消息的url，官网地址：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html
        String url =
            "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + robotToken.getAccessToken();
        //拼接推送的模版
        WxMsgDto wxMsgDto = new WxMsgDto();
        //用户的openid（要发送给的那个用户）
        wxMsgDto.setTouser(openId);
        //订阅消息模板id
        wxMsgDto.setTemplate_id("LtP5EeXT9QYlnfcgAgFI9b2r-d8LNmlt7P-Tt-nz9sk");
        //点击消息跳转的页面
        wxMsgDto.setPage("pages/index/index");

        Map<String, WxTemplateDataDto> map = new HashMap<>(5);
        //根据从小程序中的模板获取的参数，进行赋值
        map.put("thing1", new WxTemplateDataDto("药品"));
        map.put("time2", new WxTemplateDataDto("时间"));
        map.put("phrase3", new WxTemplateDataDto("状态"));
        map.put("thing5", new WxTemplateDataDto("患者姓名"));
        wxMsgDto.setData(map);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMsgDto, String.class);
        return responseEntity.getBody();
    }

}
