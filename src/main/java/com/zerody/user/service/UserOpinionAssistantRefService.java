package com.zerody.user.service;

import com.zerody.user.domain.UserOpinion;
import com.zerody.user.dto.UserOpinionAssistantRefDto;

import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/29 9:44
 */

public interface UserOpinionAssistantRefService {

    void addCeoAssistantRef(UserOpinionAssistantRefDto param);

    List<String> getAssistantUserIds(String ceoUserId);
}
