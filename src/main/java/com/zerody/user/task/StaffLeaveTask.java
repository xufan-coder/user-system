package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.feign.PartnerFeignService;
import com.zerody.user.service.AppUserPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 员工离职同步app
 * @author  DaBai
 * @date  2021/7/19 10:35
 */


@Component
@Slf4j
public class StaffLeaveTask {

    @Autowired
    private AppUserPushService appUserPushService;
    @Autowired
    private PartnerFeignService partnerFeignService;

    @XxlJob("user_leave_push")
    public ReturnT<String> execute(String param){
        ReturnT r=ReturnT.SUCCESS;
        try {
            // 获取所有离职用户用户ID
            List<AppUserPush> list = appUserPushService.getLeaveUsers();
            for (AppUserPush user : list) {
                partnerFeignService.partLogOff(user.getUserId());
            }
        } catch (Exception e) {
            r.setMsg(e.getMessage());
        }
        return r;
    }
}
