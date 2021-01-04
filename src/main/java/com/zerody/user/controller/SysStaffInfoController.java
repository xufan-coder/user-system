package com.zerody.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.service.SysStaffInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoController
 * @DateTime 2020/12/17_19:26
 * @Deacription TODO
 */
@Slf4j
@RestController
public class SysStaffInfoController {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    //删除员工下的角色
    @RequestMapping(value = "/staffInfo/role", method = RequestMethod.DELETE)
    public DataResult deleteStaffRole(String staffId, String roleId){
        try {
            return R.success();
        } catch (DefaultException e){
            log.error("企业删除错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
//        return  sysStaffInfoService.deleteStaffRole(staffId, roleId);
    }

    //给员工添加角色
    @RequestMapping(value = "/staffInfo/role", method = RequestMethod.POST)
    public DataResult staffAddRole(@RequestBody JSONObject param){
        try {
            return R.success();
        } catch (DefaultException e){
            log.error("企业删除错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
//        String staffId = param.getString("staffId");
//        String roleId = param.getString("roleId");
//        return sysStaffInfoService.staffAddRole(staffId,roleId);
    }

    //分页查当前角色下员工
    @RequestMapping(value = "/staffInfo/role", method = RequestMethod.GET)
    public DataResult selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto){

//        return sysStaffInfoService.selectPageStaffByRoleId(sysStaffInfoPageDto);
        return R.success();
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

//        return sysStaffInfoService.getPageAllStaff(sysStaffInfoPageDto);
        return R.success();
    }

    //添加员工
    @RequestMapping(value = "/staffInfo", method = RequestMethod.POST)
    public DataResult addStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){
        try {
            return R.success();
        } catch (DefaultException e){
            log.error("企业删除错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
//        return sysStaffInfoService.addStaff(setSysUserInfoDto);
    }

    //修改员工状态
    @RequestMapping(value = "/staffInfo/loginStatus/{id}/{status}", method =  RequestMethod.PUT)
    public DataResult updateStaffStatus(@PathVariable(name = "id") String staffId, @PathVariable(name = "status") Integer status){
        try {
            return R.success();
        } catch (DefaultException e){
            log.error("企业删除错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
//        return sysStaffInfoService.updateStaffStatus(staffId, status);
    }

    //修改员工
    @RequestMapping(value = "/staffInfo", method = RequestMethod.PUT)
    public DataResult updateStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){
        try {
            return R.success();
        } catch (DefaultException e){
            log.error("企业删除错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
//        return sysStaffInfoService.updateStaff(setSysUserInfoDto);
    }

    //根据员工id查询员工信息
    @GetMapping("/staffInfo/{id}")
    public DataResult selectStaffById(@PathVariable(name = "id") String staffId){

//        return sysStaffInfoService.selectStaffById(staffId);
        return R.success();
    }


    //批量删除员工
    @RequestMapping(value = "/staffInfo", method = RequestMethod.DELETE)
    public DataResult batchDeleteStaff(@RequestBody  List<String> staffIds){
//        return sysStaffInfoService.batchDeleteStaff(staffIds);
        return R.success();
    }


    //根据员工id单独删除员工
    @RequestMapping(value = "/staffInfo/{id}", method = RequestMethod.DELETE)
    public DataResult deleteStaffById(@PathVariable(name = "id") String staffId){
        try {
            return R.success();
        } catch (DefaultException e){
            log.error("企业删除错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
//        return sysStaffInfoService.deleteStaffById(staffId);

    }

    //批量导入用户excel
    @RequestMapping("batchImportUser")
    public DataResult<Object> batchImportUser(MultipartFile file){
        try {
            return R.success(sysStaffInfoService.batchImportStaff(file));
        } catch (DefaultException e){
            log.error("企业删除错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
    }


}
