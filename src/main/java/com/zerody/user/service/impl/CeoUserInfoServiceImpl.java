package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.mapper.CeoUserInfoMapper;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/4/20 14:29
 */

@Slf4j
@Service
public class CeoUserInfoServiceImpl extends BaseService<CeoUserInfoMapper, CeoUserInfo> implements CeoUserInfoService {

    private CeoUserInfoMapper ceoUserInfoMapper;
    @Override
    public CeoUserInfo getByPhone(String phone) {
        QueryWrapper<CeoUserInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(CeoUserInfo::getPhoneNumber,phone).eq(CeoUserInfo::getDeleted, YesNo.NO);
        return this.getOne(qw);
    }

    @Override
    public void updateCeoById(com.zerody.user.api.vo.CeoUserInfoVo logInfo) {
        UpdateWrapper<CeoUserInfo> uw=new UpdateWrapper<>();
        if(DataUtil.isNotEmpty(logInfo.getLastCheckSms())){
            uw.lambda().set(CeoUserInfo::getLastCheckSms,logInfo.getLastCheckSms());
        }
        if(DataUtil.isNotEmpty(logInfo.getLoginTime())){
            uw.lambda().set(CeoUserInfo::getLoginTime,logInfo.getLoginTime());
        }
        if(DataUtil.isNotEmpty(logInfo.getUserPwd())){
            uw.lambda().set(CeoUserInfo::getUserPwd,logInfo.getUserPwd());
        }
        uw.lambda().set(BaseModel::getUpdateTime,new Date())
                .eq(CeoUserInfo::getId,logInfo.getId());

        ceoUserInfoMapper.update(null,uw);
    }

    @Override
    public CeoUserInfo getUserById(String id) {
        return this.getById(id);
    }
}
