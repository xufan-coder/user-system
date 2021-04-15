package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.service.SysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName UserNameUpdateTask
 * @DateTime 2021/4/15_19:22
 * @Deacription TODO
 */
@Component
@Slf4j
public class UserNameUpdateTask {

    @Autowired
    private SysUserInfoService userService;


    /**
     *
     *  修改其他服务冗余的userName
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/15 19:24
     * @param
     * @return
     */
    @XxlJob("update_redundancy_user_name")
    public ReturnT<String> updateRedundancyUserName() {
        userService.updateRedundancyUserName();
        return ReturnT.SUCCESS;
    }

}
