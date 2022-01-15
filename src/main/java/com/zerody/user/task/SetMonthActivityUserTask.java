package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.MonthActivityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 *  设置月初在职人数
 * @author               PengQiang
 * @description          DELL
 * @date                 2022/1/15 14:41
 */
@Component
@Slf4j
public class SetMonthActivityUserTask {

    @Autowired
    private MonthActivityUserService service;

    @XxlJob("add_month_activity_user")
    public ReturnT<String> execute(String param){
        this.service.doAddMonthActivityUser();
        return ReturnT.SUCCESS;
    }
}
