package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.domain.CardUserInfo;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.CardUserMapper;
import com.zerody.user.service.CardUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author  DaBai
 * @date  2021/1/14 17:56
 */

@Slf4j
@Service
public class CardUserServiceImpl extends ServiceImpl<CardUserMapper, CardUserInfo> implements CardUserService {

    @Override
    public CardUserDto addCardUser(CardUserDto cardUser) {
        CardUserInfo info =new CardUserInfo();
        info.setId(UUIDutils.getUUID32());
        BeanUtils.copyProperties(cardUser,info);
        this.save(info);
        BeanUtils.copyProperties(info,cardUser);
        return cardUser;
    }
}
