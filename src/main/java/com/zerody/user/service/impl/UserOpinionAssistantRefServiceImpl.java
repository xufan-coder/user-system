package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.SysStaffInfo;
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
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private UserOpinionService userOpinionService;

    @Autowired
    private UserOpinionRefService userOpinionRefService;

    /** 1 自动分配 0 手动分配 */
    private static final int MANUAL_ASSIGN = 0;
    private static final int AUTOMATIC_ASSIGN = 1;

    @Override
    public void addManualAssistantRef(UserOpinionAssistantRefDto param) {

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



    public List<StaffInfoVo>  getAssistantUserInfo(String userId){
        List<String> assistantUserIds = getAssistantUserIds(userId);
        return sysStaffInfoService.getStaffInfoByIds(assistantUserIds);
    }

    @Override
    public List<String> getAssistantUserIds(String userId) {
        QueryWrapper<UserOpinionAssistantRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserOpinionAssistantRef::getUserId,userId);
        return  this.list(queryWrapper).stream().map(UserOpinionAssistantRef::getAssistantUserId).collect(Collectors.toList());
    }

    @Override
    public void addAutoAssistantRef(UserOpinionAssistantRefDto param) {
        this.addUserAssistantRef(param);
    }

    @Override
    public void addUserAssistantRef(UserOpinionAssistantRefDto param) {

        // 获取当前数据库已有的协助人
        List<String> assistantUserIds = getAssistantUserIds(param.getUserId());

        // 去重 ，只做增量添加
        List<String> result = param.getAssistantUserIds().stream().filter(r -> !assistantUserIds.contains(r)).collect(Collectors.toList());
        List<UserOpinionAssistantRef> AssistantRefList = new ArrayList<>();
        for(String assistantUserId : result){
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
            AssistantRefList.add(assistantRef);
        }
        this.saveBatch(AssistantRefList);
    }


    @Override
    public void removeByUserId(String userId,String assistantUserId) {
        QueryWrapper<UserOpinionAssistantRef> removeWrapper = new QueryWrapper<>();
        removeWrapper.lambda().eq(UserOpinionAssistantRef::getUserId,userId);
        removeWrapper.lambda().eq(UserOpinionAssistantRef::getAssistantUserId,assistantUserId);
        this.remove(removeWrapper);
    }

}
