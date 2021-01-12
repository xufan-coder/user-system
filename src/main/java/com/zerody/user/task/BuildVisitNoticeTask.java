package com.zerody.user.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @XxlJob("follow_up_message")
    public void buildVisitNoticeInfo() {
        log.info("进入客户跟进消息提醒任务;");
        taskService.buildVisitNoticeInfo();
    }
}
