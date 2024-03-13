package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserOpinionRef;
import com.zerody.user.domain.UserOpinionType;

import java.util.List;

/**
 * @author kuang
 */
public interface UserOpinionRefService extends IService<UserOpinionRef> {

    void addOpinionRef(String id, List<String> seeUserIds, Integer replyType);

    List<String> getSeeUserIds(String opinionId);

    List<String> getReplyUserIds(String opinionId,Integer replyType);

    String getAppionterName(String opinionId,Integer replyType);
}
