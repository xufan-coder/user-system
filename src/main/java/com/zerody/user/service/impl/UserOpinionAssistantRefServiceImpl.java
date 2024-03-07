package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.domain.UserOpinion;
import com.zerody.user.domain.UserOpinionAssistantRef;
import com.zerody.user.dto.UserOpinionAssistantRefDto;
import com.zerody.user.mapper.UserOpinionAssistantRefMapper;
import com.zerody.user.service.*;
import com.zerody.user.util.NoticeImUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : xufan
 * @create 2024/2/29 9:45
 */
@Service
public class UserOpinionAssistantRefServiceImpl extends ServiceImpl<UserOpinionAssistantRefMapper, UserOpinionAssistantRef> implements UserOpinionAssistantRefService {

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private UserOpinionService userOpinionService;

    @Autowired
    private UserOpinionRefService userOpinionRefService;

    /** 1 自动分配 0 手动分配 */
    private static final int MANUAL_ASSIGN = 0;
    private static final int AUTOMATIC_ASSIGN = 1;

    @Override
    public void addManualAssistantRef(UserOpinionAssistantRefDto param) {
        param.setType(MANUAL_ASSIGN);
        this.addUserAssistantRef(param);

        // 根据意见id获取该意见已经分配出去的人 ， 相同人已经推送过一次，只推送新协助人
            List<String> result = new ArrayList<>();
            for (String opinionId: param.getOpinionIds()) {
                List<String> seeUserIds = userOpinionRefService.getSeeUserIds(opinionId);
                result = param.getAssistantUserIds().stream().filter(r -> !seeUserIds.contains(r)).collect(Collectors.toList());
                //添加可查看人关联, 只做增量
                userOpinionRefService.addOpinionRef(opinionId,result,YesNo.NO);

                // 获取每条意见的发起人信息
                UserOpinion byId = userOpinionService.getById(opinionId);
                String senderInfo = "";
                if (DataUtil.isNotEmpty(byId)){
                    senderInfo = userOpinionService.getSenderInfo(byId.getUserId());
                }
                // 获取意见指派人名称
                String appionterName = this.userOpinionRefService.getAppionterName(opinionId, YesNo.NO);

                // 推送到每个新协助人
                for (String assistantUserId: result) {
                    NoticeImUtil.pushOpinionToAssistant(opinionId,assistantUserId,senderInfo,byId.getContent(),appionterName,param.getIsCeo());
                }
            }

    }



    public List<UserOpinionAssistantRef> getAssistantUserIds(String userId,Integer type){
        QueryWrapper<UserOpinionAssistantRef> qw = new QueryWrapper<>();
        qw.lambda().eq(UserOpinionAssistantRef::getUserId,userId);
        qw.lambda().eq(UserOpinionAssistantRef::getType,type);
        return this.list(qw);
    }

    @Override
    public void addAutoAssistantRef(UserOpinionAssistantRefDto param) {
        param.setType(AUTOMATIC_ASSIGN);
        this.addUserAssistantRef(param);
    }

    @Override
    public void addUserAssistantRef(UserOpinionAssistantRefDto param) {
        QueryWrapper<UserOpinionAssistantRef> removeQw = new QueryWrapper<>();
        removeQw.lambda().eq(UserOpinionAssistantRef::getUserId, param.getUserId());
        removeQw.lambda().eq(UserOpinionAssistantRef::getType, param.getType());
        this.remove(removeQw);
        if (DataUtil.isEmpty(param.getAssistantUserIds())) {
            return;
        }
        List<UserOpinionAssistantRef> AssistantRefList = new ArrayList<>();
        for(String assistantUserId : param.getAssistantUserIds()){
            UserOpinionAssistantRef assistantRef = new UserOpinionAssistantRef();
            assistantRef.setId(UUIDutils.getUUID32());
            assistantRef.setUserId(param.getUserId());
            assistantRef.setUserName(param.getUserName());
            assistantRef.setAssistantUserId(assistantUserId);
            SysUserInfo userById = sysUserInfoService.getUserById(assistantUserId);
            if (DataUtil.isNotEmpty(userById)) {
                assistantRef.setAssistantUserName(userById.getUserName());
            }
            assistantRef.setCreateTime(new Date());
            assistantRef.setType(param.getType());
            AssistantRefList.add(assistantRef);
        }
        this.saveBatch(AssistantRefList);
    }


}
