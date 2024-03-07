package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserOpinionAssistantRef;
import com.zerody.user.dto.UserOpinionAssistantRefDto;

import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/29 9:44
 */

public interface UserOpinionAssistantRefService extends IService<UserOpinionAssistantRef> {

    void addManualAssistantRef(UserOpinionAssistantRefDto param);

    List<UserOpinionAssistantRef> getAssistantRef(String userId,Integer type);

    List<String> getAssistantUserIds(String userId,Integer type);

    void addAutoAssistantRef(UserOpinionAssistantRefDto param);

    void addUserAssistantRef(UserOpinionAssistantRefDto param);
}
