package com.zerody.user.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.constant.UserTypeInfo;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.UserTypeEnum;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.export.util.ExcelHandlerUtils;
import com.zerody.user.api.dto.UserCopyDto;
import com.zerody.user.api.vo.*;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.domain.SysUserIdentifier;
import com.zerody.user.dto.*;
import com.zerody.user.service.*;
import com.zerody.user.vo.*;
import com.zerody.user.vo.BackUserRefVo;
import com.zerody.user.vo.CeoRefVo;
import com.zerody.user.vo.CompanyRefVo;
import com.zerody.user.vo.SysLoginUserInfoVo;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.LastModified;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.dto.CardUserDto;
import com.zerody.user.api.dto.LoginCheckParamDto;
import com.zerody.user.api.service.UserRemoteService;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.service.base.CheckUtil;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author PengQiang
 * @ClassName SysUserInfoController
 * @DateTime 2020/12/16_17:10
 * @Deacription TODO
 */
@Slf4j
@RequestMapping("/sys-user-info")
@RestController
public class SysUserInfoController implements UserRemoteService, LastModified {

    @Autowired
    private UseControlService useControlService;

    @Autowired
    private CheckUtil checkUtil;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private CompanyAdminService amdinService;

    @Autowired
    private CeoUserInfoService ceoUserInfoService;

    @Autowired
    private CardUserService cardUserService;

    @Autowired
    private AdminUserService amdinUserService;

    @Autowired
    private SysCompanyInfoService sysCompanyInfoService;

    @Autowired
    private SysUserIdentifierService sysUserIdentifierService;

    @Autowired
    private CeoCompanyRefService ceoCompanyRefService;

	@Override
	public long getLastModified(HttpServletRequest request) {
		return System.currentTimeMillis();
	}

	/**
	 * 根据用户ID查询单个用户
	 */
    @GetMapping("/get/{id}")
    public DataResult getUserInfoById(@PathVariable String id){
        return R.success(sysUserInfoService.getUserInfoById(id));
    }
    /**
     * 根据用户ID查询单个用户(白名单)
     */
    @GetMapping("/get/white/{id}")
    public DataResult getUserInfoWhiteById(@PathVariable String id){
        return R.success(sysUserInfoService.getUserInfoById(id));
    }



    //添加用户
    @PostMapping("/addUser")
    public com.zerody.common.bean.DataResult addUser(@Validated @RequestBody SysUserInfo userInfo){

        return sysUserInfoService.addUser(userInfo);
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          内部接口复制用户
     * @date                 2022/6/18 15:48
     * @param                [setSysUserInfoDto]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @PostMapping(value = "/copy/inner")
    @Override
    public DataResult<UserCopyResultVo> doCopyStaffInner(@Validated @RequestBody UserCopyDto param){
        try {
            log.info("copy用户入参:{}", JSON.toJSONString(param));
            return R.success(sysStaffInfoService.doCopyStaffInner(param));
        } catch (DefaultException e){
            log.error("内部接口复制员工错误:{}" + JSON.toJSONString(param), e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("内部接口复制员工错误:{} "+ JSON.toJSONString(param), e);
            return R.error("内部接口复制员工错误,请求异常");
        }
    }

    @Override
    @GetMapping("/get/user-ids/by-role-name/inner")
    public DataResult<List<String>> getUserIdsByRoleNames(@RequestParam("userType") Integer userType) {
        try {
            return R.success(this.sysUserInfoService.getUserIdsByRoleNames(userType));
        } catch (Exception e) {
            log.error("通过角色名称查询用户失败：{}",e ,e);
            return R.error(e.getMessage());
        }
    }

   /* @Override
    public DataResult<List<String>> getUserIdsByRoleNames(Integer integer) {
        return null;
    }*/

    //修改用户
    @PostMapping("/updateUser")
    public com.zerody.common.bean.DataResult updateUser(@Validated @RequestBody SysUserInfo userInfo){

        return  sysUserInfoService.updateUser(userInfo);
    }

    //根据id删除用户
    @DeleteMapping("/deleteUserById")
    public com.zerody.common.bean.DataResult deleteUserById(String userId){

        return sysUserInfoService.deleteUserById(userId);
    }

    //根据用户id批量删除用户
    @DeleteMapping("/deleteUserBatchByIds")
    public com.zerody.common.bean.DataResult deleteUserBatchByIds(List<String> ids){

        return sysUserInfoService.deleteUserBatchByIds(ids);
    }

    //分页查询用户
    @RequestMapping("/selectUserPage")
    public com.zerody.common.bean.DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto){

        return sysUserInfoService.selectUserPage(sysUserInfoPageDto);
    }



    /**
    *   登录校验账户和密码
    */
    @Override
    @RequestMapping(value = "/check-user/inner",method = POST, produces = "application/json")
    public DataResult checkLoginUser(@RequestBody LoginCheckParamDto params){
        //查询账户信息,返回个密码来校验密码
        //优先判断总裁账户
        CeoUserInfo one = ceoUserInfoService.getByPhone(params.getUserName());
        String userPwd="";
        if(DataUtil.isNotEmpty(one)){
            // 校验密码
            userPwd=one.getUserPwd();
        }else{
            CheckLoginVo checkLoginVo = sysUserInfoService.checkLoginUser(params.getUserName());
            if(DataUtil.isEmpty(checkLoginVo)){
                return R.error("当前账号未开通，请联系管理员开通！");
            }
            String companyId = this.sysStaffInfoService.selectStaffById(checkLoginVo.getStaffId()).getCompanyId();
            SysComapnyInfoVo company = this.sysCompanyInfoService.getCompanyInfoById(companyId);
            if(DataUtil.isEmpty(company)){
                return R.error("数据异常！");
            }
            if(StatusEnum.stop.getValue().equals(company.getStatus())){
                return R.error("账号被停用！");
            }
            if(   StatusEnum.deleted.getValue().equals(company.getStatus())){
                return R.error("当前账号未开通，请联系管理员开通！");
            }
            userPwd=checkLoginVo.getUserPwd();
        }
        //校验密码
        boolean success = sysUserInfoService.checkPassword(userPwd, params.getPwd());
        if(!success){
            return R.error("密码错误！");
        }
        return R.success("密码正确！");
    }

	/**
	 * 通过用户名和密码获取用户数据
	 *
	 * @param params
	 * @return
	 */
	@Override
	@RequestMapping(value = "/login-user/inner", method = POST, produces = "application/json")
	public DataResult<com.zerody.user.api.vo.SysLoginUserInfoVo> getLoginUser(@RequestBody LoginCheckParamDto params) {
		// 查询账户信息,返回个密码来校验密码
        //4-20增加总裁用户表，优先判断总裁表的用户返回
        CeoUserInfo one = ceoUserInfoService.getByPhone(params.getUserName());
        com.zerody.user.api.vo.SysLoginUserInfoVo info = new com.zerody.user.api.vo.SysLoginUserInfoVo();
        if(DataUtil.isNotEmpty(one)){
            // 校验密码
            boolean success = sysUserInfoService.checkPassword(one.getUserPwd(), params.getPwd());
            if (!success) {
                return R.error("密码错误！");
            }
            BeanUtils.copyProperties(one, info);
            info.setUserType(UserTypeEnum.CRM_CEO.name());
            info.setIsAdmin(false);
        }else {
            CheckLoginVo checkLoginVo = sysUserInfoService.checkLoginUser(params.getUserName());
            if (DataUtil.isEmpty(checkLoginVo)) {
                return R.error("当前账号未开通，请联系管理员开通！");
            }
            String companyId= sysStaffInfoService.getById(checkLoginVo.getStaffId()).getCompId();
//            String companyId = this.sysStaffInfoService.selectStaffById(checkLoginVo.getStaffId()).getCompanyId();
            SysComapnyInfoVo company = this.sysCompanyInfoService.getCompanyInfoById(companyId);
//            try {
//                useControlService.checkUserAuth(checkLoginVo.getUserId(), companyId);
//            }catch (DefaultException e){
//                log.error(params.getUserName()+"登录限制：{}",e.getMessage());
//                return R.error(e.getMessage());
//            }
            if (DataUtil.isEmpty(company)) {
                return R.error("数据异常！");
            }
            if (StatusEnum.stop.getValue().equals(company.getStatus())) {
                return R.error("账号被停用！");
            }
            if (StatusEnum.deleted.getValue().equals(company.getStatus())) {
                return R.error("当前账号未开通，请联系管理员开通！");
            }
            // 校验密码
            boolean success = sysUserInfoService.checkPassword(checkLoginVo.getUserPwd(), params.getPwd());
            if (!success) {
                return R.error("密码错误！");
            }
            SysLoginUserInfoVo sysLoginUserInfoVo = sysUserInfoService.getUserInfo(params.getUserName());
            if (DataUtil.isEmpty(sysLoginUserInfoVo)) {
                return R.error("当前账号未开通，请联系管理员开通！");
            }
            BeanUtils.copyProperties(sysLoginUserInfoVo, info);
            info.setIsAdmin(sysUserInfoService.checkUserAdmin(info.getId()));
        }
        SysUserIdentifier identifier = sysUserIdentifierService.getIdentifierInfo(info.getId());
        if(!Objects.isNull(identifier)){
            info.setEquipmentNo(identifier.getEquipmentNo());
            info.setEquipmentName(identifier.getEquipmentName());
        }
		return R.success(info);
	}

    /**
    *    登录平台管理后台校验账户和密码
     * @return
     */
    @Override
    @RequestMapping(value = "/check-admin/inner",method = POST, produces = "application/json")
    public DataResult<com.zerody.user.api.vo.AdminUserInfo> checkLoginAdmin(@RequestBody LoginCheckParamDto params) {
        //查询账户信息,返回个密码来校验密码
        com.zerody.user.api.vo.AdminUserInfo userInfo = amdinUserService.checkLoginAdmin(params.getUserName());
        if(DataUtil.isEmpty(userInfo)){
            return R.error("当前账号未开通，请联系管理员开通！");
        }
        //校验密码
        boolean success = sysUserInfoService.checkPassword(userInfo.getUserPwd(), params.getPwd());
        userInfo.setUserPwd(null);
        if(success){
            return R.success(userInfo);
        }else {
            return R.error("密码错误！");
        }
    }



    /**
    *   获取用户信息；构建token基础信息
    */
    @Override
    @RequestMapping(value = "/user-info/inner",method = GET, produces = "application/json")
    public DataResult<com.zerody.user.api.vo.SysLoginUserInfoVo> getUserInfo(@RequestParam("userName") String userName){
        //4-20增加总裁用户表，优先判断总裁表的用户返回
        CeoUserInfo one = ceoUserInfoService.getByPhone(userName);
        com.zerody.user.api.vo.SysLoginUserInfoVo info = new com.zerody.user.api.vo.SysLoginUserInfoVo();
        if(DataUtil.isNotEmpty(one)){
            BeanUtils.copyProperties(one, info);
            info.setUserType(UserTypeEnum.CRM_CEO.name());
        }else {
            SysLoginUserInfoVo sysLoginUserInfoVo = sysUserInfoService.getUserInfo(userName);
//            try {
//                useControlService.checkUserAuth(sysLoginUserInfoVo.getId(), sysLoginUserInfoVo.getCompanyId());
//            }catch (DefaultException e){
//                log.error(userName+"短信登录限制：{}",e.getMessage());
//                return R.error(e.getMessage());
//            }
            if (DataUtil.isEmpty(sysLoginUserInfoVo)) {
                return R.error("当前账号未开通，请联系管理员开通！");
            }
            SysComapnyInfoVo companyInfo = this.sysCompanyInfoService.getCompanyInfoById(sysLoginUserInfoVo.getCompanyId());
            if (DataUtil.isEmpty(companyInfo) ||  StatusEnum.deleted.getValue().equals(companyInfo.getStatus())) {
                return R.error("当前账号未开通，请联系管理员开通！");
            }
            if (StatusEnum.stop.getValue().equals(companyInfo.getStatus())) {
                return R.error("账号被停用！");
            }
            BeanUtils.copyProperties(sysLoginUserInfoVo, info);
        }
        SysUserIdentifier identifier = sysUserIdentifierService.getIdentifierInfo(info.getId());
        if(!Objects.isNull(identifier)){
            info.setEquipmentNo(identifier.getEquipmentNo());
            info.setEquipmentName(identifier.getEquipmentName());
        }
        return R.success(info);
    }

    /**
    *   修改用户表
    */
    @Override
    @RequestMapping(value = "/update/inner",method = PUT, produces = "application/json")
    public DataResult updateById(com.zerody.user.api.vo.SysUserInfo sysUserInfo) {
        SysUserInfo newInfo=new SysUserInfo();
        BeanUtils.copyProperties(sysUserInfo,newInfo);
        com.zerody.common.bean.DataResult dataResult = sysUserInfoService.updateUser(newInfo);
        if(!dataResult.isIsSuccess()){
            return R.error(dataResult.getMessage());
        }
        return R.success();
    }

    /**
    *   查询当前用户在这个企业员工关联的角色
    */
    @Override
    @RequestMapping(value = "/role/get/inner",method = GET, produces = "application/json")
    public DataResult<List<String>> getRoles(@RequestParam("userId")String userId, @RequestParam("companyId")String companyId) {
        List<String> roles=sysStaffInfoService.getStaffRoles(userId,companyId);
        return R.success(roles);
    }

    /**
     *   查询后台管理员的平台角色
     */
    @Override
    @RequestMapping(value = "/platform-role/get/inner",method = GET, produces = "application/json")
    public DataResult<String> getPlatformRoles(@RequestParam("userId")String userId) {
        try {
            String role = amdinUserService.getPlatfoemRoles(userId);
            return R.success(role);
        }catch (Exception e){
            return R.error(e.getMessage());
        }

    }

    /**
    *    根据用户id获取平台管理员信息
    */
    @Override
    @RequestMapping(value = "/admin-user-id/inner",method = GET, produces = "application/json")
    public DataResult<AdminUserInfo> getAdminUserById(@RequestParam("userId")String userId){
        try{
            AdminUserInfo userById = amdinUserService.getUserById(userId);
            return R.success(userById);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    /**
     *   根据用户id获取用户信息；
     */
    @Override
    @RequestMapping(value = "/get/inner/{id}",method = GET, produces = "application/json")
    public DataResult getUserById(@PathVariable String id){
        SysUserInfo sysUserInfo=sysUserInfoService.getUserById(id);
        if(DataUtil.isEmpty(sysUserInfo)){
            return R.error("用户不存在！");
        }
        return R.success(sysUserInfo);
    }

    /**
     *   根据角色id查询是否绑定了员工
     */
    @Override
    @RequestMapping(value = "/role/check-bind/inner",method = GET, produces = "application/json")
    public DataResult<Boolean> checkRoleBind(@RequestParam("roleId")String roleId){
       return R.success(sysUserInfoService.checkRoleBind(roleId));
    }

    /**
     *   根据平台角色id查询是否绑定了管理员
     */
    @Override
    @RequestMapping(value = "/platform-role/check-bind/inner",method = GET, produces = "application/json")
    public DataResult<Boolean> checkPlatformRoleBind(@RequestParam("roleId")String roleId){
        return R.success(sysUserInfoService.checkPlatformRoleBind(roleId));
    }

    @Override
    @RequestMapping(value = "/check-admin/inner",method = GET, produces = "application/json")
    public DataResult<Boolean> checkUserAdmin(@RequestParam("userId")String userId) {
        return R.success(sysUserInfoService.checkUserAdmin(userId));
    }

    @Override
    @RequestMapping(value = "/check-admin-back/inner",method = GET, produces = "application/json")
    public DataResult<Boolean> checkBackAdminUser(@RequestParam("userId")String userId) {
        return R.success(sysUserInfoService.checkBackAdminUser(userId));
    }



    /**
     *
     * 获取员工数据，包含公司，部门，岗位
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/7 17:24
     * @param                [userId]
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.api.vo.UserDeptVo>
     */
	@Override
	@GetMapping(value = "/user-dept")
	public DataResult<UserDeptVo> getUserDeptVo(String userId) {

        try {
            return R.success(this.sysStaffInfoService.getUserDeptVo(userId));
        } catch (DefaultException e) {
            log.error("获取员工信息出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取员工信息出错:{}", e, e);
            return R.error("获取线索汇总出错！请求异常");
        }
    }


	@Override
	@GetMapping(value = "/user-subordinates")
	public DataResult<List<String>> getUserSubordinates(String userId) {

        try {
            return R.success(this.sysStaffInfoService.getUserSubordinates(userId));
        } catch (DefaultException e) {
            log.error("获取员工下级部门信息出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取员工下级部门信息出错:{}", e, e);
            return R.error("获取员工下级部门信息出错！请求异常");
        }
	}

    @Override
    @GetMapping(value = "/admin-user/inner")
    public DataResult<AdminUserInfo> getAdminUser(@RequestParam("mobile") String mobile) {
	    try{
            AdminUserInfo adminUserInfo= amdinUserService.getByMobile(mobile);
            return R.success(adminUserInfo);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping(value = "/user-subordinates/clew/collect")
    public DataResult<IPage<SysUserClewCollectVo>> geSubordinatestUserClewCollect(PageQueryDto dto) {

        try {
            String userId = UserUtils.getUserId();
            return R.success(this.sysStaffInfoService.getSubordinatesUserClewCollect(dto, userId));
        } catch (DefaultException e) {
            log.error("获取员工下级线索汇总信息出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取员工下级线索汇总信息出错:{}", e, e);
            return R.error("获取员工下级线索汇总信息出错！请求异常");
        }
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          清空下级员工线索 (包括自己)
     * @date                 2021/1/20 17:31
     * @param                [id]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @DeleteMapping(value = "/user-subordinates/empty-clew/{id}")
    public DataResult<Object> doEmptySubordinatesUserClew(@PathVariable(name = "id") String id) {

        try {
            this.sysStaffInfoService.doEmptySubordinatesUserClew(id);
            return R.success();
        } catch (DefaultException e) {
            log.error("删除员工下级线索出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除员工下级线索出错:{}", e, e);
            return R.error("删除员工下级线索出错！请求异常");
        }
    }

    /**
     * 【绑定手机号】
     *   修改名片用户信息
     */
    @Override
    @RequestMapping(value = "/card-user-bind/inner",method = POST, produces = "application/json")
    public DataResult<CardUserInfoVo> bindMobileCardUser(@RequestBody CardUserInfoVo data) {
        try {
            CardUserInfoVo cardUserInfoVo = cardUserService.bindPhoneNumber(data);
            return R.success(cardUserInfoVo);
        } catch (Exception e) {
            log.error("修改名片用户出错:{}", e, e);
            return R.error("修改名片用户出错:"+e.getMessage());
        }
    }


    /**
     * 【绑定openID】
     *
     */
    @Override
    @RequestMapping(value = "/openid-bind/inner",method = POST, produces = "application/json")
    public DataResult<CardUserInfoVo> bindOpenId(@RequestParam(name = "openId")String openId,@RequestParam(name = "userId")String userId) {
        try {
            CardUserInfoVo vo= cardUserService.bindOpenId(openId,userId);
            return R.success(vo);
        } catch (Exception e) {
            log.error("修改名片用户出错:{}", userId, e);
            return R.error("修改名片用户出错:"+e.getMessage());
        }
    }

    /**
     * 【解绑手机号】
     *
     */
    @Override
    @RequestMapping(value = "/card-user-unbind/inner",method = POST, produces = "application/json")
    public DataResult<CardUserDto> unBindMobileCardUser(@RequestParam(name = "openId")String openId) {
        try {
            CardUserDto vo= cardUserService.unBindPhoneNumber(openId);
            return R.success(vo);
        } catch (Exception e) {
            log.error("修改名片用户出错:{}", e, e);
            return R.error("修改名片用户出错:"+e.getMessage());
        }
    }


    /**
     * 通过员工id查询用户id api
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/25 19:08
     * @param                [staffId]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @Override
    @RequestMapping(value = "/get/user-id/inner", method = GET, produces = "application/json")
    public DataResult<String> getUserIdByCompIdOrStaffId(@RequestParam(name = "id") String staffId) {
        try {
            String userId = this.sysUserInfoService.getUserIdByCompIdOrStaffId(staffId);
            return R.success(userId);
        } catch (Exception e) {
            log.error("修改名片用户出错:{}", e, e);
            return R.error("修改名片用户出错:"+e.getMessage());
        }
    }

    /**
     *
     * 查看当前员工是否是  企业/部门 管理员
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/26 20:19
     * @param                [userId, comapnyId]
     * @return               com.zerody.common.api.bean.DataResult<java.util.Map<java.lang.String,java.lang.String>>
     */
    @Override
    @RequestMapping(value = "/get-is-admin/inner", method = RequestMethod.GET)
    public DataResult<AdminVo> getIsAdminInner(String userId, String companyId){
        try {
            UserVo user = new UserVo();
            user.setUserId(userId);
            user.setCompanyId(companyId);
            return R.success(sysStaffInfoService.getIsAdmin(user));
        } catch (DefaultException e){
            log.error("获取管理员信息:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取管理员信息:{}",e.getMessage());
            return R.error("获取管理员信息,请求异常");
        }
    }

    @Override
    @RequestMapping(value = "/get/derart-id/inner", method = RequestMethod.GET)
    public DataResult<String> getDepartId(@RequestParam("userId") String userId){
        try {
            return R.success(sysStaffInfoService.getDepartId(userId));
        } catch (DefaultException e){
            log.error("获取部门id错误:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取部门id错误:{}",e,e);
            return R.error("获取部门id错误,请求异常");
        }
    }

    @Override
    @RequestMapping(value = "/get/staff-info/inner", method = RequestMethod.GET)
    public DataResult<StaffInfoVo> getStaffInfo(@RequestParam("userId")String userId){
        try {
            CeoUserInfo ceo =  this.ceoUserInfoService.getById(userId);
            if (DataUtil.isNotEmpty(ceo)) {
                StaffInfoVo staffInfoVo = new StaffInfoVo();
                BeanUtils.copyProperties(ceo, staffInfoVo);
                staffInfoVo.setUserId(ceo.getId());
                staffInfoVo.setMobile(ceo.getPhoneNumber());
                staffInfoVo.setUserAvatar(ceo.getAvatar());
                return R.success(staffInfoVo);
            }
            return R.success(sysStaffInfoService.getStaffInfo(userId));
        } catch (DefaultException e){
            log.error("获取员工id错误:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取部门id错误:{}",e,e);
            return R.error("获取部门id错误,请求异常");
        }
    }

    @RequestMapping(value = "/get/user", method = RequestMethod.GET)
    @Override
    public DataResult<List<com.zerody.user.api.vo.SysUserInfoVo>> getUserByDepartOrRole(@RequestParam(value = "departId", required = false)String departId,
                                                                              @RequestParam(value = "roleId", required = false) String roleId,
                                                                                    @RequestParam(value = "companyId", required = false) String companyId){
        try {
            if (StringUtils.isEmpty(departId) && StringUtils.isEmpty(roleId)){
                return R.success();
            }
            return R.success(sysStaffInfoService.getUserByDepartOrRole(departId, roleId, companyId));
        } catch (DefaultException e){
            log.error("获取员工id错误:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取部门id错误:{}",e,e);
            return R.error("获取部门id错误,请求异常");
        }
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          通过用户id、角色id获取它的上级用户
     * @date                 2021/3/3 16:34
     * @param                [userId, roleId]
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.SysUserInfoVo>>
     */
	@RequestMapping(value = "/superior", method = GET)
    @Override
    public DataResult<List<com.zerody.user.api.vo.SysUserInfoVo>> getSuperiorUesrByUserAndRole(@RequestParam("userId")String userId,
                                                                                     @RequestParam("roleId")String roleId){
        try {
            return R.success(sysStaffInfoService.getSuperiorUesrByUserAndRole(userId, roleId));
        } catch (DefaultException e){
            log.error("获取上级员工错误:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取上级员工错误:{}",e,e);
            return R.error("获取上级员工错误,请求异常");
        }
    }

    @Override
    @RequestMapping("/update/performance-password/inner")
    public DataResult<Object> updatePerformancePassword(@RequestBody AdminUserInfo userInfo){
        try {
            this.sysUserInfoService.updatePerformancePassword(userInfo);
            return R.success();
        } catch (DefaultException e){
            log.error("修改业绩查看密码错误:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("修改业绩查看密码错误:{}",e,e);
            return R.error("修改业绩查看密码错误!"+ e);
        }
    }

    /**
     * 通过小程序用户id查询用户
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/28 17:23
     * @param                userId
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @Override
    @RequestMapping(value = "/get/card-user/staff-info/inner", method = RequestMethod.GET)
    public DataResult<StaffInfoVo> getStaffInfoByCardUserId(@RequestParam("userId")String cardUserId){
        try {

            return R.success(sysStaffInfoService.getStaffInfoByCardUserId(cardUserId));
        } catch (DefaultException e){
            log.error("获取用户信息失败:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取用户信息失败:{}",e,e);
            return R.error("获取用户信息失败!"+ e);
        }
    }

    /*************************************************1.1版本api接口*******************************************************************/


    @RequestMapping(value = "/get/page/performance-reviews/collect", method = GET)
    public DataResult<IPage<UserPerformanceReviewsVo>> getPagePerformanceReviews(UserPerformanceReviewsPageDto param){
        try {
//            checkUtil.SetUserPositionInfo(param);
			if (!UserUtils.getUser().isBackAdmin()) {
                param.setCompanyId(UserUtils.getUser().getCompanyId());
            }else {
                checkUtil.SetUserPositionInfo(param);
            }
            return R.success(sysStaffInfoService.getPagePerformanceReviews(param));
        } catch (DefaultException e){
            log.error("获取业绩总结列表出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取业绩总结列表出错:{}",e,e);
            return R.error("获取业绩总结列表出错,请求异常");
        }
    }

    @RequestMapping(value = "/get/page/performance-reviews/collect-export", method = POST)
    public void doPerformanceReviewsExport(@RequestBody UserPerformanceReviewsPageDto param, HttpServletResponse res){
        try {
//            checkUtil.SetUserPositionInfo(param);
            if (!UserUtils.getUser().isBackAdmin()) {
                param.setCompanyId(UserUtils.getUser().getCompanyId());
            }else {
                checkUtil.SetUserPositionInfo(param);
            }
            List<UserPerformanceReviewsVo> list=sysStaffInfoService.doPerformanceReviewsExport(param, res);
            ExcelHandlerUtils.exportExcel(list, "业绩总结列表", UserPerformanceReviewsVo.class, "业绩总结列表导出.xls", res);
        } catch (DefaultException e){
            log.error("导出业绩总结列表出错:{}",e,e);
            throw new DefaultException("导出业绩总结列表出错");
        }  catch (Exception e) {
            log.error("导出业绩总结列表出错:{}",e,e);
            throw new DefaultException("导出业绩总结列表出错");
        }
    }

    /**
     *
     * 查询业绩密码
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/16 19:19
     * @param                mobile
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @Override
    @RequestMapping(value = "/get/show/performance/inner", method = RequestMethod.GET)
    public DataResult<String> getShowPerformancePassword(@RequestParam("id")String id){
        try {
            String pass = this.sysUserInfoService.getShowPerformancePassword(id);
            return R.success(pass);
        } catch (DefaultException e){
            log.error("获取业绩密码出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取业绩密码出错:{}",e,e);
            return R.error("获取业绩密码出错"+ e);
        }
    }


    @Override
    @RequestMapping(value = "/update/is-sign-order/inner", method = POST)
    public DataResult<Object> updateUserIsSignOrder(@RequestParam("id") String userId){
        try {
            this.sysUserInfoService.updateUserIsSignOrder(userId);
            return R.success();
        } catch (DefaultException e){
            log.error("获取业绩密码出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取业绩密码出错:{}",e,e);
            return R.error("获取业绩密码出错"+ e);
        }
    }

/**-------------------------------------------------------------------------------------------------------------------  */

    /**
     *
     *
     * @author               PengQiang
     * @description          修改用户头像
     * @date                 2021/3/25 18:08
     * @param                [param]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @RequestMapping(value = "/update/avatar", method = PUT)
    public DataResult<Object> updateUserAvatar(@RequestBody @Validated SetUpdateAvatarDto param) {
        try {
            param.setUserId(UserUtils.getUser().getUserId());
            this.sysUserInfoService.updateUserAvatar(param);
            return R.success();
        } catch (DefaultException e){
            log.error("设置用户头像出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("设置用户头像出错:{}",e,e);
            return R.error("设置用户头像出错"+ e);
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取用户头像
     * @date                 2021/3/26 14:02
     * @param                [userId, request, response]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @RequestMapping(value = "/get/avatar-image", method = GET)
    public DataResult<Object> getAvatarImageByUserId(@RequestParam("userId") String userId, HttpServletRequest request, HttpServletResponse response) {
		String lastModified = request.getHeader("If-Modified-Since");
		Date lastModifiedTime = null;
		if (lastModified != null) {
			try {
				lastModifiedTime = new Date(lastModified);
			} catch (Exception e) {
				log.error("lastModifiedTime error:" + lastModified);
			}
		}
		try {
//			UserVo user = UserUtils.getUser();
			if (lastModifiedTime != null) {
//				boolean isModified = this.formProcessService.isModifiedForm(key, user.getCompanyId(), lastModifiedTime);
                SysUserInfo user = this.sysUserInfoService.getById(userId);
                if (DataUtil.isNotEmpty(user.getAvatarUpdateTime())) {
                    boolean isModified = lastModifiedTime.getTime() > user.getAvatarUpdateTime().getTime();
                    if (isModified) {
                        response.setStatus(HttpStatus.NOT_MODIFIED.value());
                        return null;
                    }
                }

			}
			response.setHeader("Last-Modified", (new Date()).toString());
            this.sysUserInfoService.getAvatarImageByUserId(userId, request, response);
            return R.success();
        } catch (DefaultException e){
            log.error("设置用户头像出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("设置用户头像出错:{}",e,e);
            return R.error("设置用户头像出错"+ e);
        }
    }

    /**
     *  当前登录用户的下级组织架构
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/30 17:59
     * @param                [userId]
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    @RequestMapping(value = "/get/logn-user/subordinate/structure", method = GET)
    public DataResult<List<SysDepartmentInfoVo>> getUserSubordinateStructure(@RequestParam(value = "userId", required = false) String userId) {
        try {
            List<SysDepartmentInfoVo>  departs = this.sysStaffInfoService.getUserSubordinateStructure(userId);
            return R.success();
        } catch (DefaultException e){
            log.error("设置用户头像出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("设置用户头像出错:{}",e,e);
            return R.error("设置用户头像出错"+ e);
        }
    }

    /**
     *  获取部门直属员工
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/30 17:59
     * @param                [departId]
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    @Override
    @RequestMapping(value = "/get/depart-direct-staff/inner", method = GET)
    public DataResult<List<StaffInfoVo>> getDepartDirectStaffInfo(@RequestParam(value = "departId") String departId) {
        try {
            List<StaffInfoVo>  departs = this.sysStaffInfoService.getDepartDirectStaffInfo(departId);
            return R.success(departs);
        } catch (DefaultException e){
            log.error("设置用户头像出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("设置用户头像出错:{}",e,e);
            return R.error("设置用户头像出错"+ e);
        }
    }

    /**
     *  获取员工类型
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/30 17:59
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    @RequestMapping(value = "/get/user-type", method = GET)
    public DataResult<UserTypeInfoVo> getUserTypeInfo() {
        try {
            UserVo user = UserUtils.getUser();
            UserTypeInfoVo  departs = this.sysUserInfoService.getUserTypeInfo(user);
            return R.success(departs);
        } catch (DefaultException e){
            log.error("获取员工类型出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取员工类型出错:{}",e,e);
            return R.error("获取员工类型出错"+ e);
        }
    }

    /**
     *  获取员工类型
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/30 17:59
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    @Override
    @RequestMapping(value = "/get/user-type/inner", method = GET)
    public DataResult<UserTypeInfoInnerVo> getUsertypeInfoInner(@RequestParam(value = "userId", required = false)String userId,
                                                                         @RequestParam(value = "companyId",required = false)String companyId,
                                                                         @RequestParam(value = "departId", required = false) String departId) {
        try {
            UserVo user = new UserVo();
            user.setUserId(userId);
            user.setUserType(UserTypeInfo.CRM_CEO);
            UserTypeInfoInnerVo innerVo = new UserTypeInfoInnerVo();
            CeoUserInfo ceoUserInfo = this.ceoUserInfoService.getById(userId);
            if (DataUtil.isNotEmpty(ceoUserInfo)) {
                innerVo.setUserType(user.getUserType());
                return R.success(innerVo);
            }
            if (StringUtils.isNotEmpty(userId) && (StringUtils.isEmpty(companyId) || StringUtils.isEmpty(departId))) {
                StaffInfoVo staff  =  this.sysStaffInfoService.getStaffInfo(userId);
                user.setCompanyId(staff.getCompanyId());
                user.setDeptId(staff.getDepartId());
            } else {
                user.setDeptId(departId);
                user.setCompanyId(companyId);
            }
            UserTypeInfoVo  departs = this.sysUserInfoService.getUserTypeInfo(user);
            BeanUtils.copyProperties(departs, innerVo);
            return R.success(innerVo);
        } catch (DefaultException e){
            log.error("获取员工类型出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取员工类型出错:{}",e,e);
            return R.error("获取员工类型出错"+ e);
        }
    }



    @Override
    @RequestMapping(value = "/get-card/inner/{id}",method = GET, produces = "application/json")
    public DataResult<CardUserInfoVo> getCardUserById(@PathVariable String id) {
        CardUserInfoVo cardUserById = cardUserService.getCardUserById(id);
        if(DataUtil.isEmpty(cardUserById)){
            return R.error("用户不存在！");
        }
        return R.success(cardUserById);
    }

    /**
     *   根据CEO用户id获取用户信息；
     */
    @Override
    @RequestMapping(value = "/get-ceo/inner/{id}",method = GET, produces = "application/json")
    public DataResult<com.zerody.user.api.vo.CeoUserInfo> getCeoUserById(@PathVariable String id){
        CeoUserInfo userById = ceoUserInfoService.getUserById(id);
        if(DataUtil.isEmpty(userById)){
            return R.error("用户不存在！");
        }
        com.zerody.user.api.vo.CeoUserInfo ceo=new com.zerody.user.api.vo.CeoUserInfo();
        BeanUtils.copyProperties(userById,ceo);
        return R.success(ceo);
    }


    /**
     * 据CEO用户id获取关联企业；
     * @author  DaBai
     * @date  2022/6/20 10:26
     */
    @Override
    @RequestMapping(value = "/get-ceo-company/inner",method = GET, produces = "application/json")
    public DataResult<com.zerody.user.api.vo.CeoRefVo> getCeoCompanyById(@RequestParam String id){
        CeoRefVo ceoRef = ceoCompanyRefService.getCeoRef(id);
        com.zerody.user.api.vo.CeoRefVo vo=new com.zerody.user.api.vo.CeoRefVo();
        BeanUtils.copyProperties(ceoRef,vo);
        if(DataUtil.isEmpty(vo.getCompanys())){
            com.zerody.user.api.vo.CompanyRefVo companyRefVo=new com.zerody.user.api.vo.CompanyRefVo();
            companyRefVo.setId("NOT_COMPANY");
            companyRefVo.setCompanyName("NOT_COMPANY");
            vo.getCompanys().add(companyRefVo);
        }
        return R.success(vo);
    }

    /**
     * 据后台管理员用户id获取关联企业；
     * @author  DaBai
     * @date  2022/6/20 10:26
     */
    @Override
    @RequestMapping(value = "/get-back-company/inner",method = GET, produces = "application/json")
    public DataResult<com.zerody.user.api.vo.BackUserRefVo> getBackCompanyById(@RequestParam String id){
        BackUserRefVo backRef = ceoCompanyRefService.getBackRef(id);
        com.zerody.user.api.vo.BackUserRefVo vo=new com.zerody.user.api.vo.BackUserRefVo();
        BeanUtils.copyProperties(backRef,vo);
        if(DataUtil.isEmpty(vo.getCompanys())){
            com.zerody.user.api.vo.CompanyRefVo companyRefVo=new com.zerody.user.api.vo.CompanyRefVo();
            companyRefVo.setId("NOT_COMPANY");
            companyRefVo.setCompanyName("NOT_COMPANY");
            vo.getCompanys().add(companyRefVo);
        }
        return R.success(vo);
    }

    /**
    *   根据名片用户id获取用户信息
    */
    @Override
    @RequestMapping(value = "/get-by-card/inner/{id}",method = GET, produces = "application/json")
    public DataResult<StaffInfoVo> getUserByCardUserId(@PathVariable(value = "id") String id){
        StaffInfoVo userByCardUserId = sysUserInfoService.getUserByCardUserId(id);
        if(DataUtil.isEmpty(userByCardUserId)){
            return R.error("用户无关联信息！");
        }
        return R.success(userByCardUserId);
    }

    @Override
    public DataResult<List<StaffInfoVo>> getUserByPositionId(String s) {
        return null;
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          查询下级用户
     * @date                 2021/7/13 11:13
     * @param                []
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SubordinateUserQueryVo>
     */
    @GetMapping("/subordinate/all")
    public DataResult<List<SubordinateUserQueryVo>> getSubordinateUser(SubordinateUserQueryDto param) {
        try {
            // SubordinateUserQueryDto param = new SubordinateUserQueryDto();
            UserVo userVo = UserUtils.getUser();
            param.setDepartId(UserUtils.getUser().getDeptId());
            param.setCompanyId(UserUtils.getUser().getCompanyId());
            param.setUserId(UserUtils.getUser().getUserId());
            param.setIsCEO(UserUtils.getUser().isCEO());
            // param.setIsShowLeave(isShowLeave);
            List<SubordinateUserQueryVo> result = this.sysUserInfoService.getSubordinateUser(param);
            return R.success(result);
        } catch (DefaultException e){
            log.error("获取下级用户出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取下级用户出错:{}",e,e);
            return R.error("获取下级用户出错"+ e);
        }
    }

//    /**
//     *
//     *
//     * @author               PengQiang
//     * @description          查询下级用户
//     * @date                 2021/7/13 11:13
//     * @param                []
//     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SubordinateUserQueryVo>
//     */
//    @GetMapping("/subordinate/all")
//    public DataResult<List<SubordinateUserQueryVo>> getSubordinateUserPartner() {
//        try {
//            SubordinateUserQueryDto param = new SubordinateUserQueryDto();
//            param.setUserId(UserUtils.getUser().getUserId());
//            param.setDepartId(UserUtils.getUser().getDeptId());
//            param.setCompanyId(UserUtils.getUser().getCompanyId());
//            List<SubordinateUserQueryVo> result = this.sysUserInfoService.getSubordinateUserPartner(param);
//            return R.success(result);
//        } catch (DefaultException e){
//            log.error("获取下级用户出错:{}",e,e);
//            return R.error(e.getMessage());
//        }  catch (Exception e) {
//            log.error("获取下级用户出错:{}",e,e);
//            return R.error("获取下级用户出错"+ e);
//        }
//    }

    /**
     * 查询用户信息
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/28 17:23
     * @param                userId
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @Override
    @RequestMapping(value = "/get/batch-staff-info/inner", method = RequestMethod.GET)
    public DataResult<List<StaffInfoVo>> getStaffInfoByIds(@RequestParam(value = "userIds", required = false) List<String> userId){
        try {
            return R.success(this.sysStaffInfoService.getStaffInfoByIds(userId));
        } catch (DefaultException e){
            log.error("获取员工信息失败:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取员工信息失败:{}",e,e);
            return R.error("获取员工信息失败"+ e);
        }

    }

    /**
     * 批量查询用户信息
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/28 17:23
     * @param                userId
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @RequestMapping(value = "/get/batch-staff-info", method = POST)
    public DataResult<List<StaffInfoVo>> getBatchStaffInfoByIds(@RequestBody List<String> userId){
        try {
            if (Collections.isEmpty(userId)) {
                return R.success(new ArrayList<>());
            }
            return R.success(this.sysStaffInfoService.getStaffInfoByIds(userId));
        } catch (DefaultException e){
            log.error("获取员工信息失败:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取员工信息失败:{}",e,e);
            return R.error("获取员工信息失败"+ e);
        }

    }
    /**
     * 查询用户信息
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/28 17:23
     * @param                userId
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @Override
    @RequestMapping(value = "/get/subordinate-user-id/inner", method = RequestMethod.GET)
    public DataResult<List<String>> getSubordinateUserByUserId(@RequestParam("userId") String userId){
        try {
            return R.success(this.sysStaffInfoService.getSubordinateUserByUserId(userId));
        } catch (DefaultException e){
            log.error("获取员工信息失败:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取员工信息失败:{}",e,e);
            return R.error("获取员工信息失败"+ e);
        }
    }


    /**
     *   当前登录用户为CEO 或 后台登录 为 true
     *   当前登录为企业管理员为 true
     *   指定用户没有部门 当前登录用户不是企业管理员 为 false
     *   当前登录用户为部门管理员 且指定用户 在当前登录用户的部门下(包含下级部门) 为 true
     *   其他均为 false
     * @author               PengQiang
     * @description          当前登录用户是否是指定用户上级
     * @date                 2021/12/24 9:47
     * @param                [userId]
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<java.lang.String>>
     */
    @GetMapping("/get/login-user/is-superior")
    public DataResult<Boolean> getLoginUserIsSuperion(@RequestParam("userId") String userId){
        try {
            UserVo user = UserUtils.getUser();
            Boolean isSuperion = this.sysStaffInfoService.getLoginUserIsSuperion(user, userId);
            return R.success(isSuperion);
        } catch (DefaultException e){
            log.error("当前登录用户是否是上级失败:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("当前登录用户是否是上级失败:{}",e,e);
            return R.error("获取当前登录用户是否是上级失败!请求异常");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          查询所有在职用户或ceo
     * @date                 2022/4/15 12:02
     * @param                []
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<java.lang.String>>
     */
    @Override
    @GetMapping("/get-ids/all-be/user-ceo/inner")
    public DataResult<List<String>> getAllBeUserOrceoIdsInner() {
        try {
            return R.success(this.sysUserInfoService.getAllBeUserOrceoIdsInner());
        } catch (DefaultException e){
            log.error("查询所有的在职用户或总裁错误:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("查询所有的在职用户或总裁错误:{}",e,e);
            return R.error("查询所有的在职用户或总裁错误!请求异常");
        }
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          修改im状态
     * @date                 2022/4/28 13:31
     * @param                param
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Void>
     */
    @PutMapping("/update/im-state")
    public DataResult<Void> updateImState(@RequestBody @Validated UserImStateUpdateDto param) {
        try {
            this.sysUserInfoService.updateImState(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("修改im状态出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改im状态出错:{}", e, e);
            return R.error("修改状态出错!请联系管理员");
        }
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          获取所有用户(在职) im搜索
     * @date                 2022/4/29 16:08
     * @param                [param]
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BosStaffInfoVo>>
     */
    @GetMapping("/page/get/system-all")
    public DataResult<IPage<BosStaffInfoVo>>  getPgaeSystemAllUser(SysStaffInfoPageDto param) {
        try {
            IPage<BosStaffInfoVo> iPage = this.sysUserInfoService.getPgaeSystemAllUser(param);
            return R.success(iPage);
        } catch (DefaultException e) {
            log.error("查询系统所有用户出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询系统所有用户出错：{}", e, e);
            return R.error("获取所有用户异常!请联系管理员");
        }
    }

    /**
     *
     * 根据用户id获取用户设备
     * @author               PengQiang
     * @description          DELL
     * @date                 2022/6/5 10:07
     * @param                [ids]
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<java.lang.String>>
     */
    @GetMapping("/get/user-identifier/by-ids/inner")
    public DataResult<List<UserIdentifierQueryVo>> getUserIdentifierByIds(@RequestParam("ids") List<String> ids) {
        try {
            List<UserIdentifierQueryVo> result = this.sysUserInfoService.getUserIdentifierByIds(ids);
            return R.success(result);
        } catch (DefaultException e) {
            log.info("获取用户设备信息异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.info("获取用户设备信息异常:{}", e, e);
            return R.error("获取用户设备信息");
        }
    }


    /**
     *
     *
     * @author               luolujin
     * @description          根据手机号码查询在职用户id
     * @date                 2022/6/6
     * @param
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @Override
    @GetMapping("/get/by-mobile")
    public DataResult<StaffInfoVo> getUserInfoByMobile(@RequestParam("mobile") String mobile){
        try {
            if(StringUtils.isEmpty(mobile)){
                R.error("电话号码不能为空！");
            }
            return R.success(this.sysUserInfoService.getUserInfoByMobile(mobile));
        } catch (DefaultException e) {
            log.error("查询用户信息出错：{}", e, e);
            return R.error(e.getMessage());
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取上级 不包含企业管理员
     * @date                 2022/6/22 9:53
     * @param                []
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.api.vo.StaffInfoVo>
     */
    @GetMapping("/get/superior/not-company-admin/{id}")
    public DataResult<StaffInfoVo> getSuperiorNotCompanyAdmin(@PathVariable(value = "id") String userId) {
        try {
            StaffInfoVo staffInfoVo = this.sysUserInfoService.getSuperiorNotCompanyAdmin(userId);
            if (DataUtil.isNotEmpty(staffInfoVo)) {
                staffInfoVo.setIdentityCard(null);
                staffInfoVo.setMobile(null);
            }
            return R.success(staffInfoVo);
        } catch (DefaultException e) {
            log.error("获取上级-不包含企业管理员出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取上级-不包含企业管理员出错:{}", e, e);
            return R.error("获取上级出错!请联系管理员");
        }
    }

    /**
     * @author kuang
     * @description 查询上级所有人
     * @date  2022-7-18
     **/
    @GetMapping("/get/superior/all")
    public DataResult<List<SubordinateUserQueryVo>> getSuperiorList() {
        try {
            List<SubordinateUserQueryVo> getSuperiorList = this.sysUserInfoService.getSuperiorList(UserUtils.getUser());
            return R.success(getSuperiorList);
        } catch (DefaultException e) {
            log.error("获取所有上级出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取所有上级出错:{}", e, e);
            return R.error("获取所有上级出错!请联系管理员");
        }
    }


    /**
     * @author kuang
     * @description 账户注销
     * @date  2022/08/08
     * @Param
     **/
    @PostMapping("/logout")
    public DataResult<Object> doLogout(){
        try {
            if(UserUtils.getUser() == null) {
                return R.error("请先进行登录");
            }
            this.sysUserInfoService.doLogout(UserUtils.getUserId());
            return R.success();
        } catch (DefaultException e){
            log.error("注销账户出错:{}",UserUtils.getUser(),e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("注销账户出错:{} ",UserUtils.getUser(),e);
            return R.error("注销账户出错,请求异常");
        }
    }

}
