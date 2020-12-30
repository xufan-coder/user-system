package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.common.bean.R;
import com.zerody.common.util.MD5Utils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.dto.LoginCheckParamDto;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.vo.SysLoginUserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class SysUserInfoController {

    @Autowired
    private SysUserInfoService sysUserInfoService;



    //添加用户
    @PostMapping("/addUser")
    public DataResult addUser(@Validated @RequestBody SysUserInfo userInfo){

        return sysUserInfoService.addUser(userInfo);
    }

    //修改用户
    @PostMapping("/updateUser")
    public DataResult updateUser(@Validated @RequestBody SysUserInfo userInfo){

        return  sysUserInfoService.updateUser(userInfo);
    }

    //根据id删除用户
    @DeleteMapping("/deleteUserById")
    public DataResult deleteUserById(String userId){

        return sysUserInfoService.deleteUserById(userId);
    }

    //根据用户id批量删除用户
    @DeleteMapping("/deleteUserBatchByIds")
    public DataResult deleteUserBatchByIds(List<String> ids){

        return sysUserInfoService.deleteUserBatchByIds(ids);
    }

    //分页查询用户
    @RequestMapping("/selectUserPage")
    public DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto){

        return sysUserInfoService.selectUserPage(sysUserInfoPageDto);
    }

    //批量导入用户excel
    @RequestMapping("batchImportUser")
    public DataResult batchImportUser(MultipartFile file){

        return sysUserInfoService.batchImportUser(file);
    }


    /**
    *   登录校验账户和密码
    */
    @RequestMapping(value = "/check-user/inner",method = POST, produces = "application/json")
    public DataResult checkLoginUser(@RequestBody LoginCheckParamDto params){
        //查询账户信息
        SysLoginUserInfoVo sysLoginUserInfoVo=sysUserInfoService.getUserInfo(params.getUserName());
        if(DataUtil.isEmpty(sysLoginUserInfoVo)){
            return R.error("当前账号未开通，请联系管理员开通！");
        }
        //校验密码
        boolean success = sysUserInfoService.checkPassword(sysLoginUserInfoVo.getUserPwd(), params.getPwd());
        if(success){
            return R.success("密码正确！");
        }else {
            return R.error("密码错误！");
        }
    }

    /**
    *   获取用户信息；登录获取信息使用;
    */
    @RequestMapping(value = "/user-info/inner",method = GET, produces = "application/json")
    public DataResult getUserInfo(@RequestParam("userName") String userName){
        SysLoginUserInfoVo sysLoginUserInfoVo=sysUserInfoService.getUserInfo(userName);
        return R.success(sysLoginUserInfoVo);
    }

    /**
     *   根据用户id获取用户信息；
     */
    @RequestMapping(value = "/get/inner/{id}",method = GET, produces = "application/json")
    public DataResult getUserById(@PathVariable String id){
        SysUserInfo sysUserInfo=sysUserInfoService.getUserById(id);
        if(DataUtil.isEmpty(sysUserInfo)){
            return R.error("用户不存在！");
        }
        return R.success(sysUserInfo);
    }
}
