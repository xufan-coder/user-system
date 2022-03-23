package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.domain.SysLoginInfo;
import com.zerody.user.domain.SysUserIdentifier;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.SysUserIdentifierQueryDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.IdentifierEnum;
import com.zerody.user.mapper.SysLoginInfoMapper;
import com.zerody.user.mapper.SysUserIdentifierMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.service.SysUserIdentifierService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysUserIdentifierVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * @author kuang
 * @date
 **/
@Slf4j
@Service
public class SysUserIdentifierServiceImpl  extends ServiceImpl<SysUserIdentifierMapper, SysUserIdentifier> implements SysUserIdentifierService {

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Override
    public void addSysUserIdentifier(SysUserIdentifier data) {

        SysUserIdentifier identifier = this.getIdentifierInfo(data.getUserId());
        if(identifier != null) {
            throw new DefaultException("该账号已绑定设备号");
        }
        data.setId(UUIDutils.getUUID32());
        LoginUserInfoVo userInfoVo = sysUserInfoMapper.selectLoginUserInfo(data.getUserId());
        data.setMobile(userInfoVo.getPhoneNumber());
        data.setCompanyName(userInfoVo.getCompanyName());
        data.setDepartName(userInfoVo.getDepartName());
        data.setPositionName(userInfoVo.getPositionName());
        data.setCreateTime(new Date());
        data.setCreateUsername(userInfoVo.getUserName());
        log.info("账号设备绑定  ——> 入参：{}", JSON.toJSONString(data));
        this.save(data);
    }

    @Override
    public void addApprove(String id, String approveState, String userId) {
        SysUserIdentifier identifier = this.getIdentifierInfo(userId,id);
        if(identifier == null) {
            throw new DefaultException("未找到有效设备绑定数据");
        }
        if(approveState.equals(ApproveStatusEnum.REVOKE.name()) || approveState.equals(ApproveStatusEnum.SUCCESS.name())) {
            //设备解绑申请 已撤销/已通过
            identifier.setState(IdentifierEnum.INVALID.getValue());
        }
        SysUserInfo user = sysUserInfoService.getById(userId);
        identifier.setApproveState(approveState);
        identifier.setUpdateUsername(user.getUserName());
        identifier.setUpdateBy(userId);
        identifier.setUpdateTime(new Date());

        log.info("账号设备申请  ——> 入参：{}", JSON.toJSONString(identifier));
        this.updateById(identifier);
    }

    @Override
    public void addApprove(String userId) {
        SysUserIdentifier identifier = this.getIdentifierInfo(userId);
        if(identifier == null) {
            throw new DefaultException("未找到有效设备绑定数据");
        }
        SysUserInfo user = sysUserInfoService.getUserById(userId);
        identifier.setState(IdentifierEnum.DELETE.getValue());
        identifier.setUpdateUsername(user.getUserName());
        identifier.setUpdateBy(userId);
        identifier.setUpdateTime(new Date());

        log.info("账号设备申请  ——> 入参：{}", JSON.toJSONString(identifier));
        this.updateById(identifier);
    }

    @Override
    public List<SysUserIdentifier> getPageUserIdentifier(SysUserIdentifierQueryDto queryDto){
        QueryWrapper<SysUserIdentifier> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserIdentifier:: getCompanyId,queryDto.getCompanyId());
        queryWrapper.lambda().isNotNull(SysUserIdentifier:: getApproveState);
        return this.list(queryWrapper);
    }
    @Override
    public SysUserIdentifier getIdentifierInfo(String userId){
        return  getIdentifierInfo(userId,null);
    }

    @Override
    public SysUserIdentifier getIdentifierInfo(String userId, String id ){
        QueryWrapper<SysUserIdentifier> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserIdentifier:: getUserId, userId);
        queryWrapper.lambda().eq(SysUserIdentifier:: getState, YesNo.YES);
        if(StringUtils.isNotEmpty(id)) {
            queryWrapper.lambda().eq(SysUserIdentifier:: getId, id);
        }
        queryWrapper.lambda().last("limit 0,1");
        return this.getOne(queryWrapper);
    }

    @Override
    public SysUserIdentifierVo getUserIdentifierInfo(String userId){
        SysUserIdentifier identifier = this.getIdentifierInfo(userId);
        QueryWrapper<SysLoginInfo> loginQw = new QueryWrapper<>();
        loginQw.lambda().eq(SysLoginInfo::getUserId, userId);
        SysUserIdentifierVo identifierVo = new SysUserIdentifierVo();

        if(!Objects.isNull(identifier)){
            BeanUtils.copyProperties(identifierVo,identifierVo);
        }else {
            SysLoginInfo logInfo = sysLoginInfoMapper.selectOne(loginQw);
            LoginUserInfoVo user = sysUserInfoMapper.selectLoginUserInfo(userId);
            identifierVo.setUsername(user.getUserName());
            identifierVo.setMobile(user.getPhoneNumber());
            identifierVo.setCreateUsername(user.getCompanyName());
            identifierVo.setLastLoginTime(logInfo.getLoginTime());
        }
        identifierVo.setBinding(Objects.isNull(identifier) ? YesNo.NO : YesNo.YES);
        return identifierVo;
    }
}
