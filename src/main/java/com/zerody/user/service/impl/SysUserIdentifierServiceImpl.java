package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.domain.*;
import com.zerody.user.dto.SysUserIdentifierQueryDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.IdentifierEnum;
import com.zerody.user.mapper.*;
import com.zerody.user.service.AdminUserService;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.SysUserIdentifierService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysStaffInfoDetailsVo;
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
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private CeoUserInfoService ceoUserInfoService;

    @Autowired
    private CheckUtil checkUtil;

    @Override
    public void addSysUserIdentifier(SysUserIdentifier data) {

        SysUserIdentifier identifier = this.getIdentifierInfo(data.getUserId());
        if(identifier != null) {
            throw new DefaultException("该账号已绑定设备号");
        }
        data.setId(UUIDutils.getUUID32());
        userAssignment(data,data.getUserId());
        data.setApproveState(null);
        data.setCreateTime(new Date());
        log.info("账号设备绑定  ——> 入参：{}", JSON.toJSONString(data));
        this.save(data);
    }

    private void addIdentifier(SysUserIdentifier data){
        SysUserIdentifier userIdentifier = new SysUserIdentifier();
        BeanUtils.copyProperties(data,userIdentifier);
        userIdentifier.setId(UUIDutils.getUUID32());
        userIdentifier.setCreateTime(new Date());
        userIdentifier.setState(null);
        userIdentifier.setApproveState(null);
        userIdentifier.setUpdateTime(null);
        userIdentifier.setUpdateUsername(null);
        userIdentifier.setUpdateBy(null);
        this.save(userIdentifier);
    }

    /**赋值user信息  */
    private void userAssignment(SysUserIdentifier identifier,String userId){
        LoginUserInfoVo userInfoVo = this.sysUserInfoMapper.selectLoginUserInfo(userId);
        if(Objects.isNull(userInfoVo)) {
            CeoUserInfo ceo = ceoUserInfoService.getUserById(userId);
            identifier.setMobile(ceo.getPhoneNumber());
            identifier.setCompanyName(ceo.getCompany());
            identifier.setPositionName(ceo.getPosition());
            identifier.setCreateUsername(ceo.getUserName());
        }else {
            identifier.setMobile(userInfoVo.getPhoneNumber());
            identifier.setCompanyName(userInfoVo.getCompanyName());
            identifier.setDepartId(userInfoVo.getDepartId());
            identifier.setDepartName(userInfoVo.getDepartName());
            identifier.setPositionId(userInfoVo.getPositionId());
            identifier.setPositionName(userInfoVo.getPositionName());
            identifier.setCreateUsername(userInfoVo.getUserName());
        }
    }


    @Override
    public void addApply(String id, Integer state, String userId) {
        SysUserIdentifier identifier = this.getIdentifierInfo(userId,id);
        if(identifier == null) {
            throw new DefaultException("未找到有效设备绑定数据");
        }
        if(ApproveStatusEnum.APPROVAL.name().equals(identifier.getApproveState()) && state.equals(YesNo.YES) ){
            throw new DefaultException("已在审批中");
        }
        //设备解绑 1申请 / 0 已撤销
        if(state.equals(YesNo.YES) ) {
            identifier.setApproveState(ApproveStatusEnum.APPROVAL.name());
            identifier.setCreateTime(new Date());
            this.updateById(identifier);
        }else if(state.equals(YesNo.NO) && ApproveStatusEnum.APPROVAL.name().equals(identifier.getApproveState())){
            identifier.setApproveState(ApproveStatusEnum.REVOKE.name());
            identifier.setState(IdentifierEnum.INVALID.getValue());
            this.updateIdentifier(identifier,userId);
            userAssignment(identifier,identifier.getUserId());
            this.addIdentifier(identifier);
        }else {
            throw new DefaultException("审批状态错误");
        }
        log.info("账号设备审批  ——> 入参：{}", JSON.toJSONString(identifier));
    }

    @Override
    public void addApprove(String id, Integer state, String userId) {
        SysUserIdentifier identifier = this.getById(id);
        if(identifier == null) {
            throw new DefaultException("未找到有效设备绑定数据");
        }
        if(!ApproveStatusEnum.APPROVAL.name().equals(identifier.getApproveState())){
            throw new DefaultException("未找到有效审批数据");
        }
        //设备解绑 1 同意 / 0拒绝
        if(state.equals(YesNo.YES) ) {
            identifier.setApproveState(ApproveStatusEnum.SUCCESS.name());
        }else if(state.equals(YesNo.NO)){
            identifier.setApproveState(ApproveStatusEnum.FAIL.name());
        }else {
            throw new DefaultException("审批状态错误");
        }
        identifier.setState(IdentifierEnum.INVALID.getValue());

        log.info("账号设备审批  ——> 入参：{}", JSON.toJSONString(identifier));
        this.updateIdentifier(identifier,userId);
        if(state.equals(YesNo.NO)){
            userAssignment(identifier,identifier.getUserId());
            this.addIdentifier(identifier);
        }else {
            this.checkUtil.removeUserToken(identifier.getUserId());
        }
    }

    private void  updateIdentifier(SysUserIdentifier identifier, String userId){
        SysUserInfo user = sysUserInfoService.getById(userId);
        if(Objects.isNull(user)) {
            // 判断后台账户
            AdminUserInfo info = this.adminUserMapper.selectById(userId);
            if(Objects.isNull(info)) {
                //判断ceo账户
                CeoUserInfo ceo = ceoUserInfoService.getUserById(userId);
                identifier.setUpdateUsername(ceo.getUserName());
            }else {
                identifier.setUpdateUsername(info.getUserName());
            }
        }else {
            identifier.setUpdateUsername(user.getUserName());
        }

        identifier.setUpdateBy(userId);
        identifier.setUpdateTime(new Date());
        this.updateById(identifier);
    }

    @Override
    public void addUnbound(String userId,String updateUserId) {
        SysUserIdentifier identifier = this.getIdentifierInfo(userId);
        if(identifier == null) {
            throw new DefaultException("未找到有效设备绑定数据");
        }
        identifier.setState(IdentifierEnum.DELETE.getValue());
        this.updateIdentifier(identifier,updateUserId);
        this.checkUtil.removeUserToken(userId);
        log.info("账号设备解绑  ——> 入参：{}", JSON.toJSONString(identifier));
    }

    @Override
    public Page<SysUserIdentifier> getPageUserIdentifier(SysUserIdentifierQueryDto queryDto){
        QueryWrapper<SysUserIdentifier> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUserIdentifier:: getState,YesNo.YES,YesNo.NO);
        if(StringUtils.isNotEmpty(queryDto.getCompanyId())) {
            queryWrapper.lambda().eq(SysUserIdentifier:: getCompanyId,queryDto.getCompanyId());
        }
        if(StringUtils.isNotEmpty(queryDto.getDepartId())) {
            queryWrapper.lambda().eq(SysUserIdentifier:: getDepartId,queryDto.getDepartId());
        }
        if(StringUtils.isNotEmpty(queryDto.getMobile())){
            queryWrapper.lambda().like(SysUserIdentifier::getMobile,queryDto.getMobile());
        }
        if(StringUtils.isNotEmpty(queryDto.getUserId())){
            queryWrapper.lambda().like(SysUserIdentifier::getUserId,queryDto.getUserId());
        }
        if(StringUtils.isNotEmpty(queryDto.getApproveState())){
            queryWrapper.lambda().eq(SysUserIdentifier::getApproveState,queryDto.getApproveState());
        }else {
            queryWrapper.lambda().isNotNull(SysUserIdentifier:: getApproveState);
        }
        queryWrapper.lambda().orderByDesc(SysUserIdentifier :: getState,SysUserIdentifier:: getCreateTime);
        Page<SysUserIdentifier> page = new Page<>();
        page.setCurrent(queryDto.getCurrent());
        page.setSize(queryDto.getPageSize());
        return this.page(page,queryWrapper);
    }

    @Override
    public SysUserIdentifier getIdentifierInfo(String userId){
        return  getIdentifierInfo(userId,null);
    }

    @Override
    public SysUserIdentifier getIdentifierInfo(String userId, String id ){
        QueryWrapper<SysUserIdentifier> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserIdentifier:: getState, YesNo.YES);
        if(StringUtils.isNotEmpty(userId)) {
            queryWrapper.lambda().eq(SysUserIdentifier:: getUserId, userId);
        }
        if(StringUtils.isNotEmpty(id)) {
            queryWrapper.lambda().eq(SysUserIdentifier:: getId, id);
        }
        queryWrapper.lambda().last("limit 0,1");
        return this.getOne(queryWrapper);
    }

    @Override
    public SysUserIdentifierVo getUserIdentifierInfo(String userId){
        SysUserIdentifier identifier = this.getIdentifierInfo(userId);
        SysUserIdentifierVo identifierVo = new SysUserIdentifierVo();
        QueryWrapper<SysLoginInfo> loginQw = new QueryWrapper<>();
        loginQw.lambda().eq(SysLoginInfo::getUserId, userId);
        SysLoginInfo logInfo = sysLoginInfoMapper.selectOne(loginQw);
        if(!Objects.isNull(identifier)){
            BeanUtils.copyProperties(identifier,identifierVo);
            identifierVo.setUsername(identifier.getCreateUsername());
            // 获取ceo的最近登录时间
            if(Objects.isNull(logInfo)) {
                CeoUserInfo ceo = ceoUserInfoService.getUserById(userId);
                identifierVo.setLastLoginTime(ceo.getLoginTime());
            }else {
                identifierVo.setLastLoginTime(logInfo.getLoginTime());
            }
        }else {
            SysStaffInfoDetailsVo user = sysStaffInfoMapper.getStaffinfoDetails(userId);
            if(Objects.isNull(user)) {
                CeoUserInfo ceo = ceoUserInfoService.getUserById(userId);
                identifierVo.setUsername(ceo.getUserName());
                identifierVo.setMobile(ceo.getPhoneNumber());
                identifierVo.setCompanyName(ceo.getCompany());
                identifierVo.setCreateUsername(ceo.getUserName());
                identifierVo.setLastLoginTime(ceo.getLoginTime());
            }else {
                identifierVo.setUsername(user.getUserName());
                identifierVo.setMobile(user.getPhoneNumber());
                identifierVo.setCompanyName(user.getCompanyName());
                identifierVo.setCreateUsername(user.getUserName());
                identifierVo.setLastLoginTime(logInfo.getLoginTime());
            }
        }
        identifierVo.setBinding(Objects.isNull(identifier) ? YesNo.NO : YesNo.YES);
        return identifierVo;
    }
}
