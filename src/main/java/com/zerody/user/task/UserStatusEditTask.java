package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.service.SysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户操作操作
 * @author PengQiang
 * @ClassName CompanyEditTask
 * @DateTime 2021/5/21_19:09
 * @Deacription TODO
 */
@Component
@Slf4j
public class UserStatusEditTask {
    @Autowired
    private SysUserInfoService userInfoService;

    @XxlJob("user_status_edit")
    public ReturnT<String> execute(String param){
        //获取修改用户状态的用户
        this.userInfoService.doUserStatusEditInfo();

        return ReturnT.SUCCESS;
    }
}
