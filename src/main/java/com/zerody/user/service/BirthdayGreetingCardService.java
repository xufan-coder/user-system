package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.BirthdayGreetingCard;
import com.zerody.user.dto.BirthdayGreetingCardDto;
import com.zerody.user.vo.BirthdayGreetingCardVo;

import java.util.List;

/**
 * @author kuang
 */
public interface BirthdayGreetingCardService extends IService<BirthdayGreetingCard> {

    void addGreeting(UserVo user, BirthdayGreetingCardDto cardDto);

    void modifyTemplate(BirthdayGreetingCardDto cardDto);

    List<BirthdayGreetingCardVo> getGreetingList(Integer state);

    void modifyGreetingById(String id);
}
