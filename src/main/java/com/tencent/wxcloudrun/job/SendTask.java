package com.tencent.wxcloudrun.job;


import com.tencent.wxcloudrun.service.impl.SendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务，主类加上@Component和@EnableScheduling注解，在方法上加上@Scheduled注解
 * 注意的是，可以加个开关来决定任务是否执行
 */
@Slf4j
@Component
@EnableScheduling
public class SendTask {

    @Autowired
    SendService sendService;

    @Scheduled(cron = "0 30 9,12,19 * * ? ")
    public void haha() {
        log.info("job start ==================");
        sendService.sendTemplate();
        log.info("job end ==================");
    }
}
