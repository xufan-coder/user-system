package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.zerody.user.util.StaffInfoUtil;
import com.zerody.user.vo.LeaveUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        object.put("message",msg);
        object.put("verificationState",0);
        //判断同公司的
        LeaveUserInfoVo leave = sysStaffInfoMapper.getLeaveUserByCard(param.getCertificateCard(),param.getCompanyId());
        if(leave != null){
            msg = "该伙伴原签约["+leave.getCompanyName() +" + "+ leave.getDepartName()+"]，" +
                    "请联系即将签约团队的团队长在CRM-APP【伙伴签约申请】发起签约！（暂不支持行政办理二次签约）";
            object.put("message",msg);
            object.put("verificationState",1);
            return object;
        }
        // 判断跨公司的
        leave = sysStaffInfoMapper.getLeaveUserByCard(param.getCertificateCard(),null);
        if(leave != null){
            msg = "该伙伴原签约["+leave.getCompanyName() +" + "+ leave.getDepartName()+"]，" +
                    "不允许直接办理二次入职，请联系行政发起审批!";
            object.put("message",msg);
            object.put("verificationState",2);
            return object;
        }
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
        record.setId(UUIDutils.getUUID32());
        this.save(record);

    }

    @Override
    public void doRenewInduction(UserInductionSplitRecord induction) {
        //离职旧用户并查询出旧用户的相关信息
        SetSysUserInfoDto setSysUserInfoDto = this.doOldUserInfo(induction);

        SysUserInfo sysUserInfo = new SysUserInfo();
        DataUtil.getKeyAndValue(sysUserInfo, setSysUserInfoDto);
        log.info("添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        //参数校验
        CheckUser.checkParam(sysUserInfo, setSysUserInfoDto.getFamilyMembers());
        //查看手机号或登录名是否被占用
        Boolean flag = sysUserInfoMapper.selectUserByPhone(sysUserInfo.getPhoneNumber());
        if (flag) {
            throw new DefaultException("该手机号已存在！");
        }
        // 修改时添加身份证唯一校验 不包含离职账户
        StaffInfoVo staffInfo = this.sysStaffInfoMapper.getUserByCertificateCard(sysUserInfo.getCertificateCard(),YesNo.NO);
        if (DataUtil.isNotEmpty(staffInfo)) {
            String hintContent = "该身份证号码已在“".concat(staffInfo.getCompanyName());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(staffInfo.getDepartmentName())) {
                hintContent = hintContent.concat("-");
                hintContent = hintContent.concat(staffInfo.getDepartmentName());
            }
            hintContent = hintContent.concat("”存在，请再次确认");
            throw new DefaultException(hintContent);
        }
        //效验通过保存用户信息
        sysUserInfo.setRegisterTime(new Date());
        log.info("添加用户入库参数--{}", JSON.toJSONString(sysUserInfo));
        sysUserInfo.setCreateTime(new Date());
        sysUserInfo.setCreateUser("系统");
        sysUserInfo.setStatus(StatusEnum.activity.getValue());
        String avatar = sysUserInfo.getAvatar();
        sysUserInfo.setIsEdit(YesNo.YES);
        //  设置token删除状态 添加默认不删除token
        sysUserInfo.setIsDeleted(YesNo.NO);
        //  设置修改名称状态 添加默认 没有修改
        sysUserInfo.setIsUpdateName(YesNo.NO);
        sysUserInfoMapper.insert(sysUserInfo);
        //用户信息保存添加登录信息
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SysLoginInfo logInfo = new SysLoginInfo();
        logInfo.setUserId(sysUserInfo.getId());
        logInfo.setMobileNumber(sysUserInfo.getPhoneNumber());
        logInfo.setNickname(sysUserInfo.getNickname());
        logInfo.setAvatar(avatar);
        //初始化密码加密
        String initPwd = SysStaffInfoService.getInitPwd();
        logInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));
        logInfo.setStatus(StatusEnum.activity.getValue());
//        logInfo.setCreateId(UserUtils.getUser().getUserId()); //内部调用接口无token
        log.info("添加用户后生成登录账户入库参数--{}", JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);

        //保存员工信息
        SysStaffInfo staff = new SysStaffInfo();
        staff.setAvatar(setSysUserInfoDto.getStaffAvatar());
        staff.setPassword(initPwd);
        staff.setRecommendId(setSysUserInfoDto.getRecommendId());
        staff.setRecommendType(setSysUserInfoDto.getRecommendType());
        staff.setIntegral(setSysUserInfoDto.getIntegral());
        staff.setUserName(sysUserInfo.getUserName());
        staff.setCompId(setSysUserInfoDto.getCompanyId());
        staff.setStatus(setSysUserInfoDto.getStatus());
        staff.setUserId(sysUserInfo.getId());
        staff.setEvaluate(setSysUserInfoDto.getEvaluate());
        staff.setResumeUrl(setSysUserInfoDto.getResumeUrl());
        log.info("添加员工入库参数--{}", JSON.toJSONString(staff));
        staff.setStatus(StatusEnum.activity.getValue());
        staff.setDeleted(YesNo.NO);
        staff.setDateJoin(setSysUserInfoDto.getDateJoin());
        staff.setWorkingYears(setSysUserInfoDto.getWorkingYears());
        this.sysStaffInfoService.saveOrUpdate(staff);

        StaffInfoVo staffInfoVo = new StaffInfoVo();
        staffInfoVo.setStaffId(staff.getId());
        staffInfoVo.setUserId(sysUserInfo.getId());
        //家庭成员
        this.familyMemberService.addBatchFamilyMember(setSysUserInfoDto.getFamilyMembers(), staffInfoVo);
        //履历
        this.userResumeService.saveOrUpdateBatchResume(setSysUserInfoDto.getUserResumes(), staffInfoVo);
        //添加关系
        if (DataUtil.isNotEmpty(setSysUserInfoDto.getStaffRelationDtoList())) {
            setSysUserInfoDto.getStaffRelationDtoList().forEach(item -> {
                item.setRelationStaffId(staff.getId());
                item.setRelationStaffName(sysUserInfo.getUserName());
                item.setRelationUserId(sysUserInfo.getId());
                item.setStaffUserId(sysUserInfo.getId());
                item.setDesc(item.getDescribe());
                sysStaffRelationService.addRelation(item);
            });
        }
        //荣耀记录
        if (Objects.nonNull(setSysUserInfoDto.getStaffHistoryHonor())) {
            setSysUserInfoDto.getStaffHistoryHonor().forEach(item -> {
                item.setType(StaffHistoryTypeEnum.HONOR.name());
                item.setStaffId(staff.getId());
                staffHistoryService.addStaffHistory(item);
            });
        }
        //惩罚记录
        if (Objects.nonNull(setSysUserInfoDto.getStaffHistoryPunishment())) {
            setSysUserInfoDto.getStaffHistoryPunishment().forEach(item -> {
                item.setType(StaffHistoryTypeEnum.PUNISHMENT.name());
                item.setStaffId(staff.getId());
                staffHistoryService.addStaffHistory(item);
            });
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
        StaffInfoUtil.saveImage(setSysUserInfoDto.getDiplomas(), sysUserInfo.getId(), ImageTypeInfo.DIPLOMA);
        StaffInfoUtil.saveImage(setSysUserInfoDto.getComplianceCommitments(), sysUserInfo.getId(), ImageTypeInfo.COMPLIANCE_COMMITMENT);
        //合作申请表
        StaffInfoUtil.saveFile(setSysUserInfoDto.getCooperationFiles(), sysUserInfo.getId(), FileTypeInfo.COOPERATION_FILE);

        //部门
        if (StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())) {
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
        }

        //推送app账户
        appUserPushService.doPushAppUser(sysUserInfo.getId(), setSysUserInfoDto.getCompanyId());

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

        QueryWrapper<CommonFile> commonFileQw = new QueryWrapper<>();
        commonFileQw.lambda().eq(CommonFile::getConnectId, param.getLeaveUserId());
        commonFileQw.lambda().eq(CommonFile::getFileType, FileTypeInfo.COOPERATION_FILE);
        userInfoDto.setCooperationFiles(this.commonFileService.list(commonFileQw));

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
