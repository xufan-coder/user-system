package com.zerody.user.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.feign.AdviserFeignService;
import com.zerody.user.service.SysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kuang
 * 同步crm伙伴绑定顾问之间关联
 */

@Component
@Slf4j
public class UserAdviserImTask {

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private AdviserFeignService adviserFeignService;


    @XxlJob("user_leave_push")
    public ReturnT<String> execute(String param){
        ReturnT r=ReturnT.SUCCESS;
        try {
            QueryWrapper<SysUserInfo> qw = new QueryWrapper<>();
            qw.lambda().eq(SysUserInfo::getAdviserPush, YesNo.NO).last("limit 20");
            List<SysUserInfo> pushList = sysUserInfoService.list(qw);

            List<String> mobileList = pushList.stream().map(SysUserInfo::getPhoneNumber).collect(Collectors.toList());
            List<String> ids =  pushList.stream().map(SysUserInfo::getId).collect(Collectors.toList());
            // 同步crm伙伴绑定顾问之间关联
            if(pushList.size() > 0) {
                DataResult<Object> result = adviserFeignService.modifyAdviserPush(mobileList);
                if(result.isSuccess()) {
                    UpdateWrapper<SysUserInfo> uw = new UpdateWrapper<>();
                    uw.lambda().set(SysUserInfo::getAdviserPush,YesNo.YES);
                    uw.lambda().in(SysUserInfo::getId,ids);
                    sysUserInfoService.update(uw);
                }else {
                    log.error("crm账户新增 --同步顾问账户出错：{}",result.getMessage());
                }
            }


        } catch (Exception e) {
            r.setMsg(e.getMessage());
        }
        return r;
    }

}
