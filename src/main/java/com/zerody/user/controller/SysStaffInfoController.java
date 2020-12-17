package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
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

    @PostMapping("/staffAddRole")
    public DataResult staffAddRole(String staffId, String roleId){

        return sysStaffInfoService.staffAddRole(staffId,roleId);
    }

    @GetMapping("/selectStaffByRoleId")
    public DataResult selectStaffByRoleId(String roleId){

        return sysStaffInfoService.selectStaffByRoleId(roleId);
    }
}
