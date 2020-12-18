package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.service.SysStaffInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public DataResult staffAddRole(String staffId, String roleId){

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

}
