package com.zerody.user.handler.user;

import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.service.AppUserPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SysUserDimissionHandle
 * @DateTime 2023/2/1_17:10
 * @Deacription
 */
@Slf4j
@Component
public class SysUserDimissionHandle {



    @Autowired
    private AppUserPushService appUserPushService;

    private static AppUserPushService appUserPushStaticService;

    @PostConstruct
    private void ini() {
        appUserPushStaticService = this.appUserPushService;
    }



    /**
     *
     * 离职推送唐叁藏处理(本方法没有事务不修改只做参数处理)
     * @author               PengQiang
     * @description          DELL
     * @date                 2023/2/1 17:15
     * @param                userId 用户id
     * @return               com.zerody.user.domain.AppUserPush
     */
    public static AppUserPush staffDimissionPush(String userId) {
        AppUserPush appUserPush = appUserPushStaticService.getByUserId(userId);
        //没值给无用id
        if (DataUtil.isEmpty(appUserPush)) {
            appUserPush = new AppUserPush();
            appUserPush.setId("NOT_USER");
        }
        appUserPush.setResigned(YesNo.YES);
        appUserPush.setUpdateTime(new Date());
        return appUserPush;
    }
}
