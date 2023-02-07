package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.enums.StatusEnum;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.domain.ResignationApplication;
import com.zerody.user.feign.PartnerFeignService;
import com.zerody.user.service.AppUserPushService;
import com.zerody.user.service.ResignationApplicationService;
import com.zerody.user.service.SysStaffInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 员工离职审核通过的自动离职
 * @author  DaBai
 * @date  2021/7/19 10:35
 */


@Component
@Slf4j
public class StaffLeaveApproveTask {

    @Autowired
    private ResignationApplicationService resignationApplicationService;
    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @XxlJob("user_approve_leave")
    public ReturnT<String> execute(String param){
        ReturnT r=ReturnT.SUCCESS;
        try {
            // 获取今天离职用户
            List<ResignationApplication> list = resignationApplicationService.getLeaveUsers();
            for (ResignationApplication user : list) {
                sysStaffInfoService.updateStaffStatus(user.getUserId(), StatusEnum.stop.getValue(), user.getReason(), null);
            }
        } catch (Exception e) {
            log.error("离职出错:{}", e, e);
            r.setMsg(e.getMessage());
        }
        return r;
    }
}
