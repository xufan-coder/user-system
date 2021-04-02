package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zerody.common.bean.DataResult;
import com.zerody.common.constant.UserTypeInfo;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.check.CheckUser;
import com.zerody.user.domain.*;
import com.zerody.user.dto.SetUpdateAvatarDto;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.mapper.*;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.*;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    private static final String  INIT_PWD = "123456";//初始化密码

    private static final String DEFAULT_AVATAR = "https://tangsanzangkeji.oss-cn-beijing.aliyuncs.com/scrm/38bb2ab88a82ed2e84d187bfc065c131/picture:67730b2a883141c9a51b3da812087c26";

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
        SysUserInfo user =  sysUserInfoMapper.selectById(id);
        String staffId = sysStaffInfoMapper.getStaffIdByUserId(id);
        QueryWrapper<UnionRoleStaff> urQw = new QueryWrapper<>();
        urQw.lambda().eq(UnionRoleStaff::getStaffId, staffId);
        UnionRoleStaff role = this.unionRoleStaffMapper.selectOne(urQw);
        if (!DataUtil.isEmpty(role)){
            user.setRoleName(role.getRoleName());
        }
        return user;
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
    public List<Map<String, String>> selectAllUserId() {
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
        userUw.lambda().eq(SysUserInfo::getId, userInfo.getId());
        userUw.lambda().set(SysUserInfo::getPerformanceShowPassword, userInfo.getUserPwd());
        this.update(userUw);
    }

    @Override
    public String getShowPerformancePassword(String id) {
        SysUserInfo user = this.getById(id);
        if (DataUtil.isEmpty(user)){
            throw new DefaultException("用户不存在");
        }
        return user.getPerformanceShowPassword();
    }

    @Override
    public void updateUserIsSignOrder(String userId) {
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().eq(SysUserInfo::getId, userId);
        userUw.lambda().set(SysUserInfo::getIsSignOrder, YesNo.YES);
        this.update(userUw);
    }

    @Override
    public void updateUserAvatar(SetUpdateAvatarDto param) {
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().set(SysUserInfo::getAvatar, param.getAvatar());
        userUw.lambda().eq(SysUserInfo::getId, param.getUserId());
        userUw.lambda().set(SysUserInfo::getAvatarUpdateTime, new Date());
        this.update(userUw);
    }

    @Override
    public void getAvatarImageByUserId(String userId, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<SysUserInfo> userQw = new QueryWrapper<>();
        userQw.lambda().select(SysUserInfo::getAvatar).eq(SysUserInfo::getId, userId);
        SysUserInfo user = this.getOne(userQw);
        //没有头像时使用默认图片
        if (DataUtil.isEmpty(user)) {
            user = new SysUserInfo();
        }
        if (StringUtils.isEmpty(user.getAvatar())) {
            user.setAvatar(DEFAULT_AVATAR);
        }
        try {
            URL url = new URL(user.getAvatar());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte data[] = readInputStream(inStream);
            inStream.read(data);  //读数据
            inStream.close();
            response.addHeader("Content-Disposition", "attachment;filename="+new String( "用户头像".getBytes("gb2312"), "ISO8859-1" )+".jpg");
            OutputStream os = response.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserTypeInfoVo getUserTypeInfo(UserVo user) {
        UserTypeInfoVo userTypeInfoVo = new UserTypeInfoVo();
        AdminVo admin = this.sysStaffInfoService.getIsAdmin(user);
        if(admin.getIsCompanyAdmin()) {
            userTypeInfoVo.setUserType(UserTypeInfo.COMPANY_ADMIN);
            return userTypeInfoVo;
        } else if (admin.getIsDepartAdmin()){
            QueryWrapper<SysDepartmentInfo> departQw = new QueryWrapper<>();
            departQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.activity.getValue());
            departQw.lambda().eq(SysDepartmentInfo::getParentId, user.getDeptId());
            Integer count = this.sysDepartmentInfoMapper.selectCount(departQw);
            if (count > 0) {
                userTypeInfoVo.setUserType(UserTypeInfo.DEPUTY_GENERAL_MANAGERv);
            } else {
                userTypeInfoVo.setUserType(UserTypeInfo.LONG_TEAM);
            }
        } else {
            userTypeInfoVo.setUserType(UserTypeInfo.PARTNER);
        }
        UserStructureVo departVo =  this.sysDepartmentInfoMapper.getDepartNameById(user.getDeptId());
        userTypeInfoVo.setDepartName(departVo.getDepartName());
        return userTypeInfoVo;
    }

    @Override
    public List<String> doUserIsDeleted() {
        List<String> userIds = this.sysUserInfoMapper.getUserIdsByIsDeleted();
        if (CollectionUtils.isEmpty(userIds)) {
            return null;
        }
        this.sysUserInfoMapper.updateUserStatusAndIsDeleted(userIds);
        return userIds;
    }

    public  byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
//        inStream.close();
        return outStream.toByteArray();
    }

}
