package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zerody.common.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.check.CheckUser;
import com.zerody.user.domain.*;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.mapper.*;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.CheckLoginVo;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysLoginUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author PengQiang
 * @ClassName SysUserInfoServiceImpl
 * @DateTime 2020/12/16_17:12
 * @Deacription TODO
 */
@Slf4j
@Service
public class SysUserInfoServiceImpl extends BaseService<SysUserInfoMapper, SysUserInfo> implements SysUserInfoService {

    @Autowired
    private UnionRoleStaffMapper unionRoleStaffMapper;
    @Autowired
    private CompanyAdminMapper companyAdminMapper;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @Autowired
    private SysDepartmentInfoMapper sysDepartmentInfoMapper;

    private static final String  INIT_PWD = "123456";//初始化密码

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
    @Transactional(rollbackFor = Exception.class)
    public DataResult addUser(SysUserInfo userInfo) {
        log.info("B端添加用户入参---{}", JSON.toJSONString(userInfo));
        //如果校验不通过提示前端
        CheckUser.checkParam(userInfo);
        //通过校验 把状态设为正常使用状态
        userInfo.setStatus(StatusEnum.activity.getValue());
        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(userInfo);
        DataResult dataResult = new DataResult(ResultCodeEnum.RESULT_ERROR,true,"操作成功",null);
        if(users != null && users.size() > 0){
            dataResult.setMessage("该手机号或用户名称被占用");
            dataResult.setIsSuccess(!dataResult.isIsSuccess());
            return dataResult;
        }
        //效验通过保存用户信息
        userInfo.setRegisterTime(new Date());
        log.info("B端添加用户入库参数--{}",JSON.toJSONString(userInfo));
        this.saveOrUpdate(userInfo);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //用户信息保存添加登录信息
        SysLoginInfo logInfo = new SysLoginInfo();
        logInfo.setUserId(userInfo.getId());
        logInfo.setMobileNumber(userInfo.getPhoneNumber());
        logInfo.setUserPwd(passwordEncoder.encode(MD5Utils.MD5( INIT_PWD )));//初始化密码登录并加密
        logInfo.setNickname(userInfo.getNickname());
        logInfo.setAvatar(userInfo.getAvatar());
        logInfo.setStatus(StatusEnum.activity.getValue());
        log.info("B端添加用户后生成登录账户入库参数--{}",JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);
        return dataResult;
    }

    @Override
    public DataResult updateUser(SysUserInfo userInfo) {
        if(StringUtils.isEmpty(userInfo.getId())){
           return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "用户id不能为空", null);
        }
        this.updateById(userInfo);
        return new DataResult();
    }

    @Override
    public DataResult deleteUserById(String userId) {
        if(StringUtils.isEmpty(userId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "用户id不能为空", null);
        }
        SysUserInfo userInfo = new SysUserInfo();
        userInfo.setStatus(StatusEnum.deleted.getValue());
        userInfo.setId(userId);
        this.saveOrUpdate(userInfo);
        return new DataResult();
    }

    @Override
    public DataResult deleteUserBatchByIds(List<String> ids) {
        for (String id : ids){
            SysUserInfo userInfo = new SysUserInfo();
            userInfo.setStatus(StatusEnum.deleted.getValue());
            userInfo.setId(id);
            this.saveOrUpdate(userInfo);
        }
        return new DataResult();
    }

    @Override
    public DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto) {
        return null;
    }



    @Override
    public DataResult deleteUserRole(String staffId, String roleId) {
        return null;
    }

    @Override
    public SysLoginUserInfoVo getUserInfo(String userName) {
        SysLoginUserInfoVo sysLoginUserInfoVo = sysUserInfoMapper.selectUserInfo(userName);
        if(DataUtil.isNotEmpty(sysLoginUserInfoVo)) {
            QueryWrapper<CompanyAdmin> qw = new QueryWrapper<>();
            qw.lambda().eq(CompanyAdmin::getStaffId, sysLoginUserInfoVo.getStaffId())
                    .eq(CompanyAdmin::getCompanyId, sysLoginUserInfoVo.getCompanyId())
                    .eq(CompanyAdmin::getDeleted, YesNo.NO);
            CompanyAdmin companyAdmin = companyAdminMapper.selectOne(qw);
            if (DataUtil.isEmpty(companyAdmin)) {
                sysLoginUserInfoVo.setIsAdmin(false);
            } else {
                sysLoginUserInfoVo.setIsAdmin(true);
            }
        }
        return sysLoginUserInfoVo;
    }

    @Override
    public SysUserInfo getUserById(String id) {
        return sysUserInfoMapper.selectById(id);
    }

    @Override
    public boolean checkPassword(String userPwd, String paramPwd) {
        return new BCryptPasswordEncoder().matches(paramPwd, userPwd) ? true : false;
    }

    @Override
    public CheckLoginVo checkLoginUser(String userName) {
        return sysUserInfoMapper.selectByUserNameOrPhone(userName);
    }

    @Override
    public LoginUserInfoVo getUserInfoById(String id) {
        return sysUserInfoMapper.selectLoginUserInfo(id);
    }

    @Override
    public Boolean checkRoleBind(String roleId) {
        QueryWrapper<UnionRoleStaff> qw=new QueryWrapper<>();
        qw.lambda().eq(UnionRoleStaff::getRoleId,roleId);
       return unionRoleStaffMapper.selectCount(qw)>0;
    }

    @Override
    public Boolean checkPlatformRoleBind(String roleId) {
        QueryWrapper<CompanyAdmin> qw=new QueryWrapper<>();
        qw.lambda().eq(CompanyAdmin::getRoleId,roleId);
        return companyAdminMapper.selectCount(qw)>0;
    }

    @Override
    public Boolean checkUserAdmin(String userId) {
        return sysUserInfoMapper.checkUserAdmin(userId);
    }

    @Override
    public Boolean checkBackAdminUser(String userId) {
        return sysUserInfoMapper.checkBackAdminUser(userId);
    }

    @Override
    public List<String> selectAllUserId() {
        return sysUserInfoMapper.selectAllUserId();
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          通过员工id查用户id
     * @date                 2021/1/25 14:47
     * @param                [staffId]
     * @return               java.lang.String
     */
    @Override
    public String getUserIdByCompIdOrStaffId(String staffId) {
        SysStaffInfo staffInfo = this.sysStaffInfoMapper.selectById(staffId);
        if (DataUtil.isEmpty(staffInfo)){
            throw new DefaultException("获取异常！");
        }
        return staffInfo.getUserId();
    }

    @Override
    public void updatePerformancePassword(com.zerody.user.api.vo.AdminUserInfo userInfo) {
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().eq(SysUserInfo::getPhoneNumber, userInfo.getPhoneNumber());
        userUw.lambda().set(SysUserInfo::getPerformanceShowPassword, userInfo.getUserPwd());
        this.update(userUw);
    }


}
