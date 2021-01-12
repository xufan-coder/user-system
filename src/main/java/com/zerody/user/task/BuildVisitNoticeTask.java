package com.zerody.user.task;

import com.zerody.user.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 消息跟进提醒生成
 * @author  DaBai
 * @date  2021/1/11 17:38
 */
@Component
@Slf4j
public class BuildVisitNoticeTask {

    @Autowired
    private TaskService taskService;



    @Scheduled(cron = "0 0 1 * * ?")
    public void buildVisitNoticeInfo() {
        log.info("进入客户跟进消息提醒任务;");
        taskService.buildVisitNoticeInfo();
    }
}
