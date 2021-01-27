package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.CheckParamUtils;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.sms.api.dto.SmsDto;
import com.zerody.sms.feign.SmsFeignService;
import com.zerody.user.domain.*;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.enums.UserLoginStatusEnum;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.*;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysComapnyInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoServiceImpl
 * @DateTime 2020/12/18_15:52
 * @Deacription TODO
 */
@Slf4j
@Service
public class SysCompanyInfoServiceImpl extends BaseService<SysCompanyInfoMapper, SysCompanyInfo> implements SysCompanyInfoService {


    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysCompanyInfoMapper sysCompanyInfoMapper;

    @Autowired
    private SysDepartmentInfoService departmentInfoService;

    @Autowired
    private CompanyAdminMapper companyAdminMapper;

    @Autowired
    private SmsFeignService smsFeignService;

    @Autowired
    private OauthFeignService oauthFeignService;


    @Value("${sms.template.userTip:}")
    String userTipTemplate;

    @Value("${sms.sign.tsz:唐叁藏}")
    String smsSign;

    /**
    * @Author               PengQiang
    * @Description //TODO   添加企业
    * @Date                 2020/12/19 13:06
    * @Param                [sysCompanyInfo]
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCompany(SysCompanyInfo sysCompanyInfo) {
        log.info("B端添加企业入参--{}",sysCompanyInfo);
        //构造查询条件
        if(!CheckParamUtils.chkMobile(sysCompanyInfo.getContactPhone())){
            throw new DefaultException("企业联系人号码格式错误");
        }
        QueryWrapper<SysCompanyInfo> comQW = new QueryWrapper<>();
        comQW.lambda().ne(SysCompanyInfo::getStatus, StatusEnum.删除.getValue());
        comQW.lambda().eq(SysCompanyInfo::getCompanyName, sysCompanyInfo.getCompanyName());
        //查询该企业是否存在
        Integer count = sysCompanyInfoMapper.selectCount(comQW);
        if(count > 0 ){
            throw new DefaultException("企业名称已被占用");
        }
        QueryWrapper<SysUserInfo> userQw = new QueryWrapper<>();
        userQw.lambda().ne(SysUserInfo::getStatus, StatusEnum.删除.getValue());
        userQw.lambda().eq(SysUserInfo::getPhoneNumber, sysCompanyInfo.getContactPhone());
        //查询该手机号码是否存在
        SysUserInfo user =  this.sysUserInfoMapper.selectOne(userQw);
        if(user != null ){
            throw new DefaultException("该手机号码已存在！");
        }
        //添加企业默认为该企业为有效状态
        sysCompanyInfo.setStatus(StatusEnum.激活.getValue());
        log.info("B端添加企业入库参数--{}", JSON.toJSONString(sysCompanyInfo));
        this.saveOrUpdate(sysCompanyInfo);
        //生成用户
        SysUserInfo userInfo = new SysUserInfo();
        userInfo.setPhoneNumber(sysCompanyInfo.getContactPhone());
        userInfo.setUserName(sysCompanyInfo.getContactName());
        userInfo.setStatus(StatusEnum.激活.getValue());
        userInfo.setCreateId(UserUtils.getUserId());
        userInfo.setCreateTime(new Date());
        userInfo.setCreateUser(UserUtils.getUserName());
        userInfo.setId(UUIDutils.getUUID32());
        sysUserInfoMapper.insert(userInfo);
        //企业生成成功生成企业管理员登录账号
        SysLoginInfo sysLoginInfo = new SysLoginInfo();
        sysLoginInfo.setMobileNumber(sysCompanyInfo.getContactPhone());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String initPwd = SysStaffInfoService.getInitPwd();
        sysLoginInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5( initPwd )));
        sysLoginInfo.setUserId(userInfo.getId());
        sysLoginInfo.setCreateTime(new Date());
        sysLoginInfo.setCreateId(UserUtils.getUserId());
        sysLoginInfo.setStatus(StatusEnum.激活.getValue());
        sysLoginInfoMapper.insert(sysLoginInfo);
        SysStaffInfo staff = new SysStaffInfo();
        staff.setId(UUIDutils.getUUID32());
        staff.setCompId(sysCompanyInfo.getId());
        staff.setUserId(userInfo.getId());
        staff.setUserName(userInfo.getUserName());
        staff.setUserId(userInfo.getId());
        staff.setCreateTime(new Date());
        staff.setCreateId(UserUtils.getUserId());
        staff.setStatus(StatusEnum.激活.getValue());
        staff.setDeleted(YesNo.NO);
        this.sysStaffInfoMapper.insert(staff);
        this.saveOrUpdate(sysCompanyInfo);
        CompanyAdmin admin = new CompanyAdmin();
        admin.setId(UUIDutils.getUUID32());
        admin.setCompanyId(sysCompanyInfo.getId());
        admin.setStaffId(staff.getId());
        admin.setCreateTime(new Date());
        admin.setCreateBy(UserUtils.getUserId());
        admin.setCreateUsername(UserUtils.getUserName());
        admin.setDeleted(YesNo.NO);
        this.companyAdminMapper.insert(admin);
        //预设管理员角色
        this.oauthFeignService.addCompanyRole(sysCompanyInfo.getId());
        SmsDto smsDto=new SmsDto();
        smsDto.setMobile(userInfo.getPhoneNumber());
        Map<String, Object> content=new HashMap<>();
        content.put("userName",userInfo.getPhoneNumber());
        content.put("passWord",initPwd);
        smsDto.setContent(content);
        smsDto.setTemplateCode(userTipTemplate);
        smsDto.setSign(smsSign);
        //发送短信
        smsFeignService.sendSms(smsDto);
    }

    /**
    * @Author               PengQiang
    * @Description //TODO   修改企业状态的同时修改该企业下的用户的登录状态
    * @Date                 2020/12/18 17:46
    * @Param                [companyId, loginStatus]
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public void updateCompanyStatus(String companyId, Integer loginStatus) {
        log.info("B端修改企业登录状态入参--companyId = {},loginStatus = {}",companyId,loginStatus);
        //判断企业id 与修改的状态 是否为空
        if(StringUtils.isBlank(companyId)){
            throw new DefaultException("企业id不能为空");
        }
        if(loginStatus == null){
            throw new DefaultException("状态不能为空");
        }

        //修改企业的状态
        SysCompanyInfo sysCompanyInfo = new SysCompanyInfo();
//        sysCompanyInfo.setLoginStatus(loginStatus);
        sysCompanyInfo.setId(companyId);
        //保存状态
        log.info("B端修改企业登录参数入库参数--{}",JSON.toJSONString(sysCompanyInfo));
        this.saveOrUpdate(sysCompanyInfo);
        //查询得到该企业下所有的用户id
        List<String> userIds = sysStaffInfoMapper.selectUserByCompanyId(companyId);
        //如果这个企业下没有员工那么久不用进行后面的操作直接返回成功
        if(userIds.size() == 0 ){
            return;
        }
        //设置修改参数
        SysLoginInfo sysLoginInfo = new SysLoginInfo();
        //修改状态
        QueryWrapper<SysLoginInfo> loginQW = new QueryWrapper<>();
        loginQW.lambda().in(SysLoginInfo::getUserId, userIds);
        log.info("B端修改修改企业登录状态后修改用户登录状态入库参数--{}",JSON.toJSONString(sysLoginInfo));
        sysLoginInfoMapper.update(sysLoginInfo, loginQW);
    }

    @Override
    public IPage<SysComapnyInfoVo> getPageCompany(SysCompanyInfoDto companyInfoDto) {
        //设置分页参数
        IPage<SysComapnyInfoVo> iPage = new Page<>(companyInfoDto.getCurrent(),companyInfoDto.getPageSize());
        return sysCompanyInfoMapper.getPageCompany(companyInfoDto,iPage);
    }

    @Override
    public void updataCompany(SysCompanyInfo sysCompanyInfo) {
        if(!CheckParamUtils.chkMobile(sysCompanyInfo.getContactPhone())){
            throw new DefaultException("企业联系人号码格式错误");
        }
        QueryWrapper<SysCompanyInfo> comQW = new QueryWrapper<>();
        comQW.lambda().ne(SysCompanyInfo::getStatus, StatusEnum.删除.getValue());
        comQW.lambda().eq(SysCompanyInfo::getCompanyName, sysCompanyInfo.getCompanyName());
        comQW.lambda().ne(SysCompanyInfo::getId, sysCompanyInfo.getId());
        //查询该企业是否存在
        Integer count = sysCompanyInfoMapper.selectCount(comQW);
        if(count > 0 ){
            throw new DefaultException("企业名称已被占用");
        }
        this.saveOrUpdate(sysCompanyInfo);
    }

    /**
    * @Author               PengQiang
    * @Description //TODO   逻辑删除企业
    * @Date                 2020/12/28 9:45
    * @Param                companyId
    * @return               com.zerody.common.bean.DataResult
    */
    @Override
    public void deleteCompanyById(String companyId) {
        if (StringUtils.isEmpty(companyId)){
            throw new DefaultException("企业id为空");
        }
        SysCompanyInfo company = new SysCompanyInfo();
        company.setStatus(StatusEnum.删除.getValue());
        company.setId(companyId);
        this.saveOrUpdate(company);
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getCompId, company.getId());
        staffQw.lambda().ne(SysStaffInfo::getStatus, StatusEnum.删除.getValue());
        List<SysStaffInfo> staffs = this.sysStaffInfoMapper.selectList(staffQw);

        List<String> userIds = staffs.stream().map(SysStaffInfo::getUserId).collect(Collectors.toList());
        SysUserInfo userInfo = new SysUserInfo();
        userInfo.setStatus( StatusEnum.删除.getValue());
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().ne(SysUserInfo::getStatus, StatusEnum.删除.getValue());
        userUw.lambda().in(SysUserInfo::getId, userIds);
        this.sysUserInfoMapper.update(userInfo,userUw);
    }

    @Override
    public List<SysComapnyInfoVo> getAllCompany(String companyId) {

        List<SysComapnyInfoVo> companys = new ArrayList<>();
        //如果是crm系统就只查当前登录企业
        if(!StringUtils.isEmpty(companyId)){
            SysComapnyInfoVo company = sysCompanyInfoMapper.selectCompanyInfoById(companyId);
            //获取当前企业下的部门、岗位
            company.setDeparts(departmentInfoService.getAllDepByCompanyId(company.getId()));
            companys.add(company);
            return  companys;
        }

        //如果不是crm系统就查全部企业
        companys = sysCompanyInfoMapper.getAllCompnay();
        for (SysComapnyInfoVo company : companys){
            company.setDeparts(departmentInfoService.getAllDepByCompanyId(company.getId()));
        }
       return companys;
    }

    @Override
    public SysComapnyInfoVo getCompanyInfoById(String id) {
        SysComapnyInfoVo companyInfo = sysCompanyInfoMapper.selectCompanyInfoById(id);
        return companyInfo;
    }

    @Override
    public void updateAdminAccout(SetAdminAccountDto dto) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new DefaultException("企业id为空");
        }
        if(StringUtils.isEmpty(dto.getStaffId())){
            throw new DefaultException("员工id为空");
        }
        SysStaffInfo staff = this.sysStaffInfoMapper.selectById(dto.getStaffId());
        if(!staff.getCompId().equals(dto.getId())){
            throw new DefaultException("该员工不是该企业下的");
        }
        SysUserInfo user = this.sysUserInfoMapper.selectById(staff.getUserId());

        UpdateWrapper<SysCompanyInfo> comUw = new UpdateWrapper<>();
        comUw.lambda().set(SysCompanyInfo::getContactName, user.getUserName())
                        .set(SysCompanyInfo::getContactPhone, user.getPhoneNumber());
        comUw.lambda().eq(SysCompanyInfo::getId, dto.getId());
        this.update(comUw);
        QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
        adminQw.lambda().eq(CompanyAdmin::getDeleted, YesNo.NO);
        adminQw.lambda().eq(CompanyAdmin::getCompanyId, dto.getId());
        CompanyAdmin admin = this.companyAdminMapper.selectOne(adminQw);
        if(admin ==  null){
            admin = new CompanyAdmin();
            admin.setId(UUIDutils.getUUID32());
            admin.setStaffId(dto.getStaffId());
            admin.setCompanyId(dto.getId());
            admin.setCreateUsername(UserUtils.getUserName());
            admin.setCreateBy(UserUtils.getUserId());
            admin.setCreateTime(new Date());
            admin.setDeleted(YesNo.NO);
            this.companyAdminMapper.insert(admin);
            return;
        }
        admin.setStaffId(dto.getStaffId());
        admin.setUpdateBy(UserUtils.getUserId());
        admin.setUpdateTime(new Date());
        admin.setUpdateUsername(UserUtils.getUserName());
        this.companyAdminMapper.updateById(admin);
    }


}
