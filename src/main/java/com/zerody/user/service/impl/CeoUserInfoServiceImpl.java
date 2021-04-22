package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.CeoUserInfoPageDto;
import com.zerody.user.mapper.CeoUserInfoMapper;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/4/20 14:29
 */

@Slf4j
@Service
public class CeoUserInfoServiceImpl extends BaseService<CeoUserInfoMapper, CeoUserInfo> implements CeoUserInfoService {

    @Autowired
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

    @Override
    public void addCeoUser(CeoUserInfo ceoUserInfo) {
        ceoUserInfo.setCreateTime(new Date());
        ceoUserInfo.setCreateId(UserUtils.getUserId());
        //是否停用状态
        ceoUserInfo.setStatus(YesNo.NO);
        this.save(ceoUserInfo);
    }

    @Override
    public void updateCeoUser(CeoUserInfo ceoUserInfo) {
        ceoUserInfo.setUpdateTime(new Date());
        ceoUserInfo.setUpdateId(UserUtils.getUserId());
        this.updateById(ceoUserInfo);
    }

    @Override
    public void deleteCeoUserById(String id) {
        UpdateWrapper<CeoUserInfo> uw=new UpdateWrapper<>();
        uw.lambda().set(CeoUserInfo::getDeleted,YesNo.YES)
                .set(BaseModel::getUpdateTime,new Date())
                .eq(BaseModel::getId,id);
        this.update(uw);
    }

    @Override
    public IPage<CeoUserInfo> selectCeoUserPage(CeoUserInfoPageDto ceoUserInfoPageDto) {
        IPage<CeoUserInfo> infoVoIPage = new Page<>(ceoUserInfoPageDto.getCurrent(),ceoUserInfoPageDto.getPageSize());
        QueryWrapper<CeoUserInfo> qw=new QueryWrapper<>();
        qw.lambda().orderByDesc(BaseModel::getCreateTime);
        if(DataUtil.isNotEmpty(ceoUserInfoPageDto.getPhone())){
            qw.lambda().eq(CeoUserInfo::getPhoneNumber,ceoUserInfoPageDto.getPhone());
        }
        if(DataUtil.isNotEmpty(ceoUserInfoPageDto.getUserName())){
            qw.lambda().eq(CeoUserInfo::getUserName,ceoUserInfoPageDto.getUserName());
        }
        IPage<CeoUserInfo> ceoUserInfoIPage = ceoUserInfoMapper.selectPage(infoVoIPage, qw);
        return  ceoUserInfoIPage;
    }
}
