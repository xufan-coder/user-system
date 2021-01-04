package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.domain.SysLoginInfo;

/**
 * @author PengQiang
 * @ClassName SysLoginInfoService
 * @DateTime 2020/12/17_11:42
 * @Deacription TODO
 */
public interface SysLoginInfoService {
    DataResult addOrUpdateLogin(SysLoginInfo logInfo);

    /**************************************************************************************************
     **
     *  修改login登录信息表
     *
     * @param logInfo
     * @return {@link null }
     * @author DaBai
     * @date 2020/12/31  15:06
     */
    void updateLoginInfoByUserId(com.zerody.user.api.vo.SysLoginInfo logInfo);
}
