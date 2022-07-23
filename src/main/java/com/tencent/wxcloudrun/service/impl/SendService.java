package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSON;
import com.tencent.wxcloudrun.dto.WechatAppToken;
import com.tencent.wxcloudrun.dto.WxMsgDto;
import com.tencent.wxcloudrun.dto.WxTemplateDataDto;
import com.tencent.wxcloudrun.util.RestTemplateUtil;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SendService {

    public static final DateTimeFormatter YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");

    @Autowired
    RobotTokenComponent robotTokenComponent;

    @Autowired
    UserCodeService userCodeService;

    public void sendTemplate() {
        Set<WechatAppToken> set = userCodeService.getWechatAppTokenDtoList();
        log.info("send:{}", JSON.toJSONString(set));
        set.forEach(wechatAppToken -> sendVlogCompleteTemplateMsg(wechatAppToken.getOpenid()));
    }

    /**
     * 发送视频完成模板消息
     *
     * @param openId 用户的openId
     * @return 结果。发送成功，返回值实例：{"errcode":0,"errmsg":"ok","msgid":11111}
     */
    public String sendVlogCompleteTemplateMsg(String openId) {
        RestTemplate restTemplate = RestTemplateUtil.getInstance();
        //发送订阅消息的url，官网地址：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html
        String url =
            "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="
                + robotTokenComponent.getAccessToken();
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
        map.put("thing1", new WxTemplateDataDto("吃药啦"));
        ZonedDateTime zonedtime = LocalDateTime.now().atZone(ZoneId.from(ZoneOffset.UTC));
        ZonedDateTime converted = zonedtime.withZoneSameInstant(ZoneOffset.ofHours(8));
        map.put("time2", new WxTemplateDataDto(converted.toLocalDateTime().format(YYYY_MM_DD_HH_MM)));
        map.put("phrase3", new WxTemplateDataDto("未吃药"));
        map.put("thing5", new WxTemplateDataDto("王馋馋"));
        wxMsgDto.setData(map);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMsgDto, String.class);
        String body = responseEntity.getBody();
        log.info("sendVlogCompleteTemplateMsg:{}", body);
        return body;
    }

}
