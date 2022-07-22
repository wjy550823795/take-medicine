package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class WechatAppTokenDto {

    public Integer errcode;

    public String errmsg;

    public String openid;

    public String sessionKey;

    public String unionId;
}


