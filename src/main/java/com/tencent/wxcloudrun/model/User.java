package com.tencent.wxcloudrun.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class User implements Serializable {

    private Integer id;

    private String name;

    private String openid;

    private String unionid;

}
