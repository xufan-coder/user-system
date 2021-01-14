package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.domain.CardUserInfo;

public interface CardUserService extends IService<CardUserInfo> {

    CardUserDto addCardUser(CardUserDto cardUser);

    void bindPhoneNumber(CardUserInfo data);
}
