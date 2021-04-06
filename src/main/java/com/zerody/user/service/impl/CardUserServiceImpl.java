package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.api.vo.CardUserInfoVo;
import com.zerody.user.domain.CardUserInfo;
import com.zerody.user.domain.CardUserUnionUser;
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
        info.setStatus(StatusEnum.activity.getValue());
        info.setId(UUIDutils.getUUID32());
        info.setCreateTime(new Date());
        //校验该openId是否已经存在
        QueryWrapper<CardUserInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(CardUserInfo::getRegOpenId,info.getRegOpenId());
        CardUserInfo one = this.getOne(qw);
        if(DataUtil.isNotEmpty(one)){
            BeanUtils.copyProperties(one,cardUser);
        }else {
            this.save(info);
            BeanUtils.copyProperties(info,cardUser);
        }
        return cardUser;
    }

    @Override
    public CardUserInfoVo bindPhoneNumber(CardUserInfoVo data) {
        //先检查手机号是否存在，手机号存在则更新此账号的unionid和openID
        QueryWrapper<CardUserInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(CardUserInfo::getPhoneNumber,data.getPhoneNumber());
        CardUserInfo one = this.getOne(qw);
        CardUserInfoVo vo=new CardUserInfoVo();
        CardUserInfo newUser=null;

        //获取原OPENID
        QueryWrapper<CardUserInfo> idQw =new QueryWrapper<>();
        idQw.lambda().eq(CardUserInfo::getId,data.getId());
        CardUserInfo idUser = this.getOne(idQw);
        if(DataUtil.isEmpty(idUser)){
            throw new DefaultException("账户不存在！");
        }
        if(DataUtil.isEmpty(one)){
            newUser=new CardUserInfo();
            newUser.setId(UUIDutils.getUUID32());
            newUser.setOpenId(idUser.getRegOpenId());
            newUser.setPhoneNumber(data.getPhoneNumber());
            newUser.setCreateTime(new Date());
            this.save(newUser);
            BeanUtils.copyProperties(newUser,vo);
        }else {
            one.setOpenId(idUser.getRegOpenId());
            one.setUpdateTime(new Date());
            this.updateById(one);
            BeanUtils.copyProperties(one,vo);
        }
        //同时删除原先绑定的openId用户
        QueryWrapper<CardUserInfo> openidQw =new QueryWrapper<>();
        openidQw.lambda().eq(CardUserInfo::getOpenId,idUser.getRegOpenId());
        CardUserInfo oldUser = this.getOne(idQw);
        if(DataUtil.isNotEmpty(oldUser)){
            oldUser.setOpenId("");
            this.updateById(oldUser);
        }
        //检查是否是内部员工
        QueryWrapper<CardUserUnionUser> uQw=new QueryWrapper<>();
        uQw.lambda().eq(CardUserUnionUser::getCardId,vo.getId());
        CardUserUnionUser cardUserUnionUser = cardUserUnionCrmUserMapper.selectOne(uQw);
        vo.setIsInternal(DataUtil.isNotEmpty(cardUserUnionUser)?true:false);

        return vo;
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
    public CardUserDto getCardUserByOpenId(String openId) {
        //免密登录时，根据unionId先查询该unionId绑定的已有手机号的用户
        QueryWrapper<CardUserInfo> userQw =new QueryWrapper<>();
        userQw.lambda().eq(CardUserInfo::getOpenId,openId);
        CardUserInfo one = this.getOne(userQw);
        if(DataUtil.isEmpty(one)){
            //如果没有查到绑定手机号的用户，则查询该unionId注册时的账号
            userQw.clear();
            userQw.lambda().eq(CardUserInfo::getRegOpenId,openId);
            one=this.getOne(userQw);
            if(DataUtil.isEmpty(one)){
                throw new DefaultException("该微信用户不存在名片账户！");
            }
        }
        CardUserDto cardUser=new CardUserDto();
        BeanUtils.copyProperties(one,cardUser);
        cardUser.setUserPwd(null);
        //检查是否是内部员工
        QueryWrapper<CardUserUnionUser> qw=new QueryWrapper<>();
        qw.lambda().eq(CardUserUnionUser::getCardId,cardUser.getId());
        CardUserUnionUser cardUserUnionUser = cardUserUnionCrmUserMapper.selectOne(qw);
        cardUser.setIsInternal( DataUtil.isNotEmpty(cardUserUnionUser)?true:false);

        return cardUser;
    }

    @Override
    public CardUserDto unBindPhoneNumber(String openId) {
        QueryWrapper<CardUserInfo> userQw =new QueryWrapper<>();
        userQw.lambda().eq(CardUserInfo::getOpenId,openId);
        CardUserInfo one = this.getOne(userQw);
        if(DataUtil.isNotEmpty(one)){
            one.setOpenId("");
            one.setUpdateTime(new Date());
            this.updateById(one);
        }
        userQw.clear();
        userQw.lambda().eq(CardUserInfo::getRegOpenId,openId);
        CardUserInfo info = this.getOne(userQw);
        CardUserDto cardUser = new CardUserDto();
        if(DataUtil.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, cardUser);
            cardUser.setUserPwd(null);
            return cardUser;
        }else {
            //正常情况下不会出现没注册访客账号
            //创建一个名片用户
            CardUserInfo user = new CardUserInfo();
            user.setId(UUIDutils.getUUID32());
            user.setRegOpenId(openId);
            user.setCreateTime(new Date());
            this.save(user);
            BeanUtils.copyProperties(user, cardUser);
        }
        //检查是否是内部员工
        QueryWrapper<CardUserUnionUser> qw=new QueryWrapper<>();
        qw.lambda().eq(CardUserUnionUser::getCardId,cardUser.getId());
        CardUserUnionUser cardUserUnionUser = cardUserUnionCrmUserMapper.selectOne(qw);
        cardUser.setIsInternal( DataUtil.isNotEmpty(cardUserUnionUser)?true:false);
        return cardUser;
    }

    @Override
    public CardUserInfoVo bindOpenId(String openId,String userId) {
        QueryWrapper<CardUserInfo> userQw =new QueryWrapper<>();
        userQw.lambda().eq(CardUserInfo::getId,userId);
        CardUserInfo one = this.getOne(userQw);
        if(DataUtil.isNotEmpty(one)) {
            //同时删除原先绑定的openId用户
            QueryWrapper<CardUserInfo> openidQw =new QueryWrapper<>();
            openidQw.lambda().eq(CardUserInfo::getOpenId,openId);
            CardUserInfo oldUser = this.getOne(openidQw);
            if(DataUtil.isNotEmpty(oldUser)){
                oldUser.setOpenId("");
                this.updateById(oldUser);
            }
            one.setOpenId(openId);
            this.updateById(one);
        }
        CardUserInfoVo cardUser = new CardUserInfoVo();
        BeanUtils.copyProperties(one, cardUser);
        return cardUser;
    }

    @Override
    public CardUserInfoVo getCardUserById(String id) {
        QueryWrapper<CardUserInfo> userQw =new QueryWrapper<>();
        userQw.lambda().eq(CardUserInfo::getId,id);
        CardUserInfo one = this.getOne(userQw);
        if(DataUtil.isNotEmpty(one)){
            CardUserInfoVo cardUser = new CardUserInfoVo();
            BeanUtils.copyProperties(one, cardUser);
            return cardUser;
        }else {
            return null;
        }

    }
}
