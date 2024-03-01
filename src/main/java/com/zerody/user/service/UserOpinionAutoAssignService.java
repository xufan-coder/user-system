package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserOpinionAssistantRef;
import com.zerody.user.domain.UserOpinionAutoAssign;
import com.zerody.user.dto.UserOpinionAutoAssignDto;

import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/29 16:14
 */
public interface UserOpinionAutoAssignService extends IService<UserOpinionAutoAssign>  {

    Boolean isAutoAssign(String userId);

    void addUserAutoAssign(UserOpinionAutoAssignDto param);
}
