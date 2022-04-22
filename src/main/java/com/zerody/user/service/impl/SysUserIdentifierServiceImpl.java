package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.MQ;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.mq.RabbitMqService;
import com.zerody.common.util.EntityUtils;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.flow.api.dto.base.DataResult;
import com.zerody.flow.api.dto.process.ProcessStartDto;
import com.zerody.flow.api.dto.process.ProcessStartResultDto;
import com.zerody.flow.api.dto.process.StopProcessDto;
import com.zerody.flow.api.dto.task.TaskFormDto;
import com.zerody.flow.api.state.TaskAction;
import com.zerody.jpush.api.dto.AuroraPushDto;
import com.zerody.user.domain.*;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.domain.SysLoginInfo;
import com.zerody.user.domain.SysUserIdentifier;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.SysUserIdentifierDto;
import com.zerody.user.dto.SysUserIdentifierQueryDto;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.IdentifierEnum;
import com.zerody.user.feign.ProcessServerFeignService;
import com.zerody.user.mapper.*;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.SysUserIdentifierService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysStaffInfoDetailsVo;
import com.zerody.user.vo.SysUserIdentifierVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


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

    @Autowired
    private RabbitMqService mqService;

    @Autowired
    private ProcessServerFeignService processServerFeignService;


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
        this.pullMq(data.getUserId(),data.getDeviceId(),data.getUserDevice());
    }

    private void pullMq(String userId, String deviceId, String userDevice){
        AuroraPushDto auroraPushDto = new AuroraPushDto();
        auroraPushDto.setUserId(userId);
        auroraPushDto.setLoginTime(new Date());
        auroraPushDto.setDeviceId(deviceId);
        auroraPushDto.setUserDevice(userDevice);
        this.mqService.send(auroraPushDto, MQ.QUEUE_USER_DEVICE);
    }

    @Override
    public void addIdentifier(SysUserIdentifier data){
        SysUserIdentifier userIdentifier = new SysUserIdentifier();
        BeanUtils.copyProperties(data,userIdentifier);
        userIdentifier.setId(UUIDutils.getUUID32());
        userIdentifier.setCreateTime(new Date());
        userIdentifier.setState(null);
        userIdentifier.setApproveState(null);
        userIdentifier.setUpdateTime(null);
        userIdentifier.setUpdateUsername(null);
        userIdentifier.setUpdateBy(null);
        userIdentifier.setProcessId(null);
        userIdentifier.setProcessKey(null);
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
    public void addApplyV2(SysUserIdentifierDto dto) {
        UserVo user = dto.getUser();
        if(!user.isCEO()) {
            SysUserIdentifier identifier = this.getIdentifierInfo(user.getUserId(), dto.getId());
            if (identifier == null) {
                throw new DefaultException("未找到有效设备绑定数据");
            }
            if(dto.getState().equals(YesNo.YES)) {
                if (ApproveStatusEnum.APPROVAL.name().equals(identifier.getApproveState())) {
                    throw new DefaultException("已在审批中");
                }
                //发起流程审批
                ProcessStartDto params = new ProcessStartDto();
                params.setProcessDefKey("UnbindDevice");
                params.setCompanyId(user.getCompanyId());
                params.setStarter(user.getUserId());
                params.setStarterName(user.getUserName());

                //查询详情作为流程变量
                SysUserIdentifierVo userIdentifierInfo = this.getUserIdentifierInfo(identifier.getUserId());
                Map<String, Object> map = EntityUtils.entityToMap(userIdentifierInfo);
                String lastLoginTime = com.zerody.common.util.DateUtil.getDateTime(userIdentifierInfo.getLastLoginTime(),"yyyy-MM-dd HH:mm:ss");
                String createTime = com.zerody.common.util.DateUtil.getDateTime(userIdentifierInfo.getCreateTime(),"yyyy-MM-dd HH:mm:ss");
                map.put("lastLoginTime",lastLoginTime);
                map.put("createTime",createTime);
                params.setVariables(map);
                params.setBusinessKey(identifier.getId());
                com.zerody.flow.api.dto.base.DataResult<ProcessStartResultDto> dataResult = processServerFeignService.startProcessByKey(params);
                if (!dataResult.isSuccess()) {
                    log.info("审批流程异常：{}", JSONObject.toJSONString(dataResult));
                    throw new DefaultException("审批流程异常" + dataResult.getMessage());
                }
                ProcessStartResultDto data = dataResult.getData();
                identifier.setApproveState(ApproveStatusEnum.APPROVAL.name());
                identifier.setUpdateTime(new Date());
                identifier.setApplyTime(new Date());
                identifier.setProcessId(data.getProcInstId());
                identifier.setProcessKey("UnbindDevice");
                this.updateById(identifier);
                log.info("账号解绑审批发起  ——> 入参：{}", JSON.toJSONString(dto));
            }else if(dto.getState().equals(YesNo.NO) && ApproveStatusEnum.APPROVAL.name().equals(identifier.getApproveState())) {
                //撤销
                if(DataUtil.isNotEmpty(identifier.getProcessId())){
                    //设备解绑审批 1同意 / 0 拒绝
                    TaskFormDto param=new TaskFormDto();
                    param.setProcessInstanceId(identifier.getProcessId());
                    param.setUserId(user.getUserId());
                    param.setTenantId(user.getCompanyId());
                    param.setUserName(user.getUserName());
                    param.setRoleId(user.getRoleId());
                    param.setGroupId(user.getDeptId());
                    com.zerody.flow.api.dto.base.DataResult<?> dataResult = processServerFeignService.cancelProcess(param);
                    if(!dataResult.isSuccess()){
                        log.error("撤销流程错误——：{}", dataResult.getMessage());
                        throw new DefaultException(dataResult.getMessage());
                    }
                }
            }
        }else {
            this.addApply(dto.getId(), dto.getState(), dto.getUserId());
        }
    }

    @Override
    public void addApproveByProcess(String id, Integer state) {
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
        identifier.setUpdateTime(new Date());
        this.updateById(identifier);

        if(state.equals(YesNo.NO)){
            this.addIdentifier(identifier);
        }else {
            this.checkUtil.removeUserToken(identifier.getUserId());
            this.pullMq(identifier.getUserId(),null,null);
        }
    }

    @Override
    public void addApprove(String id, Integer state,  UserVo user) {
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
        this.updateIdentifier(identifier,user.getUserId());
        if(state.equals(YesNo.NO)){
            this.addIdentifier(identifier);
        }else {
            this.checkUtil.removeUserToken(identifier.getUserId());
            this.pullMq(identifier.getUserId(),null,null);
        }
        //同意或者拒绝后，终止掉流程
        if(DataUtil.isNotEmpty(identifier.getProcessId())){
            StopProcessDto param= new StopProcessDto();
            param.setProcessInstanceId(identifier.getProcessId());
            param.setUserId(user.getUserId());
            param.setUserName(identifier.getUpdateUsername());
            com.zerody.flow.api.dto.base.DataResult<?> dataResult = processServerFeignService.terminateProcessInner(param);
            if(!dataResult.isSuccess()){
                log.error("审核错误——：{}", dataResult.getMessage());
                throw new DefaultException(dataResult.getMessage());
            }
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
        this.pullMq(identifier.getUserId(),null,null);
        //后台直接解绑，终止掉流程
        if(DataUtil.isNotEmpty(identifier.getProcessId())&&ApproveStatusEnum.APPROVAL.name().equals(identifier.getApproveState())){
            StopProcessDto param= new StopProcessDto();
            param.setProcessInstanceId(identifier.getProcessId());
            param.setUserId(updateUserId);
            param.setUserName(identifier.getUpdateUsername());
            com.zerody.flow.api.dto.base.DataResult<?> dataResult = processServerFeignService.terminateProcessInner(param);
            if(!dataResult.isSuccess()){
                log.error("解绑流程错误——：{}", dataResult.getMessage());
                throw new DefaultException(dataResult.getMessage());
            }
        }
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
        queryWrapper.lambda().orderByDesc(SysUserIdentifier :: getState,SysUserIdentifier:: getApplyTime);
        Page<SysUserIdentifier> page = new Page<>();
        page.setCurrent(queryDto.getCurrent());
        page.setSize(queryDto.getPageSize());
        Page<SysUserIdentifier> identifierPage = this.page(page,queryWrapper);
        // 4/22 之前使用的是创建时间 兼容之前的数据 所以目前需要将申请时间赋值给创建时间字段
        identifierPage.getRecords().forEach( i -> {
            if(Objects.nonNull(i.getApplyTime())) {
                i.setCreateTime(i.getApplyTime());
            }
        });
        return identifierPage;
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
