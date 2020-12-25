package com.zerody.user.service.impl;

import com.zerody.common.bean.DataResult;
import com.zerody.user.mapper.SysLoginInfoMapper;
import com.zerody.user.pojo.SysLoginInfo;
import com.zerody.user.pojo.base.BaseStringModel;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.base.BaseStringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author PengQiang
 * @ClassName SysLoginInfoServiceImpl
 * @DateTime 2020/12/17_11:43
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysLoginInfoServiceImpl extends BaseStringService<SysLoginInfoMapper, SysLoginInfo> implements SysLoginInfoService {


    @Override
    public DataResult addOrUpdateLogin(SysLoginInfo logInfo) {
        this.saveOrUpdate(logInfo);
        return new DataResult();
    }
}
