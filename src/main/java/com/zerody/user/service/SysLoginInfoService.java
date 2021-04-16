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
    /**
     *
     * 新增或修改登录信息
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:06
     * @param                logInfo
     * @return               com.zerody.common.bean.DataResult
     */
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
