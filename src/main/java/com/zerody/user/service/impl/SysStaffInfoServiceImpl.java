package com.zerody.user.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.zerody.user.domain.SysLoginInfo;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.domain.UnionLeaderDepart;
import com.zerody.user.domain.UnionRoleStaff;
import com.zerody.user.domain.UnionStaffDepart;
import com.zerody.user.domain.UnionStaffPosition;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.enums.StaffStatusEnum;
import com.zerody.user.mapper.SysLoginInfoMapper;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.mapper.UnionLeaderDepartMapper;
import com.zerody.user.mapper.UnionRoleStaffMapper;
import com.zerody.user.mapper.UnionStaffDepartMapper;
import com.zerody.user.mapper.UnionStaffPositionMapper;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.util.IdCardUtil;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoServiceImpl
 * @DateTime 2020/12/17_17:31
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysStaffInfoServiceImpl extends BaseService<SysStaffInfoMapper, SysStaffInfo> implements SysStaffInfoService {
    public static final String[] STAFF_EXCEL_TITTLE = new String[] {"姓名","手机号码","部门","岗位","状态","性别","籍贯","民族","婚姻","出生年月日","身份证号码","户籍地址","居住地址","联系方式","电子邮箱","最高学历","毕业院校","所学专业"};

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
    private UnionLeaderDepartMapper unionLeaderDepartMapper;


    private static final String INIT_PWD = "123456";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStaff(SetSysUserInfoDto setSysUserInfoDto) {
        SysUserInfo sysUserInfo=new SysUserInfo();
        DataUtil.getKeyAndValue(sysUserInfo,setSysUserInfoDto);
        log.info("B端添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        //参数校验
         CheckUser.checkParam(sysUserInfo);
        //查看手机号或登录名是否被占用
        Boolean flag = sysUserInfoMapper.selectUserByPhone(sysUserInfo.getPhoneNumber());
        if(flag){
            throw new DefaultException("该手机号已存在！");
        }
        //效验通过保存用户信息
        sysUserInfo.setRegisterTime(new Date());
        log.info("B端添加用户入库参数--{}",JSON.toJSONString(sysUserInfo));
        sysUserInfo.setCreateTime(new Date());
        sysUserInfo.setCreateUser(UserUtils.getUserName());
        sysUserInfo.setCreateId(UserUtils.getUserId());
        sysUserInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        sysUserInfoMapper.insert(sysUserInfo);
        //用户信息保存添加登录信息
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SysLoginInfo logInfo = new SysLoginInfo();
        logInfo.setUserId(sysUserInfo.getId());
        logInfo.setMobileNumber(sysUserInfo.getPhoneNumber());
        logInfo.setNickname(sysUserInfo.getNickname());
        logInfo.setAvatar(sysUserInfo.getAvatar());
        //初始化密码加密
        logInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5( INIT_PWD )));
        logInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        log.info("B端添加用户后生成登录账户入库参数--{}",JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);
        //保存员工信息
        SysStaffInfo staff = new SysStaffInfo();
        staff.setCompId(setSysUserInfoDto.getCompanyId());
        staff.setStatus(setSysUserInfoDto.getStatus());
        staff.setUserId(sysUserInfo.getId());
        log.info("B端添加员工入库参数--{}",JSON.toJSONString(staff));
        this.saveOrUpdate(staff);
        //角色
        UnionRoleStaff rs = new UnionRoleStaff();
        rs.setStaffId(staff.getId());
        rs.setRoleId(setSysUserInfoDto.getRoleId());
        unionRoleStaffMapper.insert(rs);
        rs = new UnionRoleStaff();
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
        log.info("B端添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        //参数校验
        CheckUser.checkParam(sysUserInfo);
        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(sysUserInfo);
        if(users != null && users.size() > 0){
            throw new DefaultException("手机号或用户名被占用");
        }
        //效验通过保存用户信息
        sysUserInfo.setRegisterTime(new Date());
        log.info("B端添加用户入库参数--{}",JSON.toJSONString(sysUserInfo));
        sysUserInfo.setUpdateTime(new Date());
        sysUserInfo.setUpdateUser(UserUtils.getUserName());
        sysUserInfo.setUpdateId(UserUtils.getUserId());
        //如果员工状态为离职状态 就把用户状态转换为 禁用状态
        if (StaffStatusEnum.DIMISSION.getCode().equals(setSysUserInfoDto.getStatus())){
            sysUserInfo.setStatus(DataRecordStatusEnum.INVALID.getCode());
        }
        sysUserInfoMapper.updateById(sysUserInfo);
        QueryWrapper<SysLoginInfo> loginQW = new QueryWrapper<>();
        loginQW.lambda().eq(SysLoginInfo::getUserId, sysUserInfo.getId());
        SysLoginInfo logInfo = sysLoginInfoMapper.selectOne(loginQW);
        logInfo.setMobileNumber(sysUserInfo.getPhoneNumber());
        logInfo.setNickname(sysUserInfo.getNickname());
        logInfo.setAvatar(sysUserInfo.getAvatar());
        logInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        log.info("B端添加用户后生成登录账户入库参数--{}",JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);
        //保存员工信息

        //查询得到员工信息
        QueryWrapper<SysStaffInfo> staffQW = new QueryWrapper<>();
        staffQW.lambda().eq(SysStaffInfo::getUserId, sysUserInfo.getId());
        SysStaffInfo staff = this.getOne(staffQW);
        log.info("B端添加员工入库参数--{}",JSON.toJSONString(staff));
        this.saveOrUpdate(staff);
        //修改员工的时候删除该员工的全部角色
        QueryWrapper<UnionRoleStaff> ursQW = new QueryWrapper<>();
        ursQW.lambda().eq(UnionRoleStaff::getStaffId, staff.getId());
        unionRoleStaffMapper.delete(ursQW);
        UnionRoleStaff rs = new UnionRoleStaff();
        //给员工赋予角色
        rs.setStaffId(staff.getId());
        rs.setRoleId(setSysUserInfoDto.getRoleId());
        unionRoleStaffMapper.insert(rs);
        rs = new UnionRoleStaff();
        //删除该员工的部门
        QueryWrapper<UnionStaffPosition> uspQW = new QueryWrapper<>();
        uspQW.lambda().eq(UnionStaffPosition::getStaffId, staff.getId());
        unionStaffPositionMapper.delete(uspQW);
        //如果部门id不为空就给该员工添加部门
        if(StringUtils.isNotEmpty(setSysUserInfoDto.getPositionId())){
            UnionStaffPosition sp = new UnionStaffPosition();
            sp.setPositionId(setSysUserInfoDto.getPositionId());
            sp.setStaffId(staff.getId());
            unionStaffPositionMapper.insert(sp);
        }
        QueryWrapper<UnionStaffDepart> usdQW = new QueryWrapper<>();
        usdQW.lambda().eq(UnionStaffDepart::getStaffId, staff.getId());
        unionStaffDepartMapper.delete(usdQW);
        if(StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())){
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
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
        SysStaffInfo staff = new SysStaffInfo();
        staff.setId(staffId);
        staff.setStatus(StaffStatusEnum.DELETE.getCode());
        this.saveOrUpdate(staff);
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
        //删除部门领导
        QueryWrapper<UnionLeaderDepart> qw3 = new QueryWrapper<>();
        qw3.lambda().eq(UnionLeaderDepart::getStaffId,staffId);
        unionLeaderDepartMapper.delete(qw3);
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
        if (dataList.size() < 2) {
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
        //必填项
        Integer[] indexs = {0,1,4};
        List<String[]> errors = new ArrayList<>();
        //结果返回
        String[] errorData = new String[headers.length];
        //成功数量
        Integer successCount = 0;
        //数据总数
        Integer total = 0;
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //获得用户数
            total = dataList.size() - dataIndex;
            String[] row = null;
            //保存用户所用实体
            SysUserInfo userInfo ;
            StringBuilder errorInfo = new StringBuilder("");
            //循环行
            for (int rowIndex = dataIndex,rowlength = dataList.size() ; rowIndex < rowlength; rowIndex++) {
                //这一行的数据
                row = dataList.get(rowIndex);
                userInfo = new SysUserInfo();
                for (int lineIndex = 0,lineLength = row.length; lineIndex<lineLength; lineIndex++){
                    if(row[lineIndex] == null || "".equals(row[lineIndex])){
                        continue;
                    }
                    //去掉空格
                    row[lineIndex] = row[lineIndex].trim();
                }
                userInfo.setUserName(row[0]);
                userInfo.setPhoneNumber(row[1]);
                userInfo.setCertificateCard(row[2]);
                userInfo.setRegisterTime(new Date(row[3]));
                userInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
                //验证手机
                if(checkPhone(userInfo.getPhoneNumber())){
                    errorInfo.append("手机号码错误,");
                }
                //验证身份证
                if(!IdCardUtil.isValidatedAllIdcard(userInfo.getCertificateCard())){
                    errorInfo.append("身份证错误,");
                }
                sysUserInfoMapper.selectUserByPhoneOrLogName(userInfo);
                if(errorInfo.length() > 0){
                    //如果有错误信息就下一次循环 不保存  并记录错误信息
                    System.arraycopy(row,0,errorData,0,row.length);
                    errorData[errorData.length - 1] = errorInfo.toString();
                    errors.add(errorData);
                    //循环最后清空错误信息以方便记录下一次循环的错误信息
                    errorInfo = new StringBuilder("");
                    errorData = new String[errorData.length];
                    continue;
                }
                userInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
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
                staff.setStatus(StaffStatusEnum.BE_ON_THE_JOB.getCode());
                //保存到员工表
                this.saveOrUpdate(staff);

                SysLoginInfo loginInfo = new SysLoginInfo();
                loginInfo.setMobileNumber(userInfo.getPhoneNumber());
                loginInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5( INIT_PWD )));
                loginInfo.setUserId(userInfo.getId());
                sysLoginInfoService.addOrUpdateLogin(loginInfo);
            }
            result.put("total",total);
            if(errors.size()>0){

            }
        return result;
    }

    @Override
    public List<BosStaffInfoVo> getStaff(String companyId, String departId, String positionId) {
        return sysStaffInfoMapper.getStaff(companyId,departId,positionId);
    }

    @Override
    public IPage<BosStaffInfoVo> getAdmins(AdminsPageDto dto) {
        IPage<SysUserInfoVo> voIPage = new Page<>(dto.getCurrent(),dto.getPageSize());
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
		if(StringUtils.isEmpty(staffId))return null;
		return this.sysStaffInfoMapper.selectUserDeptInfoById(staffId);
	}


	@Override
	public List<String> getUserSubordinates(String userId) {
		String staffId=this.getStaffIdByUserId(userId);
		if(StringUtils.isEmpty(staffId))return null;
		QueryWrapper<UnionStaffDepart> qw = new QueryWrapper<>();
		qw.eq("staff_id", staffId);
		this.unionStaffDepartMapper.selectOne(qw);
		return null;
	}
    
	private String getStaffIdByUserId(String userId) {
		return this.sysStaffInfoMapper.getStaffIdByUserId(userId);
	}
    
}
