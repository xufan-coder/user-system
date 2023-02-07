package com.zerody.user.handler.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.domain.ResignationApplication;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.service.AppUserPushService;
import com.zerody.user.service.ResignationApplicationService;
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

    @Autowired
    private ResignationApplicationService resignationApplicationService;


    private static ResignationApplicationService resignationApplicationStaticService;

    @PostConstruct
    private void ini() {
        appUserPushStaticService = this.appUserPushService;
        resignationApplicationStaticService = this.resignationApplicationService;
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


    /**
     *
     * 判断是否可设置为离职
     * @author               PengQiang
     * @description          DELL
     * @date                 2023/2/7 11:08
     * @param                user 当前登录用户信息
     * @param                userId 用户id
     * @param                oldStatus 旧数据
     * @param                newStatus 修改数据
     * @return               void
     */
    public static void isDimission(UserVo user, String userId, int oldStatus, int newStatus) {
        //token为空 或者 后台操作就直接通过
        if (DataUtil.isEmpty(user) || user.isBack()) {
            return;
        }
        if (DataUtil.isEmpty(newStatus) || newStatus == oldStatus) {
            return;
        }
        QueryWrapper<ResignationApplication> resignationQw = new QueryWrapper<>();
        resignationQw.lambda().eq(ResignationApplication::getUserId, userId);
        resignationQw.lambda().eq(ResignationApplication::getApprovalState, ApproveStatusEnum.APPROVAL.name());
        int num = resignationApplicationStaticService.count(resignationQw);
        //当新的状态为离职 并且 该伙伴提交了离职申请未审批的
        if (newStatus == StatusEnum.stop.getValue() && num > 0) {
            throw new DefaultException("离职不可操作！当前伙伴已发起离职申请，请联系团队长审批。");
        }
    }
}
