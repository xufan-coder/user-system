package com.zerody.user.service;

import com.zerody.common.bean.DataResult;

/**
 * @author PengQiang
 * @ClassName SysAuthMenuService
 * @DateTime 2020/12/16_11:35
 * @Deacription TODO
 */
public interface SysAuthMenuService {

    DataResult getMenuCodeList(String compId);
}
