package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcoon.transform.starter.Transformer;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.MQ;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.mq.RabbitMqService;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.customer.api.service.ClewRemoteService;
import com.zerody.sms.feign.SmsFeignService;
import com.zerody.user.api.dto.UserCopyDto;
import com.zerody.user.api.dto.mq.StaffDimissionInfo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.api.vo.UserCopyResultVo;
import com.zerody.user.check.CheckUser;
import com.zerody.user.constant.FileTypeInfo;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.domain.*;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.*;
import com.zerody.user.enums.ApproveStatusEnum;
import com.zerody.user.enums.StaffHistoryTypeEnum;
import com.zerody.user.feign.*;
import com.zerody.user.mapper.*;
import com.zerody.user.service.*;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.util.CommonUtils;
import com.zerody.user.util.StaffInfoUtil;
import com.zerody.user.vo.*;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kuang
 */
@Slf4j
@Service
public class UserInductionSplitRecordServiceImpl extends ServiceImpl<UserInductionSplitRecordMapper,UserInductionSplitRecord>
        implements UserInductionSplitRecordService {

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;
    @Autowired
    private UnionRoleStaffMapper unionRoleStaffMapper;

    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @Autowired
    private UnionStaffDepartMapper unionStaffDepartMapper;

    @Autowired
    private OauthFeignService oauthFeignService;

    @Autowired
    private AppUserPushService appUserPushService;

    @Autowired
    private StaffHistoryService staffHistoryService;
    @Autowired
    private SysStaffRelationService sysStaffRelationService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private UserResumeService userResumeService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommonFileService commonFileService;

    @Override
    public JSONObject verification(UserInductionVerificationDto param) {
        JSONObject object = new JSONObject();
        String msg = "";
        //判断同公司的
        LeaveUserInfoVo leave = sysStaffInfoMapper.getLeaveUserByCard(param.getCertificateCard(),param.getMobile(),param.getCompanyId());
        if(leave != null){
            msg = "该伙伴原签约["+leave.getCompanyName() +" + "+ (leave.getDepartName() ==null ? "" : leave.getDepartName() )+"]，" +
                    "请联系即将签约团队的团队长在CRM-APP【伙伴签约申请】发起签约！（暂不支持行政办理二次签约）";
            object.put("message",msg);
            object.put("verificationState",1);
            return object;
        }
        // 判断跨公司的
        leave = sysStaffInfoMapper.getLeaveUserByCard(param.getCertificateCard(),param.getMobile(),null);
        if(leave != null){
            msg = "该伙伴原签约["+leave.getCompanyName() +" + "+  (leave.getDepartName() ==null ? "" : leave.getDepartName() )+"]，" +
                    "不允许直接办理二次入职，请联系行政发起审批!";
            UserInductionVerificationVo verificationVo = new UserInductionVerificationVo();
            StaffInfoVo staff  =  this.sysStaffInfoService.getStaffInfo(leave.getUserId());
            verificationVo.setLeaveUserId(staff.getUserId());
            verificationVo.setLeaveUserName(staff.getUserName());
            verificationVo.setMobile(staff.getMobile());
            verificationVo.setMobileHide(CommonUtils.mobileEncrypt(staff.getMobile()));
            verificationVo.setCertificateCard(staff.getIdentityCard());
            verificationVo.setCertificateCardHide(CommonUtils.idEncrypt(staff.getIdentityCard()));
            verificationVo.setOldCompanyName(staff.getCompanyName());
            verificationVo.setOldDeptName(staff.getDepartmentName());
            verificationVo.setOldRoleName(staff.getRoleName());
            verificationVo.setMessage(msg);
            verificationVo.setVerificationState(2);
            return JSONObject.parseObject(JSONObject.toJSONString(verificationVo));
        }

        object.put("message",msg);
        object.put("verificationState",0);
        return object;
    }

    @Override
    public void addOrUpdateRecord(UserInductionSplitDto param) {

        //判断是否已在申请中
        QueryWrapper<UserInductionSplitRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(UserInductionSplitRecord::getLeaveUserId, param.getLeaveUserId());
        qw.lambda().eq(UserInductionSplitRecord::getApproveState, ApproveStatusEnum.APPROVAL.name());
        qw.lambda().eq(UserInductionSplitRecord::getDeleted, YesNo.NO);
        if(DataUtil.isNotEmpty(this.getOne(qw))){
            throw  new DefaultException("该伙伴正在申请中!");
        }

        //  通过手机号 或者身份证号判断是否有因为调职在其他公司任职的伙伴账号
        StaffInfoVo staff  =  this.sysStaffInfoService.getStaffInfo(param.getLeaveUserId());
        SysStaffInfo info = this.sysStaffInfoService.getById(staff.getStaffId());

        Boolean isTrue = sysUserInfoMapper.getByMobileOrCard(staff.getMobile() , staff.getIdentityCard());
        if(isTrue){
            throw new DefaultException("该伙伴已在其他公司任职!");
        }
        UserInductionSplitRecord record = new UserInductionSplitRecord();
        BeanUtils.copyProperties(param,record);

        //保存
        record.setOldCompanyId(staff.getCompanyId());
        record.setOldCompanyName(staff.getCompanyName());
        record.setOldDeptId(staff.getDepartId());
        record.setOldDeptName(staff.getDepartmentName());
        record.setOldRoleId(staff.getRoleId());
        record.setOldRoleName(staff.getRoleName());
        record.setLeaveTime(info.getDateLeft());
        record.setLeaveReason(info.getLeaveReason());
        record.setCreateTime(new Date());
        record.setApproveState(ApproveStatusEnum.APPROVAL.name());
        record.setDeleted(YesNo.NO);
        record.setCreateBy(param.getUserId());
        record.setCertificateCard(staff.getIdentityCard());
        record.setLeaveUserName(staff.getUserName());
        record.setMobile(staff.getMobile());
        record.setId(UUIDutils.getUUID32());
        this.save(record);

    }

    @Override
    public void doRenewInduction(UserInductionSplitRecord induction) {

        induction.setApproveState(ApproveStatusEnum.SUCCESS.name());
        this.updateById(induction);

        //离职旧用户并查询出旧用户的相关信息
        SetSysUserInfoDto setSysUserInfoDto = this.doOldUserInfo(induction);

        SysUserInfo sysUserInfo = new SysUserInfo();
        DataUtil.getKeyAndValue(sysUserInfo, setSysUserInfoDto);
        log.info("添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        //参数校验
        CheckUser.checkParam(sysUserInfo, setSysUserInfoDto.getFamilyMembers());

        //初始化密码加密
        String initPwd = SysStaffInfoService.getInitPwd();
        // 新增用户信息
        StaffInfoUtil.saveSysUserInfo(sysUserInfo,initPwd);

        //保存员工信息
        SysStaffInfo staff = new SysStaffInfo();
        staff.setAvatar(setSysUserInfoDto.getStaffAvatar());
        staff.setPassword(initPwd);
        staff.setRecommendId(setSysUserInfoDto.getRecommendId());
        staff.setRecommendType(setSysUserInfoDto.getRecommendType());
        staff.setIntegral(setSysUserInfoDto.getIntegral());
        staff.setUserName(sysUserInfo.getUserName());
        staff.setCompId(setSysUserInfoDto.getCompanyId());
        staff.setUserId(sysUserInfo.getId());
        staff.setEvaluate(setSysUserInfoDto.getEvaluate());
        staff.setResumeUrl(setSysUserInfoDto.getResumeUrl());
        log.info("添加员工入库参数--{}", JSON.toJSONString(staff));
        staff.setStatus(StatusEnum.activity.getValue());
        staff.setDeleted(YesNo.NO);
        staff.setDateJoin(setSysUserInfoDto.getDateJoin());
        staff.setWorkingYears(setSysUserInfoDto.getWorkingYears());
        this.sysStaffInfoService.saveOrUpdate(staff);

        //成员关系处理 添加关系 ,荣耀记录,惩罚记录
        StaffInfoUtil.saveRelation(setSysUserInfoDto,sysUserInfo,staff);

        // 用户扩展信息新增 家庭成员 履历 学历证书 合规承诺书 合作申请表
        StaffInfoUtil.saveExpandInfo(setSysUserInfoDto,sysUserInfo.getId(),staff.getId());

        //部门
        if (StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())) {
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
        }

        if (StringUtils.isNotEmpty(setSysUserInfoDto.getRoleId())) {
            //角色
            UnionRoleStaff rs = new UnionRoleStaff();
            rs.setStaffId(staff.getId());
            rs.setRoleId(setSysUserInfoDto.getRoleId());
            //去查询角色名
            DataResult<?> result = oauthFeignService.getRoleById(setSysUserInfoDto.getRoleId());
            if (!result.isSuccess()) {
                throw new DefaultException("服务异常！");
            }
            JSONObject obj = (JSONObject) JSON.toJSON(result.getData());
            rs.setRoleName(obj.get("roleName").toString());
            unionRoleStaffMapper.insert(rs);
        }

        //推送app账户
        appUserPushService.doPushAppUser(sysUserInfo.getId(), setSysUserInfoDto.getCompanyId());

    }

    @Override
    public List<UserInductionGroupRecordVo> getInductionPage(String userId) {
        QueryWrapper<UserInductionSplitRecord> qw = new QueryWrapper<>();
        qw.lambda().eq(UserInductionSplitRecord::getUserId,userId);
        qw.lambda().orderByDesc(UserInductionSplitRecord::getCreateTime);
        List<UserInductionSplitRecord> list = this.list(qw);
        return list.stream().map(u -> {
            UserInductionGroupRecordVo vo = new UserInductionGroupRecordVo();
            BeanUtils.copyProperties(u,vo);
            vo.setApproveName(u.getLeaveUserName());
            vo.setApproveTime(u.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public UserInductionGroupRecordInfoVo getInductionInfo(String id) {
        UserInductionSplitRecord info = this.getById(id);
        if(info == null) {
            throw  new DefaultException("未找到相关入职申请");
        }
        UserInductionGroupRecordInfoVo infoVo = new UserInductionGroupRecordInfoVo();
        BeanUtils.copyProperties(info,infoVo);
        infoVo.setCompanyId(info.getSignCompanyId());
        infoVo.setDepartId(info.getSignDeptId());
        infoVo.setDepartName(info.getSignDept());
        infoVo.setRoleId(info.getSignRoleId());
        infoVo.setRoleName(info.getSignRole());
        infoVo.setSignReason(info.getSignReason());
        LeaveUserInfoVo leaveInfo = sysStaffInfoMapper.getLeaveUserInfo(infoVo.getLeaveUserId());
        infoVo.setLeaveInfo(leaveInfo);
        return infoVo;
    }

    private SetSysUserInfoDto doOldUserInfo(UserInductionSplitRecord param) {
        //查询出需要复制的参数
        SetSysUserInfoDto userInfoDto = this.sysStaffInfoMapper.getUserInfoByUserId(param.getLeaveUserId());
        userInfoDto.setDepartId(param.getSignDeptId());
        userInfoDto.setRoleId(param.getSignRoleId());
        userInfoDto.setCompanyId(param.getSignCompanyId());

        //查询家庭成员
        QueryWrapper<FamilyMember> familyQw = new QueryWrapper<>();
        familyQw.lambda().eq(FamilyMember::getUserId, param.getLeaveUserId());
        List<FamilyMember> familys = familyMemberService.list(familyQw);
        userInfoDto.setFamilyMembers(familys);

        //荣耀记录
        QueryWrapper<StaffHistory> historyQw = new QueryWrapper<>();
        historyQw.lambda().eq(StaffHistory::getStaffId, userInfoDto.getStaffId());
        historyQw.lambda().eq(StaffHistory::getType, StaffHistoryTypeEnum.HONOR.name());
        List<StaffHistory> honors = this.staffHistoryService.list(historyQw);
        if (DataUtil.isNotEmpty(honors)) {
            List<StaffHistoryDto> honors2 = Transformer.toList(StaffHistoryDto.class).apply(honors).done();
            userInfoDto.setStaffHistoryHonor(honors2);
        }
        //惩罚记录
        historyQw = new QueryWrapper<>();
        historyQw.lambda().eq(StaffHistory::getStaffId, userInfoDto.getStaffId());
        historyQw.lambda().eq(StaffHistory::getType, StaffHistoryTypeEnum.PUNISHMENT.name());
        List<StaffHistory> punishments = this.staffHistoryService.list(historyQw);
        if (DataUtil.isNotEmpty(punishments)) {
            List<StaffHistoryDto> punishments2 = Transformer.toList(StaffHistoryDto.class).apply(punishments).done();
            userInfoDto.setStaffHistoryPunishment(punishments2);
        }
        //个人履历
        QueryWrapper<UserResume> resumeQw = new QueryWrapper<>();
        resumeQw.lambda().eq(UserResume::getUserId, param.getLeaveUserId());
        List<UserResume> resumes = this.userResumeService.list(resumeQw);
        userInfoDto.setUserResumes(resumes);

        //合规承诺书
        userInfoDto.setComplianceCommitments(imageService.getListImages(param.getLeaveUserId(), ImageTypeInfo.COMPLIANCE_COMMITMENT));
        //学历证书
        userInfoDto.setDiplomas(imageService.getListImages(param.getLeaveUserId(), ImageTypeInfo.DIPLOMA));
        //合作申请
        List<String> listImages = imageService.getListImages(param.getLeaveUserId(), ImageTypeInfo.COOPERATION_APPLY);
        if (DataUtil.isNotEmpty(listImages)) {
            userInfoDto.setCooperationImages(listImages);
        } else {
            QueryWrapper<CommonFile> commonFileQw = new QueryWrapper<>();
            commonFileQw.lambda().eq(CommonFile::getConnectId, param.getLeaveUserId());
            commonFileQw.lambda().eq(CommonFile::getFileType, FileTypeInfo.COOPERATION_FILE);
            List<CommonFile> list = this.commonFileService.list(commonFileQw);
            List<String> images = list.stream().map(CommonFile::getFileUrl).collect(Collectors.toList());
            userInfoDto.setCooperationImages(images);
        }

        QueryWrapper<SysStaffRelation> relationQw = new QueryWrapper<>();
        relationQw.lambda().eq(SysStaffRelation::getRelationUserId, param.getLeaveUserId());
        relationQw.lambda().eq(SysStaffRelation::getDeletd, YesNo.NO);
        List<SysStaffRelation> relations = this.sysStaffRelationService.list(relationQw);
        if (DataUtil.isNotEmpty(relations)) {
            List<SysStaffRelationDto> relationDtos = Transformer.toList(SysStaffRelationDto.class).apply(relations).done();
            userInfoDto.setStaffRelationDtoList(relationDtos);
        }
        return userInfoDto;
    }

}
