package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.user.check.CheckUser;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.enums.StaffStatusEnum;
import com.zerody.user.mapper.*;
import com.zerody.user.pojo.*;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    public DataResult addStaff(SysStaffInfo staff) {
        this.saveOrUpdate(staff);
        return new DataResult();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataResult addStaff(SetSysUserInfoDto setSysUserInfoDto) {
        SysUserInfo sysUserInfo = setSysUserInfoDto.getSysUserInfo();
        log.info("B端添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        DataResult  dataResult = CheckUser.checkParam(sysUserInfo);
        //如果校验不通过提示前端
        if(!dataResult.isIsSuccess()){
            return dataResult;
        };
        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(sysUserInfo);
        dataResult = new DataResult(ResultCodeEnum.RESULT_ERROR,true,"操作成功",null);
        if(users != null && users.size() > 0){
            dataResult.setMessage("该手机号或用户名称被占用");
            dataResult.setIsSuccess(!dataResult.isIsSuccess());
            return dataResult;
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
        return new DataResult();
    }



    @Override
    public DataResult deleteStaffRole(String  staffId, String roleId) {
        QueryWrapper<UnionRoleStaff> rsQW = new QueryWrapper<>();
        rsQW.lambda().eq(UnionRoleStaff::getRoleId,roleId);
        rsQW.lambda().eq(UnionRoleStaff::getStaffId,staffId);
        unionRoleStaffMapper.delete(rsQW);
        return new DataResult();
    }

    @Override
    public DataResult staffAddRole(String staffId, String roleId) {
        UnionRoleStaff roleStaff = new UnionRoleStaff();
        roleStaff.setId(UUIDutils.getUUID32());
        roleStaff.setStaffId(staffId);
        roleStaff.setRoleId(roleId);
        unionRoleStaffMapper.insert(roleStaff);
        return new DataResult();
    }

    @Override
    public DataResult selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto) {
        //设置当前页与当前页所显示条数
        Integer pageNum = sysStaffInfoPageDto.getPageNum() == 0 ? 1 : sysStaffInfoPageDto.getPageNum();
        Integer pageSize = sysStaffInfoPageDto.getPageSize() == 0 ? 1 : sysStaffInfoPageDto.getPageSize();
        IPage<SysUserInfoVo> infoVoIPage = new Page<>(pageNum,pageSize);
        infoVoIPage = sysStaffInfoMapper.selectPageStaffByRoleId(sysStaffInfoPageDto,infoVoIPage);
        return new DataResult(infoVoIPage);
    }

    @Override
    public DataResult getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto) {
        //设置当前页与当前页所显示条数
        Integer pageNum = sysStaffInfoPageDto.getPageNum() == 0 ? 1 : sysStaffInfoPageDto.getPageNum();
        Integer pageSize = sysStaffInfoPageDto.getPageSize() == 0 ? 1 : sysStaffInfoPageDto.getPageSize();
        IPage<SysUserInfoVo> infoVoIPage = new Page<>(pageNum,pageSize);
//        //当没有传企业id过来的时候就默认查用户当前登录企业的员工
//        if(StringUtils.isEmpty(sysStaffInfoPageDto.getCompanyId())){
//            sysStaffInfoPageDto.setCompanyId();
//        }
        infoVoIPage = sysStaffInfoMapper.getPageAllStaff(sysStaffInfoPageDto,infoVoIPage);
        return new DataResult(infoVoIPage);
    }

    @Override
    public DataResult updateStaffStatus(String staffId, Integer status) {
        if(StringUtils.isEmpty(staffId)){
            return  new DataResult(ResultCodeEnum.RESULT_ERROR, false, "员工id不能为空", null);
        }
        if (status == null){
            return  new DataResult(ResultCodeEnum.RESULT_ERROR, false, "状态不能为空", null);
        }
        SysStaffInfo staff = new SysStaffInfo();
        staff.setId(staffId);
        staff.setStatus(status);
        this.saveOrUpdate(staff);
        return new DataResult();
    }

    @Override
    public DataResult getStaffInfoByOpenId(String openId) {
        if(StringUtils.isEmpty(openId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "openId不能为空", null);
        }
        SysUserInfoVo userInfo = sysStaffInfoMapper.getStaffInfoByOpenId(openId);
        userInfo.setPhoneNumber(userInfo.getPhoneNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        return new DataResult(userInfo);
    }

    @Override
    public DataResult updateStaff(SetSysUserInfoDto setSysUserInfoDto) {
        SysUserInfo sysUserInfo = setSysUserInfoDto.getSysUserInfo();
        log.info("B端添加员工入参---{}", JSON.toJSONString(sysUserInfo));
        DataResult  dataResult = CheckUser.checkParam(sysUserInfo);
        //如果校验不通过提示前端
        if(!dataResult.isIsSuccess()){
            return dataResult;
        }

        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(sysUserInfo);
        dataResult = new DataResult(ResultCodeEnum.RESULT_ERROR,true,"操作成功",null);
        if(users != null && users.size() > 0){
            dataResult.setMessage("该手机号或用户名称被占用");
            dataResult.setIsSuccess(!dataResult.isIsSuccess());
            return dataResult;
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
        return new DataResult();
    }

    @Override
    public DataResult selectStaffById(String id) {
        if(StringUtils.isEmpty(id)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "id不能为空", null);
        }
        SysUserInfoVo staff = sysStaffInfoMapper.selectStaffById(id);
        return new DataResult(staff);
    }

    @Override
    public DataResult batchDeleteStaff(List<String> staffIds) {
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
        return new DataResult();
    }

    @Override
    public DataResult deleteStaffById(String staffId) {
        if(StringUtils.isEmpty(staffId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "员工id为空", null);
        }
        SysStaffInfo staff = new SysStaffInfo();
        staff.setId(staffId);
        staff.setStatus(StaffStatusEnum.DELETE.getCode());
        this.saveOrUpdate(staff);
        return new DataResult();
    }

    @Override
    public List<String> getStaffRoles(String userId, String companyId) {
        List<String> roleIds=sysStaffInfoMapper.selectStaffRoles(userId,companyId);
        return roleIds;
    }
}
