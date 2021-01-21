package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zerody.common.bean.DataResult;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.mapper.SysLoginInfoMapper;
import com.zerody.user.domain.SysLoginInfo;
import com.zerody.user.domain.base.BaseStringModel;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.base.BaseStringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SysLoginInfoServiceImpl
 * @DateTime 2020/12/17_11:43
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysLoginInfoServiceImpl extends BaseStringService<SysLoginInfoMapper, SysLoginInfo> implements SysLoginInfoService {

    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Override
    public DataResult addOrUpdateLogin(SysLoginInfo logInfo) {
        this.saveOrUpdate(logInfo);
        return new DataResult();
    }

    @Override
    public void updateLoginInfoByUserId(com.zerody.user.api.vo.SysLoginInfo logInfo) {
        UpdateWrapper<SysLoginInfo> uw=new UpdateWrapper<>();
        if(DataUtil.isNotEmpty(logInfo.getLastChecKSms())){
            uw.lambda().set(SysLoginInfo::getLastCheckSms,logInfo.getLastChecKSms());
        }
        if(DataUtil.isNotEmpty(logInfo.getLoginTime())){
            uw.lambda().set(SysLoginInfo::getLoginTime,logInfo.getLoginTime());
        }
        if(DataUtil.isNotEmpty(logInfo.getUserPwd())){
            uw.lambda().set(SysLoginInfo::getUserPwd,logInfo.getUserPwd());
        }
        uw.lambda().set(BaseStringModel::getUpdateTime,new Date())
                .eq(SysLoginInfo::getUserId,logInfo.getUserId());
        sysLoginInfoMapper.update(null,uw);
    }
}
