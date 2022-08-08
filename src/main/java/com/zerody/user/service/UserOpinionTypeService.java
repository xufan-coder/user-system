package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserOpinionType;

import java.util.List;

/**
 * @author kuang
 */
public interface UserOpinionTypeService extends IService<UserOpinionType> {
    
    void addOpinionType(UserOpinionType opinionType);

    void updateOpinionType(UserOpinionType opinionType);

    List<UserOpinionType> getTypeAll();
}
