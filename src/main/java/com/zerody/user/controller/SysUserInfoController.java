package com.zerody.user.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import com.zerody.user.check.CheckUser;
import com.zerody.user.dto.UserPerformanceReviewsPageDto;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.*;
import javafx.geometry.Pos;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.zerody.user.api.vo.AdminUserInfo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.api.vo.CardUserInfoVo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.service.AdminUserService;
import com.zerody.user.service.CardUserService;
import com.zerody.user.service.CompanyAdminService;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

/**
 * @author PengQiang
 * @ClassName SysUserInfoController
 * @DateTime 2020/12/16_17:10
 * @Deacription TODO
 */
@Slf4j
@RequestMapping("/sys-user-info")
@RestController
public class SysUserInfoController implements UserRemoteService {

    @Autowired
    private CheckUtil checkUtil;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private CompanyAdminService amdinService;

    @Autowired
    private CardUserService cardUserService;

    @Autowired
    private AdminUserService amdinUserService;

    @Autowired
    private SysCompanyInfoService sysCompanyInfoService;
    /**
    *    根据用户ID查询单个用户
    */
    @GetMapping("/get/{id}")
    public DataResult getUserInfoById(@PathVariable String id){
        return R.success(sysUserInfoService.getUserInfoById(id));
    }


    //添加用户
    @PostMapping("/addUser")
    public com.zerody.common.bean.DataResult addUser(@Validated @RequestBody SysUserInfo userInfo){

        return sysUserInfoService.addUser(userInfo);
    }

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
        //校验密码
        boolean success = sysUserInfoService.checkPassword(checkLoginVo.getUserPwd(), params.getPwd());
        if(!success){
            return R.error("密码错误！");
        }
        return R.success("密码正确！");
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
        SysLoginUserInfoVo sysLoginUserInfoVo=sysUserInfoService.getUserInfo(userName);
        if(DataUtil.isEmpty(sysLoginUserInfoVo)){
            return R.error("当前账号未开通，请联系管理员开通！");
        }
        com.zerody.user.api.vo.SysLoginUserInfoVo info =new com.zerody.user.api.vo.SysLoginUserInfoVo();
        BeanUtils.copyProperties(sysLoginUserInfoVo,info);
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
            log.error("获取员工下级线索汇总信息出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取员工下级线索汇总信息出错:{}", e, e);
            return R.error("获取员工下级线索汇总信息出错！请求异常");
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
            log.error("修改名片用户出错:{}", e, e);
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
    public DataResult<List<com.zerody.user.vo.SysUserInfoVo>> getUserByDepartOrRole(@RequestParam(value = "departId", required = false)String departId,
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
    public DataResult<List<com.zerody.user.vo.SysUserInfoVo>> getSuperiorUesrByUserAndRole(@RequestParam("userId")String userId,
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

    /*************************************************1.1版本api接口*******************************************************************/


    @RequestMapping(value = "/get/page/performance-reviews/collect", method = GET)
    public DataResult<IPage<UserPerformanceReviewsVo>> getPagePerformanceReviews(UserPerformanceReviewsPageDto param){
        try {
            checkUtil.SetUserPositionInfo(param);
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
    public DataResult<Object> doPerformanceReviewsExport(UserPerformanceReviewsPageDto param, HttpServletResponse res){
        try {
            checkUtil.SetUserPositionInfo(param);
            param.setCurrent(1);
            param.setPageSize(10000);
            sysStaffInfoService.doPerformanceReviewsExport(param, res);
            return R.success();
        } catch (DefaultException e){
            log.error("导出业绩总结列表出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("导出业绩总结列表出错:{}",e,e);
            return R.error("导出业绩总结列表出错"+ e);
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
    public DataResult<String> getShowPerformancePassword(@RequestParam("mobile")String mobile){
        try {
            String pass = this.sysUserInfoService.getShowPerformancePassword(mobile);
            return R.success(pass);
        } catch (DefaultException e){
            log.error("获取业绩密码出错:{}",e,e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取业绩密码出错:{}",e,e);
            return R.error("获取业绩密码出错"+ e);
        }
    }

}
