package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.api.dto.LoginCheckParamDto;
import com.zerody.user.api.dto.SysUserSubordindatePageDto;
import com.zerody.user.api.service.UserRemoteService;
import com.zerody.user.api.vo.SysUserSubordinateVo;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.vo.SysLoginUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
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
@RequestMapping("/sysUserInfo")
@RestController
public class SysUserInfoController implements UserRemoteService {

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

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
        String pwd=sysUserInfoService.checkLoginUser(params.getUserName());
        if(StringUtils.isEmpty(pwd)){
            return R.error("当前账号未开通，请联系管理员开通！");
        }
        //校验密码
        boolean success = sysUserInfoService.checkPassword(pwd, params.getPwd());
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


    /**
     *  获取员工下级员工
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/6 20:07
     * @param                [dto]
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.api.vo.SysUserSubordinateVo>>
     */
    @Override
    @RequestMapping("/subordinate/page/inner")
    public DataResult<IPage<SysUserSubordinateVo>> getUserSubordinates(SysUserSubordindatePageDto dto){
        return R.success(sysUserInfoService.getUserSubordinates(dto));
    }


	@Override
	public DataResult<UserDeptVo> getUserDeptVo(String userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public DataResult<List<String>> getUserSubordinates(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}
