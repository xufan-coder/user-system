package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.adviser.api.dto.AdviserUserStatusUpdateDto;
import com.zerody.common.bean.DataResult;
import com.zerody.common.constant.MQ;
import com.zerody.common.constant.UserTypeInfo;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.enums.UserTypeEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.mq.RabbitMqService;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.*;
import com.zerody.user.api.vo.DepartInfoVo;
import com.zerody.user.check.CheckUser;
import com.zerody.user.domain.*;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.SysLoginInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.domain.UnionRoleStaff;
import com.zerody.user.domain.base.BaseModel;
import com.zerody.user.dto.*;
import com.zerody.user.feign.AdviserFeignService;
import com.zerody.user.mapper.*;
import com.zerody.user.service.CeoUserInfoService;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.util.UserTypeUtil;
import com.zerody.user.vo.*;
import com.zerody.user.vo.SysLoginUserInfoVo;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private CardUserUnionCrmUserMapper cardUserUnionCrmUserMapper;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private CeoUserInfoService ceoUserInfoService;

    @Autowired
    private AdviserFeignService adviserFeignService;

    @Autowired
    private RabbitMqService mqService;

    private static final String  INIT_PWD = "123456";//初始化密码

    private static final String DEFAULT_AVATAR = "https://tangsanzangkeji.oss-cn-beijing.aliyuncs.com/scrm/38bb2ab88a82ed2e84d187bfc065c131/picture:67730b2a883141c9a51b3da812087c26";

    @Autowired
    private CheckUtil checkUtil;

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
        CheckUser.checkParam(userInfo, null);
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
        logInfo.setCreateId(UserUtils.getUser().getUserId());
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
        log.info("删除用户  ——> 入参：userId-{}, 操作者信息：{}", userId, JSON.toJSONString(UserUtils.getUser()));
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
        log.info("批量删除用户  ——> 入参：{}, 操作者信息：{}", JSON.toJSONString(ids), JSON.toJSONString(UserUtils.getUser()));
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
        CeoUserInfo ceo = this.ceoUserInfoService.getById(id);
        if (DataUtil.isNotEmpty(ceo)) {
            SysUserInfo user = new SysUserInfo();
            user.setPhoneNumber(ceo.getPhoneNumber());
            user.setUserName(ceo.getUserName());
            user.setAvatar(ceo.getAvatar());
            user.setEmail(ceo.getEmail());
            user.setRoleName("总经办");
            user.setId(ceo.getId());
            return user;
        }
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
        CeoUserInfo ceo = this.ceoUserInfoService.getById(id);
        if (DataUtil.isNotEmpty(ceo)) {
            LoginUserInfoVo user = new LoginUserInfoVo();
            user.setPhoneNumber(ceo.getPhoneNumber());
            user.setUserName(ceo.getUserName());
            user.setAvatar(ceo.getAvatar());
            user.setCompanyName(ceo.getCompany());
            user.setPositionName(ceo.getPosition());
            user.setEmail(ceo.getEmail());
            return user;
        }
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

        //检查是否是总裁用户，如果是总裁则修改总裁表头像
        QueryWrapper<CeoUserInfo> ceoQw =new QueryWrapper<>();
        ceoQw.lambda().eq(BaseModel::getId,param.getUserId());
        CeoUserInfo one = ceoUserInfoService.getOne(ceoQw);
        if(DataUtil.isNotEmpty(one)){
            one.setAvatar(param.getAvatar());
            ceoUserInfoService.updateById(one);
        }else {
            UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
            userUw.lambda().set(SysUserInfo::getAvatar, param.getAvatar());
            userUw.lambda().eq(SysUserInfo::getId, param.getUserId());
            userUw.lambda().set(SysUserInfo::getAvatarUpdateTime, new Date());
            userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
            this.update(userUw);
        }
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
            // 添加缩放参数 按长边100px等比缩放
            URL url = new URL(user.getAvatar()+"?x-oss-process=image/resize,l_100");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte data[] = readInputStream(inStream);
            inStream.read(data);  //读数据
            inStream.close();
            response.addHeader("Content-Disposition", "inline;filename="+new String( "用户头像".getBytes("gb2312"), "ISO8859-1" )+".jpg");
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
        if (user.getUserType().equals(UserTypeEnum.CRM_CEO.getValue())) {
            //  总裁类型
            userTypeInfoVo.setUserType(UserTypeInfo.CRM_CEO);
            return userTypeInfoVo;
        }
        AdminVo admin = new AdminVo();
        if (StringUtils.isNotEmpty(user.getUserId())) {
            admin = this.sysStaffInfoService.getIsAdmin(user);
        } else {
            admin.setIsCompanyAdmin(StringUtils.isNotEmpty(user.getCompanyId()));
            admin.setIsDepartAdmin(StringUtils.isNotEmpty(user.getDeptId()));
        }
        if(admin.getIsCompanyAdmin()) {
            //  企业管理员 (总经理)
            userTypeInfoVo.setUserType(UserTypeInfo.COMPANY_ADMIN);
            return userTypeInfoVo;
        } else if (admin.getIsDepartAdmin()){
            QueryWrapper<SysDepartmentInfo> departQw = new QueryWrapper<>();
            departQw.lambda().eq(SysDepartmentInfo::getStatus, StatusEnum.activity.getValue());
            departQw.lambda().eq(SysDepartmentInfo::getParentId, user.getDeptId());
            Integer count = this.sysDepartmentInfoMapper.selectCount(departQw);
            if (count > 0) {
                //  副总类型(是部门负责人且有下级部门)
                userTypeInfoVo.setUserType(UserTypeInfo.DEPUTY_GENERAL_MANAGERv);
            } else {
                //   团队长类型(是部门负责人 没有下级部门)
                userTypeInfoVo.setUserType(UserTypeInfo.LONG_TEAM);
            }
        } else {
            //  伙伴类型(不是任何的负责人)
            userTypeInfoVo.setUserType(UserTypeInfo.PARTNER);
        }
        UserStructureVo departVo =  this.sysDepartmentInfoMapper.getDepartNameById(user.getDeptId());
        if (DataUtil.isEmpty(departVo)) {
            return userTypeInfoVo;
        }
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

    @Override
    public void updateRedundancyUserName() {
        List<StaffInfoVo> staffInfos = this.sysUserInfoMapper.getUserMobilyNameInfo();
        //  如果没有修改的就不做后面的操作
        if (CollectionUtils.isEmpty(staffInfos)) {
            return;
        }
        this.sysUserInfoMapper.updateUserNameModilyState(staffInfos);
        //  mq发送消息通知修改用户名称
        staffInfos.stream().forEach(staff -> {
            mqService.send(staff, MQ.QUEUE_USER_NAME_CUSTOMER);
        });
        log.info("发送用户修改名称通知:{}", JSON.toJSONString(staffInfos));
    }

    @Override
    public StaffInfoVo getUserByCardUserId(String id) {

        QueryWrapper<CardUserUnionUser> qw =new QueryWrapper<>();
        qw.lambda().eq(CardUserUnionUser::getCardId,id);
        List<CardUserUnionUser> cardUserUnionUsers = cardUserUnionCrmUserMapper.selectList(qw);
        if(DataUtil.isNotEmpty(cardUserUnionUsers)){
            String userId = cardUserUnionUsers.get(0).getUserId();
            //检查是否是boss账户
            QueryWrapper<CeoUserInfo> ceoQw =new QueryWrapper<>();
            ceoQw.lambda().eq(BaseModel::getId,userId);
            CeoUserInfo one = ceoUserInfoService.getOne(ceoQw);
            if(DataUtil.isNotEmpty(one)){
                StaffInfoVo staffInfoVo=new StaffInfoVo();
                staffInfoVo.setUserId(one.getId());
                staffInfoVo.setMobile(one.getPhoneNumber());
                staffInfoVo.setUserName(one.getUserName());
                staffInfoVo.setStaffAvatar(one.getAvatar());
                staffInfoVo.setUserType(UserTypeEnum.CRM_CEO.getValue());
                return staffInfoVo;
            }else {
                return sysStaffInfoService.getStaffInfo(userId);
            }
        }
        return null;
    }

    @Override
    public void doUserEditInfo() {
        List<Map<String, String>> userMap = this.sysUserInfoMapper.getDepartmentEditInfo();
        if (CollectionUtils.isEmpty(userMap)) {
            return;
        }
        this.sysUserInfoMapper.updateDepartEditInfo(userMap);
        this.mqService.send(userMap, MQ.QUEUE_USER_EDIT_CUSTOMER);
        log.info("同步部门信息  ——————> {}", JSON.toJSONString(userMap));
    }

    @Override
    public SysLoginUserInfoVo selectTransUserInfo(String userId) {
        return  this.sysUserInfoMapper.selectTransUserInfo(userId);
    }

    @Override
    public List<SubordinateUserQueryVo> getSubordinateUser(SubordinateUserQueryDto param) {
        UserVo userVo = new UserVo();
        userVo.setUserId(param.getUserId());
        userVo.setDeptId(param.getDepartId());
        userVo.setCompanyId(param.getCompanyId());
        AdminVo admin = this.sysStaffInfoService.getIsAdmin(userVo);
        if (!admin.getIsCompanyAdmin() && !admin.getIsDepartAdmin() && !param.getIsCEO()) {
            return new ArrayList<>();
        }
        param.setIsCompanyAdmin(admin.getIsCompanyAdmin());
        param.setIsDepartAdmin(admin.getIsDepartAdmin());
        List<SubordinateUserQueryVo> result = this.sysUserInfoMapper.getSubordinateUser(param);
        return result;
    }

    @Override
    public List<SubordinateUserQueryVo> getSubordinateUserPartner(SubordinateUserQueryDto param) {
        UserVo userVo = new UserVo();
        userVo.setUserId(param.getUserId());
        userVo.setDeptId(param.getDepartId());
        userVo.setCompanyId(param.getCompanyId());
        AdminVo admin = this.sysStaffInfoService.getIsAdmin(userVo);
        if (!admin.getIsCompanyAdmin() && !admin.getIsDepartAdmin()) {
            return new ArrayList<>();
        }
        param.setIsCompanyAdmin(admin.getIsCompanyAdmin());
        param.setIsDepartAdmin(admin.getIsDepartAdmin());
        List<SubordinateUserQueryVo> result = this.sysUserInfoMapper.getSubordinateUser(param);
        return result;
    }

    @Override
    public List<String> getAllBeUserOrceoIdsInner() {
        return this.sysUserInfoMapper.getAllBeUserOrceoIds();
    }

    @Override
    public void updateImState(UserImStateUpdateDto param) {
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().eq(SysUserInfo::getId, param.getId());
        userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
        userUw.lambda().set(SysUserInfo::getImState, param.getImState());
        this.update(userUw);

        UpdateWrapper<CeoUserInfo> ceoUw = new UpdateWrapper<>();
        ceoUw.lambda().eq(CeoUserInfo::getId, param.getId());
        ceoUw.lambda().set(CeoUserInfo::getImState, param.getImState());
        this.ceoUserInfoService.update(ceoUw);

    }

    @Override
    public IPage<BosStaffInfoVo> getPgaeSystemAllUser(SysStaffInfoPageDto param) {
        IPage<BosStaffInfoVo> userPage = new Page<>(param.getCurrent(), param.getPageSize());
        userPage = this.sysUserInfoMapper.getUserPage(param, userPage);
        if (userPage.getPages() > param.getCurrent()) {
            param.setCurrent(1);
            param.setPageSize(1);
        }
        if (userPage.getPages() == param.getCurrent()) {
            param.setCurrent(1);
            param.setPageSize((int)(userPage.getSize() - ((long) userPage.getRecords().size())));
        }
        if (userPage.getPages() < param.getCurrent()) {
            param.setCurrent((int)(param.getCurrent() - userPage.getPages()));
        }
        IPage<BosStaffInfoVo> ceoPage = this.ceoUserInfoService.getCeoPage(param);
        List<BosStaffInfoVo> addlist = new ArrayList<>();
        //删除第一个分页最后一页数据不够第二个分页补充的数据
        if (CollectionUtils.isEmpty(userPage.getRecords()) ) {
            int size = (int)(userPage.getSize() - (userPage.getTotal() % userPage.getSize()));
            if (size > ceoPage.getRecords().size()) {
                size = ceoPage.getRecords().size();
            }
            for (int i = 0; i < size; i++) {
                ceoPage.getRecords().remove(0);
            }
            if (size > 0) {
                //从上一页获得等量数据填充本页
                param.setCurrent(param.getCurrent() + 1);
                IPage<BosStaffInfoVo> ceoPage2  = this.ceoUserInfoService.getCeoPage(param);
                if (ceoPage2.getRecords().size() > 0) {
                    size = ceoPage2.getRecords().size() > size ? size : ceoPage2.getRecords().size();
                    addlist = ceoPage2.getRecords().subList(0, size);
                }
            }
        }
        //填充第一个分页缺少的数据
        if (userPage.getRecords().size() < userPage.getSize()) {
            userPage.getRecords().addAll(ceoPage.getRecords());
        }
        if (CollectionUtils.isNotEmpty(addlist)) {
            userPage.getRecords().addAll(addlist);
        }
        // 计算总条数
        userPage.setTotal(ceoPage.getTotal() + userPage.getTotal());
        userPage.setPages(userPage.getTotal() / userPage.getSize());
        if (userPage.getTotal() % userPage.getSize() > 0) {
            userPage.setPages(userPage.getPages() + 1);
        }
        return userPage;
    }

    @Override
    public List<UserIdentifierQueryVo> getUserIdentifierByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return this.sysUserInfoMapper.getUserIdentifierByIds(ids);
    }

    @Override
    public StaffInfoVo getUserInfoByMobile(String mobile) {
        StaffInfoVo sysUserInfo = sysUserInfoMapper.getUserInfoByMobile(mobile);
        return sysUserInfo;
    }

    @Override
    public List<AppUserNotPushVo> getNotPushAppUser() {
        return this.sysUserInfoMapper.getNotPushAppUser();
    }

    @Override
    public StaffInfoVo getSuperiorNotCompanyAdmin(String userId) {
        UserVo user = new UserVo();
        user.setUserId(userId);
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, userId);
        staffQw.lambda().last("limit 0,1");
        SysStaffInfo staff = this.sysStaffInfoService.getOne(staffQw);
        if (DataUtil.isEmpty(staff)) {
            throw new DefaultException("获取用户信息失败");
        }
        user.setCompanyId(staff.getCompId());
        //查询该用户权限
        AdminVo admin = this.sysStaffInfoService.getIsAdmin(user);
        //企业管理员没有上级(不包含ceo)
        if (admin.getIsCompanyAdmin()) {
            return null;
        }
        if (admin.getIsDepartAdmin()) {
            StaffInfoVo staffInfo = this.sysStaffInfoService.getStaffInfo(userId);
            if (DataUtil.isEmpty(staffInfo)) {
                throw new DefaultException("获取用户信息失败");
            }
            SysDepartmentInfo departInfo = this.sysDepartmentInfoMapper.selectById(staffInfo.getDepartId());
            //找不到部门 或者 没有上级 返回null 不去查询企业管理员
            if (DataUtil.isEmpty(departInfo)) {
                return null;
            }
            return this.getDepartAdminInfo(departInfo.getParentId());
        }
        //没有部门返回空
        String departId = this.sysUserInfoMapper.getDepartIdByUserId(userId);
        return this.getDepartAdminInfo(departId);
    }

    @Override
    public List<String> getUserIdsByRoleNames(Integer userType) {
        List<String> userIds = null;
        if (UserTypeInfo.CRM_CEO == userType) {
            userIds = this.ceoUserInfoService.getAllCeo();
            return userIds;
        }
        // 根据角色 查询用户id
        userIds = this.sysUserInfoMapper.getUserIdsByRoleNames(UserTypeUtil.getRoleName(userType));
        //伙伴不做其他判断
        if (DataUtil.isEmpty(userIds) || userType.intValue() == UserTypeInfo.PARTNER) {
            return userIds;
        }
        List<String> companyAdminIds = new ArrayList<>(),departAdminIds = new ArrayList<>();
        if (userType == UserTypeInfo.COMPANY_ADMIN ) {
            //当查询企业管理员时 查询出所有企业管理
            companyAdminIds =  this.sysUserInfoMapper.getAllCompanyAdmin();
        }
        if (userType == UserTypeInfo.DEPUTY_GENERAL_MANAGERv || userType == UserTypeInfo.LONG_TEAM) {
            //当查询副总、团队长时 查询出所有部门
            departAdminIds = this.sysUserInfoMapper.getAllDepartAdmin();
        }
        Map<String, String> companyAdminMap = companyAdminIds.stream().collect(Collectors.toMap(a -> a, a -> a, (k1, k2) -> k1, HashMap::new));
        Map<String, String> departAdminMap = departAdminIds.stream().collect(Collectors.toMap( a -> a, a -> a, (k1, k2) -> k1, HashMap::new));
        Iterator<String> iterator = userIds.iterator();
        while (iterator.hasNext()) {
            String userId = iterator.next();
            if (userType == UserTypeInfo.COMPANY_ADMIN && DataUtil.isEmpty(companyAdminMap.get(userId))) {
                // 是总经理角色 但是不是企业管理员 --删除
                iterator.remove();
            }
            if ((userType == UserTypeInfo.DEPUTY_GENERAL_MANAGERv || userType == UserTypeInfo.LONG_TEAM) && DataUtil.isEmpty(departAdminMap.get(userId))) {
                // 是副总、团队长角色 但是不是部门管理员 --删除
                iterator.remove();
            }
        }
        return userIds;
    }

    @Override
    public List<SubordinateUserQueryVo> getSuperiorList(UserVo user) {
        List<SubordinateUserQueryVo> superiorList;

        // 查询ceo账户
        superiorList = this.ceoUserInfoService.getList();
        if(user.isCEO()) {
            // 移除自己
            for (SubordinateUserQueryVo suv : superiorList){
                if (suv.getUserId().equals(user.getUserId())) {
                    superiorList.remove(suv);
                    break;
                }
            }
            return superiorList;
        }
        // 企业管理员(总经理)
        if(user.isCompanyAdmin()){
            return superiorList;
        }
        // 副总
        // 获取总经理账户信息
        List<SubordinateUserQueryVo> managerList =this.companyAdminMapper.getAdminList(user.getCompanyId());
        superiorList.addAll(managerList);
        if(user.isDeptAdmin()) {
            return superiorList;
        }
        String deptId = user.getDeptId().split("_")[0];
        deptId = deptId+","+user.getDeptId();
        //  团队长 伙伴
        List<SubordinateUserQueryVo> departList = this.sysDepartmentInfoMapper.getSuperiorParentList(user.getDeptId());
        // 移除自己
        for (SubordinateUserQueryVo suv : departList) {
            if (suv.getUserId().equals(user.getUserId())) {
                departList.remove(suv);
                break;
            }
        }
        superiorList.addAll(departList);
        return superiorList;
    }

    @Override
    public List<SubordinateUserQueryVo> getInnerSuperiorList(UserVo user) {
        List<SubordinateUserQueryVo> superiorList = this.ceoUserInfoService.getListCompany(user.getCompanyId());
        if(DataUtil.isEmpty(superiorList)){
            superiorList = new ArrayList<>();
        }
        // 团队长
        // 获取总经理账户信息
        List<SubordinateUserQueryVo> managerList = this.companyAdminMapper.getAdminList(user.getCompanyId());
        if(DataUtil.isNotEmpty(managerList) && managerList.size()!=0){
            superiorList.addAll(managerList);
        }
        //  副总 伙伴
        List<SubordinateUserQueryVo> departList = this.sysDepartmentInfoMapper.getSuperiorParentList(user.getDeptId());
        if(DataUtil.isNotEmpty(departList) && departList.size()!=0){
            superiorList.addAll(departList);
        }
        return superiorList;
    }

    @Override
    public void doLogout(String userId) {
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, userId);
        SysStaffInfo  staff1 = this.sysStaffInfoService.getOne(staffQw);
        this.sysUserInfoMapper.updateUserStatusAndIsDeleted(Collections.singletonList(userId));
        this.sysStaffInfoMapper.updateStatus(staff1.getId(),StatusEnum.deleted.getValue(),null);
        this.checkUtil.removeUserToken(userId);
    }

    @Override
    public StaffInfoVo getSuperiorAdmin(UserVo user) {
        if (user.isBack() || user.isCEO()) {
            return null;
        }
        //查询该用户权限
        AdminVo admin = this.sysStaffInfoService.getIsAdmin(user);
        //企业管理员没有上级(不包含ceo)
        if (admin.getIsCompanyAdmin()) {
            return null;
        }
        StaffInfoVo superior = null;
        if (admin.getIsDepartAdmin()) {
            SysDepartmentInfo departInfo = this.sysDepartmentInfoMapper.selectById(user.getDeptId());
            //找不到部门 或者 没有上级 返回null 不去查询企业管理员
            if (DataUtil.isNotEmpty(departInfo)) {
                superior = this.getDepartAdminInfo(departInfo.getParentId());
            }
        } else {
            superior = this.getDepartAdminInfo(user.getDeptId());
        }
        if (DataUtil.isEmpty(superior)) {
            superior = companyAdminMapper.getAdminInfoByCompanyId(user.getCompanyId());
        }
        return superior;
    }

    @Override
    public int doUserStatusEditInfo() {
        QueryWrapper<SysUserInfo> userQw = new QueryWrapper<>();
        userQw.select("id", 0 + " AS status_edit", "status");
        userQw.lambda().eq(SysUserInfo::getStatusEdit, YesNo.YES);
        userQw.lambda().last("limit 0, 500");
        List<SysUserInfo> users = this.list(userQw);
        if (DataUtil.isEmpty(users)) {
            return 0;
        }
        this.updateBatchById(users);
        List<AdviserUserStatusUpdateDto> usersParams = new ArrayList<>();
        users.forEach(u -> {
            if (DataUtil.isEmpty(u) || DataUtil.isEmpty(u.getStatus()) ||
                    (!u.getStatus().equals(StatusEnum.deleted.getValue()) && !u.getStatus().equals(StatusEnum.stop.getValue()))) {
                return;
            }
            AdviserUserStatusUpdateDto userParam = new AdviserUserStatusUpdateDto();
            userParam.setUserId(u.getId());
            userParam.setStatus(YesNo.NO);
            usersParams.add(userParam);
        });
        this.adviserFeignService.updateAdviserStatus(usersParams);
        return usersParams.size();
    }

    @Override
    public UnionRoleStaff getUnionRoleStaff(String userId) {
        UnionRoleStaff staff = this.sysUserInfoMapper.getUserIdUnionRoleStaff(userId);
        return staff;
    }

    @Override
    public AdminUserAllVo getAdminUserAll() {

        AdminUserAllVo allVo = new AdminUserAllVo();
        // 查询ceo账户
        List<SubordinateUserQueryVo> superiorList = this.ceoUserInfoService.getList();
        List<com.zerody.user.api.vo.CeoRefVo> ceoUser = new ArrayList<>();
        for (SubordinateUserQueryVo suv : superiorList){

            com.zerody.user.api.vo.CeoRefVo ceo = new com.zerody.user.api.vo.CeoRefVo();
            ceo.setCeoId(suv.getUserId());
            ceo.setUserName(suv.getUserName());
            ceo.setPhoneNumber(suv.getMobile());
            ceoUser.add(ceo);
        }
        allVo.setCeoUser(ceoUser);
        // 企业管理员(总经理)
        // 获取总经理账户信息
        List<SubordinateUserQueryVo> managerList =this.companyAdminMapper.getAdminList(null);
        List<CompanyInfoVo> adminUser = new ArrayList<>();
        for (SubordinateUserQueryVo suv : managerList){

            if(StringUtils.isEmpty(suv.getUserId())){
                continue;
            }
            CompanyInfoVo admin = new CompanyInfoVo();
            admin.setAdminUserId(suv.getUserId());
            admin.setAdminUserName(suv.getUserName());
            admin.setCompanyId(suv.getCompanyId());
            admin.setCompanyName(suv.getCompanyName());
            adminUser.add(admin);
        }
        allVo.setAdminUser(adminUser);
        // 团队长 副总
        List<DepartInfoVo>  departUser = this.sysDepartmentInfoMapper.getAllDepList();
        allVo.setDepartUser(departUser);
        return allVo;

    }


    @Override
    public List<SubordinateUserQueryVo> getLeaveUser(SubordinateUserQueryDto param){
        return this.sysUserInfoMapper.getLeaveUser(param);
    }

    @Override
    public List<String> getUserAllTrainNo(String companyId) {
        List<String> list= this.sysUserInfoMapper.getUserAllTrainNo(companyId);
        return list;
    }

    @Override
    public List<String> getUserIdsByUserType(Integer userType) {
        if(userType == null) {
            return null;
        }
        List<String> ceoList =  this.ceoUserInfoService.getAllCeo();
        //总裁
        if (UserTypeInfo.CRM_CEO == userType) {
            return ceoList;
        }

        List<String> adminList = this.sysUserInfoMapper.getAllCompanyAdmin();
        //总经理
        if(UserTypeInfo.COMPANY_ADMIN == userType){
            return adminList;
        }
        List<DepartInfoVo>  departUser = this.sysDepartmentInfoMapper.getAllDepList();
        //副总
        if(UserTypeInfo.DEPUTY_GENERAL_MANAGERv == userType){
            return departUser.stream().filter(s-> StringUtils.isEmpty(s.getParentDepartId())).map(DepartInfoVo::getAdminUserId).collect(Collectors.toList());
        }
        //团队长
        if(UserTypeInfo.LONG_TEAM == userType){
            return departUser.stream().filter(s-> !StringUtils.isEmpty(s.getParentDepartId())).map(DepartInfoVo::getAdminUserId).collect(Collectors.toList());
        }
        //伙伴
        if(UserTypeInfo.PARTNER == userType){
            adminList.addAll(departUser.stream().map(DepartInfoVo::getAdminUserId).collect(Collectors.toList()));

            QueryWrapper<SysStaffInfo> qw = new QueryWrapper<>();
            qw.lambda().eq(SysStaffInfo::getUserType,userType);
            qw.lambda().in(SysStaffInfo::getStatus,0,3);
            qw.lambda().notIn(SysStaffInfo::getId,adminList);
            List<SysStaffInfo> list = this.sysStaffInfoService.list(qw);
            return list.stream().map(SysStaffInfo::getUserId).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<String> getUserIdsByCompanyIdUserType(Integer userType, String companyId) {
        if(userType == null) {
            return null;
        }
        List<String> ceoList = this.ceoUserInfoService.getCeoByCompanyId(companyId);
        //总裁
        if(UserTypeInfo.CRM_CEO == userType){
            return ceoList;
        }

        List<String> adminList = this.sysUserInfoMapper.getCompanyAdmin(companyId);
        //总经理
        if(UserTypeInfo.COMPANY_ADMIN == userType){
            return adminList;
        }
        List<DepartInfoVo>  departUser = this.sysDepartmentInfoMapper.getDepList(companyId);
        //副总
        if(UserTypeInfo.DEPUTY_GENERAL_MANAGERv == userType){
            return departUser.stream().filter(s-> StringUtils.isEmpty(s.getParentDepartId())).map(DepartInfoVo::getAdminUserId).collect(Collectors.toList());
        }
        //团队长
        if(UserTypeInfo.LONG_TEAM == userType){
            return departUser.stream().filter(s-> !StringUtils.isEmpty(s.getParentDepartId())).map(DepartInfoVo::getAdminUserId).collect(Collectors.toList());
        }
        //伙伴
        if(UserTypeInfo.PARTNER == userType){
            adminList.addAll(departUser.stream().map(DepartInfoVo::getAdminUserId).collect(Collectors.toList()));

            QueryWrapper<SysStaffInfo> qw = new QueryWrapper<>();
            qw.lambda().eq(SysStaffInfo::getUserType,userType);
            qw.lambda().in(SysStaffInfo::getStatus,0,3);
            qw.lambda().notIn(SysStaffInfo::getId,adminList);
            qw.lambda().eq(SysStaffInfo::getCompId,companyId);
            List<SysStaffInfo> list = this.sysStaffInfoService.list(qw);
            return list.stream().map(SysStaffInfo::getUserId).collect(Collectors.toList());
        }
        return null;
    }

    //递归获取上级 不包含企业管理员
    private StaffInfoVo getDepartAdminInfo(String departId) {
        if (StringUtils.isEmpty(departId)) {
            return null;
        }
        SysDepartmentInfo depart = this.sysDepartmentInfoMapper.selectById(departId);
        //部门没有负责人或找不到部分返回null
        if (DataUtil.isEmpty(depart) ) {
            return null;
        }
        //没有负责人往上查找
        if (DataUtil.isEmpty(depart.getAdminAccount())) {
            QueryWrapper<SysDepartmentInfo> departQw = new QueryWrapper<>();
            departQw.lambda().eq(SysDepartmentInfo::getId, depart.getParentId());
            departQw.last("limit 0,1");
            depart = this.sysDepartmentInfoMapper.selectOne(departQw);
            if (DataUtil.isEmpty(depart)) {
                return null;
            }
            return this.getDepartAdminInfo(depart.getId());
        }
        SysStaffInfo staffInfo = this.sysStaffInfoService.getById(depart.getAdminAccount());
        if (DataUtil.isEmpty(staffInfo.getUserId())) {
            throw new DefaultException("获取用户信息失败");
        }
        SysUserInfo userInfo = this.getById(staffInfo.getUserId());
        if (DataUtil.isEmpty(userInfo)) {
            throw new DefaultException("获取用户信息失败");
        }
        // 如果是在职或者合作状态 返回上级信息(均为在职状态)
        if (StatusEnum.activity.getValue().intValue() == userInfo.getStatus().intValue() ||
                StatusEnum.teamwork.getValue().intValue() == userInfo.getStatus().intValue()) {
            return this.sysStaffInfoService.getStaffInfo(userInfo.getId());
        }

        if (DataUtil.isEmpty(depart.getParentId())) {
            return null;
        }
        return this.getDepartAdminInfo(depart.getParentId());
    }


}
