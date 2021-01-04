package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.user.check.CheckUser;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.mapper.UnionRoleStaffMapper;
import com.zerody.user.domain.SysLoginInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysLoginUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private UnionRoleStaffMapper unionRoleStaffMapper;

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
        DataResult  dataResult = CheckUser.checkParam(userInfo);
        //如果校验不通过提示前端
        if(!dataResult.isIsSuccess()){
            return dataResult;
        }
        //通过校验 把状态设为正常使用状态
        userInfo.setStatus(DataRecordStatusEnum.INVALID.getCode());
        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(userInfo);
        dataResult = new DataResult(ResultCodeEnum.RESULT_ERROR,true,"操作成功",null);
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
        logInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        log.info("B端添加用户后生成登录账户入库参数--{}",JSON.toJSONString(logInfo));
        sysLoginInfoService.addOrUpdateLogin(logInfo);
        return dataResult;
    }

    @Override
    public DataResult updateUser(SysUserInfo userInfo) {
        if(StringUtils.isEmpty(userInfo.getId())){
           return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "用户id不能为空", null);
        }
        this.saveOrUpdate(userInfo);
        return new DataResult();
    }

    @Override
    public DataResult deleteUserById(String userId) {
        if(StringUtils.isEmpty(userId)){
            return new DataResult(ResultCodeEnum.RESULT_ERROR, false, "用户id不能为空", null);
        }
        SysUserInfo userInfo = new SysUserInfo();
        userInfo.setStatus(DataRecordStatusEnum.DELETED.getCode());
        userInfo.setId(userId);
        this.saveOrUpdate(userInfo);
        return new DataResult();
    }

    @Override
    public DataResult deleteUserBatchByIds(List<String> ids) {
        for (String id : ids){
            SysUserInfo userInfo = new SysUserInfo();
            userInfo.setStatus(DataRecordStatusEnum.DELETED.getCode());
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
        return sysUserInfoMapper.selectUserInfo(userName);
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
    public String checkLoginUser(String userName) {
        return sysUserInfoMapper.selectByUserNameOrPhone(userName);
    }

}
