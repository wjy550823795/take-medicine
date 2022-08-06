package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.service.impl.SendService;
import com.tencent.wxcloudrun.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SendController {

    @Autowired
    UserService userService;

    @Autowired
    SendService sendService;


    @PostMapping("/send")
    public void send() {
        sendService.sendTemplate();
    }

    @PostMapping("/sendFangTang")
    public void sendFangTang() {
        sendService.sendFangTang();
    }

    @GetMapping("/setCode")
    public void send(String code) {
        userService.setCode(code);
    }

}
