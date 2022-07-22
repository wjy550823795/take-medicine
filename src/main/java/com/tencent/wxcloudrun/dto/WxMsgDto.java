package com.tencent.wxcloudrun.dto;

import java.util.Map;
import lombok.Data;

@Data
public class WxMsgDto {

        /**
         * 接收者（用户）的 openid
         */
        private String touser;

        /**
         * 所需下发的订阅模板id
         */
        private String template_id;

        /**
         * 点击模板卡片后的跳转页面，仅限本小程序内的页面
         */
        private String page = "pages/index/index";
        /**
         * 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
         */
        private Map<String, WxTemplateDataDto> data;
}


