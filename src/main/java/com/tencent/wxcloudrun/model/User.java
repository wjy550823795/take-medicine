package com.tencent.wxcloudrun.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@Data
@TableName("User")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String openid;

    private String unionid;

}
