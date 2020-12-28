package com.zerody.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.service.SysStaffInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoController
 * @DateTime 2020/12/17_19:26
 * @Deacription TODO
 */
@RestController
@RequestMapping("/staffInfo")
public class SysStaffInfoController {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    //删除员工下的角色
    @DeleteMapping("/deleteStaffRole")
    public DataResult deleteStaffRole(String staffId, String roleId){

        return  sysStaffInfoService.deleteStaffRole(staffId, roleId);
    }

    //给员工添加角色
    @PostMapping("/staffAddRole")
    public DataResult staffAddRole(@RequestBody JSONObject param){
        String staffId = param.getString("staffId");
        String roleId = param.getString("roleId");
        return sysStaffInfoService.staffAddRole(staffId,roleId);
    }

    //分页查当前角色下员工
    @GetMapping("/getPageStaffByRoleId")
    public DataResult selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto){

        return sysStaffInfoService.selectPageStaffByRoleId(sysStaffInfoPageDto);
    }

    //分页查询所有员工信息
    @GetMapping("/getPageAllStaff")
    public DataResult getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto){

        return sysStaffInfoService.getPageAllStaff(sysStaffInfoPageDto);
    }

    //添加员工
    @PostMapping("/addStaff")
    public DataResult addStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){

        return sysStaffInfoService.addStaff(setSysUserInfoDto);
    }

    //修改员工状态
    @PostMapping("/updateStaffStatus")
    public DataResult updateStaffStatus(String staffId, Integer status){

        return sysStaffInfoService.updateStaffStatus(staffId, status);
    }
    //修改员工
    @PostMapping("/updateStaff")
    public DataResult updateStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){

        return sysStaffInfoService.updateStaff(setSysUserInfoDto);
    }

    //根据员工id查询员工信息
    @GetMapping("/selectStaffById")
    public DataResult selectStaffById(String staffId){

        return sysStaffInfoService.selectStaffById(staffId);
    }


    //批量删除员工
    @DeleteMapping("/batchDeleteStaff")
    public DataResult batchDeleteStaff(@RequestBody  List<String> staffIds){
        return sysStaffInfoService.batchDeleteStaff(staffIds);
    }

    //根据员工id单独删除员工
    @DeleteMapping("deleteStaffById")
    public DataResult deleteStaffById(String staffId){
        return sysStaffInfoService.deleteStaffById(staffId);
    }


//    //查询系统管理员
//    @GetMapping("/getSys")
}
