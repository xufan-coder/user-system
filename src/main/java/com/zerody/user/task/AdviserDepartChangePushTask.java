package com.zerody.user.task;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zerody.adviser.api.dto.AdviserDepartChangePushDto;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.AdviserDepartChangePush;
import com.zerody.user.domain.AppUserPush;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.feign.AdviserFeignService;
import com.zerody.user.service.AdviserDepartChangePushService;
import com.zerody.user.service.AppUserPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 伙伴部门更新部门推送顾问
 * @author  DaBai
 * @date  2024/1/18 10:55
 */


@Component
@Slf4j
public class AdviserDepartChangePushTask {

    @Autowired
    private AdviserDepartChangePushService adviserDepartChangePushService;

    @Autowired
    private AdviserFeignService adviserFeignService;

    @XxlJob("adviser_depart_change_push")
    public ReturnT<String> execute(String param){
        ReturnT r=ReturnT.SUCCESS;
        try {
            // 获取所有用户ID
            List<AdviserDepartChangePush> list = adviserDepartChangePushService.selectAll();
            for (AdviserDepartChangePush push : list) {
                AdviserDepartChangePushDto dto =new AdviserDepartChangePushDto();
                dto.setCreateTime(push.getCreateTime());
                dto.setDeptId(push.getDeptId());
                dto.setUserId(push.getUserId());
                DataResult<Object> result=adviserFeignService.doAdviserDepartChangePush(dto);
                if (result.isSuccess()){
                    // 变更状态
                    UpdateWrapper<AdviserDepartChangePush> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.lambda().eq(AdviserDepartChangePush::getId,push.getId());
                    updateWrapper.lambda().set(AdviserDepartChangePush::getState, YesNo.YES);
                    updateWrapper.lambda().set(AdviserDepartChangePush::getUpdateTime,new Date());
                    updateWrapper.lambda().set(AdviserDepartChangePush::getSendTime,new Date());
                    this.adviserDepartChangePushService.update(updateWrapper);
                }else {
                    //重试
                    UpdateWrapper<AdviserDepartChangePush> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.lambda().eq(AdviserDepartChangePush::getId,push.getId());
                    updateWrapper.lambda().set(AdviserDepartChangePush::getResend, push.getResend()+1);
                    updateWrapper.lambda().set(AdviserDepartChangePush::getUpdateTime,new Date());
                    this.adviserDepartChangePushService.update(updateWrapper);
                }
            }
        } catch (Exception e) {
            r.setMsg(e.getMessage());
        }
        return r;
    }
}
