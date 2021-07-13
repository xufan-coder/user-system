package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.service.AppUserPushService;
import com.zerody.user.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author  DaBai
 * @date  2021/7/9 18:16
 */

@Component
@Slf4j
public class AppUserPushTask {

    @Autowired
    private AppUserPushService appUserPushService;

    @XxlJob("app_user_push")
    public ReturnT<String> execute(String param){
        ReturnT r=ReturnT.SUCCESS;
        try {
            // 获取所有用户ID
            List<AppUserPush> list = appUserPushService.selectAll();
            // 统计客户跟进提醒三种类型的，客户数
            for (AppUserPush user : list) {
                appUserPushService.sendAppUserInfo(user);
            }
        } catch (Exception e) {
            r.setMsg(e.getMessage());
        }
        return r;
    }
}
