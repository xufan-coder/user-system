package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysAuthRoleDto;
import com.zerody.user.pojo.SysAuthRoleInfo;
import com.zerody.user.service.SysAuthRoleInfoService;
import com.zerody.user.dto.SysAuthRolePageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysAuthRoleInfoController
 * @DateTime 2020/12/17_18:18
 * @Deacription TODO
 */
@RestController
@RequestMapping("/sysAuthRole")
public class SysAuthRoleInfoController {

    @Autowired
    private SysAuthRoleInfoService sysAuthRoleInfoService;


    //分页查询角色
    @GetMapping("/selectRolePage")
    public DataResult selectRolePage(SysAuthRolePageDto sysAuthRolePageDto){
        return sysAuthRoleInfoService.selectRolePage(sysAuthRolePageDto);
    }

    //添加角色
    @PostMapping("/addRole")
    public DataResult addRole(@Validated @RequestBody SysAuthRoleInfo sysAuthRoleInfo){

        return sysAuthRoleInfoService.addRole(sysAuthRoleInfo);
    }

    //修改角色信息
    @PostMapping("/updateRole")
    public DataResult updateRole(@Validated @RequestBody SysAuthRoleInfo sysAuthRoleInfo){

        return sysAuthRoleInfoService.updateRole(sysAuthRoleInfo);
    }

    //根据角色id删除角色
    @DeleteMapping("/deleteRoleById")
    public DataResult deleteRoleById(String roleId){
        return sysAuthRoleInfoService.deleteRoleById(roleId);
    }


    //批量删除角色
    @DeleteMapping("/deleteBatchRoleByIds")
    public DataResult deleteBatchRoleByIds(List<String> roleIds){

        return sysAuthRoleInfoService.deleteBatchByIdds(roleIds);
    }

    //删除角色下的一些用户
    @DeleteMapping("/deleteRoleDownStaff")
    public DataResult deleteRoleDownStaff(@RequestBody SysAuthRoleDto sysAuthRoleDto){

        return sysAuthRoleInfoService.deleteRoleDownStaff(sysAuthRoleDto);
    }

    //根据角色查用户
    @GetMapping("/selectRoleByStaffId")
    public DataResult selectRoleByStaffId(String staffId){

        return sysAuthRoleInfoService.selectRoleByStaffId(staffId);
    }


    //操作角色拥有菜单
    @PostMapping("/operationRoleDownMenu")
    public DataResult operationRoleDownMenu(@RequestBody SysAuthRoleDto sysAuthRoleDto){
        return  sysAuthRoleInfoService.operationRoleDownMenu(sysAuthRoleDto);
    }

}
