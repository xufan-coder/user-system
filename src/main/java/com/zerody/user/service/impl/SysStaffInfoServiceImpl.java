package com.zerody.user.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSONObject;
import com.zerody.card.api.dto.UserCardDto;
import com.zerody.card.api.dto.UserCardReplaceDto;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.util.ExcelToolUtil;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.customer.api.dto.SetUserDepartDto;
import com.zerody.customer.api.dto.UserClewDto;
import com.zerody.customer.api.service.ClewRemoteService;
import com.zerody.sms.api.dto.SmsDto;
import com.zerody.sms.feign.SmsFeignService;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.domain.*;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.enums.StaffGenderEnum;
import com.zerody.user.feign.CardFeignService;
import com.zerody.user.feign.CustomerFeignService;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.mapper.*;
import com.zerody.user.service.*;
import com.zerody.user.vo.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.FileUtil;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.check.CheckUser;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.enums.StaffStatusEnum;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.util.IdCardUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoServiceImpl
 * @DateTime 2020/12/17_17:31
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysStaffInfoServiceImpl extends BaseService<SysStaffInfoMapper, SysStaffInfo> implements SysStaffInfoService {
    public static final String[] STAFF_EXCEL_TITTLE = new String[] {"姓名*","手机号码*","部门","岗位","角色*","状态","性别","籍贯","民族","婚姻","出生年月日","身份证号码","户籍地址","居住地址","电子邮箱","学历","毕业院校","所学专业"};
    public static final String[] COMPANY_STAFF_EXCEL_TITTLE = new String[] {"姓名*","手机号码*","企业*","部门","岗位","角色*","状态","性别","籍贯","民族","婚姻","出生年月日","身份证号码","户籍地址","居住地址","电子邮箱","学历","毕业院校","所学专业"};

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
    private OauthFeignService oauthFeignService;

    @Autowired
    private CompanyAdminMapper companyAdminMapper;

    @Autowired
    private CardUserInfoMapper cardUserInfoMapper;

    @Autowired
    private CardUserUnionCrmUserMapper cardUserUnionCrmUserMapper;

    @Autowired
    private CustomerFeignService customerFeignService;

    @Autowired
    private CardFeignService cardFeignService;

    @Autowired
    private SmsFeignService smsFeignService;

    @Autowired
    private ClewRemoteService clewService;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${sms.template.userTip:}")
    String userTipTemplate;

    @Value("${sms.sign.tsz:唐叁藏}")
    String smsSign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysStaffInfo addStaff(SetSysUserInfoDto setSysUserInfoDto) {
        SysUserInfo sysUserInfo=new SysUserInfo();
        DataUtil.getKeyAndValue(sysUserInfo,setSysUserInfoDto);
        log.info("添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        //参数校验
         CheckUser.checkParam(sysUserInfo);
        //查看手机号或登录名是否被占用
        Boolean flag = sysUserInfoMapper.selectUserByPhone(sysUserInfo.getPhoneNumber());
        if(flag){
            throw new DefaultException("该手机号已存在！");
        }
        //效验通过保存用户信息
        sysUserInfo.setRegisterTime(new Date());
        log.info("添加用户入库参数--{}",JSON.toJSONString(sysUserInfo));
        sysUserInfo.setCreateTime(new Date());
        sysUserInfo.setCreateUser(UserUtils.getUserName());
        sysUserInfo.setCreateId(UserUtils.getUserId());
        sysUserInfo.setStatus(StatusEnum.激活.getValue());
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
        logInfo.setStatus(StatusEnum.激活.getValue());
        log.info("添加用户后生成登录账户入库参数--{}",JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);
        SmsDto smsDto=new SmsDto();
        smsDto.setMobile(sysUserInfo.getPhoneNumber());
        Map<String, Object> content=new HashMap<>();
        content.put("userName",sysUserInfo.getPhoneNumber());
        content.put("passWord",initPwd);
        smsDto.setContent(content);
        smsDto.setTemplateCode(userTipTemplate);
        smsDto.setSign(smsSign);

        //保存员工信息
        SysStaffInfo staff = new SysStaffInfo();
        staff.setUserName(sysUserInfo.getUserName());
        staff.setCompId(setSysUserInfoDto.getCompanyId());
        staff.setStatus(setSysUserInfoDto.getStatus());
        staff.setUserId(sysUserInfo.getId());
        log.info("添加员工入库参数--{}",JSON.toJSONString(staff));
        staff.setStatus(StatusEnum.激活.getValue());
        staff.setDeleted(YesNo.NO);
        this.saveOrUpdate(staff);
        if(StringUtils.isNotEmpty(setSysUserInfoDto.getRoleId())){
            //角色
            UnionRoleStaff rs = new UnionRoleStaff();
            rs.setStaffId(staff.getId());
            rs.setRoleId(setSysUserInfoDto.getRoleId());
            //去查询角色名
            DataResult<?>  result= oauthFeignService.getRoleById(setSysUserInfoDto.getRoleId());
            if(!result.isSuccess()){
                throw new DefaultException("服务异常！");
            }
            JSONObject obj=(JSONObject)JSON.toJSON(result.getData());
            rs.setRoleName(obj.get("roleName").toString());
            unionRoleStaffMapper.insert(rs);
        }
        //岗位
        if(StringUtils.isNotEmpty(setSysUserInfoDto.getPositionId())){
            UnionStaffPosition sp = new UnionStaffPosition();
            sp.setPositionId(setSysUserInfoDto.getPositionId());
            sp.setStaffId(staff.getId());
            unionStaffPositionMapper.insert(sp);
        }
        //部门
        if(StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())){
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
        }
        //获取企业信息的地址用于生成名片
        QueryWrapper<SysCompanyInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(SysCompanyInfo::getId,setSysUserInfoDto.getCompanyId())
                .ne(BaseModel::getStatus,StatusEnum.删除.getValue());
        SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);


        //添加员工即为内部员工需要生成名片小程序用户账号
        //生成基础名片信息
        saveCardUser(sysUserInfo,logInfo,sysCompanyInfo);
        //最后发送短信
        smsFeignService.sendSms(smsDto);
        return staff;
    }


    @Override
    public IPage<BosStaffInfoVo> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto) {
        IPage<BosStaffInfoVo> infoVoIPage = new Page<>(sysStaffInfoPageDto.getCurrent(),sysStaffInfoPageDto.getPageSize());
        return sysStaffInfoMapper.getPageAllStaff(sysStaffInfoPageDto,infoVoIPage);
    }

    @Override
    public void updateStaffStatus(String staffId, Integer status) {
        if(StringUtils.isEmpty(staffId)){
            throw new DefaultException( "员工id不能为空");
        }
        if (status == null){
            throw new DefaultException( "状态不能为空");
        }
        SysStaffInfo staff = new SysStaffInfo();
        staff.setId(staffId);
        staff.setStatus(status);
        this.saveOrUpdate(staff);
    }


    @Override
    public void updateStaff(SetSysUserInfoDto setSysUserInfoDto) {
        SysUserInfo sysUserInfo=new SysUserInfo();
        DataUtil.getKeyAndValue(sysUserInfo,setSysUserInfoDto);
        sysUserInfo.setId(setSysUserInfoDto.getId());
        //参数校验
        CheckUser.checkParam(sysUserInfo);
        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(sysUserInfo);
        if(users != null && users.size() > 0){
            throw new DefaultException("手机号或用户名被占用");
        }
        //效验通过保存用户信息
        sysUserInfo.setUpdateTime(new Date());
        sysUserInfo.setUpdateUser(UserUtils.getUserName());
        sysUserInfo.setUpdateId(UserUtils.getUserId());
        sysUserInfoMapper.updateById(sysUserInfo);
        QueryWrapper<SysLoginInfo> loginQW = new QueryWrapper<>();
        loginQW.lambda().eq(SysLoginInfo::getUserId, sysUserInfo.getId());
        SysLoginInfo logInfo = sysLoginInfoMapper.selectOne(loginQW);
        logInfo.setMobileNumber(sysUserInfo.getPhoneNumber());
        logInfo.setNickname(sysUserInfo.getNickname());
        logInfo.setAvatar(sysUserInfo.getAvatar());
        logInfo.setStatus(StatusEnum.激活.getValue());
        sysLoginInfoService.addOrUpdateLogin(logInfo);
        //保存员工信息

        //查询得到员工信息
        QueryWrapper<SysStaffInfo> staffQW = new QueryWrapper<>();
        staffQW.lambda().eq(SysStaffInfo::getUserId, sysUserInfo.getId());
        SysStaffInfo staff = this.getOne(staffQW);
        staff.setStatus(setSysUserInfoDto.getStatus());
        staff.setUserName(setSysUserInfoDto.getUserName());
        this.saveOrUpdate(staff);
        //修改员工的时候删除该员工的全部角色
        QueryWrapper<UnionRoleStaff> ursQW = new QueryWrapper<>();
        ursQW.lambda().eq(UnionRoleStaff::getStaffId, staff.getId());
        unionRoleStaffMapper.delete(ursQW);
        UnionRoleStaff rs = new UnionRoleStaff();
        //给员工赋予角色
        rs.setStaffId(staff.getId());
        rs.setRoleId(setSysUserInfoDto.getRoleId());
        //去查询角色名
        DataResult<?>  result= oauthFeignService.getRoleById(setSysUserInfoDto.getRoleId());
        if(!result.isSuccess()){
            throw new DefaultException("服务异常！");
        }
        JSONObject obj=(JSONObject)JSON.toJSON(result.getData());
        rs.setRoleName(obj.get("roleName").toString());
        unionRoleStaffMapper.insert(rs);
        //删除该员工的部门
        QueryWrapper<UnionStaffPosition> uspQW = new QueryWrapper<>();
        uspQW.lambda().eq(UnionStaffPosition::getStaffId, staff.getId());
        unionStaffPositionMapper.delete(uspQW);
        //如果岗位id不为空就给该员工添加部门
        if(StringUtils.isNotEmpty(setSysUserInfoDto.getPositionId())){
            UnionStaffPosition sp = new UnionStaffPosition();
            sp.setPositionId(setSysUserInfoDto.getPositionId());
            sp.setStaffId(staff.getId());
            unionStaffPositionMapper.insert(sp);
        }
        QueryWrapper<UnionStaffDepart> usdQW = new QueryWrapper<>();
        usdQW.lambda().eq(UnionStaffDepart::getStaffId, staff.getId());
        UnionStaffDepart dep  = this.unionStaffDepartMapper.selectOne(usdQW);
        if (DataUtil.isNotEmpty(dep) && !dep.getDepartmentId().equals(setSysUserInfoDto.getDepartId())){
            SetUserDepartDto userDepart = new SetUserDepartDto();
            userDepart.setDepartId(setSysUserInfoDto.getDepartId());
            SysStaffInfo staffInfo = this.getById(staff.getId());
            userDepart.setUserId(staffInfo.getUserId());
            DataResult r = clewService.updateCustomerAndClewDepartIdByUser(userDepart);
            if (!r.isSuccess()){
                throw new DefaultException("网络错误");
            }
        }
        unionStaffDepartMapper.delete(usdQW);
        if(StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())){
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
            QueryWrapper<SysDepartmentInfo> depQw = new QueryWrapper<>();
            depQw.lambda().eq(SysDepartmentInfo::getAdminAccount, staff.getId());
            SysDepartmentInfo depAdmin = this.sysDepartmentInfoMapper.selectOne(depQw);//原负责的部门id
            if(depAdmin == null || depAdmin.getId().equals(setSysUserInfoDto.getDepartId())){
                return;
            }
            depAdmin.setAdminAccount(null);
            this.sysDepartmentInfoMapper.updateById(depAdmin);
        }
    }

    @Override
    public SysUserInfoVo selectStaffById(String id) {
        if(StringUtils.isEmpty(id)){
            throw new DefaultException("id不能为空");
        }
        return sysStaffInfoMapper.selectStaffById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStaffById(String staffId) {
        if(StringUtils.isEmpty(staffId)){
            throw new DefaultException( "员工id为空");
        }
        SysStaffInfo staff = this.getById(staffId);
        SysUserInfo userInfo = this.sysUserInfoMapper.selectById(staff.getUserId());
        userInfo.setStatus(StatusEnum.删除.getValue());
        this.sysUserInfoMapper.updateById(userInfo);
        //删除部门
        QueryWrapper<UnionStaffDepart> qw = new QueryWrapper<>();
        qw.lambda().eq(UnionStaffDepart::getStaffId,staffId);
        unionStaffDepartMapper.delete(qw);
        //删除角色
        QueryWrapper<UnionRoleStaff> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(UnionRoleStaff::getStaffId,staffId);
        unionRoleStaffMapper.delete(qw1);
        //删除岗位
        QueryWrapper<UnionStaffPosition> qw2 = new QueryWrapper<>();
        qw2.lambda().eq(UnionStaffPosition::getStaffId,staffId);
        unionStaffPositionMapper.delete(qw2);
    }

    @Override
    public List<String> getStaffRoles(String userId, String companyId) {
        List<String> roleIds=sysStaffInfoMapper.selectStaffRoles(userId,companyId);
        return roleIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchImportStaff(MultipartFile file) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String[]> dataList = FileUtil.fileImport(file);
        if(DataUtil.isEmpty(dataList)){
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

        //  部门
        List<UnionStaffDepart> unionStaffDepartList = new ArrayList<>();
        //  岗位
        List<UnionStaffPosition> unionStaffPositionList = new ArrayList<>();
        //  角色
        List<UnionRoleStaff> unionRoleStaffList = new ArrayList<>();
        //需要发送的短信集合
        List<SmsDto> smsDtos =new ArrayList<>();
        //错误集合
        List<String[]> errors = new ArrayList<>();
        //错误行
        String[] errorData = new String[headers.length+1];
        //错误字符构造
        StringBuilder errorStr = new StringBuilder("");

        //获取企业信息的地址用于生成名片
        QueryWrapper<SysCompanyInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(SysCompanyInfo::getId,UserUtils.getUser().getCompanyId())
                .ne(BaseModel::getStatus,StatusEnum.删除.getValue());
        SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);
        //循环行
        for (int rowIndex = dataIndex; rowIndex < dataList.size();rowIndex++) {
            //这一行的数据
            String[] row = dataList.get(rowIndex);
            for (int lineIndex = 0,lineLength = row.length; lineIndex<lineLength; lineIndex++){
                if(row[lineIndex] == null || "".equals(row[lineIndex])){
                    continue;
                }
                //去掉空格
                row[lineIndex] = row[lineIndex].trim();
            }

            //校验参数；
            UnionStaffDepart unionStaffDepart=new UnionStaffDepart();
            UnionStaffPosition unionStaffPosition=new UnionStaffPosition();
            UnionRoleStaff unionRoleStaff=new UnionRoleStaff();
            checkParam(row,errorStr,unionStaffDepart,unionStaffPosition,unionRoleStaff);
            if(errorStr.length() > 0){
                //如果有错误信息就下一次循环 不保存  并记录错误信息
                System.arraycopy(row,0,errorData,0,row.length);
                errorData[errorData.length - 1] = errorStr.toString();
                errors.add(errorData);
                //循环最后清空错误信息以方便记录下一次循环的错误信息
                errorStr = new StringBuilder("");
                errorData = new String[errorData.length];
                continue;
            }
            //构建用户信息；
            String staffId = saveUser(row, smsDtos,sysCompanyInfo);
            unionStaffDepart.setStaffId(staffId);
            unionStaffPosition.setStaffId(staffId);
            unionRoleStaff.setStaffId(staffId);
            if(DataUtil.isNotEmpty(unionStaffPosition.getPositionId())){
                unionStaffPositionList.add(unionStaffPosition);
            }
            if(DataUtil.isNotEmpty(unionStaffDepart.getDepartmentId())){
                unionStaffDepartList.add(unionStaffDepart);
            }
            unionRoleStaffList.add(unionRoleStaff);
        }
        //保存关联关系
        if(DataUtil.isNotEmpty(unionStaffDepartList)){
            unionStaffDeparService.saveBatch(unionStaffDepartList);
        }
        if(DataUtil.isNotEmpty(unionStaffPositionList)){
            unionStaffPositionService.saveBatch(unionStaffPositionList);
        }
        unionRoleStaffService.saveBatch(unionRoleStaffList);

        //数据总条数
        result.put("total", dataList.size()-1);
        //异常条数
        result.put("errorCount", errors.size());
        //成功条数
        result.put("successCount", dataList.size() - errors.size()-1);
        //标红必填项
        Integer[] mustIndex = {0, 1, 4};
        if(errors.size()>0){
            String[] heads=Arrays.copyOf(headers,headers.length+1);
            heads[heads.length-1] = "导入失败原因";
            HSSFWorkbook hw = ExcelToolUtil.createExcel(heads, errors, mustIndex);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            hw.write(os);
            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
            String base64 = FileUtil.fileToBase64(inputStream);
            result.put("base64File", base64);
        }
        sendInitPwdSms(smsDtos);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchImportCompanyUser(MultipartFile file) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String[]> dataList = FileUtil.fileImport(file);
        if(DataUtil.isEmpty(dataList)){
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

        //  部门
        List<UnionStaffDepart> unionStaffDepartList = new ArrayList<>();
        //  岗位
        List<UnionStaffPosition> unionStaffPositionList = new ArrayList<>();
        //  角色
        List<UnionRoleStaff> unionRoleStaffList = new ArrayList<>();
        //需要发送的短信集合
        List<SmsDto> smsDtos =new ArrayList<>();
        //错误集合
        List<String[]> errors = new ArrayList<>();
        //错误行
        String[] errorData = new String[headers.length+1];
        //错误字符构造
        StringBuilder errorStr = new StringBuilder("");

        //循环行
        for (int rowIndex = dataIndex; rowIndex < dataList.size();rowIndex++) {
            //这一行的数据
            String[] row = dataList.get(rowIndex);
            for (int lineIndex = 0,lineLength = row.length; lineIndex<lineLength; lineIndex++){
                if(row[lineIndex] == null || "".equals(row[lineIndex])){
                    continue;
                }
                //去掉空格
                row[lineIndex] = row[lineIndex].trim();
            }

            //校验参数；
            UnionStaffDepart unionStaffDepart=new UnionStaffDepart();
            UnionStaffPosition unionStaffPosition=new UnionStaffPosition();
            UnionRoleStaff unionRoleStaff=new UnionRoleStaff();
            String companyId = checkCompanyParam(row, errorStr, unionStaffDepart, unionStaffPosition, unionRoleStaff);
            if(errorStr.length() > 0){
                //如果有错误信息就下一次循环 不保存  并记录错误信息
                System.arraycopy(row,0,errorData,0,row.length);
                errorData[errorData.length - 1] = errorStr.toString();
                errors.add(errorData);
                //循环最后清空错误信息以方便记录下一次循环的错误信息
                errorStr = new StringBuilder("");
                errorData = new String[errorData.length];
                continue;
            }
            //构建用户信息；
            String staffId = saveCompanyUser(row, smsDtos,companyId);
            unionStaffDepart.setStaffId(staffId);
            unionStaffPosition.setStaffId(staffId);
            unionRoleStaff.setStaffId(staffId);
            if(DataUtil.isNotEmpty(unionStaffPosition.getPositionId())){
                unionStaffPositionList.add(unionStaffPosition);
            }
            if(DataUtil.isNotEmpty(unionStaffDepart.getDepartmentId())){
                unionStaffDepartList.add(unionStaffDepart);
            }
            unionRoleStaffList.add(unionRoleStaff);
        }
        //保存关联关系
        if(DataUtil.isNotEmpty(unionStaffDepartList)){
            unionStaffDeparService.saveBatch(unionStaffDepartList);
        }
        if(DataUtil.isNotEmpty(unionStaffPositionList)){
            unionStaffPositionService.saveBatch(unionStaffPositionList);
        }
        unionRoleStaffService.saveBatch(unionRoleStaffList);

        //数据总条数
        result.put("total", dataList.size()-1);
        //异常条数
        result.put("errorCount", errors.size());
        //成功条数
        result.put("successCount", dataList.size() - errors.size()-1);
        //标红必填项
        Integer[] mustIndex = {0, 1,2, 5};
        if(errors.size()>0){
            String[] heads=Arrays.copyOf(headers,headers.length+1);
            heads[heads.length-1] = "导入失败原因";
            HSSFWorkbook hw = ExcelToolUtil.createExcel(heads, errors, mustIndex);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            hw.write(os);
            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
            String base64 = FileUtil.fileToBase64(inputStream);
            result.put("base64File", base64);
        }
        sendInitPwdSms(smsDtos);
        return result;
    }

    private String saveCompanyUser(String[] row,List<SmsDto> smsDtos,String companyId) {
        //表头对应下标
        //{"姓名","手机号码","企业","部门","岗位","角色","状态","性别【7】","籍贯","民族","婚姻","出生年月日"【11】,
        // "身份证号码","户籍地址","居住地址[14]","电子邮箱","最高学历","毕业院校","所学专业"【18】};
        SysUserInfo userInfo=new SysUserInfo();
        userInfo.setUserName(row[0]);
        userInfo.setPhoneNumber(row[1]);
        userInfo.setGender(row[7].equals(StaffGenderEnum.MALE.getDesc())?StaffGenderEnum.MALE.getValue():StaffGenderEnum.FEMALE.getValue());
        userInfo.setAncestral(row[8]);
        userInfo.setNation(row[9]);
        userInfo.setMaritalStatus(row[10]);
        userInfo.setBirthday(new Date(row[11]));
        userInfo.setCertificateCard(row[12]);
        userInfo.setCertificateCardAddress(row[13]);
        userInfo.setContactAddress(row[14]);
        userInfo.setEmail(row[15]);
        userInfo.setHighestEducation(row[16]);
        userInfo.setGraduatedFrom(row[17]);
        userInfo.setMajor(row[18]);
        userInfo.setRegisterTime(new Date());
        userInfo.setStatus(StatusEnum.激活.getValue());
        userInfo.setCreateId(UserUtils.getUserId());
        userInfo.setCreateUser(UserUtils.getUserName());
        userInfo.setCreateTime(new Date());
        //因为员工表跟登录表都要用到用户所有先保存用户
        sysUserInfoMapper.insert(userInfo);

        SysStaffInfo staff = new SysStaffInfo();
        staff.setCompId(companyId);
        staff.setUserName(userInfo.getUserName());
        staff.setUserId(userInfo.getId());
        Integer status =row[6].equals(StaffStatusEnum.BE_ON_THE_JOB.getDesc())?StaffStatusEnum.BE_ON_THE_JOB.getCode():
                row[6].equals(StaffStatusEnum.DIMISSION.getDesc())?StaffStatusEnum.DIMISSION.getCode():StaffStatusEnum.COLLABORATE.getCode();
        staff.setStatus(status);
        staff.setDeleted(YesNo.NO);
        //保存到员工表
        this.saveOrUpdate(staff);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SysLoginInfo loginInfo = new SysLoginInfo();
        loginInfo.setMobileNumber(userInfo.getPhoneNumber());
        String initPwd = SysStaffInfoService.getInitPwd();
        loginInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));
        loginInfo.setUserId(userInfo.getId());
        sysLoginInfoService.addOrUpdateLogin(loginInfo);
        SmsDto smsDto=new SmsDto();
        smsDto.setMobile(userInfo.getPhoneNumber());
        Map<String, Object> content=new HashMap<>();
        content.put("userName",userInfo.getPhoneNumber());
        content.put("passWord",initPwd);
        smsDto.setContent(content);
        smsDto.setTemplateCode(userTipTemplate);
        smsDto.setSign(smsSign);
        smsDtos.add(smsDto);


        //获取企业信息的地址用于生成名片
        QueryWrapper<SysCompanyInfo> qw=new QueryWrapper<>();
        qw.lambda().eq(SysCompanyInfo::getId,companyId)
                .ne(BaseModel::getStatus,StatusEnum.删除.getValue());
        SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);

        //添加员工即为内部员工需要生成名片小程序用户账号
        //生成基础名片信息
        saveCardUser(userInfo,loginInfo,sysCompanyInfo);
        return staff.getId();
    }

    private String checkCompanyParam(String[] row, StringBuilder errorStr,
                            UnionStaffDepart unionStaffDepart, UnionStaffPosition unionStaffPosition, UnionRoleStaff unionRoleStaff) {
        String companyId=null;
        //表头对应下标
        //{"姓名","手机号码","企业","部门","岗位","角色","状态","性别","籍贯","民族","婚姻","出生年月日"【11】,
        // "身份证号码","户籍地址","居住地址","电子邮箱","最高学历","毕业院校","所学专业"【18】};
        //必填项校验
        if(DataUtil.isEmpty(row[0])||DataUtil.isEmpty(row[1])||DataUtil.isEmpty(row[2])||DataUtil.isEmpty(row[5])){
            errorStr.append("请填写红色区域必填项,");
        }
        //手机号码校验
        String phone=row[1];
        if(DataUtil.isNotEmpty(phone)){
            //手机号码进行格式校验
            if(checkPhone(phone)){
                errorStr.append("手机号码格式不正确,");
            }else {
                //手机号码判断是否已注册账户
                if(sysUserInfoMapper.selectUserByPhone(phone)){
                    errorStr.append("此手机号码已注册过账户,");
                }
            }
            //身份证号码校验
            String cardId=row[12];
            //验证身份证
            if(DataUtil.isNotEmpty(cardId)) {
                if (!IdCardUtil.isValidatedAllIdcard(cardId)) {
                    errorStr.append("身份证错误,");
                }
            }
            //先校验企业存不存在，企业不存在则不需要在校验部门岗位角色
            String companyName=row[2];
            QueryWrapper<SysCompanyInfo> qw=new QueryWrapper<>();
            qw.lambda().eq(SysCompanyInfo::getCompanyName,companyName)
                    .ne(BaseModel::getStatus,StatusEnum.删除.getValue());
            SysCompanyInfo sysCompanyInfo = sysCompanyInfoMapper.selectOne(qw);
            if(DataUtil.isEmpty(sysCompanyInfo)){
                errorStr.append(companyName+"该企业不存在,");
            }else {
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
                        if(DataUtil.isNotEmpty(byName)) {
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
        return companyId;
    }

    private String saveUser(String[] row,List<SmsDto> smsDtos,SysCompanyInfo sysCompanyInfo) {
        //表头对应下标
        //{"姓名","手机号码","部门","岗位","角色","状态","性别【6】","籍贯","民族","婚姻","出生年月日"【10】,
        // "身份证号码","户籍地址","居住地址[13]","电子邮箱","最高学历","毕业院校","所学专业"【17】};
        SysUserInfo userInfo=new SysUserInfo();
        userInfo.setUserName(row[0]);
        userInfo.setPhoneNumber(row[1]);
        userInfo.setGender(row[6].equals(StaffGenderEnum.MALE.getDesc())?StaffGenderEnum.MALE.getValue():StaffGenderEnum.FEMALE.getValue());
        userInfo.setAncestral(row[7]);
        userInfo.setNation(row[8]);
        userInfo.setMaritalStatus(row[9]);
        userInfo.setBirthday(new Date(row[10]));
        userInfo.setCertificateCard(row[11]);
        userInfo.setCertificateCardAddress(row[12]);
        userInfo.setContactAddress(row[13]);
        userInfo.setEmail(row[14]);
        userInfo.setHighestEducation(row[15]);
        userInfo.setGraduatedFrom(row[16]);
        userInfo.setMajor(row[17]);
        userInfo.setRegisterTime(new Date());
        userInfo.setStatus(StatusEnum.激活.getValue());
        userInfo.setCreateId(UserUtils.getUserId());
        userInfo.setCreateUser(UserUtils.getUserName());
        userInfo.setCreateTime(new Date());
        //因为员工表跟登录表都要用到用户所有先保存用户
        sysUserInfoMapper.insert(userInfo);

        SysStaffInfo staff = new SysStaffInfo();
        //获取当前登录用户的当前公司id
        staff.setCompId(UserUtils.getUser().getCompanyId());
        staff.setUserName(userInfo.getUserName());
        staff.setUserId(userInfo.getId());
        Integer status =row[5].equals(StaffStatusEnum.BE_ON_THE_JOB.getDesc())?StaffStatusEnum.BE_ON_THE_JOB.getCode():
                row[5].equals(StaffStatusEnum.DIMISSION.getDesc())?StaffStatusEnum.DIMISSION.getCode():StaffStatusEnum.COLLABORATE.getCode();
        staff.setStatus(status);
        staff.setDeleted(YesNo.NO);
        //保存到员工表
        this.saveOrUpdate(staff);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SysLoginInfo loginInfo = new SysLoginInfo();
        loginInfo.setMobileNumber(userInfo.getPhoneNumber());
        String initPwd = SysStaffInfoService.getInitPwd();
        loginInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5(initPwd)));
        loginInfo.setUserId(userInfo.getId());
        sysLoginInfoService.addOrUpdateLogin(loginInfo);
        SmsDto smsDto=new SmsDto();
        smsDto.setMobile(userInfo.getPhoneNumber());
        Map<String, Object> content=new HashMap<>();
        content.put("userName",userInfo.getPhoneNumber());
        content.put("passWord",initPwd);
        smsDto.setContent(content);
        smsDto.setTemplateCode(userTipTemplate);
        smsDto.setSign(smsSign);
        smsDtos.add(smsDto);

        //添加员工即为内部员工需要生成名片小程序用户账号
        //生成基础名片信息
        saveCardUser(userInfo,loginInfo,sysCompanyInfo);

        return staff.getId();
    }

    private void checkParam(String[] row, StringBuilder errorStr,
                            UnionStaffDepart unionStaffDepart, UnionStaffPosition unionStaffPosition, UnionRoleStaff unionRoleStaff) {
        //当前企业ID
        String companyId = UserUtils.getUser().getCompanyId();
        //表头对应下标
        //{"姓名","手机号码","部门","岗位","角色","状态","性别","籍贯","民族","婚姻","出生年月日"【10】,
        // "身份证号码","户籍地址","居住地址","电子邮箱","最高学历","毕业院校","所学专业"【17】};
        //必填项校验
        if(DataUtil.isEmpty(row[0])||DataUtil.isEmpty(row[1])||DataUtil.isEmpty(row[4])){
            errorStr.append("请填写红色区域必填项,");
        }
        //手机号码校验
        String phone=row[1];
        if(DataUtil.isNotEmpty(phone)){
            //手机号码进行格式校验
            if(checkPhone(phone)){
                errorStr.append("手机号码格式不正确,");
            }else {
                //手机号码判断是否已注册账户
                if(sysUserInfoMapper.selectUserByPhone(phone)){
                    errorStr.append("此手机号码已注册过账户,");
                }
            }
            //身份证号码校验
            String cardId=row[11];
            //验证身份证
            if(DataUtil.isNotEmpty(cardId)) {
                if (!IdCardUtil.isValidatedAllIdcard(cardId)) {
                    errorStr.append("身份证错误");
                }
            }

            String departName=row[2];
            String jobName=row[3];
            String roleName=row[4];
            //本企业角色是否存在
            DataResult roleByName = oauthFeignService.getRoleByName(companyId, roleName);
            if(!roleByName.isSuccess()){
                throw new DefaultException("服务异常");
            }else {
                if(DataUtil.isEmpty(roleByName.getData())){
                    errorStr.append("角色不存在,");
                }else {
                    JSONObject obj=(JSONObject)JSON.toJSON(roleByName.getData());
                    unionRoleStaff.setRoleId(obj.get("id").toString());
                }
            }
            //本企业部门是否存在
            SysDepartmentInfo byName=null;
            if(DataUtil.isNotEmpty(departName)) {
                byName = sysDepartmentInfoService.getByName(departName, companyId);
                if (DataUtil.isEmpty(byName)) {
                    errorStr.append("部门不存在,");
                } else {
                    unionStaffDepart.setDepartmentId(byName.getId());
                }
            }
            //本企业岗位是否存在//如果没填部门则直接找岗位

            if(DataUtil.isNotEmpty(jobName)) {
                SysJobPosition jobByDepart=null;
                if(DataUtil.isNotEmpty(departName)){
                    if(DataUtil.isNotEmpty(byName)) {
                        jobByDepart = sysJobPositionService.getJobByDepart(jobName, companyId, byName.getId());
                    }
                }else {
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

    public void saveCardUser(SysUserInfo userInfo,SysLoginInfo loginInfo,SysCompanyInfo sysCompanyInfo){
        QueryWrapper<SysUserInfo> userInfosQw = new QueryWrapper<>();
        userInfosQw.lambda().eq(SysUserInfo::getPhoneNumber, userInfo.getPhoneNumber())
                            .orderByDesc(SysUserInfo::getCreateTime);
        List<SysUserInfo>  userInfos = this.sysUserInfoMapper.selectList(userInfosQw);
        //取得当前手机号入库的所有生成用户的记录时间降序排序 第一条必是 新增的一条
        if(CollectionUtils.isNotEmpty(userInfos) && userInfos.size() >1){
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
        CardUserInfo cardUserInfo=new CardUserInfo();
        cardUserInfo.setUserName(userInfo.getUserName());
        cardUserInfo.setPhoneNumber(userInfo.getPhoneNumber());
        cardUserInfo.setUserPwd(loginInfo.getUserPwd());
        cardUserInfo.setCreateBy(UserUtils.getUserId());
        cardUserInfo.setCreateTime(new Date());
        cardUserInfo.setStatus(StatusEnum.激活.getValue());
        cardUserInfoMapper.insert(cardUserInfo);

        //关联内部员工信息
        CardUserUnionUser cardUserUnionUser=new CardUserUnionUser();
        cardUserUnionUser.setId(UUIDutils.getUUID32());
        cardUserUnionUser.setCardId(cardUserInfo.getId());
        cardUserUnionUser.setUserId(userInfo.getId());
        cardUserUnionCrmUserMapper.insert(cardUserUnionUser);

        //生成基础名片信息
        UserCardDto cardDto=new UserCardDto();
        cardDto.setMobile(cardUserInfo.getPhoneNumber());
        cardDto.setUserName(cardUserInfo.getUserName());
        //crm用户ID
        cardDto.setUserId(userInfo.getId());
        //名片用户ID
        cardDto.setCustomerUserId(cardUserInfo.getId());
        cardDto.setAvatar(userInfo.getAvatar());
        cardDto.setEmail(userInfo.getEmail());
        cardDto.setUserId(userInfo.getId());
        cardDto.setCustomerUserId(cardUserInfo.getId());
        cardDto.setCreateBy(UserUtils.getUserId());
        cardDto.setAddressProvince(sysCompanyInfo.getCompanyAddrProvinceCode());
        cardDto.setAddressCity(sysCompanyInfo.getCompanyAddressCityCode());
        cardDto.setAddressArea(sysCompanyInfo.getCompanyAddressAreaCode());
        cardDto.setAddressDetail(sysCompanyInfo.getCompanyAddress());
        DataResult<String> card = cardFeignService.createCard(cardDto);
        if(!card.isSuccess()){
            throw new DefaultException("服务异常！");
        }
    }

    @Async
    public void sendInitPwdSms(List<SmsDto> smsDtos){
        if(DataUtil.isNotEmpty(smsDtos)) {
            smsDtos.stream().forEach(item -> {
                smsFeignService.sendSms(item);
            });
        }
    }


    @Override
    public List<BosStaffInfoVo> getStaff(String companyId, String departId, String positionId) {
        return sysStaffInfoMapper.getStaff(companyId,departId,positionId);
    }

    @Override
    public IPage<BosStaffInfoVo> getAdmins(AdminsPageDto dto) {
        IPage<BosStaffInfoVo> voIPage = new Page<>(dto.getCurrent(),dto.getPageSize());
        return sysStaffInfoMapper.selectAdmins(dto,voIPage);
    }

    /**
     * @Author               PengQiang
     * @Description //TODO   手机合法查看
     * @Date                 2020/12/20 21:27
     * @Param                [phone]
     * @return               boolean
     */
    private boolean checkPhone(String phone){
        if(StringUtils.isBlank(phone)){
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
		String staffId=this.getStaffIdByUserId(userId);
		if(StringUtils.isEmpty(staffId)){
		    throw new DefaultException("没有该员工");
        }
		return this.sysStaffInfoMapper.selectUserDeptInfoById(staffId);
	}


	/**
	 *
	 *  用户子级部门
	 * @author               PengQiang
	 * @description          DELL
	 * @date                 2021/1/7 17:54
	 * @param               userId
	 * @return               java.util.List<java.lang.String>
	 */
	@Override
	public List<String> getUserSubordinates(String userId) {
		String staffId=this.getStaffIdByUserId(userId);
		if(StringUtils.isEmpty(staffId)){
            throw new DefaultException("没有该员工");
        }

        //子级部门id集合
        List<String> depIds = new ArrayList<>();
		String thisDep = "";
		SysStaffInfo staff = this.getById(staffId);
		QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
		adminQw.lambda().eq(CompanyAdmin::getCompanyId, staff.getCompId());
        CompanyAdmin com = this.companyAdminMapper.selectOne(adminQw);
        if(!staff.getId().equals(com.getStaffId())){
            QueryWrapper<SysDepartmentInfo> depQw = new QueryWrapper<>();
            depQw.lambda().eq(SysDepartmentInfo::getAdminAccount, staffId);
            depQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.激活.getValue());
            SysDepartmentInfo dep  = this.sysDepartmentInfoMapper.selectOne(depQw);
            if(dep == null){
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
        staffQw.lambda().and(sta ->sta.eq(SysStaffInfo::getStatus, StatusEnum.激活.getValue()).or().eq(SysStaffInfo::getStatus, StaffStatusEnum.COLLABORATE.getCode()));
        SysStaffInfo staff = this.getOne(staffQw);
        if (staff == null){
            throw new DefaultException("未找到员工");
        }
        //获取当前员工企业
        QueryWrapper<CompanyAdmin> adminQw = new QueryWrapper<>();
        adminQw.lambda().eq(CompanyAdmin::getDeleted, YesNo.NO);
        adminQw.lambda().eq(CompanyAdmin::getCompanyId, staff.getCompId());
        CompanyAdmin com = this.companyAdminMapper.selectOne(adminQw);
        //设置分页参数
        IPage<SysUserClewCollectVo> iPage = new Page<>(dto.getCurrent(), dto.getCurrent() == 1 ? dto.getPageSize() - 1 : dto.getPageSize());
        //用于请求获取用户分页
        List<String> userIds = new ArrayList<>();
        //线索集合
        List<UserClewDto> clews;
        SysUserClewCollectVo userInfo = this.sysStaffInfoMapper.selectUserInfo(staff.getId());
        //查看当前员工是否是企业管理员
        if(!staff.getId().equals(com.getStaffId())){
            //不是企业管理员 查看是否是部门管理员
            QueryWrapper<SysDepartmentInfo> depQw = new QueryWrapper<>();
            depQw.lambda().eq(SysDepartmentInfo::getAdminAccount, staff.getId());
            depQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.激活.getValue());
            SysDepartmentInfo dep  = this.sysDepartmentInfoMapper.selectOne(depQw);
            //不是部门管理员获取自己的线索总汇
            if(dep == null){
                iPage = new Page<>(dto.getCurrent(), dto.getCurrent());
                iPage.setRecords(new ArrayList<>());
                iPage.getRecords().add(userInfo);
                iPage.setTotal(0);
                userIds.add(iPage.getRecords().get(0).getUserId());
                clews = this.customerFeignService.getClews(userIds).getData();
                if(CollectionUtils.isEmpty(clews)){
                    return iPage;
                }
                BeanUtils.copyProperties(clews.get(0), iPage.getRecords().get(0));
                return iPage;
            }
            //获取全部的部门
            List<SysDepartmentInfoVo> deps = sysDepartmentInfoMapper.getAllDepByCompanyId(staff.getCompId());
            //用户id集合暂存部门id集合
            userIds.add(dep.getId());
            getChilden(userIds, deps, dep.getId() );
            iPage = this.sysStaffInfoMapper.getStaffByDepIds(userIds, iPage , staff.getCompId());
            userIds.removeAll(userIds);
        } else {
            //企业管理员不需要获取下级部门
            iPage = this.sysStaffInfoMapper.getStaffByDepIds(null, iPage, staff.getCompId());

        }
        if(CollectionUtils.isEmpty(iPage.getRecords())){
            iPage.setRecords(new ArrayList<>());
        }
        iPage.getRecords().add(0, userInfo);
        userIds = iPage.getRecords().stream().map(SysUserClewCollectVo::getUserId).collect(Collectors.toList());
        clews = this.customerFeignService.getClews(userIds).getData();
        if(iPage.getCurrent() == 1){
            iPage.setSize(iPage.getSize() + iPage.getCurrent());
            iPage.setTotal(iPage.getTotal() + iPage.getCurrent());
        }
        if(CollectionUtils.isEmpty(clews)){
            return iPage;
        }
        //转为有序map
        LinkedHashMap<String, SysUserClewCollectVo> userClewMap = iPage.getRecords().stream().collect(Collectors.toMap(SysUserClewCollectVo::getUserId, a -> a, (k1, k2) -> k1,LinkedHashMap::new));
        for (UserClewDto clew : clews){
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
    public SysUserInfoVo selectStaffByUserId(String userId) {
        return sysStaffInfoMapper.selectStaffByUserId(userId);
    }

    @Override
    public IPage<BosStaffInfoVo> getWxPageAllStaff(SysStaffInfoPageDto dto) {
	    if(StringUtils.isEmpty(dto.getCompanyId())){
	        throw new DefaultException("员工企业获取失败");
        }
        IPage<BosStaffInfoVo> iPage = new Page<>(dto.getCurrent(),dto.getPageSize());
        return this.sysStaffInfoMapper.getWxPageAllStaff(dto, iPage);
    }

    @Override
    public void doEmptySubordinatesUserClew(String id) {
        //通过用户id获取员工
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, id);
        staffQw.lambda().and(sta ->sta.eq(SysStaffInfo::getStatus, StatusEnum.激活.getValue()).or().eq(SysStaffInfo::getStatus, StaffStatusEnum.COLLABORATE.getCode()));
        SysStaffInfo staff = this.getOne(staffQw);
        if (staff == null){
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
        if(!staff.getId().equals(com.getStaffId())){
            //不是企业管理员 查看是否是部门管理员
            QueryWrapper<SysDepartmentInfo> depQw = new QueryWrapper<>();
            depQw.lambda().eq(SysDepartmentInfo::getAdminAccount, staff.getId());
            depQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.激活.getValue());
            SysDepartmentInfo dep  = this.sysDepartmentInfoMapper.selectOne(depQw);
            //不是部门管理员获取自己的线索总汇
            if(dep == null){
                 this.customerFeignService.doEmpatySubordinateUserClew(userIds);
                return;
            }
            //获取全部的部门
            List<SysDepartmentInfoVo> deps = sysDepartmentInfoMapper.getAllDepByCompanyId(staff.getCompId());
            //用户id集合暂存部门id集合
            userIds.add(dep.getId());
            getChilden(userIds, deps, dep.getId() );
            users = this.sysStaffInfoMapper.getStaffAllByDepIds(userIds , staff.getCompId());
            userIds.removeAll(userIds);
        } else {
            //企业管理员不需要获取下级部门
            users = this.sysStaffInfoMapper.getStaffAllByDepIds(null, staff.getCompId());
        }
        userIds.addAll(users.stream().map(SysUserClewCollectVo::getUserId).collect(Collectors.toList()));
        this.customerFeignService.doEmpatySubordinateUserClew(userIds);
    }

    @Override
    public AdminVo getIsAdmin(UserVo user) {
	    AdminVo admin = new AdminVo();
	    String staffId = this.getStaffIdByUserId(user.getUserId());
	    QueryWrapper<CompanyAdmin> comAdminQw = new QueryWrapper<>();
	    comAdminQw.lambda().eq(CompanyAdmin::getCompanyId, user.getCompanyId());
        CompanyAdmin comAdmin = this.companyAdminMapper.selectOne(comAdminQw);
        admin.setIsCompanyAdmin(comAdmin.getStaffId().equals(staffId));
        if(!admin.getIsCompanyAdmin()){
            QueryWrapper<SysDepartmentInfo> depAdminQw = new QueryWrapper<>();
            depAdminQw.lambda().select(SysDepartmentInfo::getId)
                    .eq(SysDepartmentInfo::getAdminAccount, staffId).
                    eq(SysDepartmentInfo::getStatus, StatusEnum.激活);
            SysDepartmentInfo dep = this.sysDepartmentInfoMapper.selectOne(depAdminQw);
            admin.setIsDepartAdmin(DataUtil.isNotEmpty(dep));
            return admin;
        }
        return admin;
    }



    private String getStaffIdByUserId(String userId) {
		return this.sysStaffInfoMapper.getStaffIdByUserId(userId);
	}


	/**
	 *
	 *  递归获得子级部门
	 * @author               PengQiang
	 * @description          DELL
	 * @date                 2021/1/7 17:54
	 * @param                depIds, deps, parentId] depIds:子级部门集合，deps:企业全部部门,parentId:部门的上级部门id
	 * @return               void
	 */
	private void getChilden(List<String> depIds, List<SysDepartmentInfoVo> deps, String parentId){
        //获取子级部门集合
        List<SysDepartmentInfoVo> childs = deps.stream().filter(d -> parentId.equals(d.getParentId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(childs)){
            return;
        }
        //循环用id再次查找子级部门
        for (SysDepartmentInfoVo dep : childs){
            depIds.add(dep.getId());
            getChilden(depIds, deps, dep.getId());
        }
    }



}
