package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.service.SysUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public DataResult deleteUserBatchByIds(List<Integer> ids){

        return sysUserInfoService.deleteUserBatchByIds(ids);
    }

    @RequestMapping("/selectUserPage")
    public DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto){

        return sysUserInfoService.selectUserPage(sysUserInfoPageDto);
    }

}
