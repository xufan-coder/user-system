package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserOpinionRef;
import com.zerody.user.domain.UserOpinionType;

import java.util.List;

/**
 * @author kuang
 */
public interface UserOpinionRefService extends IService<UserOpinionRef> {

    void addOpinionRef(String id, List<String> seeUserIds);
}
