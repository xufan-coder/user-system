package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.FileUtil;
import com.zerody.user.check.CheckUser;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.enums.StaffStatusEnum;
import com.zerody.user.mapper.*;
import com.zerody.user.domain.*;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.util.IdCardUtil;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoServiceImpl
 * @DateTime 2020/12/17_17:31
 * @Deacription TODO
 */
@Service
@Slf4j
public class SysStaffInfoServiceImpl extends BaseService<SysStaffInfoMapper, SysStaffInfo> implements SysStaffInfoService {


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


    private static final String INIT_PWD = "123456";

    @Override
    public void addStaff(SysStaffInfo staff) {
        this.saveOrUpdate(staff);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStaff(SetSysUserInfoDto setSysUserInfoDto) {
        SysUserInfo sysUserInfo = setSysUserInfoDto.getSysUserInfo();
        log.info("B端添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        //参数校验
         CheckUser.checkParam(sysUserInfo);
        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(sysUserInfo);
        if(users != null && users.size() > 0){
            throw new DefaultException("该手机号或用户名称被占用");
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
        logInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5( INIT_PWD )));//初始化密码登录并加密
        logInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        log.info("B端添加用户后生成登录账户入库参数--{}",JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);
        //保存员工信息
        SysStaffInfo staff = new SysStaffInfo();
        staff.setStatus(setSysUserInfoDto.getStatus());
        staff.setUserId(setSysUserInfoDto.getSysUserInfo().getId());
        log.info("B端添加员工入库参数--{}",JSON.toJSONString(staff));
        this.saveOrUpdate(staff);
        if(setSysUserInfoDto.getRoleIds() != null && setSysUserInfoDto.getRoleIds().size() != 0 ){
            UnionRoleStaff rs = new UnionRoleStaff();
            for (String id : setSysUserInfoDto.getRoleIds()){
                rs.setStaffId(staff.getId());
                rs.setRoleId(id);
                unionRoleStaffMapper.insert(rs);
                rs = new UnionRoleStaff();
            }
        }
        if(StringUtils.isNotEmpty(setSysUserInfoDto.getPositionId())){
            UnionStaffPosition sp = new UnionStaffPosition();
            sp.setPositionId(setSysUserInfoDto.getPositionId());
            sp.setStaffId(staff.getId());
            unionStaffPositionMapper.insert(sp);
        }

        if(StringUtils.isNotEmpty(setSysUserInfoDto.getDepartId())){
            UnionStaffDepart sd = new UnionStaffDepart();
            sd.setDepartmentId(setSysUserInfoDto.getDepartId());
            sd.setStaffId(staff.getId());
            unionStaffDepartMapper.insert(sd);
        }
    }



    @Override
    public void deleteStaffRole(String  staffId, String roleId) {
        QueryWrapper<UnionRoleStaff> rsQW = new QueryWrapper<>();
        rsQW.lambda().eq(UnionRoleStaff::getRoleId,roleId);
        rsQW.lambda().eq(UnionRoleStaff::getStaffId,staffId);
        unionRoleStaffMapper.delete(rsQW);
    }

    @Override
    public void staffAddRole(String staffId, String roleId) {
        UnionRoleStaff roleStaff = new UnionRoleStaff();
        roleStaff.setId(UUIDutils.getUUID32());
        roleStaff.setStaffId(staffId);
        roleStaff.setRoleId(roleId);
        unionRoleStaffMapper.insert(roleStaff);
    }

    @Override
    public IPage<SysUserInfoVo> selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto) {
        IPage<SysUserInfoVo> infoVoIPage = new Page<>(sysStaffInfoPageDto.getCurrent(),sysStaffInfoPageDto.getPageSize());
        return sysStaffInfoMapper.selectPageStaffByRoleId(sysStaffInfoPageDto,infoVoIPage);
    }

    @Override
    public IPage<SysUserInfoVo> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto) {
        IPage<SysUserInfoVo> infoVoIPage = new Page<>(sysStaffInfoPageDto.getCurrent(),sysStaffInfoPageDto.getPageSize());
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
        SysUserInfo sysUserInfo = setSysUserInfoDto.getSysUserInfo();
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
        if(setSysUserInfoDto.getRoleIds() != null && setSysUserInfoDto.getRoleIds().size() != 0 ){
            UnionRoleStaff rs = new UnionRoleStaff();
            //给员工赋予角色
            for (String id : setSysUserInfoDto.getRoleIds()){
                rs.setStaffId(staff.getId());
                rs.setRoleId(id);
                unionRoleStaffMapper.insert(rs);
                rs = new UnionRoleStaff();
            }
        }
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
    public void batchDeleteStaff(List<String> staffIds) {
        SysStaffInfo staff = new SysStaffInfo();
        for (String id : staffIds){
            if(StringUtils.isEmpty(id)){
                continue;
            }
            staff.setStatus(DataRecordStatusEnum.DELETED.getCode());
            staff.setId(id);
            //逻辑删除员工
            this.saveOrUpdate(staff);
        }
    }

    @Override
    public void deleteStaffById(String staffId) {
        if(StringUtils.isEmpty(staffId)){
            throw new DefaultException( "员工id为空");
        }
        SysStaffInfo staff = new SysStaffInfo();
        staff.setId(staffId);
        staff.setStatus(StaffStatusEnum.DELETE.getCode());
        this.saveOrUpdate(staff);
    }

    @Override
    public List<String> getStaffRoles(String userId, String companyId) {
        List<String> roleIds=sysStaffInfoMapper.selectStaffRoles(userId,companyId);
        return roleIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchImportStaff(MultipartFile file) {
        //excel标题 用于导入失败提醒生成新的excel
        String[] titles = {"姓名","手机号","身份证号","入职日期（格式：yyyy-MM-dd）"};
        //必填项
        Integer[] indexs = {0,1,2,3};
        List<String[]> errors = new ArrayList<>();
        //结果返回
        Map<String, Object> result = new HashMap<>();
        String[] errorData = new String[titles.length];
        Integer headSize = 1; //从第几行开始读取数据
        Integer successCount = 0; //成功数量
        Integer total = 0; //数据总数
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        try {
            List<String[]> dataList = FileUtil.fileImport(file);
            //判断当前导入excel是否有数据
            if(dataList.size()<=headSize){
                  throw new DefaultException("文件没有数据");
            }
            total = dataList.size() - headSize; //获得用户数
            result.put("total",total);
            String[] row = null;
            SysUserInfo userInfo ; //保存用户所用实体
            StringBuilder errorInfo = new StringBuilder("");
            //循环行
            for (int rowIndex = headSize,rowlength = dataList.size() ; rowIndex < rowlength; rowIndex++) {
                //这一行的数据
                row = dataList.get(rowIndex);
                userInfo = new SysUserInfo();
                for (int lineIndex = 0,lineLength = row.length; lineIndex<lineLength; lineIndex++){
                    if(row[lineIndex] == null || "".equals(row[lineIndex])){
                        continue;
                    }
                    row[lineIndex] = row[lineIndex].trim();//去掉空格
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
                if(errorInfo.length() > 0){ //如果有错误信息就下一次循环 不保存  并记录错误信息
                    System.arraycopy(row,0,errorData,0,row.length);
                    errorData[errorData.length - 1] = errorInfo.toString();
                    errors.add(errorData);
                    errorInfo = new StringBuilder("");//循环最后清空错误信息以方便记录下一次循环的错误信息
                    errorData = new String[errorData.length];
                    continue;
                }
                userInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
                userInfo.setCreateId(UserUtils.getUserId());
                userInfo.setCreateUser(UserUtils.getUserName());
                userInfo.setCreateTime(new Date());
                sysUserInfoMapper.insert(userInfo); //因为员工表跟登录表都要用到用户所有先保存用户
                SysStaffInfo staff = new SysStaffInfo();
//                staff.setCompId(UserUtils.get); //获取当前登录用户的当前公司id
                staff.setUserName(userInfo.getUserName());
                staff.setUserId(userInfo.getId());
//                staff.setStatus(StaffStatusEnum.getCodeByDesc());
                this.saveOrUpdate(staff); //保存到员工表

                SysLoginInfo loginInfo = new SysLoginInfo();
                loginInfo.setMobileNumber(userInfo.getPhoneNumber());
                loginInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5( INIT_PWD )));
                loginInfo.setUserId(userInfo.getId());
                sysLoginInfoService.addOrUpdateLogin(loginInfo);
            }

            if(errors.size()>0){

            }
        } catch (IOException e) {
            log.error("导入失败---:{}",e);
            e.printStackTrace();
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
}
