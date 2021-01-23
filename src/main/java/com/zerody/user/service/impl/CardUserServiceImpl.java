package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.api.vo.CardUserInfoVo;
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
        BeanUtils.copyProperties(cardUser,info);
        info.setStatus(StatusEnum.激活.getValue());
        info.setId(UUIDutils.getUUID32());
        this.save(info);
        BeanUtils.copyProperties(info,cardUser);
        return cardUser;
    }

    @Override
    public CardUserInfoVo bindPhoneNumber(CardUserInfoVo data) {
        //先检查手机号是否存在，手机号存在则更新此账号的unionid和openID
        QueryWrapper<CardUserInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(CardUserInfo::getPhoneNumber,data.getPhoneNumber());
        CardUserInfo one = this.getOne(qw);
        if(DataUtil.isEmpty(one)){
            //如果手机号查不到则绑定该手机号到该用户id
            QueryWrapper<CardUserInfo> idQw =new QueryWrapper<>();
            idQw.lambda().eq(CardUserInfo::getId,data.getId());
            CardUserInfo idUser = this.getOne(qw);
            if(DataUtil.isEmpty(idUser)){
                throw new DefaultException("账户不存在！");
            }
            idUser.setUnionId(data.getUnionId());
            idUser.setPhoneNumber(data.getPhoneNumber());
            idUser.setUpdateTime(new Date());
            this.updateById(idUser);
            BeanUtils.copyProperties(data,idUser);
        }else {
            one.setUnionId(data.getUnionId());
            one.setOpenId(data.getOpenId());
            one.setUpdateTime(new Date());
            this.updateById(one);
            BeanUtils.copyProperties(data,one);
        }
        return data;
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

    @Override
    public CardUserDto getCardUserByUnionId(String unionId) {
        //免密登录时，根据unionId先查询该unionId绑定的已有手机号的用户
        QueryWrapper<CardUserInfo> userQw =new QueryWrapper<>();
        userQw.lambda().eq(CardUserInfo::getUnionId,unionId);
        CardUserInfo one = this.getOne(userQw);
        if(DataUtil.isEmpty(one)){
            //如果没有查到绑定手机号的用户，则查询该unionId注册时的账号
            userQw.clear();
            userQw.lambda().eq(CardUserInfo::getRegUnionId,unionId);
            one=this.getOne(userQw);
            if(DataUtil.isEmpty(one)){
                throw new DefaultException("该微信用户不存在名片账户！");
            }
        }
        CardUserDto cardUser=new CardUserDto();
        BeanUtils.copyProperties(one,cardUser);
        cardUser.setUserPwd(null);
        return cardUser;
    }
}
