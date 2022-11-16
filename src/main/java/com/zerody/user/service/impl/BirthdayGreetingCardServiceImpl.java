package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.BirthdayGreetingCard;
import com.zerody.user.dto.BirthdayGreetingCardDto;
import com.zerody.user.dto.GreetingListDto;
import com.zerody.user.mapper.BirthdayGreetingCardMapper;
import com.zerody.user.service.BirthdayGreetingCardService;
import com.zerody.user.vo.BirthdayGreetingCardVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kuang
 */
@Service
public class BirthdayGreetingCardServiceImpl extends ServiceImpl<BirthdayGreetingCardMapper, BirthdayGreetingCard> implements BirthdayGreetingCardService {


    @Override
    public void addGreeting(UserVo user, BirthdayGreetingCardDto cardDto) {
        BirthdayGreetingCard card = new BirthdayGreetingCard();
        card.setCreateBy(user.getUserId());
        card.setCardUrl(cardDto.getCardUrl());
        card.setSort(cardDto.getSort());
        card.setState(cardDto.getState());
        card.setType(cardDto.getType());
        card.setCreateTime(new Date());
        card.setId(UUIDutils.getUUID32());
        this.save(card);

    }

    @Override
    public void modifyTemplate(BirthdayGreetingCardDto cardDto) {
        BirthdayGreetingCard card = new BirthdayGreetingCard();
        card.setId(cardDto.getId());
        card.setCardUrl(cardDto.getCardUrl());
        card.setState(cardDto.getState());
        card.setSort(cardDto.getSort());
        card.setType(cardDto.getType());
        this.updateById(card);
    }

    @Override
    public List<BirthdayGreetingCardVo> getGreetingList(Integer state) {
        QueryWrapper<BirthdayGreetingCard> qw = new QueryWrapper<>();
        qw.lambda().eq(BirthdayGreetingCard::getDeleted, YesNo.YES);
        qw.lambda().eq(state !=null,BirthdayGreetingCard::getState, state);
        qw.lambda().orderByDesc(BirthdayGreetingCard::getSort);
        List<BirthdayGreetingCard>  cards =  this.list(qw);

        List<BirthdayGreetingCardVo> cardVos = new ArrayList<>();
        for(BirthdayGreetingCard card : cards){
            BirthdayGreetingCardVo vo = new BirthdayGreetingCardVo();
            BeanUtils.copyProperties(card,vo);
            cardVos.add(vo);
        }
        //
        return cardVos;
    }

    @Override
    public List<BirthdayGreetingCardVo> getGreetingList(GreetingListDto param) {
        QueryWrapper<BirthdayGreetingCard> qw = new QueryWrapper<>();
        qw.lambda().eq(DataUtil.isNotEmpty(param.getType()),BirthdayGreetingCard::getType,param.getType());
        qw.lambda().eq(DataUtil.isNotEmpty(param.getState()),BirthdayGreetingCard::getState,param.getState());
        qw.lambda().eq(BirthdayGreetingCard::getDeleted, YesNo.YES);
        qw.lambda().orderByDesc(BirthdayGreetingCard::getSort);
        List<BirthdayGreetingCard>  cards =  this.list(qw);

        List<BirthdayGreetingCardVo> cardVos = new ArrayList<>();
        for(BirthdayGreetingCard card : cards){
            BirthdayGreetingCardVo vo = new BirthdayGreetingCardVo();
            BeanUtils.copyProperties(card,vo);
            cardVos.add(vo);
        }
        return cardVos;
    }


    @Override
    public void modifyGreetingById(String id) {
        BirthdayGreetingCard card = new BirthdayGreetingCard();
        card.setId(id);
        card.setDeleted(YesNo.NO);
        this.updateById(card);
    }
}
