package com.zerody.user.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.service.AppUserPushService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.vo.AppUserNotPushVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author  DaBai
 * @date  2021/7/9 18:16
 */

@Component
@Slf4j
public class AppUserPushInitTask {

    @Autowired
    private AppUserPushService appUserPushService;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    /**
     *
     *  2023-04-11 已废除
     * @author PengQiang
     * @description 彭强
     * @date 2023/4/11 9:36
     * @param param
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
    @XxlJob("app_user_push_init")
    public ReturnT<String> execute(String param){
        ReturnT r=ReturnT.SUCCESS;
        try {
            // 查询出未同步的用户
            List<AppUserNotPushVo> list = this.sysUserInfoService.getNotPushAppUser();
            for (AppUserNotPushVo user : list) {
                appUserPushService.doPushAppUser(user.getUserId(), user.getCompanyId());
            }
        } catch (Exception e) {
            r.setMsg(e.getMessage());
        }
        return r;
    }
}
