package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.pojo.SysDepartmentInfo;
import com.zerody.user.service.SysDepartmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoController
 * @DateTime 2020/12/19_13:18
 * @Deacription TODO
 */
@RestController
@RequestMapping("/departmentInfo")
public class SysDepartmentInfoController {

    @Autowired
    private SysDepartmentInfoService sysDepartmentInfoService;

    //分页查询部门
    @GetMapping("/getPageDepartment")
    public DataResult getPageDepartment(SysDepartmentInfoDto sysDepartmentInfoDto){
        return sysDepartmentInfoService.getPageDepartment(sysDepartmentInfoDto);
    }

    //修改部门登录状态
    @PostMapping("/updateDepartmentStatus")
    public DataResult updateDepartmentStatus(String depId, Integer loginStauts){

        return sysDepartmentInfoService.updateDepartmentStatus(depId, loginStauts);
    }

    //添加部门
    @PostMapping("/addDepartment")
    public DataResult addDepartment(@Validated @RequestBody SysDepartmentInfo sysDepartmentInfo){
        return sysDepartmentInfoService.addDepartment(sysDepartmentInfo);
    }

    //修改部门
    @PostMapping("/updateDepartment")
    public DataResult updateDepartment(@Validated @RequestBody SysDepartmentInfo sysDepartmentInfo){
        return sysDepartmentInfoService.updateDepartment(sysDepartmentInfo);
    }

    //删除部门
    @DeleteMapping("/deleteDepartmentById")
    public DataResult deleteDepartmentById(String depId){
        return sysDepartmentInfoService.deleteDepartmentById(depId);
    }
}
