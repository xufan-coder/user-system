package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.domain.UserOpinionAssistantRef;
import com.zerody.user.dto.UserOpinionAssistantRefDto;
import com.zerody.user.mapper.UserOpinionAssistantRefMapper;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.UserOpinionAssistantRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void addCeoAssistantRef(UserOpinionAssistantRefDto param) {
        QueryWrapper<UserOpinionAssistantRef> removeQw = new QueryWrapper<>();
        removeQw.lambda().eq(UserOpinionAssistantRef::getCeoUserId, param.getCeoUserId());
        this.remove(removeQw);
        if (DataUtil.isEmpty(param.getAssistantUserIds())) {
            return;
        }

        for (String assistantUserId: param.getAssistantUserIds()) {
            UserOpinionAssistantRef userOpinionAssistantRef = new UserOpinionAssistantRef();
            userOpinionAssistantRef.setId(UUIDutils.getUUID32());
            userOpinionAssistantRef.setCeoUserId(param.getCeoUserId());
            userOpinionAssistantRef.setCeoUserName(param.getCeoUserName());
            userOpinionAssistantRef.setAssistantUserId(assistantUserId);
            SysUserInfo userById = sysUserInfoService.getUserById(assistantUserId);
            if (DataUtil.isNotEmpty(userById)){
                userOpinionAssistantRef.setAssistantUserName(userById.getUserName());
            }
            userOpinionAssistantRef.setCreateTime(new Date());
            this.save(userOpinionAssistantRef);
        }
    }

    public List<String> getAssistantUserIds(String ceoUserId){
        QueryWrapper<UserOpinionAssistantRef> qw = new QueryWrapper<>();
        qw.lambda().eq(UserOpinionAssistantRef::getCeoUserId,ceoUserId);
        List<UserOpinionAssistantRef> list = this.list(qw);
        return list.stream().map(UserOpinionAssistantRef::getAssistantUserId).collect(Collectors.toList());
    }


}
