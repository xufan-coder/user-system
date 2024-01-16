package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.enums.StatusEnum;
import com.zerody.user.domain.ResignationApplication;
import com.zerody.user.feign.AdviserFeignService;
import com.zerody.user.service.ImportInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author : xufan
 * @create 2024/1/16 15:44
 */
@Slf4j
@Component
@RefreshScope
public class StaffAdviserStateTask {

    @Autowired
    private AdviserFeignService adviserFeignService;

    @XxlJob("staff_adviser_state_init_task")
    public ReturnT<String> execute(String param){
        // 初始化旧数据 顾问状态

        ReturnT r=ReturnT.SUCCESS;
        try {
            // 获取crm伙伴顾问

        } catch (Exception e) {
            log.error("初始化顾问状态出错:{}", e, e);
            r.setMsg(e.getMessage());
        }
        return r;
    }
}
