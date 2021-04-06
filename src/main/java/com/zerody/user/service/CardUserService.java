package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.api.vo.CardUserInfoVo;
import com.zerody.user.domain.CardUserInfo;
/**
 *
 *
 * @author               dabai
 * @description          DELL
 * @date                 2021/1/19 14:38
 * @param
 * @return
 */
public interface CardUserService extends IService<CardUserInfo> {

    /**
     *
     *  添加名片用户
     * @author               dabai
     * @description          DELL
     * @date                 2021/1/19 14:37
     * @param                cardUser
     * @return               com.zerody.user.api.dto.CardUserDto
     */
    CardUserDto addCardUser(CardUserDto cardUser);

    /**
     *
     *  —
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:38
     * @param                data
     * @return               void
     */
    CardUserInfoVo bindPhoneNumber(CardUserInfoVo data);

    /**
     *
     * _
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:38
     * @param                userId
     * @return               com.zerody.user.api.dto.CardUserDto
     */
    CardUserDto checkCardUser(String userId);

    /**************************************************************************************************
     **
     * 名片登录时UnionID账户是否存在
     *
     * @param openId
     * @return {@link CardUserDto }
     * @author DaBai
     * @date 2021/1/21  14:54
     */
    CardUserDto getCardUserByOpenId(String openId);

    /**************************************************************************************************
     **
     * 解绑名片用户手机号
     *
     * @param openId
     * @return {@link CardUserInfoVo }
     * @author DaBai
     * @date 2021/1/23  11:31
     */
    CardUserDto unBindPhoneNumber(String openId);

    /**************************************************************************************************
     **
     * 绑定OPENID
     *
     * @param openId
     * @return {@link null }
     * @author DaBai
     * @date 2021/1/30  17:40
     */
    CardUserInfoVo bindOpenId(String openId,String userId);

    /**************************************************************************************************
     **
     *
     *
     * @param id
     * @return {@link id }
     * @author DaBai
     * @date 2021/4/6  13:29
     */
    CardUserInfoVo getCardUserById(String id);
}
