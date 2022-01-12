package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.card.api.dto.UserCardDto;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.constant.MQ;
import com.zerody.common.constant.TimeDimensionality;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.enums.UserTypeEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.mq.RabbitMqService;
import com.zerody.common.util.*;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.sms.api.dto.SmsDto;
import com.zerody.sms.feign.SmsFeignService;
import com.zerody.user.api.dto.RatioPageDto;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.api.vo.CompanyInfoVo;
import com.zerody.user.domain.*;
import com.zerody.user.dto.ReportFormsQueryDto;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.enums.UserLoginStatusEnum;
import com.zerody.user.feign.CardFeignService;
import com.zerody.user.feign.ContractFeignService;
import com.zerody.user.feign.CustomerFeignService;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.*;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.*;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    private SysStaffInfoService sysStaffInfoService;

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

    @Autowired
    private CardFeignService cardService;

    @Autowired
    private CardUserInfoMapper cardUserMapper;

    @Autowired
    private CardUserUnionCrmUserMapper cardUserUnionCrmUserMapper;

    @Autowired
    private SysDepartmentInfoService departService;

    @Autowired
    private RabbitMqService mqService;

    @Autowired
    private CheckUtil checkUtil;

    @Autowired
    private ContractFeignService contractFeignService;

    @Autowired
    private CustomerFeignService customerFeignService;

    @Value("${sms.template.userTip:}")
    String userTipTemplate;

    @Value("${sms.sign.tsz:唐叁藏}")
    String smsSign;

    /**
     * @return com.zerody.common.bean.DataResult
     * @Author PengQiang
     * @Description //TODO   添加企业
     * @Date 2020/12/19 13:06
     * @Param [sysCompanyInfo]
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCompany(SysCompanyInfo sysCompanyInfo) {
        log.info("添加企业  ——> 入参：{}, 操作者信息：{}", JSON.toJSONString(sysCompanyInfo), JSON.toJSONString(UserUtils.getUser()));
        log.info("B端添加企业入参--{}", sysCompanyInfo);
        //构造查询条件
        if (!CheckParamUtils.chkMobile(sysCompanyInfo.getContactPhone())) {
            throw new DefaultException("企业联系人号码格式错误");
        }
        QueryWrapper<SysCompanyInfo> comQW = new QueryWrapper<>();
        comQW.lambda().ne(SysCompanyInfo::getStatus, StatusEnum.deleted.getValue());
        comQW.lambda().eq(SysCompanyInfo::getCompanyName, sysCompanyInfo.getCompanyName());
        //查询该企业是否存在
        Integer count = sysCompanyInfoMapper.selectCount(comQW);
        if (count > 0) {
            throw new DefaultException("企业名称已被占用");
        }
        QueryWrapper<SysUserInfo> userQw = new QueryWrapper<>();
        userQw.lambda().ne(SysUserInfo::getStatus, StatusEnum.deleted.getValue());
        userQw.lambda().eq(SysUserInfo::getPhoneNumber, sysCompanyInfo.getContactPhone());
        //查询该手机号码是否存在
        SysUserInfo user = this.sysUserInfoMapper.selectOne(userQw);
        if (user != null) {
            throw new DefaultException("该手机号码已存在！");
        }
        //添加企业默认为该企业为有效状态
        sysCompanyInfo.setStatus(StatusEnum.activity.getValue());
        log.info("B端添加企业入库参数--{}", JSON.toJSONString(sysCompanyInfo));
        sysCompanyInfo.setCreateId(UserUtils.getUserId());
        sysCompanyInfo.setCreateUser(UserUtils.getUserName());
        sysCompanyInfo.setIsEdit(YesNo.YES);
        sysCompanyInfo.setIsUpdateName(YesNo.NO);
        this.saveOrUpdate(sysCompanyInfo);
        SetSysUserInfoDto userInfoDto = new SetSysUserInfoDto();
        userInfoDto.setCertificateCard(sysCompanyInfo.getCertificateCard());
        userInfoDto.setCompanyId(sysCompanyInfo.getId());
        userInfoDto.setPhoneNumber(sysCompanyInfo.getContactPhone());
        userInfoDto.setUserName(sysCompanyInfo.getContactName());
        SysStaffInfo staff = this.sysStaffInfoService.addStaff(userInfoDto);
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

    }


    /**
     * @return com.zerody.common.bean.DataResult
     * @Author PengQiang
     * @Description //TODO   修改企业状态的同时修改该企业下的用户的登录状态
     * @Date 2020/12/18 17:46
     * @Param [companyId, loginStatus]
     */
    @Override
    public void updateCompanyStatus(String companyId, Integer loginStatus) {
        log.info("B端修改企业登录状态入参--companyId = {},loginStatus = {}", companyId, loginStatus);
        //判断企业id 与修改的状态 是否为空
        if (StringUtils.isBlank(companyId)) {
            throw new DefaultException("企业id不能为空");
        }
        if (loginStatus == null) {
            throw new DefaultException("状态不能为空");
        }

        //修改企业的状态
        SysCompanyInfo sysCompanyInfo = new SysCompanyInfo();
//        sysCompanyInfo.setLoginStatus(loginStatus);
        sysCompanyInfo.setId(companyId);
        //保存状态
        log.info("B端修改企业登录参数入库参数--{}", JSON.toJSONString(sysCompanyInfo));
        this.saveOrUpdate(sysCompanyInfo);
        //查询得到该企业下所有的用户id
        List<String> userIds = sysStaffInfoMapper.selectUserByCompanyId(companyId);
        //如果这个企业下没有员工那么久不用进行后面的操作直接返回成功
        if (userIds.size() == 0) {
            return;
        }
        //设置修改参数
        SysLoginInfo sysLoginInfo = new SysLoginInfo();
        //修改状态
        QueryWrapper<SysLoginInfo> loginQW = new QueryWrapper<>();
        loginQW.lambda().in(SysLoginInfo::getUserId, userIds);
        log.info("B端修改修改企业登录状态后修改用户登录状态入库参数--{}", JSON.toJSONString(sysLoginInfo));
        sysLoginInfoMapper.update(sysLoginInfo, loginQW);
    }

    @Override
    public IPage<SysComapnyInfoVo> getPageCompany(SysCompanyInfoDto companyInfoDto) {
        //设置分页参数
        IPage<SysComapnyInfoVo> iPage = new Page<>(companyInfoDto.getCurrent(), companyInfoDto.getPageSize());
        return sysCompanyInfoMapper.getPageCompany(companyInfoDto, iPage);
    }

    @Override
    public void updateCompany(SysCompanyInfo sysCompanyInfo) {
        log.info("修改企业信息  ——> 入参：{}, 操作者信息：{}", JSON.toJSONString(sysCompanyInfo), JSON.toJSONString(UserUtils.getUser()));
        if (!CheckParamUtils.chkMobile(sysCompanyInfo.getContactPhone())) {
            throw new DefaultException("企业联系人号码格式错误");
        }
        QueryWrapper<SysCompanyInfo> comQW = new QueryWrapper<>();
        comQW.lambda().ne(SysCompanyInfo::getStatus, StatusEnum.deleted.getValue());
        comQW.lambda().eq(SysCompanyInfo::getCompanyName, sysCompanyInfo.getCompanyName());
        comQW.lambda().ne(SysCompanyInfo::getId, sysCompanyInfo.getId());
        //查询该企业是否存在
        Integer count = sysCompanyInfoMapper.selectCount(comQW);
        if (count > 0) {
            throw new DefaultException("企业名称已被占用");
        }
        SysCompanyInfo company = this.getById(sysCompanyInfo.getId());
        if (!company.getCompanyName().equals(sysCompanyInfo.getCompanyName())) {
            sysCompanyInfo.setIsUpdateName(YesNo.YES);
        }
        sysCompanyInfo.setIsEdit(YesNo.YES);
        this.saveOrUpdate(sysCompanyInfo);
    }

    /**
     * @return com.zerody.common.bean.DataResult
     * @Author PengQiang
     * @Description //TODO   逻辑删除企业
     * @Date 2020/12/28 9:45
     * @Param companyId
     */
    @Override
    public void deleteCompanyById(String companyId) {
        log.info("删除企业  ——> 入参：companyId-{}, 操作者信息：{}", companyId, JSON.toJSONString(UserUtils.getUser()));
        if (StringUtils.isEmpty(companyId)) {
            throw new DefaultException("企业id为空");
        }
        QueryWrapper<SysDepartmentInfo> departQw = new QueryWrapper<>();
        departQw.lambda().eq(SysDepartmentInfo::getCompId, companyId);
        departQw.lambda().ne(SysDepartmentInfo::getStatus, StatusEnum.deleted.getValue());
        int count = this.departmentInfoService.count(departQw);
        if (count > 0) {
            throw new DefaultException("企业下有部门无法删除该企业");
        }
        SysCompanyInfo company = new SysCompanyInfo();
        company.setStatus(StatusEnum.deleted.getValue());
        company.setId(companyId);
        company.setIsEdit(YesNo.YES);
        this.saveOrUpdate(company);
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getCompId, company.getId());
        staffQw.lambda().ne(SysStaffInfo::getStatus, StatusEnum.deleted.getValue());
        List<SysStaffInfo> staffs = this.sysStaffInfoMapper.selectList(staffQw);

        List<String> userIds = staffs.stream().map(SysStaffInfo::getUserId).collect(Collectors.toList());
        SysUserInfo userInfo = new SysUserInfo();
        userInfo.setIsDeleted(YesNo.YES);
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().in(SysUserInfo::getId, userIds);
        this.sysUserInfoMapper.update(userInfo, userUw);
    }

    @Override
    public List<SysComapnyInfoVo> getAllCompany(String companyId) {
        List<SysComapnyInfoVo> companys = new ArrayList<>();
        //如果是crm系统就只查当前登录企业
        if (!StringUtils.isEmpty(companyId)) {
            SysComapnyInfoVo company = sysCompanyInfoMapper.selectCompanyInfoById(companyId);
            //获取当前企业下的部门、岗位
            company.setDeparts(departmentInfoService.getAllDepByCompanyId(company.getId()));
            companys.add(company);
            return companys;
        }

        //如果不是crm系统就查全部企业
        companys = sysCompanyInfoMapper.getAllCompnay();
        for (SysComapnyInfoVo company : companys) {
            company.setDeparts(departmentInfoService.getAllDepByCompanyId(company.getId()));
        }
        return companys;
    }

    @Override
    public List<SysComapnyInfoVo> getUserCompany(UserVo userVo) {
        List<SysComapnyInfoVo> companys = new ArrayList<>();
        AdminVo adminVo=sysStaffInfoService.getIsAdmin(userVo);
        //&&!adminVo.getIsDepartAdmin()
        if (!adminVo.getIsCompanyAdmin()) {
            if (!StringUtils.isEmpty(userVo.getCompanyId())) {
                SysComapnyInfoVo company = sysCompanyInfoMapper.selectCompanyInfoById(userVo.getCompanyId());
                //获取当前企业下的部门、岗位
                company.setDeparts(departmentInfoService.getAllDepByDepartId(company.getId(),userVo.getDeptId()));
                companys.add(company);
                return companys;
            }
        } else {
            if (!StringUtils.isEmpty(userVo.getCompanyId())) {
                SysComapnyInfoVo company = sysCompanyInfoMapper.selectCompanyInfoById(userVo.getCompanyId());
                //获取当前企业下的部门、岗位
                company.setDeparts(departmentInfoService.getAllDepByCompanyId(company.getId()));
                companys.add(company);
                return companys;
            }
            //如果不是crm系统就查全部企业
            companys = sysCompanyInfoMapper.getAllCompnay();
            for (SysComapnyInfoVo company : companys) {
                company.setDeparts(departmentInfoService.getAllDepByCompanyId(company.getId()));
            }
            return companys;
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
        log.info("设置企业管理员  ——> 入参：{}, 操作者信息：{}", JSON.toJSONString(dto), JSON.toJSONString(UserUtils.getUser()));
        if (StringUtils.isEmpty(dto.getId())) {
            throw new DefaultException("企业id为空");
        }
        if (StringUtils.isEmpty(dto.getStaffId())) {
            throw new DefaultException("员工id为空");
        }
        SysStaffInfo staff = this.sysStaffInfoMapper.selectById(dto.getStaffId());
        if (!staff.getCompId().equals(dto.getId())) {
            throw new DefaultException("该员工不是该企业下的");
        }
        SysUserInfo user = this.sysUserInfoMapper.selectById(staff.getUserId());

        UpdateWrapper<SysCompanyInfo> comUw = new UpdateWrapper<>();
        comUw.lambda().set(SysCompanyInfo::getContactName, user.getUserName())
                .set(SysCompanyInfo::getContactPhone, user.getPhoneNumber())
                .set(SysCompanyInfo::getIsEdit, YesNo.YES);
        comUw.lambda().eq(SysCompanyInfo::getId, dto.getId());
        this.update(comUw);
        QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
        adminQw.lambda().eq(CompanyAdmin::getDeleted, YesNo.NO);
        adminQw.lambda().eq(CompanyAdmin::getCompanyId, dto.getId());
        CompanyAdmin admin = this.companyAdminMapper.selectOne(adminQw);
        String oldAdminUserId = null;
        if (DataUtil.isNotEmpty(admin)) {
            SysStaffInfo oldAdmin = this.sysStaffInfoMapper.selectById(admin.getStaffId());
            oldAdminUserId = oldAdmin.getUserId();
        }
        if (admin == null) {
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
        //  设置企业负责人 清除该员工token
        this.checkUtil.removeUserToken(user.getId());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(oldAdminUserId)) {
            this.checkUtil.removeUserToken(oldAdminUserId);
        }
    }

    @Override
    public List<SysComapnyInfoVo> getCompanyInfoByAddr(List<String> cityCodes) {
        return this.sysCompanyInfoMapper.getCompanyInfoByAddr(cityCodes);
    }

    @Override
    public List<CompanyInfoVo> getCompanyInfoByIds(List<String> ids) {
        List<CompanyInfoVo> coms = this.sysCompanyInfoMapper.getCompanyInfoByIds(ids);
        return coms;
    }

    @Override
    public List<SysComapnyInfoVo> getCompanyAll() {
        return this.sysCompanyInfoMapper.getCompanyAll();
    }

    @Override
    public String getNameById(String id) {
        //  获取企业名称
        QueryWrapper<SysCompanyInfo> comQw = new QueryWrapper<>();
        comQw.lambda().select(SysCompanyInfo::getCompanyName);
        comQw.lambda().eq(SysCompanyInfo::getId, id);
        SysCompanyInfo company = this.getOne(comQw);
        if (DataUtil.isEmpty(company)) {
            return null;
        }
        return company.getCompanyName();
    }

    @Override
    public void updateRedundancyCompanyName() {
        //  获取修改名称的企业
        List<CompanyInfoVo> companyInfos = this.sysCompanyInfoMapper.getHaveUpdateCompanyName();
        if (CollectionUtils.isEmpty(companyInfos)) {
            return;
        }
        //  修改回原来的状态
        this.sysCompanyInfoMapper.updateIsUpdateName(companyInfos);
        //  发送消息修改冗余的企业名称
        companyInfos.stream().forEach(c -> {
            this.mqService.send(c, MQ.QUEUE_COMPANY_NAME_CUSTOMER);
        });

        log.info("发送企业名称修改通知:{}", JSON.toJSONString(companyInfos));
    }

    @Override
    public void doCompangEditInfo() {
        List<Map<String, String>> companyInfoMap = this.sysCompanyInfoMapper.getCompangEditInfo();
        if (CollectionUtils.isEmpty(companyInfoMap)) {
            return;
        }
        this.sysCompanyInfoMapper.updateCompanyEdit(companyInfoMap);
        mqService.send(companyInfoMap, MQ.QUEUE_COMPANY_EDIT_CUSTOMER);
        log.info("同步企业表 ————> {}", JSON.toJSONString(companyInfoMap));
    }

    @Override
    public Page<CompanyInfoVo> getPageInner(RatioPageDto pageQueryDto) {
        //设置分页参数
        IPage<SysComapnyInfoVo> iPage = new Page<>(pageQueryDto.getCurrent(), pageQueryDto.getPageSize());
        Page<CompanyInfoVo> pageCompanyInner = sysCompanyInfoMapper.getPageCompanyInner(pageQueryDto, iPage);
        return pageCompanyInner;
    }

    @Override
    public List<String> getNotSmsCompany() {
        return this.sysCompanyInfoMapper.getNotSmsCompany();
    }

    @Override
    public List<ReportFormsQueryVo> getReportForms(ReportFormsQueryDto param) {
        DataResult<List<String>> salesmanRolesResult = this.oauthFeignService.getSalesmanRole(param.getCompanyId());
        if (!salesmanRolesResult.isSuccess()) {
            throw new DefaultException("获取角色错误");
        }
        param.setSalesmanRoles(salesmanRolesResult.getData());
        int salesmanNum = this.sysStaffInfoMapper.getSalesmanNum(param);
        List<ReportFormsQueryVo> list = new ArrayList<>();
        if (DataUtil.isNotEmpty(param.getUserId())) {
            param.setTitle("伙伴");
            list = this.sysUserInfoMapper.getUserById(param.getUserId(), param.getSalesmanRoles());
        } else if (DataUtil.isNotEmpty(param.getDepartId())) {
            Boolean lastDepart = this.departService.getDepartIsFinally(param.getDepartId(), Boolean.TRUE);
            if (lastDepart) {
                param.setTitle("伙伴");
                list = this.sysUserInfoMapper.getUserByDepartId(param.getDepartId(), param.getSalesmanRoles());
                if (CollectionUtils.isNotEmpty(list)) {
                    List<String> userIds = list.stream().map(ReportFormsQueryVo::getId).collect(Collectors.toList());
                    param.setUserIds(userIds);
                }
                param.setDepartId(null);
            } else {
                param.setTitle("部门");
                list = this.departService.getDepartBusiness(param.getCompanyId(), param.getDepartId(), param.getSalesmanRoles());
            }
        } else if (DataUtil.isNotEmpty(param.getCompanyId())) {
            param.setTitle("部门");
            list = this.departService.getDepartBusiness(param.getCompanyId(), param.getDepartId(), param.getSalesmanRoles());
        } else {
            param.setTitle("公司");
            list = this.sysCompanyInfoMapper.getCompanyBusiness(param.getSalesmanRoles());
        }
        Map<String, ReportFormsQueryVo> signMap = null;
        Map<String, InviteStateVo> inviteMap = null;
        try {
            DataResult<List<ReportFormsQueryVo>> signResult = this.contractFeignService.getReportForms(param);
            if (!signResult.isSuccess()) {
                log.error("合同报表查询#合同信息查询出错:{}", signResult.getMessage());
            }
            List<ReportFormsQueryVo> sign = signResult.getData();
            if (CollectionUtils.isNotEmpty(sign)) {
                signMap = sign.stream().collect(Collectors.toMap(ReportFormsQueryVo::getId, a -> a, (k1, k2) -> k1, HashMap::new));
            }
        } catch (Exception e) {
            log.error("合同报表查询出错:{}", e, e);
        }
        try {
            DataResult<List<InviteStateVo>> signResult = this.customerFeignService.getInviteStatis(param);
            if (!signResult.isSuccess()) {
                log.error("报表查询#客户信息查询出错:{}", signResult.getMessage());
            }
            List<InviteStateVo> invite = signResult.getData();
            if (CollectionUtils.isNotEmpty(invite)) {
                inviteMap = invite.stream().collect(Collectors.toMap(InviteStateVo::getId, a -> a, (k1, k2) -> k1, HashMap::new));
            }
        } catch (Exception e) {
            log.error("客户报表查询出错:{}", e, e);
        }
        String id;
        String name;
        Integer num;
        ReportFormsQueryVo total = new ReportFormsQueryVo();
        total.setName("合计");
        for (ReportFormsQueryVo rfq : list) {
            id = rfq.getId();
            name = rfq.getName();
            num = salesmanNum;
            if (CollectionUtils.isNotEmpty(signMap) && DataUtil.isNotEmpty(signMap.get(rfq.getId()))) {
                BeanUtils.copyProperties(signMap.get(rfq.getId()), rfq);
                rfq.setId(id);
                rfq.setName(name);
                rfq.setSalesmanNum(num);
            }
            if (CollectionUtils.isNotEmpty(inviteMap) && DataUtil.isNotEmpty(inviteMap.get(rfq.getId()))) {
                rfq.setInviteNum(inviteMap.get(rfq.getId()).getInviteNum());
                rfq.setVisitNum(inviteMap.get(rfq.getId()).getSignNum());
            }
            total.setSignMoney(new BigDecimal(total.getSignMoney()).add(new BigDecimal(rfq.getSignMoney())).toString());
            total.setSignNum(total.getSignNum() + rfq.getSignNum());
            total.setSignMoneyTotal(new BigDecimal(total.getSignMoneyTotal()).add(new BigDecimal(rfq.getSignMoneyTotal())).toString());
            total.setSignNumTotal(total.getSignNumTotal() + rfq.getSignNumTotal());
            total.setApproveMoney(new BigDecimal(total.getApproveMoney()).add(new BigDecimal(rfq.getApproveMoney())).toString());
            total.setApproveNum(total.getApproveNum() + rfq.getApproveNum());
            total.setApproveMoneyTotal(new BigDecimal(total.getApproveMoneyTotal()).add(new BigDecimal(rfq.getApproveMoneyTotal())).toString());
            total.setApproveNumTotal(total.getApproveNumTotal() + rfq.getApproveNumTotal());
            total.setLoansMoney(new BigDecimal(total.getLoansMoney()).add(new BigDecimal(rfq.getLoansMoney())).toString());
            total.setLoansNum(total.getLoansNum() + rfq.getLoansNum());
            total.setLoansMoneyTotal(new BigDecimal(total.getLoansMoneyTotal()).add(new BigDecimal(rfq.getLoansMoneyTotal())).toString());
            total.setLoansNumTotal(total.getLoansNumTotal() + rfq.getLoansNumTotal());
            total.setNotProceedsMoney(new BigDecimal(total.getNotProceedsMoney()).add(new BigDecimal(rfq.getNotProceedsMoney())).toString());
            total.setNotProceedsNum(total.getNotProceedsNum() + rfq.getNotProceedsNum());
            total.setPerformanceMoney(new BigDecimal(total.getPerformanceMoney()).add(new BigDecimal(rfq.getPerformanceMoney())).toString());
            total.setPerformanceNum(total.getPerformanceNum() + rfq.getPerformanceNum());
            total.setPerformanceMoneyTotal(new BigDecimal(total.getPerformanceMoneyTotal()).add(new BigDecimal(rfq.getPerformanceMoneyTotal())).toString());
            total.setPerformanceNumTotal(total.getPerformanceNumTotal() + rfq.getPerformanceNumTotal());
            total.setPaymentCount(new BigDecimal(total.getPaymentCount()).add(new BigDecimal(rfq.getPaymentCount())).toString());
            total.setSalesmanNum(total.getSalesmanNum() + rfq.getSalesmanNum());
            total.setPaymentUserNum(total.getPaymentUserNum() + rfq.getPaymentUserNum());
            total.setPaymentMoney(new BigDecimal(total.getPaymentMoney()).add(new BigDecimal(rfq.getPaymentMoney())).toString());
            total.setPerCapitaPerformance(new BigDecimal(total.getPerCapitaPerformance()).add(new BigDecimal(rfq.getPerCapitaPerformance())).toString());
            total.setStaffPaymentRate(new BigDecimal(total.getSignMoney()).add(new BigDecimal(rfq.getSignMoney())).toString());
            total.setInviteNum(total.getInviteNum() + rfq.getInviteNum());
            total.setVisitNum(total.getVisitNum() + rfq.getVisitNum());
        }
        list.add(total);
        return list;
    }

    @Override
    public void getReportFormsExport(HttpServletResponse response, ReportFormsQueryDto param) throws IOException {
        List<ReportFormsQueryVo> list = this.getReportForms(param);
        if (CollectionUtils.isEmpty(list)) {
            throw new DefaultException("暂无数据导出");
        }
        String timePeriodStr = "总";
        if (StringUtils.isNotEmpty(param.getTimePeriod())) {
            switch (param.getTimePeriod()) {
                case TimeDimensionality.DAY:
                    timePeriodStr = "今日";
                    break;
                case TimeDimensionality.YESTER:
                    timePeriodStr = "昨日";
                    break;
                case TimeDimensionality.WEEK:
                    timePeriodStr = "本周";
                    break;
                case TimeDimensionality.MONTH:
                    timePeriodStr = "本月";
                    break;
                case TimeDimensionality.QUARTER:
                    timePeriodStr = "本季";
                    break;
                case TimeDimensionality.YEAR:
                    timePeriodStr = "本年";
                    break;
            }
        }
        List<String[]> exportData = new ArrayList<>();
        final String[] header = {param.getTitle(), timePeriodStr + "签单/笔数", "总共签单/笔数", timePeriodStr + "审批数额/笔数"
                , "总审批额/笔数", timePeriodStr + "放款额/笔数", "总放款额/笔数", "已放未收/笔数", timePeriodStr + "业绩/笔数",
                "总业绩/笔数", "回款点数", "人均业绩", "人员回款率", "邀约人数", "上门人数"};
        String[] rowData;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ReportFormsQueryVo info : list) {
            rowData = new String[15];
            int index = 0;
            rowData[index++] = info.getName();
            rowData[index++] = info.getSignMoney() + "/" + info.getSignNum() + "笔";
            rowData[index++] = info.getSignMoneyTotal() + "/" + info.getSignNumTotal() + "笔";
            rowData[index++] = info.getApproveMoney() + "/" + info.getApproveNum() + "笔";
            rowData[index++] = info.getApproveMoneyTotal() + "/" + info.getApproveNumTotal() + "笔";
            rowData[index++] = info.getLoansMoney() + "/" + info.getLoansNum() + "笔";
            rowData[index++] = info.getLoansMoneyTotal() + "/" + info.getLoansNumTotal() + "笔";
            rowData[index++] = info.getNotProceedsMoney() + "/" + info.getNotProceedsNum() + "笔";
            rowData[index++] = info.getPerformanceMoney() + "/" + info.getPerformanceNum() + "笔";
            rowData[index++] = info.getPerformanceMoneyTotal() + "/" + info.getPerformanceNumTotal() + "笔";
            rowData[index++] = info.getPaymentCount() + "%";
            rowData[index++] = info.getPerCapitaPerformance();
            rowData[index++] = info.getStaffPaymentRate() + "%";
            rowData[index++] = String.valueOf(info.getInviteNum());
            rowData[index++] = String.valueOf(info.getVisitNum());
            exportData.add(rowData);
        }
        exportData.get(exportData.size() - 1)[12] = "-";
        exportData.get(exportData.size() - 1)[10] = "-";
        HSSFWorkbook workbook = ExcelToolUtil.dataExcel(header, exportData);
        String fileName = "报表_" + System.currentTimeMillis();
//        response.getContentType("octets/stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xls");
        response.setCharacterEncoding("UTF-8");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();

    }

    @Override
    public List<SalesmanRoleInfoVo> getSalesmanRole(List<String> companyId, List<String> departId, List<String> userId) {
        DataResult<List<String>> salesmanRolesResult = this.oauthFeignService.getSalesmanRole(null);
        if (!salesmanRolesResult.isSuccess() || org.apache.commons.collections.CollectionUtils.isEmpty(salesmanRolesResult.getData())) {
            throw new DefaultException("获取角色错误");
        }
        if (CollectionUtils.isNotEmpty(companyId)) {
            return this.sysStaffInfoMapper.getSalesmanNumCompayList(companyId,  salesmanRolesResult.getData());
        }
        if (CollectionUtils.isNotEmpty(departId)) {
            return this.sysStaffInfoMapper.getSalesmanNumDepartList(departId,  salesmanRolesResult.getData());
        }
        if (CollectionUtils.isNotEmpty(userId)) {
            return this.sysStaffInfoMapper.getSalesmanNumUserList(userId,  salesmanRolesResult.getData());
        }
        return new ArrayList<>();
    }

    public void saveCardUser(SysUserInfo userInfo,SysLoginInfo loginInfo,SysCompanyInfo sysCompanyInfo){
        //添加员工即为内部员工需要生成名片小程序用户账号
        CardUserInfo cardUserInfo = new CardUserInfo();
        cardUserInfo.setUserName(userInfo.getUserName());
        cardUserInfo.setPhoneNumber(userInfo.getPhoneNumber());
        cardUserInfo.setCreateBy(UserUtils.getUserId());
        cardUserInfo.setCreateTime(new Date());
        cardUserInfo.setStatus(StatusEnum.activity.getValue());
        this.cardUserMapper.insert(cardUserInfo);

        //关联内部员工信息
        CardUserUnionUser cardUserUnionUser = new CardUserUnionUser();
        cardUserUnionUser.setId(UUIDutils.getUUID32());
        cardUserUnionUser.setCardId(cardUserInfo.getId());
        cardUserUnionUser.setUserId(userInfo.getId());
        cardUserUnionCrmUserMapper.insert(cardUserUnionUser);

        //生成基础名片信息
        UserCardDto cardDto = new UserCardDto();
        cardDto.setMobile(cardUserInfo.getPhoneNumber());
        cardDto.setUserName(cardUserInfo.getUserName());
        cardDto.setUserId(cardUserInfo.getId());
        cardDto.setAvatar(userInfo.getAvatar());
        cardDto.setEmail(userInfo.getEmail());
        cardDto.setCreateBy(UserUtils.getUserId());
        cardDto.setAddressProvince(sysCompanyInfo.getCompanyAddrProvinceCode());
        cardDto.setAddressCity(sysCompanyInfo.getCompanyAddressCityCode());
        cardDto.setAddressArea(sysCompanyInfo.getCompanyAddressAreaCode());
        cardDto.setAddressDetail(sysCompanyInfo.getCompanyAddress());
        DataResult<String> card = cardService.createCard(cardDto);
        if (!card.isSuccess()) {
            throw new DefaultException("服务异常！");
        }
    }

}
