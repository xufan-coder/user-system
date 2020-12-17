package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.pojo.SysLoginInfo;

/**
 * @author PengQiang
 * @ClassName SysLoginInfoService
 * @DateTime 2020/12/17_11:42
 * @Deacription TODO
 */
public interface SysLoginInfoService {
    DataResult addLogin(SysLoginInfo logInfo);
}
