package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.dto.LoginCheckParamDto;
import com.zerody.user.api.service.UserRemoteService;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.enums.LoginTypeEnum;
import com.zerody.user.service.CompanyAdminService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.vo.CheckLoginVo;
import com.zerody.user.vo.SysLoginUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author PengQiang
 * @ClassName SysUserInfoController
 * @DateTime 2020/12/16_17:10
 * @Deacription TODO
 */
@Slf4j
@RequestMapping("/sysUserInfo")
@RestController
public class SysUserInfoController implements UserRemoteService {

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private CompanyAdminService amdinService;
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
        if(LoginTypeEnum.BACK_PC.getCode().equals(params.getType())){
            //管理员登录需要去校验管理员表
            CompanyAdmin admin = amdinService.getAdminByStaffId(checkLoginVo.getStaffId());
            if(DataUtil.isEmpty(admin)){
                return R.error("当前账号未开通，请联系管理员开通！");
            }
        }
        //校验密码
        boolean success = sysUserInfoService.checkPassword(checkLoginVo.getUserPwd(), params.getPwd());
        if(success){
            return R.success("密码正确！");
        }else {
            return R.error("密码错误！");
        }
    }

    /**
    *   获取用户信息；构建token基础信息
    */
    @Override
    @RequestMapping(value = "/user-info/inner",method = GET, produces = "application/json")
    public DataResult getUserInfo(@RequestParam("userName") String userName){
        SysLoginUserInfoVo sysLoginUserInfoVo=sysUserInfoService.getUserInfo(userName);
        return R.success(sysLoginUserInfoVo);
    }

    /**
    *   修改用户表
    */
    @Override
    public DataResult updateById(com.zerody.user.api.vo.SysUserInfo sysUserInfo) {
        SysUserInfo newInfo=new SysUserInfo();
        DataUtil.getKeyAndValue(newInfo,sysUserInfo);
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
    

}
