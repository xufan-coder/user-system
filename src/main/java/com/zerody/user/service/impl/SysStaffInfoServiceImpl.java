package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itcoon.transform.starter.Transformer;
import com.zerody.card.api.dto.UserCardDto;
import com.zerody.card.api.dto.UserCardReplaceDto;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.constant.CustomerQueryType;
import com.zerody.common.constant.MQ;
import com.zerody.common.constant.UserTypeInfo;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.enums.customer.MaritalStatusEnum;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.mq.RabbitMqService;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.PhoneHomeLocationUtils;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.common.utils.FileUtil;
import com.zerody.common.vo.IdCardDate;
import com.zerody.common.vo.UserVo;
import com.zerody.contract.api.vo.PerformanceReviewsVo;
import com.zerody.customer.api.dto.SetUserDepartDto;
import com.zerody.customer.api.dto.UserClewDto;
import com.zerody.customer.api.service.ClewRemoteService;
import com.zerody.log.api.constant.DataCodeType;
import com.zerody.log.api.constant.SystemCodeType;
import com.zerody.sms.api.dto.SmsDto;
import com.zerody.sms.feign.SmsFeignService;
import com.zerody.user.api.dto.UserCopyDto;
import com.zerody.user.api.dto.mq.StaffDimissionInfo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.api.vo.UserCopyResultVo;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.check.CheckUser;
import com.zerody.user.constant.FileTypeInfo;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.constant.ImportResultInfoType;
import com.zerody.user.domain.*;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.*;
import com.zerody.user.enums.*;
import com.zerody.user.feign.*;
import com.zerody.user.handler.user.SysUserDimissionHandle;
import com.zerody.user.mapper.*;
import com.zerody.user.service.*;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.util.*;
import com.zerody.user.vo.*;
import com.zerody.user.vo.dict.DictQuseryVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.FileUtil;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.check.CheckUser;
import com.zerody.user.enums.StaffStatusEnum;
import com.zerody.user.service.base.BaseService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoServiceImpl
 * @DateTime 2020/12/17_17:31
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysStaffInfoServiceImpl extends BaseService<SysStaffInfoMapper, SysStaffInfo> implements SysStaffInfoService {
    public static final String[] STAFF_EXCEL_TITTLE = new String[]{"姓名*", "手机号码*", "部门", "岗位", "角色*", "签约日期*", "推荐人手机号码" ,"状态", "性别", "籍贯", "民族", "婚姻", "出生年月日", "身份证号码*", "户籍地址", "居住地址", "电子邮箱", "学历", "毕业院校", "所学专业", "紧急联系人姓名", "紧急联系人电话", "紧急联系人关系", "家庭成员姓名", "家庭成员关系", "家庭成员电话","从事行业", "联系地址"};
    public static final String[] COMPANY_STAFF_EXCEL_TITTLE = new String[]{"姓名*", "手机号码*", "企业*", "部门", "岗位", "角色*", "签约日期*", "推荐人手机号码", "状态", "性别", "籍贯", "民族", "婚姻", "出生年月日", "身份证号码*", "户籍地址", "居住地址", "电子邮箱", "学历", "毕业院校", "所学专业", "紧急联系人姓名", "紧急联系人电话", "紧急联系人关系", "家庭成员姓名", "家庭成员关系", "家庭成员电话", "从事行业", "联系地址"};
    private static final String[] PERFORMANCE_REVIEWS_EXPORT_TITLE = {"企业名称", "部门", "角色", "姓名", "业绩收入", "回款笔数", "放款金额", "放款笔数", "签单金额", "签单笔数", "在审批金额", "审批笔数", "月份"};
    @Autowired
    private UnionRoleStaffMapper unionRoleStaffMapper;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Autowired
    private UnionStaffDepartMapper unionStaffDepartMapper;

    @Autowired
    private UnionStaffPositionMapper unionStaffPositionMapper;

    @Autowired
    private SysDepartmentInfoMapper sysDepartmentInfoMapper;

    @Autowired
    private SysCompanyInfoMapper sysCompanyInfoMapper;

    @Autowired
    private UnionStaffDeparService unionStaffDeparService;

    @Autowired
    private UnionStaffPositionService unionStaffPositionService;

    @Autowired
    private UnionRoleStaffService unionRoleStaffService;

    @Autowired
    private SysDepartmentInfoService sysDepartmentInfoService;
    @Autowired
    private SysJobPositionService sysJobPositionService;
    @Autowired
    private SysJobPositionMapper sysJobPositionMapper;
    @Autowired
    private OauthFeignService oauthFeignService;

    @Autowired
    private CompanyAdminMapper companyAdminMapper;

    @Autowired
    private CardUserInfoMapper cardUserInfoMapper;

    @Autowired
    private CardUserUnionCrmUserMapper cardUserUnionCrmUserMapper;

    @Autowired
    private ClewFeignService clewFeignService;

    @Autowired
    private CardFeignService cardFeignService;

    @Autowired
    private SmsFeignService smsFeignService;

    @Autowired
    private ClewRemoteService clewService;

    @Autowired
    private ContractFeignService contractService;

    @Autowired
    private CheckUtil checkUtil;

    @Autowired
    private RabbitMqService mqService;

    @Autowired
    private CustomerFeignService customerService;

    @Autowired
    private AppUserPushService appUserPushService;

    @Autowired
    private StaffBlacklistService staffBlacklistService;
    @Autowired
    private StaffHistoryService staffHistoryService;
    @Autowired
    private SysStaffRelationService sysStaffRelationService;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private ImportInfoService importInfoService;

    @Autowired
    private ImportResultInfoService importResultInfoService;

    @Autowired
    private FamilyMemberService familyMemberService;
    @Autowired
    private CeoUserInfoService ceoUserInfoService;

    @Autowired
    private DictService dictService;
    @Autowired
    private CeoCompanyRefService ceoCompanyRefService;

    @Autowired
    private PositionRecordService positionRecordService;

    @Autowired
    private UserResumeService userResumeService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommonFileService commonFileService;

    @Autowired
    private ConvertImageService convertImageService;

    @Autowired
    private BlacklistOperationRecordService blacklistOperationRecordService;

    @Autowired
    private PrepareExecutiveRecordService prepareExecutiveRecordService;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${sms.template.userTip:}")
    String userTipTemplate;

    @Value("${sms.sign.tsz:唐叁藏}")
    String smsSign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysStaffInfo addStaff(SetSysUserInfoDto setSysUserInfoDto) {
        // 注* 涉及到离职的修改时 查看 doCopyStaffInner 方法是否也需要修改
        SysUserInfo sysUserInfo = new SysUserInfo();
        DataUtil.getKeyAndValue(sysUserInfo, setSysUserInfoDto);
        log.info("添加伙伴入参---{}", JSON.toJSONString(sysUserInfo));

        //参数校验
        CheckUser.checkParam(sysUserInfo, setSysUserInfoDto.getFamilyMembers());
        //离职不校验参数
        if(DataUtil.isNotEmpty(setSysUserInfoDto.getStatus())){
            if(setSysUserInfoDto.getStatus()!=1){
                CheckUser.checkParamList(setSysUserInfoDto);
            }
        }
        //查看手机号或登录名是否被占用
        Boolean flag = sysUserInfoMapper.selectUserByPhone(sysUserInfo.getPhoneNumber());
        if (flag) {
            throw new DefaultException("该手机号已存在！");
        }

        if(StringUtils.isNotEmpty(setSysUserInfoDto.getTerminals()) &&
                SystemCodeType.SYSTEM_CRM_PC.equals(setSysUserInfoDto.getTerminals())){
            //判断同公司的
            LeaveUserInfoVo leave = sysStaffInfoMapper.getLeaveUserByCard(setSysUserInfoDto.getCertificateCard(),
                    setSysUserInfoDto.getPhoneNumber(),setSysUserInfoDto.getCompanyId());
            if(leave != null){
                throw new DefaultException("该伙伴原签约["+leave.getCompanyName() +" + "+ leave.getDepartName()+"]，" +
                        "请联系即将签约团队的团队长在CRM-APP【伙伴签约申请】发起签约！（暂不支持行政办理二次签约）");
            }
            // 判断跨公司的
            leave = sysStaffInfoMapper.getLeaveUserByCard(setSysUserInfoDto.getCertificateCard(),setSysUserInfoDto.getPhoneNumber(),null);
            if(leave != null){
                throw new DefaultException("该伙伴原签约["+leave.getCompanyName() +" + "+ leave.getDepartName()+"]，" +
                        "不允许直接办理二次入职，请联系行政发起审批!");
            }
        }

        // 新增时添加身份证唯一校验 包含离职账户
        StaffInfoVo staffInfo = this.sysStaffInfoMapper.getUserByCertificateCard(sysUserInfo.getCertificateCard(),YesNo.YES);
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
        sysUserInfo.setCreateUser(UserUtils.getUserName());
        sysUserInfo.setCreateId(UserUtils.getUserId());
        sysUserInfo.setStatus(StatusEnum.activity.getValue());
        //获取身份证年月日
        IdCardDate date = DateUtil.getIdCardDate(sysUserInfo.getCertificateCard());
        //月
        sysUserInfo.setBirthdayMonth(date.getMonth());
        //日
        sysUserInfo.setBirthdayDay(date.getDay());
        String avatar = sysUserInfo.getAvatar();
        sysUserInfo.setAvatar(null);
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
        logInfo.setAvatar(sysUserInfo.getAvatar());
        //初始化密码加密
        String initPwd = SysStaffInfoService.getInitPwd();
        logInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));
        logInfo.setStatus(StatusEnum.activity.getValue());
        logInfo.setCreateId(UserUtils.getUser().getUserId());
        log.info("添加用户后生成登录账户入库参数--{}", JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);
//        SmsDto smsDto = new SmsDto();
//        smsDto.setMobile(sysUserInfo.getPhoneNumber());
//        Map<String, Object> content = new HashMap<>();
//        content.put("userName", sysUserInfo.getPhoneNumber());
//        content.put("passWord", initPwd);
//        smsDto.setContent(content);
//        smsDto.setTemplateCode(userTipTemplate);
//        smsDto.setSign(smsSign);

        //保存员工信息
        SysStaffInfo staff = new SysStaffInfo();
        staff.setAvatar(avatar);
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
        staff.setDateLeft(setSysUserInfoDto.getDateLeft());
        staff.setLeaveReason(setSysUserInfoDto.getLeaveReason());
        log.info("添加员工入库参数--{}", JSON.toJSONString(staff));
        //staff.setStatus(StatusEnum.activity.getValue());
        staff.setDeleted(YesNo.NO);
        staff.setDateJoin(setSysUserInfoDto.getDateJoin());
        staff.setWorkingYears(setSysUserInfoDto.getWorkingYears());
        staff.setIsDiamondMember(setSysUserInfoDto.getIsDiamondMember());
        this.saveOrUpdate(staff);

        //成员关系处理 添加关系 ,荣耀记录,惩罚记录
        StaffInfoUtil.saveRelation(setSysUserInfoDto,sysUserInfo,staff);

        // 用户扩展信息新增 家庭成员 履历 学历证书 合规承诺书 合作申请表
        StaffInfoUtil.saveExpandInfo(setSysUserInfoDto,sysUserInfo.getId(),staff.getId());

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

        //岗位
        String positionName = null;
        if (StringUtils.isNotEmpty(setSysUserInfoDto.getPositionId())) {
            UnionStaffPosition sp = new UnionStaffPosition();
            sp.setPositionId(setSysUserInfoDto.getPositionId());
            sp.setStaffId(staff.getId());
            unionStaffPositionMapper.insert(sp);
            QueryWrapper<SysJobPosition> qw = new QueryWrapper<>();
            qw.lambda().eq(SysJobPosition::getId, setSysUserInfoDto.getPositionId())
                    .ne(BaseModel::getStatus, StatusEnum.deleted.getValue());
            SysJobPosition sysJobPosition = sysJobPositionMapper.selectOne(qw);
            if (DataUtil.isNotEmpty(sysJobPosition)) {
                positionName = sysJobPosition.getPositionName();
            }
        }
        //部门
        if (StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())) {
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
        }


        //获取企业信息的地址用于生成名片
        QueryWrapper<SysCompanyInfo> qw = new QueryWrapper<>();
        qw.lambda().eq(SysCompanyInfo::getId, setSysUserInfoDto.getCompanyId())
                .ne(BaseModel::getStatus, StatusEnum.deleted.getValue());
        SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);


        //添加员工即为内部员工需要生成名片小程序用户账号
        //生成基础名片信息
//        saveCardUser(sysUserInfo, logInfo, sysCompanyInfo, positionName);
//        //最后发送短信
//        smsFeignService.sendSms(smsDto);
        //推送app账户
        appUserPushService.doPushAppUser(sysUserInfo.getId(), setSysUserInfoDto.getCompanyId());

        UserLogUtil.addUserLog(sysUserInfo,UserUtils.getUser(),"签约伙伴", DataCodeType.PARTNER_ADD);
        return staff;
    }





    @Override
    public UserCopyResultVo doCopyStaffInner(UserCopyDto param) {
        //离职旧用户并查询出旧用户的相关信息
        PrepareExecutiveRecordVo prepareExecutiveRecordVo = this.prepareExecutiveRecordService.getPrepareExecutiveRecord(param.getOldUserId());
        SetSysUserInfoDto setSysUserInfoDto = this.doOldUserInfo(param);
        if (DataUtil.isNotEmpty(param.getReinstateId())) {
            SysUserInfo user = new SysUserInfo();
            user.setId(param.getReinstateId());
            user.setStatus(YesNo.NO);
            user.setIsEdit(YesNo.YES);
            this.sysUserInfoService.updateById(user);
            PrepareExecutiveRecordVo prepareExecutiveRecord = this.prepareExecutiveRecordService.getPrepareExecutiveRecordInner(user.getId());
            if(DataUtil.isNotEmpty(prepareExecutiveRecord)) {
                prepareExecutiveRecord.setIsPrepareExecutive(2);
                PrepareExecutiveRecord record = new PrepareExecutiveRecord();
                BeanUtils.copyProperties(prepareExecutiveRecord,record);
                this.prepareExecutiveRecordService.updateById(record);
                user.setIsPrepareExecutive(YesNo.YES);
            }
            UpdateWrapper<SysStaffInfo> staffUw = new UpdateWrapper<>();
            staffUw.lambda().eq(SysStaffInfo::getUserId, param.getReinstateId());
            staffUw.lambda().set(SysStaffInfo::getStatus, YesNo.NO);
            staffUw.lambda().set(SysStaffInfo::getDateLeft, null);
            this.update(staffUw);
            UserCopyResultVo result = new UserCopyResultVo();
            result.setUserId(param.getReinstateId());
            //删除token
            this.checkUtil.removeUserToken(param.getOldUserId());
            //转移后原公司伙伴的客户给上级
            StaffDimissionInfo staffDimissionInfo = new StaffDimissionInfo();
            staffDimissionInfo.setUserId(param.getOldUserId());
            staffDimissionInfo.setOperationUserId("系统");
            staffDimissionInfo.setOperationUserName("系统");
            //mq发送离职客户转移
            this.mqService.send(staffDimissionInfo, MQ.QUEUE_STAFF_DIMISSION);
            return result;
        }
        SysUserInfo sysUserInfo = new SysUserInfo();
        DataUtil.getKeyAndValue(sysUserInfo, setSysUserInfoDto);
        log.info("添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        //参数校验
        CheckUser.checkParam(sysUserInfo, setSysUserInfoDto.getFamilyMembers());

        //初始化密码加密
        String initPwd = SysStaffInfoService.getInitPwd();
        //获取旧账号之前的状态
        // 新增用户信息
        if(DataUtil.isNotEmpty(prepareExecutiveRecordVo)){
            sysUserInfo.setIsPrepareExecutive(prepareExecutiveRecordVo.getIsPrepareExecutive());
        }
//        SmsDto smsDto = new SmsDto();
//        smsDto.setMobile(sysUserInfo.getPhoneNumber());
//        Map<String, Object> content = new HashMap<>();
//        content.put("userName", sysUserInfo.getPhoneNumber());
//        content.put("passWord", initPwd);
//        smsDto.setContent(content);
//        smsDto.setTemplateCode(userTipTemplate);
//        smsDto.setSign(smsSign);

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
        this.saveOrUpdate(staff);
        //成员关系处理 添加关系 ,荣耀记录,惩罚记录
        StaffInfoUtil.saveRelation(setSysUserInfoDto,sysUserInfo,staff);

        log.info("用户id"+sysUserInfo.getId());
        log.info("员工id"+staff.getId());
        // 用户扩展信息新增 家庭成员 履历 学历证书 合规承诺书 合作申请表
        StaffInfoUtil.saveExpandInfo(setSysUserInfoDto,sysUserInfo.getId(),staff.getId());

        String roleName=null;
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
            roleName=obj.get("roleName").toString();
            unionRoleStaffMapper.insert(rs);
        }
        //岗位
        String positionName = null;
        if (StringUtils.isNotEmpty(setSysUserInfoDto.getPositionId())) {
            UnionStaffPosition sp = new UnionStaffPosition();
            sp.setPositionId(setSysUserInfoDto.getPositionId());
            sp.setStaffId(staff.getId());
            unionStaffPositionMapper.insert(sp);
            QueryWrapper<SysJobPosition> qw = new QueryWrapper<>();
            qw.lambda().eq(SysJobPosition::getId, setSysUserInfoDto.getPositionId())
                    .ne(BaseModel::getStatus, StatusEnum.deleted.getValue());
            SysJobPosition sysJobPosition = sysJobPositionMapper.selectOne(qw);
            if (DataUtil.isNotEmpty(sysJobPosition)) {
                positionName = sysJobPosition.getPositionName();
            }
        }
        //部门
        if (StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())) {
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
        }
        //获取企业信息的地址用于生成名片
        QueryWrapper<SysCompanyInfo> qw = new QueryWrapper<>();
        qw.lambda().eq(SysCompanyInfo::getId, setSysUserInfoDto.getCompanyId())
                .ne(BaseModel::getStatus, StatusEnum.deleted.getValue());
        SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);


        StaffInfoUtil.saveSysUserInfo(sysUserInfo,initPwd);
        //新用户 预备高管状态变更
        if(DataUtil.isNotEmpty(prepareExecutiveRecordVo)){
            if(DataUtil.isNotEmpty(prepareExecutiveRecordVo.getIsPrepareExecutive()) && prepareExecutiveRecordVo.getIsPrepareExecutive()==1) {
                PrepareExecutiveRecord record = new PrepareExecutiveRecord();
                record.setUserId(sysUserInfo.getId());
                record.setUserName(sysUserInfo.getUserName());
                record.setCompanyId(setSysUserInfoDto.getCompanyId());
                record.setCompanyName(sysCompanyInfo.getCompanyName());
                record.setRoleName(roleName);
                record.setRoleId(setSysUserInfoDto.getRoleId());
                record.setUserId(sysUserInfo.getId());
                record.setIsPrepareExecutive(1);
                record.setEnterDate(new Date());
                this.prepareExecutiveRecordService.save(record);
            }
        }
        //添加员工即为内部员工需要生成名片小程序用户账号
        //生成基础名片信息
//        saveCardUser(sysUserInfo, logInfo, sysCompanyInfo, positionName);
//        //最后发送短信
//        smsFeignService.sendSms(smsDto);
        //推送app账户
        appUserPushService.doPushAppUser(sysUserInfo.getId(), setSysUserInfoDto.getCompanyId());
        //最后 本公司离职客户给副总
        StaffDimissionInfo staffDimissionInfo = new StaffDimissionInfo();
        staffDimissionInfo.setUserId(param.getOldUserId());
        staffDimissionInfo.setOperationUserId("系统");
        staffDimissionInfo.setOperationUserName("系统");
        //mq发送离职客户转移
        this.mqService.send(staffDimissionInfo, MQ.QUEUE_STAFF_DIMISSION);
        UserCopyResultVo copyResult = new UserCopyResultVo();
        copyResult.setUserId(sysUserInfo.getId());
        this.checkUtil.removeUserToken(param.getOldUserId());
        return copyResult;
    }


    @Override
    public IPage<BosStaffInfoVo> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto) {
        if (StringUtils.isNotEmpty(sysStaffInfoPageDto.getCompanyId())) {
            //  如果企业id不为空 则查询该企业的负责人
            QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
            adminQw.lambda().eq(CompanyAdmin::getCompanyId, sysStaffInfoPageDto.getCompanyId());
            CompanyAdmin admin = this.companyAdminMapper.selectOne(adminQw);
            //  获取该企业负责人
            sysStaffInfoPageDto.setStaffId(DataUtil.isEmpty(admin) ? null : admin.getStaffId());
        }
        if (StringUtils.isNotEmpty(sysStaffInfoPageDto.getDepartId())) {
            //  获取得到部门负责人
            SysDepartmentInfo departInfo = this.sysDepartmentInfoMapper.selectById(sysStaffInfoPageDto.getDepartId());
            sysStaffInfoPageDto.setStaffId(departInfo.getAdminAccount());
        }
        IPage<BosStaffInfoVo> infoVoIPage = new Page<>(sysStaffInfoPageDto.getCurrent(), sysStaffInfoPageDto.getPageSize());
        IPage<BosStaffInfoVo> vo = sysStaffInfoMapper.getPageAllStaff(sysStaffInfoPageDto, infoVoIPage);
        return vo;
    }

    @Override
    public IPage<BosStaffInfoVo> getPageAllSuperiorStaff(SysStaffInfoPageDto sysStaffInfoPageDto) {
        IPage<BosStaffInfoVo> infoVoIPage = new Page<>(sysStaffInfoPageDto.getCurrent(), sysStaffInfoPageDto.getPageSize());
        List<String> depIds = SetSuperiorIdUtil.getSuperiorIds(sysStaffInfoPageDto.getDepartId());
        return sysStaffInfoMapper.getPageAllSuperiorStaff(sysStaffInfoPageDto, infoVoIPage, depIds);
    }

    @Override
    public void updateStaffStatus(String userId, Integer status, String leaveReason,UserVo user) {
        if (StringUtils.isEmpty(userId)) {
            throw new DefaultException("用户idid不能为空");
        }
        if (status == null) {
            throw new DefaultException("状态不能为空");
        }

        StaffInfoVo leaveUser = getStaffInfo(userId);
        SysUserInfo oldUserInfo = sysUserInfoMapper.selectById(userId);
        SysUserInfo userInfo = new SysUserInfo();
        userInfo.setId(userId);
        userInfo.setStatus(status);
        userInfo.setIsEdit(YesNo.YES);
        this.sysUserInfoMapper.updateById(userInfo);
        UpdateWrapper<SysStaffInfo> staffUw = new UpdateWrapper<>();
        staffUw.lambda().set(SysStaffInfo::getStatus, status);
        staffUw.lambda().eq(SysStaffInfo::getUserId, userId);
        staffUw.lambda().set(SysStaffInfo::getLeaveReason, leaveReason);
        this.update(staffUw);
        if (StatusEnum.stop.getValue() == status.intValue() ) {
            StaffDimissionInfo staffDimissionInfo = new StaffDimissionInfo();
            staffDimissionInfo.setUserId(userId);
            //添加一条任职记录
            PositionRecord positionRecord = new PositionRecord();
            positionRecord.setId(UUIDutils.getUUID32());
            positionRecord.setCertificateCard(leaveUser.getIdentityCard());
            positionRecord.setCompanyId(leaveUser.getCompanyId());
            positionRecord.setCompanyName(leaveUser.getCompanyName());
            positionRecord.setUserId(leaveUser.getUserId());
            positionRecord.setUserName(leaveUser.getUserName());
            positionRecord.setPositionTime(new Date());
            positionRecord.setCreateTime(new Date());
            positionRecord.setRoleName(leaveUser.getRoleName());
            positionRecord.setQuitTime(new Date());
            positionRecord.setQuitReason(leaveReason);
            positionRecordService.save(positionRecord);
            appUserPushService.updateById(SysUserDimissionHandle.staffDimissionPush(userId));
            this.mqService.send(staffDimissionInfo, MQ.QUEUE_STAFF_DIMISSION);
        }
        if (StatusEnum.stop.getValue() == status.intValue() || StatusEnum.deleted.getValue() == status.intValue()) {
            this.checkUtil.removeUserToken(userId);
        }
        UserLogUtil.addUserLog(oldUserInfo ,user ,status, DataCodeType.PARTNER_MODIFY);
    }


    @Override
    @Transactional
    public void updateStaff(SetSysUserInfoDto setSysUserInfoDto, UserVo user,boolean isTraverse) throws ParseException, IllegalAccessException {
        // 注* 涉及到离职的修改时 查看 doCopyStaffInner 方法是否也需要修改
        boolean removeToken = true;
        if (setSysUserInfoDto.getStatus().intValue() == StatusEnum.stop.getValue() && StringUtils.isEmpty(setSysUserInfoDto.getLeaveReason())) {
            throw new DefaultException("离职原因不能为空");
        }
        log.info("修改用户信息  ——> 入参：{}, 操作者信息：{}", JSON.toJSONString(setSysUserInfoDto), JSON.toJSONString(UserUtils.getUser()));
        SysUserInfo oldUserInfo = sysUserInfoMapper.selectById(setSysUserInfoDto.getId());
        SysUserInfo sysUserInfo = new SysUserInfo();
        DataUtil.getKeyAndValue(sysUserInfo, setSysUserInfoDto);
        sysUserInfo.setId(setSysUserInfoDto.getId());
        //参数校验
        CheckUser.checkParam(sysUserInfo, setSysUserInfoDto.getFamilyMembers());
        //离职不校验参数
        if(setSysUserInfoDto.getStatus()!=1){
            CheckUser.checkParamList(setSysUserInfoDto);
        }
        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(sysUserInfo);
        if (users != null && users.size() > 0) {
            throw new DefaultException("手机号或用户名被占用");
        }
        if (!oldUserInfo.getStatus().equals(setSysUserInfoDto.getStatus())) {
            sysUserInfo.setStatusEdit(YesNo.YES);
        }
        SysUserDimissionHandle.isUpdateDimission(user, setSysUserInfoDto.getId(), oldUserInfo.getStatus(), setSysUserInfoDto.getStatus());
        //修改离职伙伴为在职时判断
        if(oldUserInfo.getStatus() == 1 && !oldUserInfo.getStatus().equals(setSysUserInfoDto.getStatus())) {
            // 并且是pc端操作
            if(StringUtils.isNotEmpty(setSysUserInfoDto.getTerminals()) &&
                    SystemCodeType.SYSTEM_CRM_PC.equals(setSysUserInfoDto.getTerminals())){
                //判断同公司的
                LeaveUserInfoVo leave = sysStaffInfoMapper.getLeaveUserByCard(oldUserInfo.getCertificateCard(),oldUserInfo.getPhoneNumber(),setSysUserInfoDto.getCompanyId());
                if(leave != null){
                    throw new DefaultException("该伙伴原签约["+leave.getCompanyName() +" + "+ leave.getDepartName()+"]，" +
                            "请联系即将签约团队的团队长在CRM-APP【伙伴签约申请】发起签约！（暂不支持行政办理二次签约）");
                }
                // 判断跨公司的
                leave = sysStaffInfoMapper.getLeaveUserByCard(oldUserInfo.getCertificateCard(),oldUserInfo.getPhoneNumber(),null);
                if(leave != null){
                    throw new DefaultException("该伙伴原签约["+leave.getCompanyName() +" + "+ leave.getDepartName()+"]，" +
                            "不允许直接办理二次入职，请联系行政发起审批!");
                }
            }
        }
        if(isTraverse){
            BlacklistOperationRecordAddDto operationRecord = new BlacklistOperationRecordAddDto();
            operationRecord.setMobile(oldUserInfo.getPhoneNumber());
            operationRecord.setIdentityCard(oldUserInfo.getCertificateCard());
            operationRecord.setType(1);
            operationRecord.setRemarks("修改伙伴信息");
            blacklistOperationRecordService.addBlacklistOperationRecord(operationRecord,user);
        }
        //判断离职用户 是否是预备高管 如果是 则退学
        if(StatusEnum.stop.getValue().equals(setSysUserInfoDto.getStatus())){
            PrepareExecutiveRecordVo prepareExecutiveRecord = this.prepareExecutiveRecordService.getPrepareExecutiveRecordInner(setSysUserInfoDto.getId());
            if(DataUtil.isNotEmpty(prepareExecutiveRecord)){
                if(DataUtil.isEmpty(setSysUserInfoDto.getDateLeft()) ||
                        prepareExecutiveRecord.getEnterDate().after(setSysUserInfoDto.getDateLeft())){
                    throw new DefaultException("当前离职时间小于预备高管入学时间，不允许离职");
                }
                prepareExecutiveRecord.setOutDate(setSysUserInfoDto.getDateLeft());
                prepareExecutiveRecord.setOutReason(setSysUserInfoDto.getLeaveReason());
                prepareExecutiveRecord.setIsPrepareExecutive(2);
                PrepareExecutiveRecord record = new PrepareExecutiveRecord();
                BeanUtils.copyProperties(prepareExecutiveRecord,record);
                this.prepareExecutiveRecordService.updateById(record);
                sysUserInfo.setIsPrepareExecutive(2);

            }
        }
        // 修改时添加身份证唯一校验 不包含离职账户
        StaffInfoVo staffInfoIdCard = this.sysStaffInfoMapper.getUserByCertificateCard(sysUserInfo.getCertificateCard(),YesNo.NO);
        if (DataUtil.isNotEmpty(staffInfoIdCard) && !staffInfoIdCard.getUserId().equals(setSysUserInfoDto.getId())) {
            String hintContent = "该身份证号码已在“".concat(staffInfoIdCard.getCompanyName());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(staffInfoIdCard.getDepartmentName())) {
                hintContent = hintContent.concat("-");
                hintContent = hintContent.concat(staffInfoIdCard.getDepartmentName());
            }
            hintContent = hintContent.concat("”存在，请再次确认");
            throw new DefaultException(hintContent);
        }
        if(oldUserInfo.getCertificateCard().equals(setSysUserInfoDto.getCertificateCard())){
            UpdateWrapper<PositionRecord> wrapper = new UpdateWrapper<>();
            wrapper.lambda().eq(PositionRecord::getUserId,oldUserInfo.getId()).
                    set(PositionRecord::getCertificateCard,setSysUserInfoDto.getCertificateCard());
            this.positionRecordService.update(wrapper);
        }
        //效验通过保存用户信息
        log.info("修改员工信息入参-已通过校验:{}", JSON.toJSONString(setSysUserInfoDto));
        sysUserInfo.setUpdateTime(new Date());
        sysUserInfo.setUpdateUser(UserUtils.getUserName());
        sysUserInfo.setIsEdit(YesNo.YES);
        sysUserInfo.setUpdateId(UserUtils.getUserId());
        String avatar = setSysUserInfoDto.getAvatar();
        sysUserInfo.setAvatar(null);
        //  如果名称有修改就修改名称修改状态 用于定时任务发送MQ消息
        if (!oldUserInfo.getUserName().equals(setSysUserInfoDto.getUserName())) {
            sysUserInfo.setIsUpdateName(YesNo.YES);
            this.checkUtil.removeUserToken(sysUserInfo.getId());
            removeToken = !removeToken;
        }
        //获取身份证年月日
        IdCardDate date = DateUtil.getIdCardDate(sysUserInfo.getCertificateCard());
        //月
        sysUserInfo.setBirthdayMonth(date.getMonth());
        //日
        sysUserInfo.setBirthdayDay(date.getDay());
        sysUserInfoMapper.updateById(sysUserInfo);

        //判断身份证和手机号码是否被修改
        if(!oldUserInfo.getCertificateCard().equals(setSysUserInfoDto.getCertificateCard()) ||
                !oldUserInfo.getPhoneNumber().equals(setSysUserInfoDto.getPhoneNumber())){
            QueryWrapper<StaffBlacklist> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(StaffBlacklist::getUserId,setSysUserInfoDto.getId());
            wrapper.lambda().last("limit 0,1");
            StaffBlacklist blacklist = this.staffBlacklistService.getOne(wrapper);
            //如果该用户存在黑名单中，同时修改黑名单单的身份证和手机信息
            if(ObjectUtils.isNotEmpty(blacklist)){
                blacklist.setIdentityCard(setSysUserInfoDto.getCertificateCard());
                blacklist.setMobile(setSysUserInfoDto.getPhoneNumber());
                this.staffBlacklistService.updateById(blacklist);
            }
        }
        //处理null修改问题
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().set(SysUserInfo::getTrainNo, sysUserInfo.getTrainNo());
        userUw.lambda().eq(SysUserInfo::getId, sysUserInfo.getId());
        //处理null修改问题
        this.sysUserInfoService.update(userUw);

        QueryWrapper<SysLoginInfo> loginQW = new QueryWrapper<>();
        loginQW.lambda().eq(SysLoginInfo::getUserId, sysUserInfo.getId());
        SysLoginInfo logInfo = sysLoginInfoMapper.selectOne(loginQW);
        logInfo.setMobileNumber(sysUserInfo.getPhoneNumber());
        logInfo.setNickname(sysUserInfo.getNickname());
        logInfo.setAvatar(sysUserInfo.getAvatar());
        logInfo.setStatus(StatusEnum.activity.getValue());
        logInfo.setUpdateId(UserUtils.getUserId());
        sysLoginInfoService.addOrUpdateLogin(logInfo);
        //保存员工信息
        BlacklistOperationRecordAddDto operationRecord = new BlacklistOperationRecordAddDto();
        operationRecord.setMobile(setSysUserInfoDto.getPhoneNumber());
        operationRecord.setIdentityCard(setSysUserInfoDto.getCertificateCard());
        operationRecord.setType(1);
        operationRecord.setRemarks("修改用户");
        blacklistOperationRecordService.addBlacklistOperationRecord(operationRecord,user);
        //查询得到员工信息
        QueryWrapper<SysStaffInfo> staffQW = new QueryWrapper<>();
        staffQW.lambda().eq(SysStaffInfo::getUserId, sysUserInfo.getId());
        SysStaffInfo staff =  this.getOne(staffQW);
        // 对比伙伴信息
        List<UserCompar> staffList = UserCompareUtil.compareTwoClass(staff,setSysUserInfoDto);

        if (setSysUserInfoDto.getRecommendType().intValue() == 1 && StringUtils.isEmpty(setSysUserInfoDto.getRecommendId())) {
            throw new DefaultException("请选择推荐人");
        }
        if (setSysUserInfoDto.getRecommendType().intValue() == 1 && setSysUserInfoDto.getRecommendId().equals(staff.getId())) {
            throw new DefaultException("不能选择自己为推荐人");
        }
        //  员工为离职状态时 清除token
        if (StatusEnum.stop.getValue().equals(setSysUserInfoDto.getStatus()) && DataUtil.isEmpty(setSysUserInfoDto.getDateLeft())) {
            setSysUserInfoDto.setDateLeft(new Date());
        }

        staff.setRecommendId(setSysUserInfoDto.getRecommendId());
        staff.setRecommendType(setSysUserInfoDto.getRecommendType());
        staff.setIntegral(setSysUserInfoDto.getIntegral());
        staff.setStatus(setSysUserInfoDto.getStatus());
        staff.setAvatar(setSysUserInfoDto.getAvatar());
        staff.setUserName(setSysUserInfoDto.getUserName());
        staff.setResumeUrl(setSysUserInfoDto.getResumeUrl());
        staff.setEvaluate(setSysUserInfoDto.getEvaluate());
        staff.setLeaveReason(setSysUserInfoDto.getLeaveReason());
        staff.setDateJoin(setSysUserInfoDto.getDateJoin());//入职时间
        staff.setWorkingYears(setSysUserInfoDto.getWorkingYears());//在职年限
        staff.setDateLeft(setSysUserInfoDto.getDateLeft());//离职时间
        staff.setIsDiamondMember(setSysUserInfoDto.getIsDiamondMember());//是否是钻石会员
        if (DataUtil.isEmpty(setSysUserInfoDto.getRecommendType()) || setSysUserInfoDto.getRecommendType().intValue() == 0) {
            staff.setRecommendId("");
        }
        this.saveOrUpdate(staff);
        if (StatusEnum.activity.getValue().equals(setSysUserInfoDto.getStatus()) || StatusEnum.teamwork.getValue().equals(setSysUserInfoDto.getStatus())) {

            UpdateWrapper<SysStaffInfo> staffUw = new UpdateWrapper<>();
            staffUw.lambda().set(SysStaffInfo::getDateLeft, null);
            staffUw.lambda().eq(SysStaffInfo::getId, staff.getId());
            this.update(staffUw);
        }
        if(Objects.isNull(setSysUserInfoDto.getDateJoin())){
            if(StringUtils.isNotEmpty(staff.getId())) {
                this.sysStaffInfoMapper.updateDateJoin(staff.getId(),null);
            }
        }
        StaffInfoVo staffInfoVo = new StaffInfoVo();
        staffInfoVo.setStaffId(staff.getId());
        staffInfoVo.setUserId(sysUserInfo.getId());

        // 获取埋点数据内容
        List<String> contentList = new ArrayList<>();

        // 处理家庭关系新旧对比
        List<FamilyMember> familyList = familyMemberService.getFamilyList(staffInfoVo.getUserId());
        if(DataUtil.isNotEmpty(setSysUserInfoDto.getFamilyMembers()) && setSysUserInfoDto.getFamilyMembers().size()!=0){
            String familyStr = StaffHistoryUtil.updateFamily(familyList,setSysUserInfoDto.getFamilyMembers());
            if(StringUtils.isNotEmpty(familyStr)){
                contentList.add(familyStr);
            }
        }

        //处理个人履历新旧对比
        List<UserResume>  resumeList = this.userResumeService.getResumeList(staffInfoVo.getUserId());
        String resumeStr = StaffHistoryUtil.updateResume(resumeList,setSysUserInfoDto.getUserResumes());
        if(StringUtils.isNotEmpty(resumeStr)){
            contentList.add(resumeStr);
        }

        //处理学历证书新旧对比
        List<String> diplomas =  this.imageService.getListImages(staffInfoVo.getUserId(),ImageTypeInfo.DIPLOMA);
        String diplomasStr = StaffHistoryUtil.getDiplomas(diplomas,setSysUserInfoDto.getDiplomas(),"学历证书");
        if(StringUtils.isNotEmpty(diplomasStr)){
            contentList.add(diplomasStr);
        }


        // 处理合规承若书新旧对比
        diplomas =  this.imageService.getListImages(staffInfoVo.getUserId(),ImageTypeInfo.COMPLIANCE_COMMITMENT);
        diplomasStr = StaffHistoryUtil.getDiplomas(diplomas,setSysUserInfoDto.getComplianceCommitments(),"合规承若书");
        if(StringUtils.isNotEmpty(diplomasStr)){
            contentList.add(diplomasStr);
        }

        // 合作申请表新旧对比
        /*diplomas =  this.commonFileService.getListFiles(staffInfoVo.getUserId(),FileTypeInfo.COOPERATION_FILE);
        List<String> newFiles = new ArrayList<>();
        if(DataUtil.isNotEmpty(setSysUserInfoDto.getCooperationFiles())) {
            newFiles = setSysUserInfoDto.getCooperationFiles().stream().map(CommonFile::getFileUrl).collect(Collectors.toList());
        }

        diplomasStr = StaffHistoryUtil.getDiplomas(diplomas,newFiles,"合作申请表");
        if(StringUtils.isNotEmpty(diplomasStr)){
            contentList.add(diplomasStr);
        }*/
        diplomas =  this.imageService.getListImages(staffInfoVo.getUserId(),ImageTypeInfo.COOPERATION_APPLY);
        diplomasStr = StaffHistoryUtil.getDiplomas(diplomas,setSysUserInfoDto.getCooperationImages(),"合作申请表");
        if(StringUtils.isNotEmpty(diplomasStr)){
            contentList.add(diplomasStr);
        }

        // 用户扩展信息新增 家庭成员 履历 学历证书 合规承诺书 合作申请表
        StaffInfoUtil.saveExpandInfo(setSysUserInfoDto,sysUserInfo.getId(),staff.getId());

        //删除
        StaffHistoryQueryDto staffHistoryQueryDto = new StaffHistoryQueryDto();
        staffHistoryQueryDto.setStaffId(setSysUserInfoDto.getStaffId());
        staffHistoryQueryDto.setId(setSysUserInfoDto.getStaffId());


        //荣耀记录
        if (Objects.nonNull(setSysUserInfoDto.getStaffHistoryHonor()) && setSysUserInfoDto.getStaffHistoryHonor().size() > 0) {
            List<StaffHistoryDto> newHistoryList = setSysUserInfoDto.getStaffHistoryHonor();
            staffHistoryQueryDto.setType(StaffHistoryTypeEnum.HONOR.name());

            // 对比荣耀记录的 新增 编辑 删除操作
            List<StaffHistoryVo> historyVos = this.staffHistoryService.queryStaffHistory(staffHistoryQueryDto);
            String honor = StaffHistoryUtil.updateHistory(historyVos,newHistoryList,"荣耀");
            if(StringUtils.isNotEmpty(honor)){
                contentList.add(honor);
            }

            this.staffHistoryService.removeStaffHistory(staffHistoryQueryDto);
            setSysUserInfoDto.getStaffHistoryHonor().forEach(item -> {
                item.setType(StaffHistoryTypeEnum.HONOR.name());
                item.setStaffId(setSysUserInfoDto.getStaffId());
                this.staffHistoryService.modifyStaffHistory(item);
            });

        } else {
            staffHistoryQueryDto.setType(StaffHistoryTypeEnum.HONOR.name());
            StringBuilder honor = new StringBuilder();
            List<StaffHistoryVo> historyVos = this.staffHistoryService.queryStaffHistory(staffHistoryQueryDto);
            for(StaffHistoryVo oldHistory: historyVos){
                honor.append("删除了荣耀记录:[").append(oldHistory.getTime()).append("    ").append(oldHistory.getDescribe()).append("]    ");
            }
            if(StringUtils.isNotEmpty(honor.toString())){
                contentList.add(honor.toString());
            }

            this.staffHistoryService.removeStaffHistory(staffHistoryQueryDto);
            StaffHistoryDto staffHistoryDto = new StaffHistoryDto();
            staffHistoryDto.setType(StaffHistoryTypeEnum.HONOR.name());
            staffHistoryDto.setStaffId(setSysUserInfoDto.getStaffId());
            this.staffHistoryService.modifyStaffHistory(staffHistoryDto);
        }
        //惩罚记录
        if (Objects.nonNull(setSysUserInfoDto.getStaffHistoryPunishment()) && setSysUserInfoDto.getStaffHistoryPunishment().size() > 0) {
            staffHistoryQueryDto.setType(StaffHistoryTypeEnum.PUNISHMENT.name());

            List<StaffHistoryDto> newHistoryList = setSysUserInfoDto.getStaffHistoryPunishment();
            List<StaffHistoryVo> historyVos = this.staffHistoryService.queryStaffHistory(staffHistoryQueryDto);
            // 对比惩罚记录的 新增 编辑 删除操作
            String honor = StaffHistoryUtil.updateHistory(historyVos,newHistoryList,"惩罚");
            if(StringUtils.isNotEmpty(honor)){
                contentList.add(honor);
            }
            this.staffHistoryService.removeStaffHistory(staffHistoryQueryDto);
            setSysUserInfoDto.getStaffHistoryPunishment().forEach(item -> {
                item.setType(StaffHistoryTypeEnum.PUNISHMENT.name());
                item.setStaffId(setSysUserInfoDto.getStaffId());
                this.staffHistoryService.modifyStaffHistory(item);
            });
        } else {
            staffHistoryQueryDto.setType(StaffHistoryTypeEnum.PUNISHMENT.name());
            StringBuilder honor = new StringBuilder();
            List<StaffHistoryVo> historyVos = this.staffHistoryService.queryStaffHistory(staffHistoryQueryDto);
            for(StaffHistoryVo oldHistory: historyVos){
                honor.append("删除了惩罚记录:[").append(oldHistory.getTime()).append("    ").append(oldHistory.getDescribe()).append("]    ");
            }
            if(StringUtils.isNotEmpty(honor.toString())){
                contentList.add(honor.toString());
            }

            this.staffHistoryService.removeStaffHistory(staffHistoryQueryDto);
            StaffHistoryDto staffHistoryDto = new StaffHistoryDto();
            staffHistoryDto.setType(StaffHistoryTypeEnum.PUNISHMENT.name());
            staffHistoryDto.setStaffId(setSysUserInfoDto.getStaffId());
            this.staffHistoryService.modifyStaffHistory(staffHistoryDto);
        }
        //关系
        if (Objects.nonNull(setSysUserInfoDto.getStaffRelationDtoList())) {
            //删除
            SysStaffRelationDto sysStaffRelationDto = new SysStaffRelationDto();
            sysStaffRelationDto.setRelationStaffId(setSysUserInfoDto.getStaffId());
            sysStaffRelationDto.setStaffId(setSysUserInfoDto.getStaffId());
            if(setSysUserInfoDto.getStaffRelationDtoList().size() >0) {
                List<SysStaffRelationDto> newRelationVos = setSysUserInfoDto.getStaffRelationDtoList();
                List<SysStaffRelationVo> relationVos = this.sysStaffRelationService.queryRelationByListId(sysStaffRelationDto);
                String honor = StaffHistoryUtil.updateRelation(relationVos,newRelationVos);
                if(StringUtils.isNotEmpty(honor)){
                    contentList.add(honor);
                }
            }else {
                List<SysStaffRelationVo> relationVos = this.sysStaffRelationService.queryRelationByListId(sysStaffRelationDto);
                if(relationVos.size() >0) {
                    contentList.add("删除了关系记录");
                }
            }
            this.sysStaffRelationService.removeRelation(sysStaffRelationDto);
            //添加
            setSysUserInfoDto.getStaffRelationDtoList().forEach(item -> {
                item.setRelationStaffId(setSysUserInfoDto.getStaffId());
                item.setRelationStaffName(setSysUserInfoDto.getUserName());
                item.setRelationUserId(setSysUserInfoDto.getId());
                sysStaffRelationService.addRelation(item);
            });
        }

        //当状态为在职时判断是否在其他公司入职
        if (oldUserInfo.getStatus().intValue() == StatusEnum.stop.getValue() &&
                (StatusEnum.activity.getValue() == setSysUserInfoDto.getStatus().intValue()
                        || StatusEnum.teamwork.getValue() == setSysUserInfoDto.getStatus().intValue())) {
//            QueryWrapper<SysStaffInfo> activityQw = new QueryWrapper<>();
//            activityQw.lambda().inSql(true, SysStaffInfo::getUserId,
//                    "SELECT id FROM sys_user_info WHERE status IN (0, 3) AND phone_number ='"
//                            .concat(sysUserInfo.getPhoneNumber()).concat("'")
//            );
//            activityQw.lambda().ne(SysStaffInfo::getCompId, staff.getCompId());
//            activityQw.lambda().groupBy(SysStaffInfo::getCompId);
//            int activityCompanyCount = this.count(activityQw);
//            if (activityCompanyCount > 0) {
//                throw new DefaultException("该员工已在其他公司入职！");
//            }
            QueryWrapper<StaffBlacklist> blackQw = new QueryWrapper<>();
            blackQw.lambda().eq(StaffBlacklist::getUserId,staff.getUserId());
            blackQw.lambda().eq(StaffBlacklist::getState, StaffBlacklistApproveState.BLOCK.name());
            blackQw.lambda().last("limit 0,1");
            StaffBlacklist one = this.staffBlacklistService.getOne(blackQw);
            if (DataUtil.isNotEmpty(one)) {
                this.staffBlacklistService.doRelieveByStaffId(one.getId());
            }
        }

        //修改员工的时候删除该员工的全部角色
        QueryWrapper<UnionRoleStaff> ursQW = new QueryWrapper<>();
        ursQW.lambda().eq(UnionRoleStaff::getStaffId, staff.getId());
        UnionRoleStaff userRole = this.unionRoleStaffMapper.selectOne(ursQW);
        unionRoleStaffMapper.delete(ursQW);
        UnionRoleStaff rs = new UnionRoleStaff();
        //给员工赋予角色
        rs.setStaffId(staff.getId());
        rs.setRoleId(setSysUserInfoDto.getRoleId());

        //赋予旧角色进行比对
        if(userRole !=null){
            oldUserInfo.setRoleName(userRole.getRoleName());
        }

        //去查询角色名
        DataResult<?> result = oauthFeignService.getRoleById(setSysUserInfoDto.getRoleId());
        if (!result.isSuccess()) {
            throw new DefaultException("服务异常！");
        }
        JSONObject obj = (JSONObject) JSON.toJSON(result.getData());
        rs.setRoleName(obj.get("roleName").toString());
        //赋予角色名称
        setSysUserInfoDto.setRoleName(rs.getRoleName());
        unionRoleStaffMapper.insert(rs);
        //删除该员工的岗位
        QueryWrapper<UnionStaffPosition> uspQW = new QueryWrapper<>();
        uspQW.lambda().eq(UnionStaffPosition::getStaffId, staff.getId());
        UnionStaffPosition position = unionStaffPositionMapper.selectOne(uspQW);
        unionStaffPositionMapper.delete(uspQW);
        if(position !=null) {
            SysJobPosition job = sysJobPositionMapper.selectById(position.getPositionId());
            oldUserInfo.setPositionName(job == null ? "" : job.getPositionName());
        }
        //如果岗位id不为空就给该员工添加部门
        if (StringUtils.isNotEmpty(setSysUserInfoDto.getPositionId())) {
            UnionStaffPosition sp = new UnionStaffPosition();
            sp.setPositionId(setSysUserInfoDto.getPositionId());
            sp.setStaffId(staff.getId());
            unionStaffPositionMapper.insert(sp);
            SysJobPosition job = sysJobPositionMapper.selectById(setSysUserInfoDto.getPositionId());
            setSysUserInfoDto.setPositionName(job == null ? "" : job.getPositionName());
        }
        QueryWrapper<UnionStaffDepart> usdQW = new QueryWrapper<>();
        usdQW.lambda().eq(UnionStaffDepart::getStaffId, staff.getId());
        UnionStaffDepart dep = this.unionStaffDepartMapper.selectOne(usdQW);
        String oldDepart = null;
        if (DataUtil.isNotEmpty(dep)) {
            oldDepart = dep.getDepartmentId();
        }

        //修改部门
        if (!Objects.equals(oldDepart, setSysUserInfoDto.getDepartId())) {
            log.info("员工修改了部门-修改线索客户冗余的部门id-修改的员工id:{}", staff.getId());
            SetUserDepartDto userDepart = new SetUserDepartDto();
            userDepart.setDepartId(setSysUserInfoDto.getDepartId());
            SysStaffInfo staffInfo = this.getById(staff.getId());
            userDepart.setUserId(staffInfo.getUserId());
            SysDepartmentInfo departInfo = this.sysDepartmentInfoService.getById(userDepart.getDepartId());
            if (DataUtil.isNotEmpty(departInfo)) {
                userDepart.setDepartName(departInfo.getDepartName());
                setSysUserInfoDto.setDepartName(departInfo.getDepartName());
            }
            //赋予旧部门进行比对
            SysDepartmentInfo oldDept = this.sysDepartmentInfoService.getById(oldDepart);
            oldUserInfo.setDepartName(oldDept == null ? "":oldDept.getDepartName());

            DataResult r = clewService.updateCustomerAndClewDepartIdByUser(userDepart);
            if (!r.isSuccess()) {
                throw new DefaultException("修改线索负责人名称失败!");
            }
            List<String> departIds = new ArrayList<>();
            if (DataUtil.isNotEmpty(oldDepart)) {
                departIds.add(oldDepart);
            }
            if (DataUtil.isNotEmpty(setSysUserInfoDto.getDepartId())) {
                departIds.add(setSysUserInfoDto.getDepartId());
            }
            if (DataUtil.isNotEmpty(departIds)) {
                UpdateWrapper<SysDepartmentInfo> departEditUw = new UpdateWrapper<>();
                departEditUw.lambda().set(SysDepartmentInfo::getIsEdit, YesNo.YES);
                departEditUw.lambda().in(SysDepartmentInfo::getId, departIds);
                this.sysDepartmentInfoService.update(departEditUw);
            }
        }
        if (removeToken && DataUtil.isEmpty(dep)) {
            if (!DataUtil.isEmpty(setSysUserInfoDto.getDepartId())) {
                this.checkUtil.removeUserToken(sysUserInfo.getId());
                removeToken = !removeToken;
            }
        } else {
            if (DataUtil.isEmpty(setSysUserInfoDto.getDepartId())) {
                this.checkUtil.removeUserToken(sysUserInfo.getId());
                removeToken = !removeToken;
            } else if (!dep.getDepartmentId().equals(setSysUserInfoDto.getDepartId())) {
                //  如果修改了部门并且是上个部门的负责人 则把该员工的负责人删除
                SysDepartmentInfo depInfo = this.sysDepartmentInfoMapper.selectById(dep.getDepartmentId());
                if (staff.getId().equals(depInfo.getAdminAccount())) {
                    UpdateWrapper<SysDepartmentInfo> depUw = new UpdateWrapper<>();
                    depUw.lambda().set(SysDepartmentInfo::getAdminAccount, null);
                    depUw.lambda().eq(SysDepartmentInfo::getId, depInfo.getId());
                    this.sysDepartmentInfoService.update(depUw);
                }
                this.checkUtil.removeUserToken(sysUserInfo.getId());
                removeToken = !removeToken;
            }
        }


        //  员工为离职状态时 增加app推送
        //离职时， 添加伙伴的任职记录
        if (StatusEnum.stop.getValue().equals(setSysUserInfoDto.getStatus())) {
            appUserPushService.updateById(SysUserDimissionHandle.staffDimissionPush(sysUserInfo.getId()));
            StaffDimissionInfo staffDimissionInfo = new StaffDimissionInfo();
            staffDimissionInfo.setUserId(setSysUserInfoDto.getId());
            staffDimissionInfo.setOperationUserId(UserUtils.getUser().getUserId());
            staffDimissionInfo.setOperationUserName(UserUtils.getUser().getUserName());
            this.mqService.send(staffDimissionInfo, MQ.QUEUE_STAFF_DIMISSION);

            SysCompanyInfo sysCompanyInfo = this.sysCompanyInfoMapper.selectById(staff.getCompId());
            QueryWrapper<UnionRoleStaff> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UnionRoleStaff::getStaffId,setSysUserInfoDto.getStaffId());
            UnionRoleStaff unionRoleStaff = this.unionRoleStaffMapper.selectOne(queryWrapper);
            PositionRecord positionRecord = new PositionRecord();
            positionRecord.setId(UUIDutils.getUUID32());
            positionRecord.setUserId(staff.getUserId());
            positionRecord.setUserName(staff.getUserName());
            positionRecord.setCompanyId(staff.getCompId());
            positionRecord.setCompanyName(sysCompanyInfo.getCompanyName());
            positionRecord.setCertificateCard(sysUserInfo.getCertificateCard());
            positionRecord.setRoleName(unionRoleStaff.getRoleName());
            positionRecord.setPositionTime(staff.getDateJoin());
            positionRecord.setQuitTime(staff.getDateLeft());
            positionRecord.setQuitReason(staff.getLeaveReason());
            positionRecord.setCreateBy(user.getUserId());
            positionRecord.setCreateName(user.getUserName());
            positionRecord.setCreateTime(new Date());
            this.positionRecordService.save(positionRecord);
        }
        //  员工为离职状态时 清除token
        if (removeToken && StatusEnum.stop.getValue().equals(setSysUserInfoDto.getStatus())) {
            this.checkUtil.removeUserToken(sysUserInfo.getId());
            removeToken = !removeToken;
        }
        if (removeToken) {
            if (DataUtil.isEmpty(userRole)) {
                if (StringUtils.isNotEmpty(setSysUserInfoDto.getRoleId())) {
                    this.checkUtil.removeUserToken(sysUserInfo.getId());
                    removeToken = !removeToken;
                }
            } else {
                if (StringUtils.isEmpty(setSysUserInfoDto.getRoleId())) {
                    this.checkUtil.removeUserToken(sysUserInfo.getId());
                    removeToken = !removeToken;
                } else if (!setSysUserInfoDto.getRoleId().equals(userRole.getRoleId())) {
                    this.checkUtil.removeUserToken(sysUserInfo.getId());
                    removeToken = !removeToken;
                }
            }
        }

        UserInfoComparDto userCompart = new UserInfoComparDto();
        BeanUtils.copyProperties(setSysUserInfoDto,userCompart);
        // 新旧值比较  用于记录伙伴操作埋点数据
        List<UserCompar> comparList = UserCompareUtil.compareTwoClass(oldUserInfo,userCompart);
        comparList.addAll(staffList);
        String content = UserCompareUtil.convertCompars(comparList);
        UserLogUtil.addUserLog(oldUserInfo,user,content,contentList, DataCodeType.PARTNER_MODIFY);

        unionStaffDepartMapper.delete(usdQW);
        if (StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())) {
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
            QueryWrapper<SysDepartmentInfo> depQw = new QueryWrapper<>();
            depQw.lambda().eq(SysDepartmentInfo::getAdminAccount, staff.getId());
            SysDepartmentInfo depAdmin = this.sysDepartmentInfoMapper.selectOne(depQw);//原负责的部门id
            if (depAdmin == null || depAdmin.getId().equals(setSysUserInfoDto.getDepartId())) {
                return;
            }
            depAdmin.setAdminAccount(null);
            this.sysDepartmentInfoMapper.updateById(depAdmin);
        }
        //如果手机号码更改了则解除名片关联,并按照新的手机号创建新名片
//        if (DataUtil.isNotEmpty(oldUserInfo) && (!oldUserInfo.getPhoneNumber().equals(setSysUserInfoDto.getPhoneNumber()))) {
//            //先新增名片
//            CardUserInfo cardUserInfo = new CardUserInfo();
//            cardUserInfo.setUserName(sysUserInfo.getUserName());
//            cardUserInfo.setPhoneNumber(sysUserInfo.getPhoneNumber());
//            cardUserInfo.setCreateTime(new Date());
//            cardUserInfo.setStatus(StatusEnum.activity.getValue());
//            cardUserInfoMapper.insert(cardUserInfo);

            //解除名片限制
//            UserCardReplaceDto userReplace = new UserCardReplaceDto();
//            userReplace.setNewUserId(null);
//            userReplace.setOldUserId(sysUserInfo.getId());
//            this.cardFeignService.updateCardUser(userReplace);

//            //生成基础名片信息
//            UserCardDto cardDto = new UserCardDto();
//            cardDto.setMobile(cardUserInfo.getPhoneNumber());
//            cardDto.setUserName(cardUserInfo.getUserName());
//            //crm用户ID
//            cardDto.setUserId(sysUserInfo.getId());
//            //名片用户ID
//            cardDto.setCustomerUserId(cardUserInfo.getId());
//            cardDto.setAvatar(sysUserInfo.getAvatar());
//            cardDto.setEmail(sysUserInfo.getEmail());
//            cardDto.setCustomerUserId(cardUserInfo.getId());
//            cardDto.setCreateBy(UserUtils.getUserId());
//            log.info("新增名片信息{}", JSON.toJSON(cardDto));
//            DataResult<String> card = cardFeignService.createCard(cardDto);
//            if (!card.isSuccess()) {
//                throw new DefaultException("服务异常！");
//            }
//            //直接修改该用户关联的名片，且解除旧关联
//            UpdateWrapper<CardUserUnionUser> uw = new UpdateWrapper<>();
//            uw.lambda().eq(CardUserUnionUser::getUserId, oldUserInfo.getId());
//            uw.lambda().set(CardUserUnionUser::getCardId, cardUserInfo.getId());
//            cardUserUnionCrmUserMapper.update(null, uw);
//        }

        log.info("批量分配客户信息  ——> 结果：{}, 操作者信息：{}", JSON.toJSONString(setSysUserInfoDto), JSON.toJSONString(UserUtils.getUser()));
    }

    @Override
    public SysUserInfoVo selectStaffById(String id,boolean isTraverse,UserVo userVo) {
        if (StringUtils.isEmpty(id)) {
            throw new DefaultException("id不能为空");
        }
        //查看员工详情
        SysUserInfoVo userInfo = sysStaffInfoMapper.selectStaffById(id);
        if (DataUtil.isEmpty(userInfo)) {
            throw new DefaultException("找不到用户");
        }
        if (StringUtils.isNotEmpty(userInfo.getImState())) {
            //获取字典信息
            DictQuseryVo imState = this.dictService.getListById(userInfo.getImState());
            if (DataUtil.isNotEmpty(imState)) {
                userInfo.setImStateName(imState.getDictName());
            }
        }
        if(isTraverse==true){
            BlacklistOperationRecordAddDto operationRecord = new BlacklistOperationRecordAddDto();
            operationRecord.setMobile(userInfo.getPhoneNumber());
            operationRecord.setIdentityCard(userInfo.getCertificateCard());
            operationRecord.setType(0);
            operationRecord.setRemarks("查看伙伴档案");
            blacklistOperationRecordService.addBlacklistOperationRecord(operationRecord,userVo);
        }
        RecommendInfoVo recommendInfo = null;
        if (DataUtil.isNotEmpty(userInfo) && StringUtils.isNotEmpty(userInfo.getRecommendId()) && userInfo.getRecommendType().intValue() == 1) {
            recommendInfo = this.sysStaffInfoMapper.getRecommendInfo(userInfo.getRecommendId());
            userInfo.setRecommendInfo(recommendInfo);
        }
        if (DataUtil.isNotEmpty(recommendInfo) && StringUtils.isNotEmpty(recommendInfo.getRecommendId()) && userInfo.getRecommendType().intValue() == 1) {
            recommendInfo = this.sysStaffInfoMapper.getRecommendInfo(recommendInfo.getRecommendId());
            userInfo.setRecommendSecond(recommendInfo);
        }
        QueryWrapper<UserResume> rq = new QueryWrapper<>();
        rq.lambda().eq(UserResume::getUserId, userInfo.getId());
        rq.lambda().orderByDesc(UserResume::getCreateTime);
        userInfo.setUserResumes(this.userResumeService.list(rq));


        if(Objects.nonNull(userInfo)) {
            //获取荣耀和惩罚记录
            getRecord(id, userInfo);
            SysStaffRelationDto sysStaffRelationDto = new SysStaffRelationDto();
            sysStaffRelationDto.setRelationStaffId(id);
            List<SysStaffRelationVo> sysStaffRelationVos = this.sysStaffRelationService.queryRelationList(sysStaffRelationDto).getSysStaffRelationVos();
            userInfo.setStaffRelationDtoList(sysStaffRelationVos);
        }
        QueryWrapper<FamilyMember> fmQw = new QueryWrapper<>();
        fmQw.lambda().eq(FamilyMember::getUserId, userInfo.getId());
        fmQw.lambda().orderByDesc(FamilyMember::getOrderNum);
        userInfo.setFamilyMembers(this.familyMemberService.list(fmQw));

        //合规承诺书
        QueryWrapper<Image> imageQw = new QueryWrapper<>();
        imageQw.lambda().eq(Image::getConnectId, userInfo.getId());
        imageQw.lambda().eq(Image::getImageType, ImageTypeInfo.COMPLIANCE_COMMITMENT);
        imageQw.lambda().orderByDesc(Image::getCreateTime);
        userInfo.setComplianceCommitments(this.imageService.list(imageQw).stream().map(s->s.getImageUrl()).collect(Collectors.toList()));

        //学历证书
        imageQw.clear();
        imageQw.lambda().eq(Image::getConnectId, userInfo.getId());
        imageQw.lambda().eq(Image::getImageType, ImageTypeInfo.DIPLOMA);
        imageQw.lambda().orderByDesc(Image::getCreateTime);
        userInfo.setDiplomas(this.imageService.list(imageQw).stream().map(s->s.getImageUrl()).collect(Collectors.toList()));

        //合作申请表 (先查询图片表(image)，如果没有数据查询文件表(commonFile))
        imageQw.clear();
        imageQw.lambda().eq(Image::getConnectId, userInfo.getId());
        imageQw.lambda().eq(Image::getImageType, ImageTypeInfo.COOPERATION_APPLY);
        imageQw.lambda().orderByDesc(Image::getCreateTime);
        List<String> imgList = this.imageService.list(imageQw).stream().map(s -> s.getImageUrl()).collect(Collectors.toList());
        if (DataUtil.isNotEmpty(imgList)) {
            userInfo.setCooperationImages(imgList);
        } else {
            QueryWrapper<CommonFile> fileQw = new QueryWrapper<>();
            fileQw.lambda().eq(CommonFile::getConnectId, userInfo.getId());
            fileQw.lambda().eq(CommonFile::getFileType, FileTypeInfo.COOPERATION_FILE);
            fileQw.lambda().orderByDesc(CommonFile::getCreateTime);
            List<CommonFile> list = this.commonFileService.list(fileQw);
            List<String> images = list.stream().map(CommonFile::getFileUrl).collect(Collectors.toList());
            userInfo.setCooperationImages(images);
        }

        UserVo user = new UserVo();
        user.setUserId(userInfo.getId());
        user.setCompanyId(userInfo.getCompanyId());
        //获取荣耀和惩罚记录
        getRecord(userInfo.getStaffId(), userInfo);
        //查询关系
        SysStaffRelationDto sysStaffRelationDto = new SysStaffRelationDto();
        sysStaffRelationDto.setRelationStaffId(userInfo.getStaffId());
        List<SysStaffRelationVo> sysStaffRelationVos = this.sysStaffRelationService.queryRelationList(sysStaffRelationDto).getSysStaffRelationVos();
        userInfo.setStaffRelationDtoList(sysStaffRelationVos);
        AdminVo admin = this.getIsAdmin(user);
        userInfo.setIsCompanyAdmin(admin.getIsCompanyAdmin());
        userInfo.setIsDepartAdmin(admin.getIsDepartAdmin());

        if (admin.getIsCompanyAdmin()) {
            userInfo.setSuperiorName(userInfo.getUserName());
            return userInfo;
        }
        if (StringUtils.isEmpty(userInfo.getDepartId())) {
            return userInfo;
        }
        if (admin.getIsDepartAdmin()) {
            String departId = userInfo.getDepartId();
            if (departId.indexOf("_") == -1) {
                QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
                staffQw.lambda().inSql(SysStaffInfo::getId, "select staff_id from company_admin where company_id = '".concat(userInfo.getCompanyId()).concat("'"));
                SysStaffInfo sysStaffInfo = this.getOne(staffQw);
                userInfo.setSuperiorName(DataUtil.isEmpty(sysStaffInfo) ? null : sysStaffInfo.getUserName());
                return userInfo;
            }
            departId = departId.substring(0, departId.lastIndexOf("_"));
            SysDepartmentInfo departInfo = this.sysDepartmentInfoMapper.selectById(departId);
            if (StringUtils.isEmpty(departInfo.getAdminAccount())) {
//                userInfo.setSuperiorName("暂无上级");
                return userInfo;
            }
        }

        //获取部门信息
        SysDepartmentInfo departInfo = this.sysDepartmentInfoMapper.selectById(userInfo.getDepartId());
        if (StringUtils.isEmpty(departInfo.getAdminAccount())) {
            return userInfo;
        }
        //根据部门管理员id获取员工信息
        SysStaffInfo staffInfo = this.sysStaffInfoMapper.selectById(departInfo.getAdminAccount());
        userInfo.setSuperiorName(staffInfo.getUserName());
//        userInfo.setPhoneNumber(CommonUtils.mobileEncrypt(userInfo.getPhoneNumber()));
//        userInfo.setCertificateCard(CommonUtils.idEncrypt( userInfo.getCertificateCard()));
        return userInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStaffById(String staffId) {
        log.info("删除员工  ——> 入参：staffId-{}, 操作者信息：{}", staffId, JSON.toJSONString(UserUtils.getUser()));
        if (StringUtils.isEmpty(staffId)) {
            throw new DefaultException("员工id为空");
        }
        SysStaffInfo staff = this.getById(staffId);
        //  检查改员工下是否有客户
        DataResult<Integer> result = this.customerService.getCustomerCountInner(staff.getUserId(), null, null);
        if (!result.isSuccess()) {
            throw new DefaultException("获取客户信息失败");
        }
        if (result.getData() > 0) {
            throw new DefaultException("该员工名下拥有客户无法删除，请将该员工的客户变更给其他员工再重新操作！");
        }
        //  检查改员工下是否有客户
        DataResult<Integer> resultContract = this.contractService.getSignOrderCountInner(staff.getUserId(), null, null);
        if (!resultContract.isSuccess()) {
            throw new DefaultException("获取签单信息失败");
        }
        if (resultContract.getData() > 0) {
            throw new DefaultException("该员工拥有签单数据无法删除");
        }
        SysUserInfo userInfo = this.sysUserInfoMapper.selectById(staff.getUserId());
        userInfo.setStatus(StatusEnum.deleted.getValue());
        userInfo.setIsEdit(YesNo.YES);
        userInfo.setStatusEdit(YesNo.YES);
        this.sysUserInfoMapper.updateById(userInfo);
        //删除部门
        QueryWrapper<UnionStaffDepart> qw = new QueryWrapper<>();
        qw.lambda().eq(UnionStaffDepart::getStaffId, staffId);
        unionStaffDepartMapper.delete(qw);
        //删除角色
        QueryWrapper<UnionRoleStaff> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(UnionRoleStaff::getStaffId, staffId);
        unionRoleStaffMapper.delete(qw1);
        //删除岗位
        QueryWrapper<UnionStaffPosition> qw2 = new QueryWrapper<>();
        qw2.lambda().eq(UnionStaffPosition::getStaffId, staffId);
        unionStaffPositionMapper.delete(qw2);
        List<String> userIds = new ArrayList<>();
        userIds.add(userInfo.getId());
        // 删除员工号 去掉企业管理
        QueryWrapper<CompanyAdmin> companyAdminUw = new QueryWrapper<>();
        companyAdminUw.lambda().eq(CompanyAdmin::getStaffId, staffId);
        companyAdminUw.lambda().eq(CompanyAdmin::getCompanyId, staffId);
        CompanyAdmin admin = this.companyAdminMapper.selectOne(companyAdminUw);
        // 不管是不是企业负责人 删除就完事了
        companyAdminMapper.delete(companyAdminUw);

        if (DataUtil.isNotEmpty(admin)) {
            UpdateWrapper<SysCompanyInfo> companyUw = new UpdateWrapper<>();
            companyUw.lambda().set(SysCompanyInfo::getContactPhone, null);
            companyUw.lambda().set(SysCompanyInfo::getContactName, null);
            companyUw.lambda().eq(SysCompanyInfo::getId, admin.getCompanyId());
            this.sysCompanyInfoMapper.update(null, companyUw);
        }


        // 删除员工时去掉他身上的负责人
        UpdateWrapper<SysDepartmentInfo> departUw = new UpdateWrapper<>();
        departUw.lambda().set(SysDepartmentInfo::getAdminAccount, null);
        departUw.lambda().eq(SysDepartmentInfo::getAdminAccount, staffId);
        this.sysDepartmentInfoService.update(departUw);
        //删除员工历史记录和图片
        StaffHistoryQueryDto staffHistoryQueryDto = new StaffHistoryQueryDto();
        staffHistoryQueryDto.setId(staffId);
        staffHistoryQueryDto.setStaffId(staffId);
        this.staffHistoryService.removeStaffHistory(staffHistoryQueryDto);

        this.oauthFeignService.removeToken(userIds);

        UserLogUtil.addUserLog(userInfo,UserUtils.getUser(),"删除伙伴",DataCodeType.PARTNER_DEL);
    }

    @Override
    public List<String> getStaffRoles(String userId, String companyId) {
        List<String> roleIds = sysStaffInfoMapper.selectStaffRoles(userId, companyId);
        return roleIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String doBatchImportUser(MultipartFile file, UserVo user) throws Exception {
        List<String[]> dataList = new ArrayList<>();
        try {
            dataList = FileUtil.fileImport(file);
        } catch (IOException e) {
            log.error("员工导入pc-excel读取错误-导入用户{}-错误信息{}", JSON.toJSONString(user), e, e);
            throw new DefaultException("excel读取错误!请使用下的模板");
        }
        if (DataUtil.isEmpty(dataList)) {
            throw new DefaultException("请检查上传数据是否正确！");
        }
        // 1：表示只有提示和表头，没有数据
        if (dataList.size() < 3) {
            throw new DefaultException("导入的模板为空，没有数据！");
        }
        //删除第一条表格填写说明数据
        dataList.remove(0);
        // 获取表头数组
        String[] headers = dataList.get(0);
        //读的起始行
        int dataIndex = 0;
        //判断表头字段是否符合要求
        boolean isLegitimate = false;
        //表头校验，只判断前一行，如果不正确直接返回
        for (String[] header : dataList) {
            if (Arrays.equals(header, STAFF_EXCEL_TITTLE)) {
                isLegitimate = true;
            }
            if (++dataIndex == 1 || isLegitimate) {
                break;
            }
        }
        if (!isLegitimate) {
            throw new DefaultException("您上传的文件中表头不匹配系统最新要求的表头字段，请下载最新模板核对表头并按照要求填写！");
        }
        ImportInfo importInfo = new ImportInfo();
        importInfo.setImportState(ImportStateEnum.UNDERWAY.name());
        StaffInfoVo staff = this.getStaffInfo(user.getUserId());
        importInfo.setCompanyName(staff.getCompanyName());
        importInfo.setCompanyId(user.getCompanyId());
        importInfo.setUserId(user.getUserId());
        importInfo.setUserName(user.getUserName());
        importInfo.setImportFileName(file.getOriginalFilename());
        importInfo.setImportType(ImportResultInfoType.STAFF_EXTERNAL);
        importInfo.setCreateTime(new Date());
        importInfo.setExcelRows(0);
        importInfo.setSuccessNum(0);
        importInfo.setErrorNum(0);
        this.importInfoService.save(importInfo);
        List<String[]> finalDataList = dataList;
        new Thread(() -> {
            try {
                int saveRow = 50;

                for (int i = 1, size = finalDataList.size(); i < size; i += saveRow) {
                    if (saveRow + i >= size ) {
                        saveRow = size - i;
                    }
                    //校验以及保存伙伴内控名单
                    Map<String, Integer> result = this.doBatchImportUserSave(finalDataList.subList(i, i + saveRow), importInfo, user);
                    importInfo.setExcelRows(importInfo.getExcelRows() + result.get("total"));
                    importInfo.setSuccessNum(importInfo.getSuccessNum() + result.get("successCount"));
                    importInfo.setErrorNum(importInfo.getErrorNum() + result.get("errorCount"));
                }
            } catch (Exception e) {
                log.error("员工导入出错#错误信息：{}", e, e);
            } finally {
                importInfo.setImportState(ImportStateEnum.ACCOMPLISH.name());
                this.importInfoService.updateById(importInfo);
            }
        }).start();

//        //数据总条数
//        result.put("total", dataList.size() - 1);
//        //异常条数
//        result.put("errorCount", errors.size());
//        //成功条数
//        result.put("successCount", dataList.size() - errors.size() - 1);
//        //标红必填项
//        Integer[] mustIndex = {0, 1, 4, 11};
//        if (errors.size() > 0) {
//            String[] heads = Arrays.copyOf(headers, headers.length + 1);
//            heads[heads.length - 1] = "导入失败原因";
//            HSSFWorkbook hw = ExcelToolUtil.createExcel(heads, errors, mustIndex);
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            hw.write(os);
//            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
//            String base64 = FileUtil.fileToBase64(inputStream);
//            result.put("base64File", base64);
//        }
////        sendInitPwdSms(smsDtos);
        return importInfo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String doBatchImportCompanyUser(MultipartFile file, UserVo user) throws Exception {
        List<String[]> dataList = null;
        try {
            dataList = FileUtil.fileImport(file);
        } catch (IOException e) {
            log.error("员工导入admin-excel读取错误-导入用户{}-错误信息{}", JSON.toJSONString(user), e, e);
            throw new DefaultException("excel读取错误!请使用下的模板");
        }
        if (DataUtil.isEmpty(dataList)) {
            throw new DefaultException("请检查上传数据是否正确！");
        }
        // 1：表示只有提示和表头，没有数据
        if (dataList.size() < 3) {
            throw new DefaultException("导入的模板为空，没有数据！");
        }
        //删除第一条表格填写说明数据
        dataList.remove(0);
        // 获取表头数组
        String[] headers = dataList.get(0);
        //读的起始行
        int dataIndex = 0;
        //判断表头字段是否符合要求
        boolean isLegitimate = false;
        //表头校验，只判断前一行，如果不正确直接返回
        for (String[] header : dataList) {
            if (Arrays.equals(header, COMPANY_STAFF_EXCEL_TITTLE)) {
                isLegitimate = true;
            }
            if (++dataIndex == 1 || isLegitimate) {
                break;
            }
        }
        if (!isLegitimate) {
            throw new DefaultException("您上传的文件中表头不匹配系统最新要求的表头字段，请下载最新模板核对表头并按照要求填写！");
        }
        ImportInfo importInfo = new ImportInfo();
        importInfo.setImportState(ImportStateEnum.UNDERWAY.name());
        importInfo.setCompanyName("后台管理员");
        importInfo.setUserId(user.getUserId());
        importInfo.setUserName(user.getUserName());
        importInfo.setImportFileName(file.getOriginalFilename());
        importInfo.setImportType(ImportResultInfoType.STAFF_EXTERNAL);
        importInfo.setCreateTime(new Date());
        importInfo.setExcelRows(0);
        importInfo.setSuccessNum(0);
        importInfo.setErrorNum(0);
        this.importInfoService.save(importInfo);
        List<String[]> finalDataList = dataList;
        new Thread(() -> {
            try {
                int saveRow = 50;

                for (int i = 1, size = finalDataList.size(); i < size; i += saveRow) {
                    if (saveRow + i >= size ) {
                        saveRow = size - i;
                    }
                    //校验以及保存伙伴内控名单
                    Map<String, Integer> result = this.doBatchImportCompanyUserSave(finalDataList.subList(i, i + saveRow), importInfo, user);
                    importInfo.setExcelRows(importInfo.getExcelRows() + result.get("total"));
                    importInfo.setSuccessNum(importInfo.getSuccessNum() + result.get("successCount"));
                    importInfo.setErrorNum(importInfo.getErrorNum() + result.get("errorCount"));
                }
            } catch (Exception e) {
                log.error("员工后台导入出错#错误信息：{}", e, e);
            } finally {
                importInfo.setImportState(ImportStateEnum.ACCOMPLISH.name());
                this.importInfoService.updateById(importInfo);
            }
        }).start();

//        //数据总条数
//        result.put("total", dataList.size() - 1);
//        //异常条数
//        result.put("errorCount", errors.size());
//        //成功条数
//        result.put("successCount", dataList.size() - errors.size() - 1);
//        //标红必填项
//        Integer[] mustIndex = {0, 1, 4, 11};
//        if (errors.size() > 0) {
//            String[] heads = Arrays.copyOf(headers, headers.length + 1);
//            heads[heads.length - 1] = "导入失败原因";
//            HSSFWorkbook hw = ExcelToolUtil.createExcel(heads, errors, mustIndex);
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            hw.write(os);
//            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
//            String base64 = FileUtil.fileToBase64(inputStream);
//            result.put("base64File", base64);
//        }
////        sendInitPwdSms(smsDtos);
        return importInfo.getId();
    }

    private String saveCompanyUser(String[] row, List<SmsDto> smsDtos, String companyId, UserVo user, StringBuilder recommendId, List<SysUserInfo> userInfos) throws ParseException {
        //表头对应下标
        //{"姓名","手机号码","企业","部门","岗位","角色", "推荐人手机号码","状态","性别【7】","籍贯","民族","婚姻","出生年月日"【11】,
        // "身份证号码","户籍地址","居住地址[14]","电子邮箱","最高学历","毕业院校","所学专业"【18】};
        Integer status = StringUtils.isEmpty(row[8]) ? StatusEnum.activity.getValue() : row[8].equals(StatusEnum.activity.getDesc()) ? StatusEnum.activity.getValue() :
                row[8].equals(StatusEnum.teamwork.getDesc()) ? StatusEnum.teamwork.getValue() : StatusEnum.stop.getValue();
        SysUserInfo userInfo = new SysUserInfo();
        SysStaffInfo staff = new SysStaffInfo();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        userInfo.setUserName(row[0]);
        userInfo.setPhoneNumber(row[1]);
        int index = 5 ;
        staff.setDateJoin(format.parse(row[++index]));
        ++index;
        ++index;
        userInfo.setGender(row[++index].equals(StaffGenderEnum.MALE.getDesc()) ? StaffGenderEnum.MALE.getValue() : StaffGenderEnum.FEMALE.getValue());
        userInfo.setAncestral(row[++index]);
        userInfo.setNation(row[++index]);
        userInfo.setMaritalStatus(StringUtils.isEmpty(row[++index]) ? null : MaritalStatusEnum.getCodeByName(row[index]).getCode());
        userInfo.setBirthday(StringUtils.isEmpty(row[++index]) ? null : format.parse(row[index]));
        userInfo.setCertificateCard(row[++index]);
        //获取身份证年月日
        IdCardDate date = DateUtil.getIdCardDate(userInfo.getCertificateCard());
        //月
        userInfo.setBirthdayMonth(date.getMonth());
        //日
        userInfo.setBirthdayDay(date.getDay());
        userInfo.setCertificateCardAddress(row[++index]);
        userInfo.setContactAddress(row[++index]);
        userInfo.setEmail(row[++index]);
        userInfo.setHighestEducation(row[++index]);
        userInfo.setGraduatedFrom(row[++index]);
        userInfo.setMajor(row[++index]);
        userInfo.setRegisterTime(new Date());
        userInfo.setStatus(status);
        userInfo.setUrgentName(row[++index]);
        userInfo.setUrgentPhone(row[++index]);
        userInfo.setUrgentRelation(row[++index]);
        userInfo.setCreateId(user.getUserId());
        userInfo.setCreateUser(user.getUserName());
        userInfo.setCreateTime(new Date());
        //因为员工表跟登录表都要用到用户所有先保存用户
        sysUserInfoMapper.insert(userInfo);
        SysUserInfo userInfo1 = new SysUserInfo();
        userInfo1.setIsEdit(YesNo.YES);
        userInfo1.setId(userInfo.getId());
        userInfos.add(userInfo1);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String initPwd = SysStaffInfoService.getInitPwd();
        staff.setCompId(companyId);
        staff.setUserName(userInfo.getUserName());
        staff.setUserId(userInfo.getId());
        staff.setPassword(initPwd);
        staff.setStatus(status);
        staff.setDeleted(YesNo.NO);
        staff.setCreateTime(new Date());
        staff.setCreateId(user.getUserId());
        staff.setCreateUser(user.getUserName());
        staff.setRecommendId(recommendId.toString());
        staff.setRecommendType(0);
        if (StringUtils.isNotEmpty(recommendId.toString())) {
            staff.setRecommendType(1);
        }

        FamilyMember family = new FamilyMember();
        family.setName(row[++index]);
        family.setRelationship(row[++index]);
        family.setMobile(row[++index]);
        family.setProfession(row[++index]);
        family.setContactAddress(row[++index]);
        StaffInfoVo staffInfoVo = new StaffInfoVo();
        staffInfoVo.setUserId(userInfo.getId());
        staffInfoVo.setStaffId(staff.getId());
        //其中一个不为空就保存
        if (DataUtil.isNotEmpty(family.getName()) || DataUtil.isNotEmpty(family.getRelationship()) ||
                DataUtil.isNotEmpty(family.getMobile()) || DataUtil.isNotEmpty(family.getProfession()) || DataUtil.isNotEmpty(family.getContactAddress())) {
            this.familyMemberService.addFamilyMember(family, staffInfoVo);
        }

        //保存到员工表
        this.save(staff);
        SysLoginInfo loginInfo = new SysLoginInfo();
        loginInfo.setMobileNumber(userInfo.getPhoneNumber());
        loginInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));
        loginInfo.setStatus(StatusEnum.activity.getValue());
        loginInfo.setUserId(userInfo.getId());
        loginInfo.setCreateId(user.getUserId());
        sysLoginInfoService.addOrUpdateLogin(loginInfo);
//        SmsDto smsDto = new SmsDto();
//        smsDto.setMobile(userInfo.getPhoneNumber());
//        Map<String, Object> content = new HashMap<>();
//        content.put("userName", userInfo.getPhoneNumber());
//        content.put("passWord", initPwd);
//        smsDto.setContent(content);
//        smsDto.setTemplateCode(userTipTemplate);
//        smsDto.setSign(smsSign);
//        smsDtos.add(smsDto);


        //获取企业信息的地址用于生成名片
        QueryWrapper<SysCompanyInfo> qw = new QueryWrapper<>();
        qw.lambda().eq(SysCompanyInfo::getId, companyId)
                .ne(BaseModel::getStatus, StatusEnum.deleted.getValue());
        SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);

        //添加员工即为内部员工需要生成名片小程序用户账号
        //生成基础名片信息
//        saveCardUser(userInfo, loginInfo, sysCompanyInfo, row[4]);
        //推送app账户
        appUserPushService.doPushAppUser(userInfo.getId(), sysCompanyInfo.getId());
        return staff.getId();
    }

    private String checkCompanyParam(String[] row, StringBuilder errorStr,
                                     UnionStaffDepart unionStaffDepart, UnionStaffPosition unionStaffPosition, UnionRoleStaff unionRoleStaff, StringBuilder recommendId) {
        String companyId = null;
        //表头对应下标
        //{"姓名","手机号码","企业","部门","岗位","角色","状态","性别","籍贯","民族","婚姻","出生年月日"【11】,
        // "身份证号码","户籍地址","居住地址","电子邮箱","最高学历","毕业院校","所学专业"【18】, "家庭成员姓名", "家庭成员关系", "家庭成员电话","从事行业", "联系地址"};
        //必填项校验
        if (DataUtil.isEmpty(row[0]) || DataUtil.isEmpty(row[1]) || DataUtil.isEmpty(row[2]) || DataUtil.isEmpty(row[5]) || DataUtil.isEmpty(row[6]) || DataUtil.isEmpty(row[14])) {
            errorStr.append("请填写红色区域必填项,");
        }
        //手机号码校验
        String phone = row[1];
        if (DataUtil.isNotEmpty(phone)) {
            //手机号码进行格式校验
            phone = PhoneHomeLocationUtils.importPhoneDispose(phone);
            row[1] = phone;
            boolean fild = true;
            try {
                PhoneHomeLocationUtils.checkPhone(phone);
            } catch (DefaultException e) {
                errorStr.append(e.getMessage());
                fild = !fild;
            }
            if (StringUtils.isNotEmpty(row[22]) && !PhoneHomeLocationUtils.checkPhoneBoolean(row[22])) {
                errorStr.append("紧急联系人电话不合法,");
            }

            if (StringUtils.isNotEmpty(row[26]) && !PhoneHomeLocationUtils.checkPhoneBoolean(row[26])) {
                errorStr.append("家庭成员电话不合法,");
            }
            //手机号码判断是否已注册账户
            if (fild && sysUserInfoMapper.selectUserByPhone(phone)) {
                errorStr.append("此手机号码已注册过账户,");
            }

            //手机号码判断是否是内控名单
            MobileAndIdentityCardDto dto = new MobileAndIdentityCardDto();
            dto.setMobile(phone);
            dto.setIdentityCard(row[14]);
            MobileBlacklistQueryVo blacklistByMobile = staffBlacklistService.getBlacklistByMobile(dto,null,false);
            if (fild && DataUtil.isNotEmpty(blacklistByMobile)&&blacklistByMobile.getIsBlock()) {
                errorStr.append("已被添加到内控名单，请到内控名单查看原因,");
            }

            //身份证号码校验
            String cardId = row[14];
            //验证身份证
            if (DataUtil.isNotEmpty(cardId)) {
                if (!IdCardUtil.isValidatedAllIdcard(cardId)) {
                    errorStr.append("身份证错误,");
                } else {
                    // 新增时添加身份证唯一校验 包含离职账户
                    StaffInfoVo staffInfo = this.sysStaffInfoMapper.getUserByCertificateCard(cardId,YesNo.YES);
                    if (DataUtil.isNotEmpty(staffInfo)) {
                        errorStr.append("该身份证号码已在“").append(staffInfo.getCompanyName());
                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(staffInfo.getDepartmentName())) {
                            errorStr.append("-");
                            errorStr.append(staffInfo.getDepartmentName());
                        }
                        errorStr.append("”存在");
                    }
                }
            }
            //先校验企业存不存在，企业不存在则不需要在校验部门岗位角色
            String companyName = row[2];
            QueryWrapper<SysCompanyInfo> qw = new QueryWrapper<>();
            qw.lambda().eq(SysCompanyInfo::getCompanyName, companyName)
                    .ne(BaseModel::getStatus, StatusEnum.deleted.getValue());
            SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);
            if (DataUtil.isEmpty(sysCompanyInfo)) {
                errorStr.append(companyName + "该企业不存在,");
            } else {
                //企业ID
                companyId = sysCompanyInfo.getId();

                String departName = row[3];
                String jobName = row[4];
                String roleName = row[5];
                //本企业角色是否存在
                DataResult roleByName = oauthFeignService.getRoleByName(companyId, roleName);
                if (!roleByName.isSuccess()) {
                    throw new DefaultException("服务异常");
                } else {
                    if (DataUtil.isEmpty(roleByName.getData())) {
                        errorStr.append("角色不存在,");
                    } else {
                        JSONObject obj = (JSONObject) JSON.toJSON(roleByName.getData());
                        unionRoleStaff.setRoleId(obj.get("id").toString());
                        unionRoleStaff.setRoleName(roleName);
                    }
                }
                // 判断同公司的
                LeaveUserInfoVo leave = sysStaffInfoMapper.getLeaveUserByCard(cardId,phone,companyId);
                if(leave != null){
                    errorStr.append("该伙伴原签约[").append(leave.getCompanyName()).append(" + ").
                            append(leave.getDepartName()).append("]，\"请联系即将签约团队的团队长在CRM-APP【伙伴签约申请】发起签约！，").
                            append("（暂不支持行政办理二次签约）");
                }
                // 判断跨公司的
                leave = sysStaffInfoMapper.getLeaveUserByCard(cardId,phone,null);
                if(leave != null){
                    throw new DefaultException("该伙伴原签约["+leave.getCompanyName() +" + "+ leave.getDepartName()+"]，" +
                            "不允许直接办理二次入职，请联系行政发起审批!");
                }

                String recommendMobile = row[7];
                if (StringUtils.isNotEmpty(recommendMobile)) {
                    if (recommendMobile.equals(phone)) {
                        errorStr.append("推荐人不能是自身");
                    }
                    StaffInfoVo staff = this.sysStaffInfoMapper.getStaffInfoByMobile(recommendMobile);
                    if (DataUtil.isEmpty(staff)) {
                        errorStr.append("推荐人不存在");
                    } else {
                        recommendId.append(staff.getStaffId());
                    }
                }
                //本企业部门是否存在
                SysDepartmentInfo byName = null;
                if (DataUtil.isNotEmpty(departName)) {
                    byName = sysDepartmentInfoService.getByName(departName, companyId);
                    if (DataUtil.isEmpty(byName)) {
                        errorStr.append("部门不存在,");
                    } else {
                        unionStaffDepart.setDepartmentId(byName.getId());
                    }
                }
                //本企业岗位是否存在//如果没填部门则直接找岗位
                if (DataUtil.isNotEmpty(jobName)) {
                    SysJobPosition jobByDepart = null;
                    if (DataUtil.isNotEmpty(departName)) {
                        if (DataUtil.isNotEmpty(byName)) {
                            jobByDepart = sysJobPositionService.getJobByDepart(jobName, companyId, byName.getId());
                        }
                    } else {
                        jobByDepart = sysJobPositionService.getJobByComp(jobName, companyId);
                    }
                    if (DataUtil.isEmpty(jobByDepart)) {
                        errorStr.append("岗位不存在,");
                    } else {
                        unionStaffPosition.setPositionId(jobByDepart.getId());
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(row[12])) {
            if (DataUtil.isEmpty(MaritalStatusEnum.getCodeByName(row[12]))) {
                errorStr.append("婚姻状况错误");
            }
        }
        return companyId;
    }

    private String saveUser(String[] row, List<SmsDto> smsDtos, SysCompanyInfo sysCompanyInfo, UserVo user, StringBuilder recommendId, List<SysUserInfo> userInfos) throws ParseException {
        //表头对应下标
        //{"姓名","手机号码","部门","岗位","角色", "签约日期", "推荐人手机号码","状态","性别【6】","籍贯","民族","婚姻","出生年月日"【10】,
        // "身份证号码","户籍地址","居住地址[13]","电子邮箱","最高学历","毕业院校","所学专业"【17】};
        Integer status = StringUtils.isEmpty(row[7]) ? StatusEnum.activity.getValue() : row[7].equals(StatusEnum.activity.getDesc()) ? StatusEnum.activity.getValue() :
                row[7].equals(StatusEnum.teamwork.getDesc()) ? StatusEnum.teamwork.getValue() : StatusEnum.stop.getValue();
        SysUserInfo userInfo = new SysUserInfo();
        SysStaffInfo staff = new SysStaffInfo();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        userInfo.setUserName(row[0]);
        userInfo.setPhoneNumber(row[1]);
        staff.setDateJoin(format.parse(row[5]));
        int index = 7;
        userInfo.setGender(row[++index].equals(StaffGenderEnum.MALE.getDesc()) ? StaffGenderEnum.MALE.getValue() : StaffGenderEnum.FEMALE.getValue());
        userInfo.setAncestral(row[++index]);
        userInfo.setNation(row[++index]);
        userInfo.setMaritalStatus(StringUtils.isEmpty(row[++index]) ? null : MaritalStatusEnum.getCodeByName(row[index]).getCode());
        userInfo.setBirthday(StringUtils.isEmpty(row[++index]) ? null : format.parse(row[index]));
        userInfo.setCertificateCard(row[++index]);
        //获取身份证年月日
        IdCardDate date = DateUtil.getIdCardDate(userInfo.getCertificateCard());
        //月
        userInfo.setBirthdayMonth(date.getMonth());
        //日
        userInfo.setBirthdayDay(date.getDay());
        userInfo.setCertificateCardAddress(row[++index]);
        userInfo.setContactAddress(row[++index]);
        userInfo.setEmail(row[++index]);
        userInfo.setHighestEducation(row[++index]);
        userInfo.setGraduatedFrom(row[++index]);
        userInfo.setMajor(row[++index]);
        userInfo.setRegisterTime(new Date());
        userInfo.setStatus(status);
        userInfo.setCreateId(user.getUserId());
        userInfo.setUrgentName(row[++index]);
        userInfo.setUrgentPhone(row[++index]);
        userInfo.setUrgentRelation(row[++index]);
        userInfo.setCreateUser(user.getUserName());
        userInfo.setCreateTime(new Date());
        //因为员工表跟登录表都要用到用户所有先保存用户
        sysUserInfoMapper.insert(userInfo);

        SysUserInfo userInfo1 = new SysUserInfo();
        userInfo1.setIsEdit(YesNo.YES);
        userInfo1.setId(userInfo.getId());
        userInfos.add(userInfo1);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;
        String initPwd = SysStaffInfoService.getInitPwd();
        //获取当前登录用户的当前公司id
        staff.setCompId(user.getCompanyId());
        staff.setUserName(userInfo.getUserName());
        staff.setUserId(userInfo.getId());
        staff.setStatus(status);
        staff.setPassword(initPwd);
        staff.setDeleted(YesNo.NO);
        staff.setCreateTime(new Date());
        staff.setCreateId(user.getUserId());
        staff.setCreateUser(user.getUserName());
        staff.setRecommendId(recommendId.toString());
        staff.setRecommendType(0);
        if (StringUtils.isNotEmpty(recommendId.toString())) {
            staff.setRecommendType(1);
        }

        //保存到员工表
        this.save(staff);


        FamilyMember family = new FamilyMember();
        family.setName(row[++index]);
        family.setRelationship(row[++index]);
        family.setMobile(row[++index]);
        family.setProfession(row[++index]);
        family.setContactAddress(row[++index]);
        StaffInfoVo staffInfoVo = new StaffInfoVo();
        staffInfoVo.setUserId(userInfo.getId());
        staffInfoVo.setStaffId(staff.getId());
        //其中一个不为空就保存
        if (DataUtil.isNotEmpty(family.getName()) || DataUtil.isNotEmpty(family.getRelationship()) ||
                DataUtil.isNotEmpty(family.getMobile()) || DataUtil.isNotEmpty(family.getProfession()) || DataUtil.isNotEmpty(family.getContactAddress())) {
            this.familyMemberService.addFamilyMember(family, staffInfoVo);
        }

        SysLoginInfo loginInfo = new SysLoginInfo();
        loginInfo.setMobileNumber(userInfo.getPhoneNumber());
        loginInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));
        loginInfo.setStatus(StatusEnum.activity.getValue());
        loginInfo.setUserId(userInfo.getId());
        loginInfo.setCreateId(user.getUserId());
        sysLoginInfoService.addOrUpdateLogin(loginInfo);
//        SmsDto smsDto = new SmsDto();
//        smsDto.setMobile(userInfo.getPhoneNumber());
//        Map<String, Object> content = new HashMap<>();
//        content.put("userName", userInfo.getPhoneNumber());
//        content.put("passWord", initPwd);
//        smsDto.setContent(content);
//        smsDto.setTemplateCode(userTipTemplate);
//        smsDto.setSign(smsSign);
//        smsDtos.add(smsDto);

        //添加员工即为内部员工需要生成名片小程序用户账号
        //生成基础名片信息
//        saveCardUser(userInfo, loginInfo, sysCompanyInfo, row[3]);

        //推送app账户
        appUserPushService.doPushAppUser(userInfo.getId(), sysCompanyInfo.getId());

        return staff.getId();
    }

    private void checkParam(String[] row, StringBuilder errorStr,
                            UnionStaffDepart unionStaffDepart, UnionStaffPosition unionStaffPosition, UnionRoleStaff unionRoleStaff, UserVo user, StringBuilder recommendId) {
        //当前企业ID
        String companyId = user.getCompanyId();
        //表头对应下标
        //{"姓名","手机号码","部门","岗位","角色", "推荐人手机号码","状态","性别","籍贯","民族","婚姻","出生年月日"【10】,
        // "身份证号码","户籍地址","居住地址","电子邮箱","最高学历","毕业院校","所学专业"【17】};
        //必填项校验
        if (DataUtil.isEmpty(row[0]) || DataUtil.isEmpty(row[1]) || DataUtil.isEmpty(row[4]) || DataUtil.isEmpty(row[5]) || DataUtil.isEmpty(row[13])) {
            errorStr.append("请填写红色区域必填项,");
        }
        //手机号码校验
        String phone = row[1];
        if (DataUtil.isNotEmpty(phone)) {
            //手机号码进行格式校验
            phone = PhoneHomeLocationUtils.importPhoneDispose(phone);
            row[1] = phone;
            boolean fild = true;
            try {
                PhoneHomeLocationUtils.checkPhone(phone);
            } catch (DefaultException e) {
                errorStr.append(e.getMessage());
                fild = !fild;
            }
            if (StringUtils.isNotEmpty(row[21]) && !PhoneHomeLocationUtils.checkPhoneBoolean(row[21])) {
                errorStr.append("紧急联系人电话不合法,");
            }

            if (StringUtils.isNotEmpty(row[25]) && !PhoneHomeLocationUtils.checkPhoneBoolean(row[25])) {
                errorStr.append("家庭成员电话不合法,");
            }
            //手机号码判断是否已注册账户
            if (fild && sysUserInfoMapper.selectUserByPhone(phone)) {
                errorStr.append("此手机号码已注册过账户,");
            }

            //手机号码判断是否是内控名单
            MobileAndIdentityCardDto dto = new MobileAndIdentityCardDto();
            dto.setMobile(phone);
            dto.setIdentityCard(row[13]);
            MobileBlacklistQueryVo blacklistByMobile = staffBlacklistService.getBlacklistByMobile(dto,user,false);
            if (fild && DataUtil.isNotEmpty(blacklistByMobile)&&blacklistByMobile.getIsBlock()) {
                errorStr.append("已被添加到内控名单，请到内控名单查看原因,");
            }

            //身份证号码校验
            String cardId = row[13];
            //验证身份证
            if (DataUtil.isNotEmpty(cardId)) {
                if (!IdCardUtil.isValidatedAllIdcard(cardId)) {
                    errorStr.append("身份证错误");
                } else {
                    // 新增时添加身份证唯一校验 包含离职账户
                    StaffInfoVo staffInfo = this.sysStaffInfoMapper.getUserByCertificateCard(cardId,YesNo.YES);
                    if (DataUtil.isNotEmpty(staffInfo)) {
                        errorStr.append("该身份证号码已在“").append(staffInfo.getCompanyName());
                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(staffInfo.getDepartmentName())) {
                            errorStr.append("-");
                            errorStr.append(staffInfo.getDepartmentName());
                        }
                        errorStr.append("”存在");
                    }
                }
            }

            // 判断同公司的
            LeaveUserInfoVo leave = sysStaffInfoMapper.getLeaveUserByCard(cardId,phone,companyId);
            if(leave != null){
                errorStr.append("该伙伴原签约[").append(leave.getCompanyName()).append(" + ").
                        append(leave.getDepartName()).append("]，\"请联系即将签约团队的团队长在CRM-APP【伙伴签约申请】发起签约！，").
                        append("（暂不支持行政办理二次签约）");
            }
            // 判断跨公司的
            leave = sysStaffInfoMapper.getLeaveUserByCard(cardId,phone,null);
            if(leave != null){
                throw new DefaultException("该伙伴原签约["+leave.getCompanyName() +" + "+ leave.getDepartName()+"]，" +
                        "不允许直接办理二次入职，请联系行政发起审批!");
            }

            String departName = row[2];
            String jobName = row[3];
            String roleName = row[4];
            //本企业角色是否存在
            DataResult roleByName = oauthFeignService.getRoleByName(companyId, roleName);
            if (!roleByName.isSuccess()) {
                throw new DefaultException("服务异常");
            } else {
                if (DataUtil.isEmpty(roleByName.getData())) {
                    errorStr.append("角色不存在,");
                } else {
                    JSONObject obj = (JSONObject) JSON.toJSON(roleByName.getData());
                    unionRoleStaff.setRoleId(obj.get("id").toString());
                    unionRoleStaff.setRoleName(roleName);
                }
            }
            String recommendMobile = row[6];
            if (StringUtils.isNotEmpty(recommendMobile)) {
                if (recommendMobile.equals(phone)) {
                    errorStr.append("推荐人不能是自身");
                }
                StaffInfoVo staff = this.sysStaffInfoMapper.getStaffInfoByMobile(recommendMobile);
                if (DataUtil.isEmpty(staff)) {
                    errorStr.append("推荐人不存在");
                } else {
                    recommendId.append(staff.getStaffId());
                }
            }
            //本企业部门是否存在
            SysDepartmentInfo byName = null;
            if (DataUtil.isNotEmpty(departName)) {
                byName = sysDepartmentInfoService.getByName(departName, companyId);
                if (DataUtil.isEmpty(byName)) {
                    errorStr.append("部门不存在,");
                } else {
                    unionStaffDepart.setDepartmentId(byName.getId());
                }
            }
            //本企业岗位是否存在//如果没填部门则直接找岗位

            if (DataUtil.isNotEmpty(jobName)) {
                SysJobPosition jobByDepart = null;
                if (DataUtil.isNotEmpty(departName)) {
                    if (DataUtil.isNotEmpty(byName)) {
                        jobByDepart = sysJobPositionService.getJobByDepart(jobName, companyId, byName.getId());
                    }
                } else {
                    jobByDepart = sysJobPositionService.getJobByComp(jobName, companyId);
                }
                if (DataUtil.isEmpty(jobByDepart)) {
                    errorStr.append("岗位不存在,");
                } else {
                    unionStaffPosition.setPositionId(jobByDepart.getId());
                }
            }
        }
        if (StringUtils.isNotEmpty(row[11])) {
            if (DataUtil.isEmpty(MaritalStatusEnum.getCodeByName(row[11]))) {
                errorStr.append("婚姻状况错误");
            }
        }
    }

    public void saveCardUser(SysUserInfo userInfo, SysLoginInfo loginInfo, SysCompanyInfo sysCompanyInfo, String positionName) {
        QueryWrapper<SysUserInfo> userInfosQw = new QueryWrapper<>();
        userInfosQw.lambda().eq(SysUserInfo::getPhoneNumber, userInfo.getPhoneNumber())
                .orderByDesc(SysUserInfo::getCreateTime);
        List<SysUserInfo> userInfos = this.sysUserInfoMapper.selectList(userInfosQw);
        //取得当前手机号入库的所有生成用户的记录时间降序排序 第一条必是 新增的一条
        if (CollectionUtils.isNotEmpty(userInfos) && userInfos.size() > 1) {
            //通过手机号拿到最近的 删除或者离职的 员工的id
            String oldUserId = userInfos.get(1).getId();
            QueryWrapper<CardUserUnionUser> cardUnionQw = new QueryWrapper<>();
            cardUnionQw.lambda().eq(CardUserUnionUser::getUserId, oldUserId);
            CardUserUnionUser cardUserUnionUser = new CardUserUnionUser();
            cardUserUnionUser.setUserId(userInfo.getId());
            this.cardUserUnionCrmUserMapper.update(cardUserUnionUser, cardUnionQw);
            UserCardReplaceDto userReplace = new UserCardReplaceDto();
            userReplace.setNewUserId(userInfo.getId());
            userReplace.setOldUserId(oldUserId);
            this.cardFeignService.updateCardUser(userReplace);
            return;
        }
        //添加员工即为内部员工需要生成名片小程序用户账号

        //先查询改手机号是否存在绑定了的名片用户
        QueryWrapper<CardUserInfo> qw = new QueryWrapper<>();
        qw.lambda().eq(CardUserInfo::getPhoneNumber, userInfo.getPhoneNumber());
        List<CardUserInfo> cardUserInfos = cardUserInfoMapper.selectList(qw);
        CardUserInfo cardUserInfo = null;
        if (DataUtil.isNotEmpty(cardUserInfos)) {
            //如果内部员工先注册的名片，则不生成基础信息
            cardUserInfo = cardUserInfos.get(0);
            //如果没创建名片，则帮该员工创建一个名片
            DataResult<UserCardDto> cardByUserId = cardFeignService.getCardByUserId(cardUserInfo.getId());
            if (!cardByUserId.isSuccess()) {
                throw new DefaultException("名片服务异常！");
            }
            UserCardDto data = cardByUserId.getData();
            if (DataUtil.isEmpty(data)) {
                //生成基础名片信息
                UserCardDto cardDto = new UserCardDto();
                cardDto.setMobile(cardUserInfo.getPhoneNumber());
                cardDto.setUserName(cardUserInfo.getUserName());
                //crm用户ID
                cardDto.setUserId(userInfo.getId());
                //名片用户ID
                cardDto.setCustomerUserId(cardUserInfo.getId());
                cardDto.setAvatar(userInfo.getAvatar());
                cardDto.setEmail(userInfo.getEmail());
                cardDto.setCustomerUserId(cardUserInfo.getId());
                cardDto.setCreateBy(UserUtils.getUserId());
                cardDto.setAddressProvince(sysCompanyInfo.getCompanyAddrProvinceCode());
                cardDto.setAddressCity(sysCompanyInfo.getCompanyAddressCityCode());
                cardDto.setAddressArea(sysCompanyInfo.getCompanyAddressAreaCode());
                cardDto.setAddressDetail(sysCompanyInfo.getCompanyAddress());
                cardDto.setPosition(positionName);
                cardDto.setCompany(sysCompanyInfo.getCompanyName());
                DataResult<String> card = cardFeignService.createCard(cardDto);
                if (!card.isSuccess()) {
                    throw new DefaultException("服务异常！");
                }
            } else {
                //把名片账户的名片绑定该新用户
                UserCardReplaceDto userReplace = new UserCardReplaceDto();
                userReplace.setNewUserId(userInfo.getId());
                userReplace.setCardUserId(cardUserInfo.getId());
                this.cardFeignService.updateUserBycardUser(userReplace);
            }
        } else {
            cardUserInfo = new CardUserInfo();
            cardUserInfo.setUserName(userInfo.getUserName());
            cardUserInfo.setPhoneNumber(userInfo.getPhoneNumber());
            cardUserInfo.setUserPwd(loginInfo.getUserPwd());
//            cardUserInfo.setCreateBy(UserUtils.getUserId());
            cardUserInfo.setCreateTime(new Date());
            cardUserInfo.setStatus(StatusEnum.activity.getValue());
            cardUserInfoMapper.insert(cardUserInfo);
            //解除名片限制
            UserCardReplaceDto userReplace = new UserCardReplaceDto();
            userReplace.setNewUserId(null);
            userReplace.setOldUserId(userInfo.getId());
            this.cardFeignService.updateCardUser(userReplace);

            //生成基础名片信息
            UserCardDto cardDto = new UserCardDto();
            cardDto.setMobile(cardUserInfo.getPhoneNumber());
            cardDto.setUserName(cardUserInfo.getUserName());
            //crm用户ID
            cardDto.setUserId(userInfo.getId());
            //名片用户ID
            cardDto.setCustomerUserId(cardUserInfo.getId());
            cardDto.setAvatar(userInfo.getAvatar());
            cardDto.setEmail(userInfo.getEmail());
            cardDto.setCustomerUserId(cardUserInfo.getId());
//            cardDto.setCreateBy(UserUtils.getUserId());
            cardDto.setAddressProvince(sysCompanyInfo.getCompanyAddrProvinceCode());
            cardDto.setAddressCity(sysCompanyInfo.getCompanyAddressCityCode());
            cardDto.setAddressArea(sysCompanyInfo.getCompanyAddressAreaCode());
            cardDto.setAddressDetail(sysCompanyInfo.getCompanyAddress());
            cardDto.setPosition(positionName);
            cardDto.setCompany(sysCompanyInfo.getCompanyName());
            DataResult<String> card = cardFeignService.createCard(cardDto);
            if (!card.isSuccess()) {
                throw new DefaultException("服务异常！");
            }
        }

        //关联内部员工信息
        CardUserUnionUser cardUserUnionUser = new CardUserUnionUser();
        cardUserUnionUser.setId(UUIDutils.getUUID32());
        cardUserUnionUser.setCardId(cardUserInfo.getId());
        cardUserUnionUser.setUserId(userInfo.getId());
        cardUserUnionCrmUserMapper.insert(cardUserUnionUser);
    }

    @Async
    public void sendInitPwdSms(List<SmsDto> smsDtos) {
        if (DataUtil.isNotEmpty(smsDtos)) {
            smsDtos.stream().forEach(item -> {
                smsFeignService.sendSms(item);
            });
        }
    }


    @Override
    public List<BosStaffInfoVo> getStaff(String companyId, String departId, String positionId) {
        return sysStaffInfoMapper.getStaff(companyId, departId, positionId);
    }

    @Override
    public IPage<BosStaffInfoVo> getAdmins(AdminsPageDto dto) {
        IPage<BosStaffInfoVo> voIPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        IPage<BosStaffInfoVo> page= sysStaffInfoMapper.selectAdmins(dto, voIPage);
        List<BosStaffInfoVo> records = page.getRecords();
        if(DataUtil.isNotEmpty(records)){
            records.stream().forEach(item->{
                //获取后台管理员以及关联企业信息
                BackUserRefVo backRef = ceoCompanyRefService.getBackRef(item.getId());
                if(DataUtil.isNotEmpty(backRef) && DataUtil.isNotEmpty(backRef.getCompanys())){
                    //企业名称
                    List<String> collect = backRef.getCompanys().stream().map(s -> s.getCompanyName()).collect(Collectors.toList());
                    item.setCompanys(collect);
                }
            });
        }
        page.setRecords(records);
        return page;
    }

    /**
     * @return boolean
     * @Author PengQiang
     * @Description //TODO   手机合法查看
     * @Date 2020/12/20 21:27
     * @Param [phone]
     */
    private boolean checkPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        if (phone.trim().length() == 11) {
            return false;
        }

        String regex = "^(1[3-9]\\d{9}$)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            return false;
        }

        return true;
    }


    @Override
    public UserDeptVo getUserDeptVo(String userId) {
        String staffId = this.getStaffIdByUserId(userId);
        if (StringUtils.isEmpty(staffId)) {
            throw new DefaultException("没有该员工");
        }
        return this.sysStaffInfoMapper.selectUserDeptInfoById(staffId);
    }


    /**
     * 用户子级部门
     *
     * @param userId
     * @return java.util.List<java.lang.String>
     * @author PengQiang
     * @description DELL
     * @date 2021/1/7 17:54
     */
    @Override
    public List<String> getUserSubordinates(String userId) {
        String staffId = this.getStaffIdByUserId(userId);
        if (StringUtils.isEmpty(staffId)) {
            throw new DefaultException("没有该员工");
        }

        //子级部门id集合
        List<String> depIds = new ArrayList<>();
        String thisDep = "";
        SysStaffInfo staff = this.getById(staffId);
        QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
        adminQw.lambda().eq(CompanyAdmin::getCompanyId, staff.getCompId());
        CompanyAdmin com = this.companyAdminMapper.selectOne(adminQw);
        if (!staff.getId().equals(com.getStaffId())) {
            QueryWrapper<SysDepartmentInfo> depQw = new QueryWrapper<>();
            depQw.lambda().eq(SysDepartmentInfo::getAdminAccount, staffId);
            depQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.activity.getValue());
            SysDepartmentInfo dep = this.sysDepartmentInfoMapper.selectOne(depQw);
            if (dep == null) {
                return null;
            }
            thisDep = dep.getId();
            depIds.add(thisDep);
        }
        //获取全部的部门
        List<SysDepartmentInfoVo> deps = sysDepartmentInfoMapper.getAllDepByCompanyId(staff.getCompId());

        this.getChilden(depIds, deps, thisDep);
        return depIds;
    }


    @Override
    public IPage<SysUserClewCollectVo> getSubordinatesUserClewCollect(PageQueryDto dto, String userId) {
        //通过用户id获取员工
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, userId);
        staffQw.lambda().and(sta -> sta.eq(SysStaffInfo::getStatus, StatusEnum.activity.getValue()).or().eq(SysStaffInfo::getStatus, StaffStatusEnum.COLLABORATE.getCode()));
        SysStaffInfo staff = this.getOne(staffQw);
        if (staff == null) {
            throw new DefaultException("未找到员工");
        }
        //获取当前员工企业
        QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
        adminQw.lambda().eq(CompanyAdmin::getDeleted, YesNo.NO);
        adminQw.lambda().eq(CompanyAdmin::getCompanyId, staff.getCompId());
        CompanyAdmin com = this.companyAdminMapper.selectOne(adminQw);
        //设置分页参数
        IPage<SysUserClewCollectVo> iPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        //用于请求获取用户分页
        List<String> userIds = new ArrayList<>();
        //线索集合
        List<UserClewDto> clews;
        SysUserClewCollectVo userInfo = this.sysStaffInfoMapper.selectUserInfo(staff.getId());
        //查看当前员工是否是企业管理员
        if (!DataUtil.isNotEmpty(com) || !staff.getId().equals(com.getStaffId())) {
            //不是企业管理员 查看是否是部门管理员
            QueryWrapper<SysDepartmentInfo> depQw = new QueryWrapper<>();
            depQw.lambda().eq(SysDepartmentInfo::getAdminAccount, staff.getId());
            depQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.activity.getValue());
            SysDepartmentInfo dep = this.sysDepartmentInfoMapper.selectOne(depQw);
            //不是部门管理员获取自己的线索总汇
            if (dep == null) {
                if (dto.getCurrent() > 1) {
                    return iPage;
                }
                iPage = new Page<>(dto.getCurrent(), dto.getCurrent());
                iPage.setRecords(new ArrayList<>());
                iPage.getRecords().add(userInfo);
                iPage.setTotal(1);
                userIds.add(iPage.getRecords().get(0).getUserId());
                clews = this.clewFeignService.getClews(userIds).getData();
                if (CollectionUtils.isEmpty(clews)) {
                    return iPage;
                }
                BeanUtils.copyProperties(clews.get(0), iPage.getRecords().get(0));
                return iPage;
            }
            //获取全部的部门
            List<SysDepartmentInfoVo> deps = sysDepartmentInfoMapper.getAllDepByCompanyId(staff.getCompId());
            //用户id集合暂存部门id集合
            userIds.add(dep.getId());
            getChilden(userIds, deps, dep.getId());
            iPage = this.sysStaffInfoMapper.getStaffByDepIds(userIds, iPage, staff.getCompId(), userInfo.getUserId());
            if (iPage.getRecords().size() > 0) {
                iPage.getRecords().get(0).setDepartAdmin(true);
            }
            userIds.removeAll(userIds);
        } else {
            //企业管理员不需要获取下级部门
            iPage = this.sysStaffInfoMapper.getStaffByDepIds(null, iPage, staff.getCompId(), userInfo.getUserId());
            if (CollectionUtils.isEmpty(iPage.getRecords())) {
                return iPage;
            }
            iPage.getRecords().get(0).setCompanyAdmin(true);
        }
        if (CollectionUtils.isEmpty(iPage.getRecords())) {
            iPage.setRecords(new ArrayList<>());
        }
        userIds = iPage.getRecords().stream().map(SysUserClewCollectVo::getUserId).collect(Collectors.toList());
        clews = this.clewFeignService.getClews(userIds).getData();
        if (CollectionUtils.isEmpty(clews)) {
            return iPage;
        }
        //转为有序map
        LinkedHashMap<String, SysUserClewCollectVo> userClewMap = iPage.getRecords().stream().collect(Collectors.toMap(SysUserClewCollectVo::getUserId, a -> a, (k1, k2) -> k1, LinkedHashMap::new));
        for (UserClewDto clew : clews) {
            BeanUtils.copyProperties(clew, userClewMap.get(clew.getUserId()));
        }
        iPage.setRecords(new ArrayList<>(userClewMap.values()));
        return iPage;
    }

    @Override
    public CopyStaffInfoVo selectStaffInfo(String staffId) {
        return sysStaffInfoMapper.selectStaffInfo(staffId);
    }

    @Override
    public SysUserInfoVo selectStaffByUserId(String userId,UserVo userVo,boolean isTraverse) {
        SysUserInfoVo ceoInfo = this.ceoUserInfoService.getCeoInfoByUserId(userId);
        if (DataUtil.isNotEmpty(ceoInfo)) {
            ceoInfo.setSensitivePhone(ceoInfo.getPhoneNumber());
            ceoInfo.setIdentityCardNum(ceoInfo.getCertificateCard());
            ceoInfo.setPhoneNumber(CommonUtils.mobileEncrypt(ceoInfo.getPhoneNumber()));
            ceoInfo.setCertificateCard(CommonUtils.idEncrypt( ceoInfo.getCertificateCard()));
            if (StringUtils.isNotEmpty(ceoInfo.getImState())) {
                DictQuseryVo imState = this.dictService.getListById(ceoInfo.getImState());
                if (DataUtil.isNotEmpty(imState)) {
                    ceoInfo.setImStateName(imState.getDictName());
                }
            }
            return ceoInfo;
        }
        //通过用户id查询员工
        SysUserInfoVo userInfo = sysStaffInfoMapper.selectStaffByUserId(userId);

        if (DataUtil.isEmpty(userInfo)) {
            log.error("用户信息不存在！" + userId);
            throw new DefaultException("用户信息不存在");
        }
        if (StringUtils.isNotEmpty(userInfo.getImState())) {
            DictQuseryVo imState = this.dictService.getListById(userInfo.getImState());
            if (DataUtil.isNotEmpty(imState)) {
                userInfo.setImStateName(imState.getDictName());
            }
        }
        //推荐人
        RecommendInfoVo recommendInfo = null;
        if (DataUtil.isNotEmpty(userInfo) && StringUtils.isNotEmpty(userInfo.getRecommendId()) && userInfo.getRecommendType().intValue() == 1) {
            recommendInfo = this.sysStaffInfoMapper.getRecommendInfo(userInfo.getRecommendId());
            userInfo.setRecommendInfo(recommendInfo);
        }
        if (DataUtil.isNotEmpty(recommendInfo) && StringUtils.isNotEmpty(recommendInfo.getRecommendId()) && userInfo.getRecommendType().intValue() == 1) {
            recommendInfo = this.sysStaffInfoMapper.getRecommendInfo(recommendInfo.getRecommendId());
            userInfo.setRecommendSecond(recommendInfo);
        }
        if(isTraverse==true){
            BlacklistOperationRecordAddDto operationRecord = new BlacklistOperationRecordAddDto();
            operationRecord.setMobile(userInfo.getPhoneNumber());
            operationRecord.setIdentityCard(userInfo.getCertificateCard());
            operationRecord.setType(0);
            operationRecord.setRemarks("app伙伴详情");
            blacklistOperationRecordService.addBlacklistOperationRecord(operationRecord,userVo);
        }
        UserVo user = new UserVo();
        user.setUserId(userInfo.getId());
        user.setCompanyId(userInfo.getCompanyId());

        //家庭成员实体
        QueryWrapper<FamilyMember> fmQw = new QueryWrapper<>();
        fmQw.lambda().eq(FamilyMember::getUserId, userInfo.getId());
        fmQw.lambda().orderByDesc(FamilyMember::getOrderNum);
        userInfo.setFamilyMembers(this.familyMemberService.list(fmQw));

        //个人履历
        QueryWrapper<UserResume> rq = new QueryWrapper<>();
        rq.lambda().eq(UserResume::getUserId, userInfo.getId());
        rq.lambda().orderByDesc(UserResume::getCreateTime);
        userInfo.setUserResumes(this.userResumeService.list(rq));

        //获取荣耀和惩罚记录、家庭成员
        getRecord(userInfo.getStaffId(), userInfo);
        //添加合规承诺书、学历证书、合作申请表
        getLetterOfUndertakingAndDiplomas(userInfo);

        //查询企业内部关系信息
        SysStaffRelationDto sysStaffRelationDto = new SysStaffRelationDto();
        sysStaffRelationDto.setRelationStaffId(userInfo.getStaffId());
        List<SysStaffRelationVo> sysStaffRelationVos = this.sysStaffRelationService.queryRelationList(sysStaffRelationDto).getSysStaffRelationVos();
        log.info("企业内部关系数据 {}", sysStaffRelationVos);
        userInfo.setStaffRelationDtoList(sysStaffRelationVos);

        AdminVo admin = this.getIsAdmin(user);
        userInfo.setIsCompanyAdmin(admin.getIsCompanyAdmin());
        userInfo.setIsDepartAdmin(admin.getIsDepartAdmin());
        userInfo.setSensitivePhone(userInfo.getPhoneNumber());
        userInfo.setIdentityCardNum(userInfo.getCertificateCard());
        userInfo.setPhoneNumber(CommonUtils.mobileEncrypt(userInfo.getPhoneNumber()));
        userInfo.setCertificateCard(CommonUtils.idEncrypt( userInfo.getCertificateCard()));
        if (admin.getIsCompanyAdmin()) {
            userInfo.setSuperiorName(userInfo.getUserName());
            return userInfo;
        }
        if (StringUtils.isEmpty(userInfo.getDepartId())) {
            return userInfo;
        }
        if (admin.getIsDepartAdmin()) {
            String departId = userInfo.getDepartId();
            if (departId.indexOf("_") == -1) {
                QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
                staffQw.lambda().inSql(SysStaffInfo::getId, "select staff_id from company_admin where company_id = '".concat(userInfo.getCompanyId()).concat("'"));
                SysStaffInfo sysStaffInfo = this.getOne(staffQw);
                userInfo.setSuperiorName(DataUtil.isEmpty(sysStaffInfo) ? null : sysStaffInfo.getUserName());
                return userInfo;
            }
            departId = departId.substring(0, departId.lastIndexOf("_"));
            SysDepartmentInfo departInfo = this.sysDepartmentInfoMapper.selectById(departId);
            if (StringUtils.isEmpty(departInfo.getAdminAccount())) {
//                userInfo.setSuperiorName("暂无上级");
                return userInfo;
            }
        }

        SysDepartmentInfo departInfo = this.sysDepartmentInfoMapper.selectById(userInfo.getDepartId());
        if (StringUtils.isEmpty(departInfo.getAdminAccount())) {
            return userInfo;
        }
        SysStaffInfo staffInfo = this.sysStaffInfoMapper.selectById(departInfo.getAdminAccount());
        userInfo.setSuperiorName(staffInfo.getUserName());
        return userInfo;
    }

    @Override
    public IPage<BosStaffInfoVo> getWxPageAllStaff(SysStaffInfoPageDto dto) {
        if (StringUtils.isEmpty(dto.getCompanyId())) {
            throw new DefaultException("员工企业获取失败");
        }
        IPage<BosStaffInfoVo> iPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        return this.sysStaffInfoMapper.getWxPageAllStaff(dto, iPage);
    }

    @Override
    public void doEmptySubordinatesUserClew(String id) {
        //通过用户id获取员工
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, id);
        staffQw.lambda().and(sta -> sta.eq(SysStaffInfo::getStatus, StatusEnum.activity.getValue()).or().eq(SysStaffInfo::getStatus, StaffStatusEnum.COLLABORATE.getCode()));
        SysStaffInfo staff = this.getOne(staffQw);
        if (staff == null) {
            throw new DefaultException("未找到员工");
        }
        //获取当前员工企业
        QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
        adminQw.lambda().eq(CompanyAdmin::getDeleted, YesNo.NO);
        adminQw.lambda().eq(CompanyAdmin::getCompanyId, staff.getCompId());
        CompanyAdmin com = this.companyAdminMapper.selectOne(adminQw);
        //用于请求获取用户分页
        List<String> userIds = new ArrayList<>();
        //线索集合
        List<UserClewDto> clews;
        SysUserClewCollectVo userInfo = this.sysStaffInfoMapper.selectUserInfo(staff.getId());
        userIds.add(userInfo.getUserId());
        List<SysUserClewCollectVo> users = new ArrayList<>();
        //查看当前员工是否是企业管理员
        if (!staff.getId().equals(com.getStaffId())) {
            //不是企业管理员 查看是否是部门管理员
            QueryWrapper<SysDepartmentInfo> depQw = new QueryWrapper<>();
            depQw.lambda().eq(SysDepartmentInfo::getAdminAccount, staff.getId());
            depQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.activity.getValue());
            SysDepartmentInfo dep = this.sysDepartmentInfoMapper.selectOne(depQw);
            //不是部门管理员获取自己的线索总汇
            if (dep == null) {
                DataResult<Object> reslut = this.clewFeignService.doEmpatySubordinateUserClew(userIds);
                if (!reslut.isSuccess()) {
                    throw new DefaultException(reslut.getMessage());
                }
                return;
            }
            //获取全部的部门
            List<SysDepartmentInfoVo> deps = sysDepartmentInfoMapper.getAllDepByCompanyId(staff.getCompId());
            //用户id集合暂存部门id集合
            userIds.add(dep.getId());
            getChilden(userIds, deps, dep.getId());
            users = this.sysStaffInfoMapper.getStaffAllByDepIds(userIds, staff.getCompId());
            userIds.removeAll(userIds);
        } else {
            //企业管理员不需要获取下级部门
            users = this.sysStaffInfoMapper.getStaffAllByDepIds(null, staff.getCompId());
        }
        userIds.addAll(users.stream().map(SysUserClewCollectVo::getUserId).collect(Collectors.toList()));
        DataResult<Object> reslut = this.clewFeignService.doEmpatySubordinateUserClew(userIds);
        if (!reslut.isSuccess()) {
            throw new DefaultException(reslut.getMessage());
        }
    }

    @Override
    public AdminVo getIsAdmin(UserVo user) {
        AdminVo admin = new AdminVo();
        String staffId = this.getStaffIdByUserId(user.getUserId());
        QueryWrapper<CompanyAdmin> comAdminQw = new QueryWrapper<>();
        comAdminQw.lambda().eq(CompanyAdmin::getCompanyId, user.getCompanyId());
        CompanyAdmin comAdmin = this.companyAdminMapper.selectOne(comAdminQw);
        admin.setIsCompanyAdmin(DataUtil.isNotEmpty(comAdmin) && comAdmin.getStaffId().equals(staffId));
        if (!admin.getIsCompanyAdmin()) {
            QueryWrapper<SysDepartmentInfo> depAdminQw = new QueryWrapper<>();
            depAdminQw.lambda().select(SysDepartmentInfo::getId)
                    .eq(SysDepartmentInfo::getAdminAccount, staffId).
                    eq(SysDepartmentInfo::getStatus, StatusEnum.activity);
            SysDepartmentInfo dep = this.sysDepartmentInfoMapper.selectOne(depAdminQw);
            admin.setIsDepartAdmin(DataUtil.isNotEmpty(dep));
            return admin;
        }
        admin.setIsDepartAdmin(Boolean.FALSE);
        return admin;
    }

    @Override
    public String getDepartId(String userId) {
        String staffId = this.getStaffIdByUserId(userId);
        QueryWrapper<UnionStaffDepart> usdQW = new QueryWrapper<>();
        usdQW.lambda().eq(UnionStaffDepart::getStaffId, staffId);
        UnionStaffDepart usd = unionStaffDeparService.getOne(usdQW);
        if (DataUtil.isEmpty(usd)) {
            return null;
        }
        return usd.getDepartmentId();
    }

    @Override
    public StaffInfoVo getStaffInfo(String userId) {
        StaffInfoVo staffInfoVo = this.sysStaffInfoMapper.getStaffInfoInner(userId);
        if (DataUtil.isEmpty(staffInfoVo)) {
            throw new DefaultException("找不到伙伴");
        }
        return staffInfoVo;
    }

    @Override
    public List<com.zerody.user.api.vo.SysUserInfoVo> getUserByDepartOrRole(String departId, String roleId, String companyId) {

        return this.sysStaffInfoMapper.getUserByDepartOrRole(departId, roleId, companyId);
    }

    @Override
    public List<com.zerody.user.api.vo.SysUserInfoVo> getSuperiorUesrByUserAndRole(String userId, String roleId) {
        String staffId = this.getStaffIdByUserId(userId);
        QueryWrapper<UnionStaffDepart> usdQw = new QueryWrapper<>();
        usdQw.lambda().eq(UnionStaffDepart::getStaffId, staffId);
        UnionStaffDepart usd = this.unionStaffDeparService.getOne(usdQw);
        if (DataUtil.isEmpty(usd)) {
            return null;
        }
        List<UnionStaffDepart> usds = this.unionStaffDepartMapper.getStaffByRole(roleId);
        List<String> staffIds = this.getSuperiorStaff(usd.getDepartmentId(), usds);
        if (CollectionUtils.isNotEmpty(staffIds)) {
            return this.sysStaffInfoMapper.getStaffByIds(staffIds);
        }
        return null;
    }

    @Override
    public IPage<UserPerformanceReviewsVo> getPagePerformanceReviews(UserPerformanceReviewsPageDto param) throws ParseException {
        String time = null;
        //限制放款日期条件查询不能超过当前月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        IPage<UserPerformanceReviewsVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.sysStaffInfoMapper.getPagePerformanceReviews(param, iPage);
        if (DataUtil.isEmpty(iPage.getRecords())) {
            return iPage;
        }
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(param.getTime())) {
            param.setTime(sdf.format(new Date()));
        }
        String thisDateString = sdf.format(new Date());
        String[] times = param.getTime().split("-");
        String[] thisDateStrings = thisDateString.split("-");
        if ((Integer.valueOf(times[0]) > Integer.valueOf(thisDateStrings[0])) || (Integer.valueOf(times[0]).equals(Integer.valueOf(thisDateStrings[0])) && Integer.valueOf(times[1]) > Integer.valueOf(thisDateStrings[1]))) {
            iPage.setTotal(0);
            iPage.setRecords(new ArrayList<>());
            return iPage;
        }
        this.setPerformanceReviews(param, iPage.getRecords());
//        UserPerformanceReviewsVo userPerformanceReviewsVo=new UserPerformanceReviewsVo();
//        this.sysStaffInfoMapper.getPagePerformanceReviews(param).forEach(item->{
//            userPerformanceReviewsVo.setPerformanceIncome(String.valueOf(Double.valueOf(userPerformanceReviewsVo.getPerformanceIncome())+Double.valueOf(item.getPerformanceIncome())));
//            userPerformanceReviewsVo.setPaymentNumber(userPerformanceReviewsVo.getPaymentNumber().add(item.getPaymentNumber()));
//            userPerformanceReviewsVo.setLoanMoney(String.valueOf(Double.valueOf(userPerformanceReviewsVo.getLoanMoney())+Double.valueOf(item.getLoanMoney())));
//            userPerformanceReviewsVo.setLoanNumber(userPerformanceReviewsVo.getLoanNumber()+item.getLoanNumber());
//            userPerformanceReviewsVo.setSignOrderMoney(String.valueOf(Double.valueOf(userPerformanceReviewsVo.getSignOrderMoney())+Double.valueOf(item.getSignOrderMoney())));
//            userPerformanceReviewsVo.setSignOrderNumber(userPerformanceReviewsVo.getSignOrderNumber()+item.getSignOrderNumber());
//            userPerformanceReviewsVo.setWaitApprovalMoney(String.valueOf(Double.valueOf(userPerformanceReviewsVo.getWaitApprovalMoney())+Double.valueOf(item.getWaitApprovalMoney())));
//            userPerformanceReviewsVo.setWaitApprovalNumber(userPerformanceReviewsVo.getWaitApprovalNumber()+item.getWaitApprovalNumber());
//        });
//        userPerformanceReviewsVo.setCompanyName("合计");
//        iPage.getRecords().add(userPerformanceReviewsVo);
        return iPage;
    }
    @Override
    public List<UserPerformanceReviewsVo> getPagePerformanceReviewsList(UserPerformanceReviewsPageDto param) throws ParseException {
        String time = null;
        //限制放款日期条件查询不能超过当前月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<UserPerformanceReviewsVo> list = this.sysStaffInfoMapper.getPagePerformanceReviews(param);
        if (DataUtil.isEmpty(list)) {
            return list;
        }
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(param.getTime())) {
            param.setTime(sdf.format(new Date()));
        }
        String thisDateString = sdf.format(new Date());
        String[] times = param.getTime().split("-");
        String[] thisDateStrings = thisDateString.split("-");
        this.setPerformanceReviews(param, list);
        return list;
    }

    @Override
    public List<UserPerformanceReviewsVo> doPerformanceReviewsExport(UserPerformanceReviewsPageDto param, HttpServletResponse response) throws IOException, ParseException {
        List<UserPerformanceReviewsVo> list = null;
        if (CollectionUtils.isEmpty(param.getUserIds())) {
            list = this.getPagePerformanceReviewsList(param);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(param.getTime())) {
                param.setTime(sdf.format(new Date()));
            }
            list = this.sysStaffInfoMapper.getPagePerformanceReviewsByUserIds(param.getUserIds());
            String thisDateString = sdf.format(new Date());
            String[] times = param.getTime().split("-");
            String[] thisDateStrings = thisDateString.split("-");
            if ((Integer.valueOf(times[0]) > Integer.valueOf(thisDateStrings[0])) || (Integer.valueOf(times[0]).equals(Integer.valueOf(thisDateStrings[0])) && Integer.valueOf(times[1]) > Integer.valueOf(thisDateStrings[1]))) {
            } else {
                this.setPerformanceReviews(param, list);
            }
        }
        return list;
    }

    @Override
    public StaffInfoVo getStaffInfoByCardUserId(String cardUserId) {
        QueryWrapper<CardUserUnionUser> cardUserQw = new QueryWrapper<>();
        cardUserQw.lambda().eq(CardUserUnionUser::getCardId, cardUserId);
        CardUserUnionUser cardUserUnionUser = cardUserUnionCrmUserMapper.selectOne(cardUserQw);
        if (DataUtil.isEmpty(cardUserUnionUser)) {
            return null;
        }
        return this.getStaffInfo(cardUserUnionUser.getUserId());
    }

    @Override
    public List<SysDepartmentInfoVo> getUserSubordinateStructure(String userId) {
        return null;
    }

    @Override
    public List<StaffInfoVo> getDepartDirectStaffInfo(String departId) {
        List<StaffInfoVo> staffs = this.sysStaffInfoMapper.getDepartDirectStaffInfo(departId);
        return staffs;
    }

    @Override
    public IPage<BosStaffInfoVo> getPageAllActiveDutyStaff(SysStaffInfoPageDto sysStaffInfoPageDto) {
        if (StringUtils.isNotEmpty(sysStaffInfoPageDto.getCompanyId())) {
            //  如果企业id不为空 则查询该企业的负责人
            QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
            adminQw.lambda().eq(CompanyAdmin::getCompanyId, sysStaffInfoPageDto.getCompanyId());
            CompanyAdmin admin = this.companyAdminMapper.selectOne(adminQw);
            //  获取该企业负责人
            sysStaffInfoPageDto.setStaffId(DataUtil.isEmpty(admin) ? null : admin.getStaffId());
        }
        if (StringUtils.isNotEmpty(sysStaffInfoPageDto.getDepartId())) {
            //  获取得到部门负责人
            SysDepartmentInfo departInfo = this.sysDepartmentInfoMapper.selectById(sysStaffInfoPageDto.getDepartId());
            sysStaffInfoPageDto.setStaffId(departInfo.getAdminAccount());
        }
        IPage<BosStaffInfoVo> infoVoIPage = new Page<>(sysStaffInfoPageDto.getCurrent(), sysStaffInfoPageDto.getPageSize());
        return sysStaffInfoMapper.getPageAllActiveDutyStaff(sysStaffInfoPageDto, infoVoIPage);
    }

    @Override
    public List<StaffInfoVo> getStaffInfoByIds(List<String> userId) {
        if (CollectionUtils.isEmpty(userId)) {
            return new ArrayList<>();
        }
        List<StaffInfoVo> user = this.sysStaffInfoMapper.getStaffInfoByIds(userId);
        if (user == null) {
            user = new ArrayList<>();
        }
        List<StaffInfoVo> ceoUser = this.ceoUserInfoService.getStaffInfoByIds(userId);
        if (CollectionUtils.isEmpty(ceoUser)) {
            return user;
        }
        user.addAll(ceoUser);
        return user;
    }

    @Override
    public List<String> getSubordinateUserByUserId(String userId) {
        StaffInfoVo staff = this.getStaffInfo(userId);
        UserVo user = new UserVo();
        user.setCompanyId(staff.getCompanyId());
        user.setUserId(staff.getUserId());
        AdminVo admin = this.getIsAdmin(user);
        if (admin.getIsCompanyAdmin()) {
            List<String> userIds = this.sysStaffInfoMapper.getUserIdByCompIdOrDeptId(staff.getCompanyId(), null);
            return userIds;
        }
        if (admin.getIsDepartAdmin()) {
            List<String> userIds = this.sysStaffInfoMapper.getUserIdByCompIdOrDeptId(staff.getCompanyId(), staff.getDepartId());
            return userIds;
        }
        return null;
    }

    @Override
    public List<CustomerQueryDimensionalityVo> getCustomerQuerydimensionality(UserVo user) {
        List<CustomerQueryDimensionalityVo> result = null;
        //总裁获取全部企业的
        if (user.isCEO()) {
            result = this.sysCompanyInfoMapper.getCustomerQuerydimensionality();
            return result;
        }
        UserTypeInfoVo userType = this.sysUserInfoService.getUserTypeInfo(user);
        // 企业管理获取 一级部门的
        if (userType.getUserType().intValue() == UserTypeInfo.COMPANY_ADMIN) {
            //企业管理员不管他的部门
            user.setDeptId(null);
            result = this.sysDepartmentInfoMapper.getCustomerQuerydimensionality(user);
            CustomerQueryDimensionalityVo dimeVo = new CustomerQueryDimensionalityVo();
            dimeVo.setId(CustomerQueryType.POST);
            dimeVo.setName("本企业客户");
            result.add(0, dimeVo);
        } else if (userType.getUserType().intValue() == UserTypeInfo.DEPUTY_GENERAL_MANAGERv) {
            result = this.sysDepartmentInfoMapper.getCustomerQuerydimensionality(user);
            CustomerQueryDimensionalityVo dimeVo = new CustomerQueryDimensionalityVo();
            dimeVo.setId(CustomerQueryType.SUBORDINATE);
            dimeVo.setName("本部门及下级部门负责的");
            result.add(0, dimeVo);
            dimeVo = new CustomerQueryDimensionalityVo();
            dimeVo.setId(CustomerQueryType.POST);
            dimeVo.setName("本部门的");
            result.add(0, dimeVo);
        } else if (userType.getUserType().intValue() == UserTypeInfo.LONG_TEAM) {
            result = this.sysStaffInfoMapper.getCustomerQuerydimensionality(user);
            CustomerQueryDimensionalityVo dimeVo = new CustomerQueryDimensionalityVo();
            dimeVo.setId(CustomerQueryType.SUBORDINATE);
            dimeVo.setName("本人及下属的");
            result.add(0, dimeVo);
        }
        if (CollectionUtils.isEmpty(result)) {
            result = new ArrayList<>();
        }
        CustomerQueryDimensionalityVo dimeVo = new CustomerQueryDimensionalityVo();
        dimeVo.setId(CustomerQueryType.CHARGE);
        dimeVo.setName("我的客户");
        result.add(0, dimeVo);
        return result;
    }

    @Override
    public SysStaffInfoDetailsVo getStaffDetailsCount(String userId) {
        String type = "A";
        SysStaffInfoDetailsVo sysStaffInfoDetailsVo = this.sysStaffInfoMapper.getStaffinfoDetails(userId);
        if (Objects.nonNull(sysStaffInfoDetailsVo)) {
            //获取统计
            DataResult<PerformanceInfoVo> performanceInfoVoDataResult = this.contractService.getPerformanceInfoContainSubordinate(sysStaffInfoDetailsVo.getUserId());
            sysStaffInfoDetailsVo.setLoanMoney(performanceInfoVoDataResult.getData().getPaymentMoney());
            sysStaffInfoDetailsVo.setPaymentMoney(performanceInfoVoDataResult.getData().getMoney());
            sysStaffInfoDetailsVo.setSignOrderMoney(performanceInfoVoDataResult.getData().getSignOrderMoney());
            sysStaffInfoDetailsVo.setSignFailNumber(performanceInfoVoDataResult.getData().getSignFailNumber());
            CompanyAdmin companyAdmin = this.companyAdminMapper.selectOne(new QueryWrapper<CompanyAdmin>().lambda().eq(CompanyAdmin::getStaffId, sysStaffInfoDetailsVo.getStaffId()));
            if (Objects.nonNull(companyAdmin)) {
                if (org.apache.commons.lang.StringUtils.isNotEmpty(companyAdmin.getCompanyId())) {
                    sysStaffInfoDetailsVo.setCompanyId(companyAdmin.getCompanyId());
                }
            }
            QueryWrapper<SysDepartmentInfo> depAdminQw = new QueryWrapper<>();
            depAdminQw.lambda().select(SysDepartmentInfo::getId)
                    .eq(SysDepartmentInfo::getAdminAccount, sysStaffInfoDetailsVo.getStaffId()).
                    eq(SysDepartmentInfo::getStatus, StatusEnum.activity);
            SysDepartmentInfo dep = this.sysDepartmentInfoMapper.selectOne(depAdminQw);
            if (Objects.nonNull(dep)) {
                if (org.apache.commons.lang.StringUtils.isNotEmpty(dep.getAdminAccount())) {
                    sysStaffInfoDetailsVo.setDeptId(dep.getAdminAccount());
                }
            }
            //客户统计
            DataResult<Integer> customerCount = this.customerService.getStaffCustomerDetailsCount(sysStaffInfoDetailsVo.getUserId(), sysStaffInfoDetailsVo.getCompanyId(), sysStaffInfoDetailsVo.getDeptId(), null);
            sysStaffInfoDetailsVo.setCustomerCount(customerCount.getData());
            //A类统计
            DataResult<Integer> customerTypeCount = this.customerService.getStaffCustomerDetailsCount(sysStaffInfoDetailsVo.getUserId(), sysStaffInfoDetailsVo.getCompanyId(), sysStaffInfoDetailsVo.getDeptId(), type);
            sysStaffInfoDetailsVo.setCustomerTypeCount(customerTypeCount.getData());

        }
        return sysStaffInfoDetailsVo;
    }

    @Override
    public List<CheckLoginVo> getNotSendPwdSmsStaff(String companyId) {
        return this.sysStaffInfoMapper.getNotSendPwdSmsStaff(companyId);
    }

    @Override
    public void doSendStaffPwdSms(List<CheckLoginVo> subList) {
        SmsDto smsDto;
        Map<String, Object> content;
        UpdateWrapper<SysStaffInfo> uwStaff = new UpdateWrapper<>();
        for (CheckLoginVo user : subList) {
            //循环发送短信
            smsDto = new SmsDto();
            smsDto.setMobile(user.getPhoneNumber());
            content = new HashMap<>();
            content.put("userName", user.getPhoneNumber());
            content.put("passWord", user.getUserPwd());
            smsDto.setContent(content);
            smsDto.setTemplateCode(userTipTemplate);
            smsDto.setSign(smsSign);
            smsFeignService.sendSms(smsDto);
            //发送短信后明文密码清空
            uwStaff.clear();
            uwStaff.lambda().set(SysStaffInfo::getPassword, "");
            uwStaff.lambda().eq(SysStaffInfo::getId, user.getStaffId());
            this.update(uwStaff);
        }

    }

    @Override
    public Boolean getLoginUserIsSuperion(UserVo user, String userId) {
        if (userId.equals(user.getUserId())) {
            return Boolean.TRUE;
        }
        // 如果是后台或ceo 就直接返回true 是上级
        if (user.isBackAdmin()) {
            return Boolean.TRUE;
        }
        AdminVo admin = this.getIsAdmin(user);
        StaffInfoVo staff = this.getStaffInfo(userId);
        //如果是企业管理 查看指定用户是否与企业管理员同一个企业下
        if (admin.getIsCompanyAdmin()) {
            return user.getCompanyId().equals(staff.getCompanyId());
        }
        if (admin.getIsDepartAdmin()) {
            List<String> ids = this.sysDepartmentInfoService.getSubordinateIdsById(user.getDeptId());
            if (CollectionUtils.isEmpty(ids)) {
                ids = new ArrayList<>();
                ids.add(user.getDeptId());
            }
            String departId = this.getDepartId(userId);
            if (StringUtils.isEmpty(departId)) {
                return Boolean.FALSE;
            }
            return ids.indexOf(departId) != -1;
        }
        return Boolean.FALSE;
    }

    @Override
    public List<SysUserInfo> getJobUser(String parentId) {
        return this.sysStaffInfoMapper.getJobUser(parentId);
    }

    @Override
    public List<StaffInfoVo> getAllDuytUserInner() {
        return this.sysUserInfoMapper.getAllDuytUser();
    }

    @Override
    public SysStaffInfo getUserInfo(String id) {
        LambdaQueryWrapper<SysStaffInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStaffInfo::getId, id);
        wrapper.eq(SysStaffInfo::getDeleted, YesNo.NO);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public UserStatistics statisticsUsers(SetSysUserInfoDto userInfoDto) {
        return this.sysStaffInfoMapper.statisticsUsers(userInfoDto);
    }

    @Override
    public List<StaffInfoByCompanyVo> getStaffByCompany(String companyId, Integer isShowLeave) {
        return sysStaffInfoMapper.getStaffByCompany(companyId, isShowLeave);
    }


    @Override
    public List<AppUserVo> querySysStaffInfoList(String departmentId) {
        return this.sysStaffInfoMapper.querySysStaffInfoList(departmentId);
    }

    @Override
    public List<AppUserVo> queryCompStaff(String compId) {
        return this.sysStaffInfoMapper.queryCompStaff(compId);
    }

    @Override
    public Boolean checkCompInCharge(String userId) {
        LambdaQueryWrapper<SysStaffInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStaffInfo::getUserId ,userId);
        SysStaffInfo sysStaffInfo = this.baseMapper.selectOne(wrapper);
        if (DataUtil.isNotEmpty(sysStaffInfo) && sysStaffInfo.getUserType().equals(0)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public LeaveUserInfoVo getQuitUserInfo(String userId) {
        LeaveUserInfoVo info = this.sysStaffInfoMapper.getLeaveUserInfo(userId);
        if(info == null) {
            throw new DefaultException("未找到伙伴离职信息");
        }
        return info;
    }

    @Override
    public void updateIdCard(IdCardUpdateDto dto) {
        //上传伙伴身份证
        LambdaUpdateWrapper<SysUserInfo> uw=new LambdaUpdateWrapper<>();
        uw.eq(BaseModel::getId,dto.getUserId())
                .set(SysUserInfo::getIdCardFront,dto.getIdCardFront())
                .set(SysUserInfo::getIdCardReverse,dto.getIdCardReverse());
        this.sysUserInfoService.update(uw);
    }

    @Override
    public List<StaffInfoByAddressBookVo> getAllUser(ComUserQueryDto queryDto) {
        List<StaffInfoByAddressBookVo> staffInfoVos = this.sysUserInfoMapper.getAllUser(queryDto);
        return staffInfoVos;
    }
    @Override
    public Map<String, Object> getSameDept(String userId, String chooseUserId) {
        Map<String, Object> map =new HashMap<>();
        StaffInfoVo staffInfo = this.getStaffInfo(userId);
        StaffInfoVo staffInfo1 = this.getStaffInfo(chooseUserId);
        if(DataUtil.isNotEmpty(staffInfo)&&DataUtil.isNotEmpty(staffInfo1)){
            if(!staffInfo.getDepartId().equals(staffInfo1.getDepartId())){
                map.put("otherDept",YesNo.YES);
            }
            if(DataUtil.isNotEmpty(staffInfo1.getDepartId())){
                String[] s1 = staffInfo1.getDepartId().split("_");
                String[] s = staffInfo.getDepartId().split("_");
                if(s1[0].equals(s[0])){
                    map.put("sameDept",YesNo.YES);
                }
            }
        }
        return map;
    }
    @Override
    public String getLeaderUserId(String userId) {
        StaffInfoVo staffInfo = this.getStaffInfo(userId);
        QueryWrapper<SysDepartmentInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(BaseModel::getId,staffInfo.getDepartId());

            if(staffInfo.getDepartId() != null) {
                qw.clear();
                qw.lambda().eq(BaseModel::getId,staffInfo.getDepartId().split("_")[0]);
                SysDepartmentInfo leader1 = this.sysDepartmentInfoService.getOne(qw);
                if(DataUtil.isNotEmpty(leader1)){
                    SysStaffInfo byId1 = this.getById(leader1.getAdminAccount());
                    if(DataUtil.isNotEmpty(byId1)){
                        return  byId1.getUserId();
                    }
                }
            }
        return null;
    }

    @Override
    public List<String> getLeaderUserId(String userId,Integer sameDept) {
        List<String> result =new ArrayList<>();
        StaffInfoVo staffInfo = this.getStaffInfo(userId);
        QueryWrapper<SysDepartmentInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(BaseModel::getId,staffInfo.getDepartId());
        //团队长
        SysDepartmentInfo leader = this.sysDepartmentInfoService.getOne(qw);
        if(DataUtil.isNotEmpty(leader)) {
            SysStaffInfo byId = this.getById(leader.getAdminAccount());
            if (DataUtil.isNotEmpty(byId)) {
                result.add(byId.getUserId());
            }
        }

        //副总
        if(sameDept==null || sameDept != YesNo.YES){

            if(staffInfo.getDepartId() != null) {
                qw.clear();
                qw.lambda().eq(BaseModel::getId,staffInfo.getDepartId().split("_")[0]);
                SysDepartmentInfo leader1 = this.sysDepartmentInfoService.getOne(qw);
                if(DataUtil.isNotEmpty(leader1)){
                    SysStaffInfo byId1 = this.getById(leader1.getAdminAccount());
                    if(DataUtil.isNotEmpty(byId1)){
                        result.add(byId1.getUserId());
                    }
                }
            }
        }

        if(DataUtil.isEmpty(result)){
            return null;
        }

        //if(sameDept != null && sameDept == YesNo.YES){
        result = result.stream().distinct().collect(Collectors.toList());
        if (!result.contains(userId)) {
            return result;
        }
        result.remove(userId);
        //}
        return result;
    }

    @Override
    public Integer getCheckUserId(String userId, String chargeId){
        StaffInfoVo staffInfo = this.getStaffInfo(userId);
        QueryWrapper<SysDepartmentInfo> qw =new QueryWrapper<>();
        qw.lambda().eq(BaseModel::getId,staffInfo.getDepartId());
        //团队长
        SysDepartmentInfo leader = this.sysDepartmentInfoService.getOne(qw);
        if(DataUtil.isNotEmpty(leader)) {
            SysStaffInfo byId = this.getById(leader.getAdminAccount());
            if (DataUtil.isNotEmpty(byId) && chargeId.equals(byId.getUserId())) {
                return YesNo.YES;
            }
        }
        return YesNo.NO;
    }

    @Override
    public Map<String, Object> getChargeLevel(String chargeId) {
        StaffInfoVo staffInfo = this.getStaffInfo(chargeId);
            Map<String, Object> map =new HashMap<>();
            //(企业管理员:0、 伙伴:1、 团队长:2、 副总:3)
        if(DataUtil.isNotEmpty(staffInfo)){
            map.put("level",staffInfo.getUserType());
        }else {
            //如果有负责人却没有类型，则默认为1
            map.put("level",1);
        }

        return map;
    }

    @Override
    public StaffInfoVo getOneStaffInfo(String phone, String idCard, String companyId) {
        return this.sysStaffInfoMapper.getOneStaffInfo(phone, idCard, companyId);
    }

    @Override
    public List<String> getDeptLeader(String userId,String signDeptId) {
        List<String> result =new ArrayList<>();
        StaffInfoVo staffInfo = this.getStaffInfo(userId);
        QueryWrapper<SysDepartmentInfo> qw =new QueryWrapper<>();
        // 优先获取部门id的团队长
        if(StringUtils.isNotEmpty(signDeptId)){
            qw.lambda().eq(BaseModel::getId,signDeptId);
        }else {
            qw.lambda().eq(BaseModel::getId,staffInfo.getDepartId());
        }


        String dept = StringUtils.isNotEmpty(signDeptId) ? signDeptId: staffInfo.getDepartId();
        //团队长  并且是分配在团队的而不是在部门
        SysDepartmentInfo leader = this.sysDepartmentInfoService.getOne(qw);
        if(DataUtil.isNotEmpty(leader) && !dept.split("_")[0].equals(dept)) {
            SysStaffInfo byId = this.getById(leader.getAdminAccount());
            if (DataUtil.isNotEmpty(byId)) {
                result.add(byId.getUserId());
            }
        }

        //副总
        if(staffInfo.getDepartId() != null || StringUtils.isNotEmpty(signDeptId)) {
            qw.clear();
            // 优先获取部门id的副总
            if(StringUtils.isNotEmpty(signDeptId)){
                qw.lambda().eq(BaseModel::getId,signDeptId.split("_")[0]);
            }else {
                qw.lambda().eq(BaseModel::getId,staffInfo.getDepartId().split("_")[0]);
            }

            SysDepartmentInfo leader1 = this.sysDepartmentInfoService.getOne(qw);
            if(DataUtil.isNotEmpty(leader1)){
                SysStaffInfo byId1 = this.getById(leader1.getAdminAccount());
                if(DataUtil.isNotEmpty(byId1)){
                    result.add(byId1.getUserId());
                }
            }
        }
        // 总经理
        if(StringUtils.isNotEmpty(staffInfo.getCompanyId())){
            QueryWrapper<CompanyAdmin> comAdminQw = new QueryWrapper<>();
            comAdminQw.lambda().eq(CompanyAdmin::getCompanyId, staffInfo.getCompanyId());
            CompanyAdmin comAdmin = this.companyAdminMapper.selectOne(comAdminQw);
            if(DataUtil.isNotEmpty(comAdmin)){
                SysStaffInfo byId = this.getById(comAdmin.getStaffId());
                if(DataUtil.isNotEmpty(byId)){
                    result.add(byId.getUserId());
                }
            }
        }
        if(result.size() == 0 && StringUtils.isEmpty(signDeptId)) {
            throw new DefaultException("未找到管理层任何信息！");
        }
        if(result.size() > 0) {
            result = result.stream().distinct().collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<StaffInfoVo> getCompanyIdInner(String companyId) {
        return this.sysUserInfoMapper.getCompanyIdInner(companyId);
}


    private String getStaffIdByUserId(String userId) {
        return this.sysStaffInfoMapper.getStaffIdByUserId(userId);
    }


    /**
     * 递归获得子级部门
     *
     * @param depIds, deps, parentId] depIds:子级部门集合，deps:企业全部部门,parentId:部门的上级部门id
     * @return void
     * @author PengQiang
     * @description DELL
     * @date 2021/1/7 17:54
     */
    private void getChilden(List<String> depIds, List<SysDepartmentInfoVo> deps, String parentId) {
        //获取子级部门集合
        List<SysDepartmentInfoVo> childs = deps.stream().filter(d -> parentId.equals(d.getParentId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childs)) {
            return;
        }
        //循环用id再次查找子级部门
        for (SysDepartmentInfoVo dep : childs) {
            depIds.add(dep.getId());
            getChilden(depIds, deps, dep.getId());
        }
    }


    private List<String> getSuperiorStaff(String parent, List<UnionStaffDepart> usds) {
        List<UnionStaffDepart> us = usds.stream().filter(a -> parent.equals(a.getDepartmentId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(us)) {
            List<String> staffIds = us.stream().map(UnionStaffDepart::getStaffId).collect(Collectors.toList());
            return staffIds;
        }
        if (parent.lastIndexOf("_") == -1) {
            return null;
        }
        return this.getSuperiorStaff(parent.substring(0, parent.lastIndexOf("_")), usds);
    }

    private void setPerformanceReviews(UserPerformanceReviewsPageDto param, List<UserPerformanceReviewsVo> list) {
        param.setName(null);
        String time = null;
        //限制放款日期条件查询不能超过当前月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(param.getTime())) {
            param.setTime(sdf.format(new Date()));
        }
        List<String> userId = list.stream().map(UserPerformanceReviewsVo::getUserId).collect(Collectors.toList());
        DataResult<List<PerformanceReviewsVo>> prResult = contractService.getPerformanceReviews(userId, param.getName(), param.getTime());
        if (!prResult.isSuccess()) {
            throw new DefaultException("获取业绩总结报表出错");
        }
        List<PerformanceReviewsVo> prs = prResult.getData();
        String[] times = param.getTime().split("-");
        time = times[0].concat("年");
        time = time.concat(times[1].concat("月"));
        String finalTime = time;
        Map<String, PerformanceReviewsVo> perMap = CollectionUtils.isEmpty(prs) ? null : prs.stream().collect(Collectors.toMap(PerformanceReviewsVo::getUserId, p -> p, (k1, k2) -> k1, HashMap::new));
        list.stream().forEach(r -> {
            if (DataUtil.isNotEmpty(perMap) && DataUtil.isNotEmpty(perMap.get(r.getUserId()))) {
                BeanUtils.copyProperties(perMap.get(r.getUserId()), r);
            }
            r.setMonth(finalTime);
        });
    }


    /**
     * 添加合规承诺书、学历证书
     *
     * @param userInfo 用户信息
     */
    private void getLetterOfUndertakingAndDiplomas(SysUserInfoVo userInfo){
        //合规承诺书
        QueryWrapper<Image> imageQw = new QueryWrapper<>();
        imageQw.lambda().eq(Image::getConnectId, userInfo.getId());
        imageQw.lambda().eq(Image::getImageType, ImageTypeInfo.COMPLIANCE_COMMITMENT);
        imageQw.lambda().orderByDesc(Image::getCreateTime);
        userInfo.setComplianceCommitments(this.imageService.list(imageQw).stream().map(s->s.getImageUrl()).collect(Collectors.toList()));

        //学历证书
        imageQw.clear();
        imageQw.lambda().eq(Image::getConnectId, userInfo.getId());
        imageQw.lambda().eq(Image::getImageType, ImageTypeInfo.DIPLOMA);
        imageQw.lambda().orderByDesc(Image::getCreateTime);
        userInfo.setDiplomas(this.imageService.list(imageQw).stream().map(s->s.getImageUrl()).collect(Collectors.toList()));

        //合作申请表
        imageQw.clear();
        imageQw.lambda().eq(Image::getConnectId, userInfo.getId());
        imageQw.lambda().eq(Image::getImageType, ImageTypeInfo.COOPERATION_APPLY);
        imageQw.lambda().orderByDesc(Image::getCreateTime);
        List<String> cooperationImages = this.imageService.list(imageQw).stream().map(Image::getImageUrl).collect(Collectors.toList());
        if (DataUtil.isNotEmpty(cooperationImages)) {
            userInfo.setCooperationImages(cooperationImages);
        } else {
            QueryWrapper<CommonFile> fileQw = new QueryWrapper<>();
            fileQw.lambda().eq(CommonFile::getConnectId, userInfo.getId());
            fileQw.lambda().eq(CommonFile::getFileType, FileTypeInfo.COOPERATION_FILE);
            fileQw.lambda().orderByDesc(CommonFile::getCreateTime);
            List<CommonFile> list = this.commonFileService.list(fileQw);
            List<String> images = list.stream().map(CommonFile::getFileUrl).collect(Collectors.toList());
            userInfo.setCooperationImages(images);
        }
    }

    /**
     * 获取荣耀和惩罚记录
     *
     * @param id 员工id
     * @param userInfo 用户信息
     */
    private void getRecord(String id, SysUserInfoVo userInfo) {
        //家庭成员
        QueryWrapper<FamilyMember> fmQw = new QueryWrapper<>();
        fmQw.lambda().eq(FamilyMember::getUserId, userInfo.getId());
        fmQw.lambda().orderByDesc(FamilyMember::getOrderNum);
        userInfo.setFamilyMembers(this.familyMemberService.list(fmQw));

        //荣耀记录
        if(Objects.nonNull(userInfo)) {
            StaffHistoryQueryDto staffHonor = new StaffHistoryQueryDto();
            staffHonor.setStaffId(id);
            staffHonor.setType(StaffHistoryTypeEnum.HONOR.name());
            List<StaffHistoryVo> staffHistoryVos = this.staffHistoryService.queryStaffHistory(staffHonor);
            if (staffHistoryVos.size() > 0 && Objects.nonNull(staffHistoryVos)) {
                userInfo.setStaffHistoryHonor(staffHistoryVos);
            } else {
                userInfo.setStaffHistoryHonor(Lists.newArrayList());
            }
            //惩罚记录
            StaffHistoryQueryDto staffPunishment = new StaffHistoryQueryDto();
            staffPunishment.setStaffId(id);
            staffPunishment.setType(StaffHistoryTypeEnum.PUNISHMENT.name());
            List<StaffHistoryVo> staffHistoryVos1 = this.staffHistoryService.queryStaffHistory(staffPunishment);
            if (staffHistoryVos1.size() > 0 && Objects.nonNull(staffHistoryVos1)) {
                userInfo.setStaffHistoryPunishment(staffHistoryVos1);
            } else {
                userInfo.setStaffHistoryPunishment(Lists.newArrayList());
            }
        }
    }

    private Map<String, Integer> doBatchImportCompanyUserSave(List<String[]> dataList, ImportInfo imp, UserVo user) throws ParseException {
        Map<String, Integer> result = new HashMap<>();
        List<ImportResultInfo> errors = new ArrayList<>();
        Integer excelRows = 0;
        ImportResultInfo importInfo ;
        UserImportErrorDataDto errorData = new UserImportErrorDataDto();
        Map<String, String> mobiles = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);
        Map<String, String> idCards = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);
        //  部门
        List<UnionStaffDepart> unionStaffDepartList = new ArrayList<>();
        //  岗位
        List<UnionStaffPosition> unionStaffPositionList = new ArrayList<>();
        //  角色
        List<UnionRoleStaff> unionRoleStaffList = new ArrayList<>();
        //需要发送的短信集合
        List<SmsDto> smsDtos = new ArrayList<>();
        //错误字符构造
        StringBuilder errorStr = new StringBuilder("");
        //错误字符构造
        StringBuilder recommendId = new StringBuilder("");
        List<SysUserInfo> sysUserInfos = new ArrayList<>();
        //循环行
        for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
            //这一行的数据
            boolean rowIsEmpty = true;
            String[] row = dataList.get(rowIndex);
            for (int lineIndex = 0, lineLength = row.length; lineIndex < lineLength; lineIndex++) {
                if (row[lineIndex] == null || "".equals(row[lineIndex])) {
                    continue;
                }
                //去掉空格
                row[lineIndex] = row[lineIndex].trim();
                if (rowIsEmpty && StringUtils.isNotEmpty(row[lineIndex])) {
                    rowIsEmpty = !rowIsEmpty;
                }
            }
            //  空行校验
            if (rowIsEmpty) {
                continue;
            }
            //校验参数；
            UnionStaffDepart unionStaffDepart = new UnionStaffDepart();
            UnionStaffPosition unionStaffPosition = new UnionStaffPosition();
            UnionRoleStaff unionRoleStaff = new UnionRoleStaff();
            recommendId = new StringBuilder();
            if (DataUtil.isNotEmpty(mobiles.get(row[1]))) {
                errorStr.append("表格中重复手机号,");
            }
            if (DataUtil.isNotEmpty(idCards.get(row[14]))) {
                errorStr.append("表格中重复身份证号,");
            }
            mobiles.put(row[1], row[1]);
            idCards.put(row[14], row[14]);
            try {
                String companyId = checkCompanyParam(row, errorStr, unionStaffDepart, unionStaffPosition, unionRoleStaff, recommendId);
                if (errorStr.length() > 0) {
                    importInfo = new ImportResultInfo();
                    errorData = new UserImportErrorDataDto();
                    this.setUserImportInfo(dataList.get(rowIndex), errorData, user);
                    importInfo.setImportId(imp.getId());
                    importInfo.setCreateTime(new Date());
                    importInfo.setErrorCause(errorStr.toString());
                    importInfo.setUserId(user.getUserId());
                    importInfo.setImportContent(JSON.toJSONString(errorData));
                    importInfo.setType(ImportResultInfoType.STAFF_EXTERNAL);
                    importInfo.setState(YesNo.YES);
                    errors.add(importInfo);
                    errorStr = new StringBuilder();
                    continue;
                }
                //构建用户信息；
                String staffId = saveCompanyUser(row, smsDtos, companyId, user, recommendId, sysUserInfos);
                unionStaffDepart.setStaffId(staffId);
                unionStaffPosition.setStaffId(staffId);
                unionRoleStaff.setStaffId(staffId);
                if (DataUtil.isNotEmpty(unionStaffPosition.getPositionId())) {
                    unionStaffPositionList.add(unionStaffPosition);
                }
                if (DataUtil.isNotEmpty(unionStaffDepart.getDepartmentId())) {
                    unionStaffDepartList.add(unionStaffDepart);
                }
                unionRoleStaffList.add(unionRoleStaff);
            } catch (ParseException e) {
                log.error("admin导入员工数据检验出错:{}", e, e);
                importInfo = new ImportResultInfo();
                errorData = new UserImportErrorDataDto();
                errorStr.append("日期格式错误");
                this.setUserImportInfo(dataList.get(rowIndex), errorData, user);
                importInfo.setImportId(imp.getId());
                importInfo.setCreateTime(new Date());
                importInfo.setErrorCause(errorStr.toString());
                importInfo.setUserId(user.getUserId());
                importInfo.setImportContent(JSON.toJSONString(errorData));
                importInfo.setType(ImportResultInfoType.STAFF_EXTERNAL);
                importInfo.setState(YesNo.YES);
                errors.add(importInfo);
                errorStr = new StringBuilder();
            } catch (Exception e) {
                log.error("admin导入员工数据检验出错:{}", e, e);
                importInfo = new ImportResultInfo();
                errorData = new UserImportErrorDataDto();
                errorStr.append("检验出错");
                this.setUserImportInfo(dataList.get(rowIndex), errorData, user);
                importInfo.setImportId(imp.getId());
                importInfo.setCreateTime(new Date());
                importInfo.setErrorCause(errorStr.toString());
                importInfo.setUserId(user.getUserId());
                importInfo.setImportContent(JSON.toJSONString(errorData));
                importInfo.setType(ImportResultInfoType.STAFF_EXTERNAL);
                importInfo.setState(YesNo.YES);
                errors.add(importInfo);
                errorStr = new StringBuilder();
            }
        }
        //保存关联关系
        if (DataUtil.isNotEmpty(unionStaffDepartList)) {
            unionStaffDeparService.saveBatch(unionStaffDepartList);
        }
        if (DataUtil.isNotEmpty(unionStaffPositionList)) {
            unionStaffPositionService.saveBatch(unionStaffPositionList);
        }
        if (DataUtil.isNotEmpty(sysUserInfos)) {
            this.sysUserInfoService.updateBatchById(sysUserInfos);
        }
        unionRoleStaffService.saveBatch(unionRoleStaffList);
        this.importResultInfoService.saveBatch(errors);
        //数据总条数
        result.put("total", dataList.size());
        //异常条数
        result.put("errorCount", errors.size());
        //成功条数
        result.put("successCount", dataList.size() - errors.size());
        return result;
    }

    private Map<String, Integer> doBatchImportUserSave(List<String[]> dataList, ImportInfo imp, UserVo user) throws ParseException {
        Map<String, Integer> result = new HashMap<>();
        List<ImportResultInfo> errors = new ArrayList<>();
        Integer excelRows = 0;
        ImportResultInfo importInfo ;
        UserImportErrorDataDto errorData = new UserImportErrorDataDto();
        Map<String, String> mobiles = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);
        Map<String, String> idCards = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);

        //  部门
        List<UnionStaffDepart> unionStaffDepartList = new ArrayList<>();
        //  岗位
        List<UnionStaffPosition> unionStaffPositionList = new ArrayList<>();
        //  角色
        List<UnionRoleStaff> unionRoleStaffList = new ArrayList<>();
        //需要发送的短信集合
        List<SmsDto> smsDtos = new ArrayList<>();

        //错误字符构造
        StringBuilder errorStr = new StringBuilder("");

        StringBuilder recommendId = new StringBuilder("");
        //获取企业信息的地址用于生成名片
        QueryWrapper<SysCompanyInfo> qw = new QueryWrapper<>();
        qw.lambda().eq(SysCompanyInfo::getId, user.getCompanyId())
                .ne(BaseModel::getStatus, StatusEnum.deleted.getValue()).last("limit 0,1");
        SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);
        List<SysUserInfo> sysUserInfos = new ArrayList<>();
        //循环行
        for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
            //这一行的数据
            boolean rowIsEmpty = true;
            String[] row = dataList.get(rowIndex);
            for (int lineIndex = 0, lineLength = row.length; lineIndex < lineLength; lineIndex++) {
                if (row[lineIndex] == null || "".equals(row[lineIndex])) {
                    continue;
                }
                //去掉空格
                row[lineIndex] = row[lineIndex].trim();
                if (rowIsEmpty && StringUtils.isNotEmpty(row[lineIndex])) {
                    rowIsEmpty = !rowIsEmpty;
                }
            }
            //  空行校验
            if (rowIsEmpty) {
                continue;
            }
            //校验参数；
            UnionStaffDepart unionStaffDepart = new UnionStaffDepart();
            UnionStaffPosition unionStaffPosition = new UnionStaffPosition();
            UnionRoleStaff unionRoleStaff = new UnionRoleStaff();
            recommendId = new StringBuilder("");
            if (DataUtil.isNotEmpty(mobiles.get(row[1]))) {
                errorStr.append("表格中重复手机号,");
            }
            if (DataUtil.isNotEmpty(idCards.get(row[13]))) {
                errorStr.append("表格中重复身份证号,");
            }
            mobiles.put(row[1], row[1]);
            idCards.put(row[13], row[13]);
            try {
                checkParam(row, errorStr, unionStaffDepart, unionStaffPosition, unionRoleStaff, user, recommendId);
                if (errorStr.length() > 0) {
                    importInfo = new ImportResultInfo();
                    errorData = new UserImportErrorDataDto();
                    this.setUserImportInfo(dataList.get(rowIndex), errorData, user);
                    importInfo.setImportId(imp.getId());
                    importInfo.setCreateTime(new Date());
                    importInfo.setErrorCause(errorStr.toString());
                    importInfo.setUserId(user.getUserId());
                    importInfo.setImportContent(JSON.toJSONString(errorData));
                    importInfo.setType(ImportResultInfoType.STAFF_EXTERNAL);
                    importInfo.setState(YesNo.YES);
                    errors.add(importInfo);
                    errorStr = new StringBuilder();
                    continue;
                }
                //构建用户信息；
                String staffId = saveUser(row, smsDtos, sysCompanyInfo, user, recommendId, sysUserInfos);
                unionStaffDepart.setStaffId(staffId);
                unionStaffPosition.setStaffId(staffId);
                unionRoleStaff.setStaffId(staffId);
                if (DataUtil.isNotEmpty(unionStaffPosition.getPositionId())) {
                    unionStaffPositionList.add(unionStaffPosition);
                }
                if (DataUtil.isNotEmpty(unionStaffDepart.getDepartmentId())) {
                    unionStaffDepartList.add(unionStaffDepart);
                }
                unionRoleStaffList.add(unionRoleStaff);
            } catch (ParseException e) {
                log.error("pc导入员工数据检验出错:{}", e, e);
                importInfo = new ImportResultInfo();
                errorData = new UserImportErrorDataDto();
                errorStr.append("日期格式错误-正确的日期格式(2022-01-01)");
                this.setUserImportInfo(dataList.get(rowIndex), errorData, user);
                importInfo.setImportId(imp.getId());
                importInfo.setCreateTime(new Date());
                importInfo.setErrorCause(errorStr.toString());
                importInfo.setUserId(user.getUserId());
                importInfo.setImportContent(JSON.toJSONString(errorData));
                importInfo.setType(ImportResultInfoType.STAFF_EXTERNAL);
                importInfo.setState(YesNo.YES);
                errors.add(importInfo);
                errorStr = new StringBuilder();
            } catch (Exception e) {
                log.error("pc导入员工数据检验出错:{}", e, e);
                importInfo = new ImportResultInfo();
                errorData = new UserImportErrorDataDto();
                errorStr.append("检验出错");
                this.setUserImportInfo(dataList.get(rowIndex), errorData, user);
                importInfo.setImportId(imp.getId());
                importInfo.setCreateTime(new Date());
                importInfo.setErrorCause(errorStr.toString());
                importInfo.setUserId(user.getUserId());
                importInfo.setImportContent(JSON.toJSONString(errorData));
                importInfo.setType(ImportResultInfoType.STAFF_EXTERNAL);
                importInfo.setState(YesNo.YES);
                errors.add(importInfo);
                errorStr = new StringBuilder();
            }
        }
        //保存关联关系
        if (DataUtil.isNotEmpty(unionStaffDepartList)) {
            unionStaffDeparService.saveBatch(unionStaffDepartList);
        }
        if (DataUtil.isNotEmpty(unionStaffPositionList)) {
            unionStaffPositionService.saveBatch(unionStaffPositionList);
        }
        if (DataUtil.isNotEmpty(sysUserInfos)) {
            this.sysUserInfoService.updateBatchById(sysUserInfos);
        }
        unionRoleStaffService.saveBatch(unionRoleStaffList);
        this.importResultInfoService.saveBatch(errors);
        //数据总条数
        result.put("total", dataList.size());
        //异常条数
        result.put("errorCount", errors.size());
        //成功条数
        result.put("successCount", dataList.size() - errors.size());
        return result;
    }

    private void setUserImportInfo(String[] data, UserImportErrorDataDto bean, UserVo user) {
        int index = 0;
        bean.setName(data[index++]);
        bean.setMobile(data[index++]);
        if (user.isBack()) {
            bean.setCompanyName(data[index++]);
        }
        bean.setDepartName(data[index++]);
        bean.setJobName(data[index++]);
        bean.setRoleName(data[index++]);
        bean.setDateJoin(data[index++]);
        bean.setRecommendMobile(data[index++]);
        bean.setStatus(data[index++]);
        bean.setGender(data[index++]);
        bean.setAncestral(data[index++]);
        bean.setNation(data[index++]);
        bean.setMarital(data[index++]);
        bean.setBirthday((data[index++]));
        bean.setIdCard(data[index++]);
        bean.setCertificateCardAddress(data[index++]);
        bean.setContactAddress(data[index++]);
        bean.setEmail(data[index++]);
        bean.setHighestEducation(data[index++]);
        bean.setGraduatedFrom(data[index++]);
        bean.setMajor(data[index++]);
        bean.setUrgentName(data[index++]);
        bean.setUrgentPhone(data[index++]);
        bean.setUrgentRelation(data[index++]);
        bean.setFamilyName(data[index++]);
        bean.setFamilyMobile(data[index++]);
        bean.setRelationship(data[index++]);
        bean.setProfession(data[index++]);
        bean.setFamilyContactAddress(data[index++]);
    }

    private SetSysUserInfoDto doOldUserInfo(UserCopyDto param) {
        //查询出需要复制的参数
        SetSysUserInfoDto userInfoDto = this.sysStaffInfoMapper.getUserInfoByUserId(param.getOldUserId());
        SysUserInfo byId = sysUserInfoService.getById(param.getOldUserId());
        QueryWrapper<SysStaffInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysStaffInfo::getUserId,byId.getId());
        SysStaffInfo sysStaffInfo = this.sysStaffInfoMapper.selectOne(wrapper);
        SysCompanyInfo companyInfo = this.sysCompanyInfoMapper.selectById(sysStaffInfo.getCompId());
        SysCompanyInfo sysCompany = this.sysCompanyInfoMapper.selectById(param.getCompanyId());
        userInfoDto.setDepartId(param.getDepartId());
        userInfoDto.setPositionId(param.getJobId());
        userInfoDto.setRoleId(param.getRoleId());
        userInfoDto.setCompanyId(param.getCompanyId());
        //把员工设为离职
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, param.getOldUserId());
        SysStaffInfo  staff1 = this.getOne(staffQw);

        //如果已经是离职的则不处理
        Boolean leaveFlag=false;
        if(StatusEnum.stop.getValue().equals(staff1.getStatus())){
            leaveFlag=true;
        }
        if(!leaveFlag) {
            UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
            userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
            userUw.lambda().set(SysUserInfo::getStatus, StatusEnum.stop.getValue());
            userUw.lambda().eq(true, SysUserInfo::getId, param.getOldUserId());
            this.sysUserInfoService.update(userUw);
        }
        if (leaveFlag==false) {
            //查询旧用户是否 加入预备高管 如果加入则退学
            PrepareExecutiveRecordVo prepareExecutiveRecordVo = this.prepareExecutiveRecordService.getPrepareExecutiveRecordInner(param.getOldUserId());
            if(DataUtil.isNotEmpty(prepareExecutiveRecordVo)){
                if(prepareExecutiveRecordVo.getEnterDate().after(new Date())){
                    throw new DefaultException("当前调离时间小于预备高管入学时间，不允许调离");
                }
                prepareExecutiveRecordVo.setOutDate(new Date());
                prepareExecutiveRecordVo.setOutReason("从"+companyInfo.getCompanyName()+"调离至"+sysCompany.getCompanyName());
                prepareExecutiveRecordVo.setIsPrepareExecutive(2);
                PrepareExecutiveRecord record = new PrepareExecutiveRecord();
                BeanUtils.copyProperties(prepareExecutiveRecordVo,record);
                this.prepareExecutiveRecordService.updateById(record);
                SysUserInfo info = sysUserInfoService.getById(param.getOldUserId());
                info.setIsPrepareExecutive(2);
                this.sysUserInfoService.updateById(info);
            }
        }
        //添加一条任职记录
        SetSysUserInfoDto sysUserInfoDto = this.sysStaffInfoMapper.getUserInfoByUserId(param.getOldUserId());
        PositionRecord positionRecord = new PositionRecord();
        QueryWrapper<UnionRoleStaff> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UnionRoleStaff::getStaffId,sysStaffInfo.getId());
        UnionRoleStaff unionRoleStaff = this.unionRoleStaffMapper.selectOne(queryWrapper);
        positionRecord.setRoleName(unionRoleStaff.getRoleName());
        positionRecord.setId(UUIDutils.getUUID32());
        positionRecord.setCertificateCard(sysUserInfoDto.getCertificateCard());
        positionRecord.setCompanyId(sysUserInfoDto.getCompanyId());
        positionRecord.setCompanyName(companyInfo.getCompanyName());
        positionRecord.setUserId(param.getOldUserId());
        positionRecord.setUserName(sysUserInfoDto.getUserName());
        positionRecord.setPositionTime(sysUserInfoDto.getDateJoin());
        positionRecord.setCreateTime(new Date());
        positionRecord.setQuitTime(new Date());
        positionRecord.setQuitReason("从"+companyInfo.getCompanyName()+"调离至"+sysCompany.getCompanyName());
        positionRecordService.save(positionRecord);

        if(!leaveFlag) {
            // 修改员工档案为离职
            this.sysStaffInfoMapper.updateStatus(staff1.getId(), StatusEnum.stop.getValue(), "调离新公司");
            this.checkUtil.removeUserToken(staff1.getUserId());
        }
        //查询家庭成员
        QueryWrapper<FamilyMember> familyQw = new QueryWrapper<>();
        familyQw.lambda().eq(FamilyMember::getUserId, param.getOldUserId());
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
        resumeQw.lambda().eq(UserResume::getUserId, param.getOldUserId());
        List<UserResume> resumes = this.userResumeService.list(resumeQw);
        userInfoDto.setUserResumes(resumes);
        //合规承诺书
        userInfoDto.setComplianceCommitments(imageService.getListImages(param.getOldUserId(), ImageTypeInfo.COMPLIANCE_COMMITMENT));
        //学历证书
        userInfoDto.setDiplomas(imageService.getListImages(param.getOldUserId(), ImageTypeInfo.DIPLOMA));

        //合作申请
        List<String> listImages = imageService.getListImages(param.getOldUserId(), ImageTypeInfo.COOPERATION_APPLY);
        if (DataUtil.isNotEmpty(listImages)) {
            userInfoDto.setCooperationImages(listImages);
        } else {
            QueryWrapper<CommonFile> commonFileQw = new QueryWrapper<>();
            commonFileQw.lambda().eq(CommonFile::getConnectId, param.getOldUserId());
            commonFileQw.lambda().eq(CommonFile::getFileType, FileTypeInfo.COOPERATION_FILE);
            List<CommonFile> list = this.commonFileService.list(commonFileQw);
            List<String> images = list.stream().map(CommonFile::getFileUrl).distinct().collect(Collectors.toList());
            userInfoDto.setCooperationImages(images);
        }

        QueryWrapper<SysStaffRelation> relationQw = new QueryWrapper<>();
        relationQw.lambda().eq(SysStaffRelation::getRelationUserId, param.getOldUserId());
        relationQw.lambda().eq(SysStaffRelation::getDeletd, YesNo.NO);
        List<SysStaffRelation> relations = this.sysStaffRelationService.list(relationQw);
        if (DataUtil.isNotEmpty(relations)) {
            List<SysStaffRelationDto> relationDtos = Transformer.toList(SysStaffRelationDto.class).apply(relations).done();
            userInfoDto.setStaffRelationDtoList(relationDtos);
        }
        return userInfoDto;
    }
}
