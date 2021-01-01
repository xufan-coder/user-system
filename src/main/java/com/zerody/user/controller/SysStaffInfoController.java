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
public class SysStaffInfoController {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    //删除员工下的角色
    @RequestMapping(value = "/staffInfo/role", method = RequestMethod.DELETE)
    public DataResult deleteStaffRole(String staffId, String roleId){

        return  sysStaffInfoService.deleteStaffRole(staffId, roleId);
    }

    //给员工添加角色
    @RequestMapping(value = "/staffInfo/role", method = RequestMethod.POST)
    public DataResult staffAddRole(@RequestBody JSONObject param){
        String staffId = param.getString("staffId");
        String roleId = param.getString("roleId");
        return sysStaffInfoService.staffAddRole(staffId,roleId);
    }

    //分页查当前角色下员工
    @RequestMapping(value = "/staffInfo/role", method = RequestMethod.GET)
    public DataResult selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto){

        return sysStaffInfoService.selectPageStaffByRoleId(sysStaffInfoPageDto);
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          分页查询所有员工信息
     * @date                 2021/1/1 12:38
     * @param                sysStaffInfoPageDto
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/staffInfo", method = RequestMethod.GET)
    public DataResult getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto){

        return sysStaffInfoService.getPageAllStaff(sysStaffInfoPageDto);
    }

    //添加员工
    @RequestMapping(value = "/staffInfo", method = RequestMethod.POST)
    public DataResult addStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){

        return sysStaffInfoService.addStaff(setSysUserInfoDto);
    }

    //修改员工状态
    @RequestMapping(value = "/staffInfo/loginStatus/{id}/{status}", method =  RequestMethod.PUT)
    public DataResult updateStaffStatus(@PathVariable(name = "id") String staffId, @PathVariable(name = "status") Integer status){

        return sysStaffInfoService.updateStaffStatus(staffId, status);
    }

    //修改员工
    @RequestMapping(value = "/staffInfo", method = RequestMethod.PUT)
    public DataResult updateStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){

        return sysStaffInfoService.updateStaff(setSysUserInfoDto);
    }

    //根据员工id查询员工信息
    @GetMapping("/staffInfo/{id}")
    public DataResult selectStaffById(@PathVariable(name = "id") String staffId){

        return sysStaffInfoService.selectStaffById(staffId);
    }


    //批量删除员工
    @RequestMapping(value = "/staffInfo", method = RequestMethod.DELETE)
    public DataResult batchDeleteStaff(@RequestBody  List<String> staffIds){
        return sysStaffInfoService.batchDeleteStaff(staffIds);
    }

    //根据员工id单独删除员工
    @RequestMapping(value = "/staffInfo/{id}", method = RequestMethod.DELETE)
    public DataResult deleteStaffById(@PathVariable(name = "id") String staffId){
        return sysStaffInfoService.deleteStaffById(staffId);
    }



}
