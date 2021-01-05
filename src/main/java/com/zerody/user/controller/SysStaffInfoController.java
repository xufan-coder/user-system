package com.zerody.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;
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
@RequestMapping("/staff-info")
public class SysStaffInfoController {

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    //删除员工下的角色
    @RequestMapping(value = "/role", method = RequestMethod.DELETE)
    public DataResult<Object> deleteStaffRole(String staffId, String roleId){
        try {
            sysStaffInfoService.deleteStaffRole(staffId, roleId);
            return R.success();
        } catch (DefaultException e){
            log.error("删除员工角色错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("删除员工角色错误:{}",e.getMessage());
            return R.error("删除员工角色失败,请求异常");
        }
    }

    //给员工添加角色
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public DataResult<Object> staffAddRole(@RequestBody JSONObject param){
        try {
            String staffId = param.getString("staffId");
            String roleId = param.getString("roleId");
            sysStaffInfoService.staffAddRole(staffId,roleId);
            return R.success();
        } catch (DefaultException e){
            log.error("员工添加角色错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("员工添加角色错误:{}",e.getMessage());
            return R.error("员工添加角色失败,请求异常");
        }

    }

    //分页查当前角色下员工
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public DataResult<IPage<SysUserInfoVo>> selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto){
        return R.success(sysStaffInfoService.selectPageStaffByRoleId(sysStaffInfoPageDto));
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
    @RequestMapping(value = "", method = RequestMethod.GET)
    public DataResult<IPage<SysUserInfoVo>> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto){

        return R.success(sysStaffInfoService.getPageAllStaff(sysStaffInfoPageDto));
    }

    //添加员工
    @RequestMapping(value = "", method = RequestMethod.POST)
    public DataResult<Object> addStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){
        try {
            sysStaffInfoService.addStaff(setSysUserInfoDto);
            return R.success();
        } catch (DefaultException e){
            log.error("添加员工错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("添加员工错误:{}",e.getMessage());
            return R.error("添加员工失败,请求异常");
        }
    }

    //修改员工状态
    @RequestMapping(value = "/loginStatus/{id}/{status}", method =  RequestMethod.PUT)
    public DataResult<Object> updateStaffStatus(@PathVariable(name = "id") String staffId, @PathVariable(name = "status") Integer status){
        try {
            sysStaffInfoService.updateStaffStatus(staffId, status);
            return R.success();
        } catch (DefaultException e){
            log.error("修改员工状态错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("修改员工状态错误:{}",e.getMessage());
            return R.error("修改员工状态失败,请求异常");
        }
    }

    //修改员工
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public DataResult<Object> updateStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){
        try {
            sysStaffInfoService.updateStaff(setSysUserInfoDto);
            return R.success();
        } catch (DefaultException e){
            log.error("修改员工信息错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("修改员工信息错误:{}",e.getMessage());
            return R.error("修改员工信息失败,请求异常");
        }
    }

    //根据员工id查询员工信息
    @GetMapping("/{id}")
    public DataResult<SysUserInfoVo> selectStaffById(@PathVariable(name = "id") String staffId){
        return R.success(sysStaffInfoService.selectStaffById(staffId));
    }


    //批量删除员工
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public DataResult<Object> batchDeleteStaff(@RequestBody  List<String> staffIds){

        try {
            sysStaffInfoService.batchDeleteStaff(staffIds);
            return R.success();
        } catch (DefaultException e){
            log.error("批量删除员工错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("批量删除员工错误:{}",e.getMessage());
            return R.error("批量删除员工失败,请求异常");
        }
    }


    //根据员工id单独删除员工
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public DataResult<Object> deleteStaffById(@PathVariable(name = "id") String staffId){
        try {
            sysStaffInfoService.deleteStaffById(staffId);
            return R.success();
        } catch (DefaultException e){
            log.error("删除员工错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("删除员工错误:{}",e.getMessage());
            return R.error("删除员工失败,请求异常");
        }

    }

    //批量导入用户excel
    @RequestMapping("/batchImportUser")
    public DataResult<Object> batchImportUser(MultipartFile file){
        try {
            return R.success(sysStaffInfoService.batchImportStaff(file));
        } catch (DefaultException e){
            log.error("批量导入员工错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("批量导入员工错误:{}",e.getMessage());
            return R.error("批量导入员工失败,请求异常");
        }
    }


   /**
   *   按企业获取员工
    *   按部门获取员工
    *   按岗位企业员工
   */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public DataResult<List<BosStaffInfoVo>> getStaff(@RequestParam(value = "companyId",required = false) String companyId,
                                                     @RequestParam(value = "departId",required = false) String departId,
                                                     @RequestParam(value = "positionId",required = false) String positionId){
        return R.success(sysStaffInfoService.getStaff(companyId,departId,positionId));
    }


    /**
    *   分页获取管理员列表
    */
    @RequestMapping(value = "/get-admins", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getAdmins(AdminsPageDto dto){
        return R.success(sysStaffInfoService.getAdmins(dto));
    }

}
