package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.user.check.CheckUser;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.mapper.*;
import com.zerody.user.pojo.*;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.service.base.BaseStringService;
import com.zerody.user.vo.SysStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UnionStaffDepartMapper unionStaffDepartMapper;

    @Autowired
    private UnionStaffPositionMapper unionStaffPositionMapper;

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
        }
        //通过校验 把状态设为正常使用状态
        sysUserInfo.setStatus(DataRecordStatusEnum.INVALID.getCode());
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
        sysUserInfo.setStatus(DataRecordStatusEnum.INVALID.getCode());
        sysUserInfoMapper.insert(sysUserInfo);
        //用户信息保存添加登录信息
        SysLoginInfo logInfo = new SysLoginInfo();
        logInfo.setUserId(sysUserInfo.getId());
        logInfo.setMobileNumber(sysUserInfo.getPhoneNumber());
        logInfo.setNickname(sysUserInfo.getNickname());
        logInfo.setAvatar(sysUserInfo.getAvatar());
        logInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        log.info("B端添加用户后生成登录账户入库参数--{}",JSON.toJSONString(logInfo));
        sysLoginInfoService.addLogin(logInfo);
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
        infoVoIPage = sysStaffInfoMapper.getPageAllStaff(sysStaffInfoPageDto,infoVoIPage);
        return new DataResult(infoVoIPage);
    }
}
