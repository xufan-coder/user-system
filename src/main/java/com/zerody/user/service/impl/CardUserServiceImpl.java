package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.domain.CardUserInfo;
import com.zerody.user.domain.CardUserUnionUser;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.CardUserMapper;
import com.zerody.user.mapper.CardUserUnionCrmUserMapper;
import com.zerody.user.service.CardUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/1/14 17:56
 */

@Slf4j
@Service
public class CardUserServiceImpl extends ServiceImpl<CardUserMapper, CardUserInfo> implements CardUserService {

    @Autowired
    private CardUserUnionCrmUserMapper cardUserUnionCrmUserMapper;

    @Override
    public CardUserDto addCardUser(CardUserDto cardUser) {
        CardUserInfo info =new CardUserInfo();
        info.setId(UUIDutils.getUUID32());
        BeanUtils.copyProperties(cardUser,info);
        this.save(info);
        BeanUtils.copyProperties(info,cardUser);
        return cardUser;
    }

    @Override
    public void bindPhoneNumber(CardUserInfo data) {
        QueryWrapper<CardUserInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(CardUserInfo::getId,data.getId());
        CardUserInfo one = this.getOne(qw);
        if(DataUtil.isEmpty(one)){
            throw new DefaultException("账号不存在！");
        }
        one.setPhoneNumber(data.getPhoneNumber());
        one.setUpdateTime(new Date());
        this.updateById(one);
    }

    @Override
    public CardUserDto checkCardUser(String userId) {
        QueryWrapper<CardUserUnionUser> qw=new QueryWrapper<>();
        qw.lambda().eq(CardUserUnionUser::getUserId,userId);
        CardUserUnionUser cardUserUnionUser = cardUserUnionCrmUserMapper.selectOne(qw);
        if(DataUtil.isEmpty(cardUserUnionUser)){
            throw new DefaultException("该员工未关联名片夹！");
        }
        QueryWrapper<CardUserInfo> userQw =new QueryWrapper<>();
        userQw.lambda().eq(CardUserInfo::getId,cardUserUnionUser.getCardId());
        CardUserInfo one = this.getOne(userQw);
        if(DataUtil.isEmpty(cardUserUnionUser)){
            throw new DefaultException("该名片用户不存在！");
        }
        CardUserDto cardUser=new CardUserDto();
        BeanUtils.copyProperties(one,cardUser);
        cardUser.setUserPwd(null);
        return cardUser;
    }
}
