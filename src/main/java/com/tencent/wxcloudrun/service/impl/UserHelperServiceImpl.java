package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserHelperService;
import org.springframework.stereotype.Service;

@Service
public class UserHelperServiceImpl extends
                                   ServiceImpl<UserMapper, User> implements UserHelperService {

}
