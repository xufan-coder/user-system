package com.zerody.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.service.DepartRemoteService;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.api.vo.SysUserInfo;
import com.zerody.user.api.vo.UserDepartInfoVo;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.SysComapnyInfoVo;
import com.zerody.user.vo.SysDepartmentInfoVo;
import com.zerody.user.vo.UserStructureVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoController
 * @DateTime 2020/12/19_13:18
 * @Deacription TODO
 */
@Slf4j
@RestController
@RequestMapping("/depart")
public class SysDepartmentInfoController implements DepartRemoteService {

    @Autowired
    private SysDepartmentInfoService sysDepartmentInfoService;

    @Autowired
    private SysStaffInfoService staffService;

    /**
    *    根据企业获取部门
    */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public DataResult<List<SysDepartmentInfo>> getDepartmentByComp(@RequestParam(value = "compId") String compId){
        return R.success(sysDepartmentInfoService.getDepartmentByComp(compId));
    }

    /**
    *    添加部门
    */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public DataResult<Object> addDepartment(@Validated @RequestBody SysDepartmentInfo sysDepartmentInfo){
        try {
            sysDepartmentInfoService.addDepartment(sysDepartmentInfo);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加部门登录状态错误:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("添加部门登录状态错误:{}", e.getMessage());
            return R.error("添加失败,请求异常");
        }
    }

    /**
    *    修改部门
    */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public DataResult<Object> updateDepartment(@Validated @RequestBody SysDepartmentInfo sysDepartmentInfo){

        try {
            sysDepartmentInfoService.updateDepartment(sysDepartmentInfo);
            return R.success();
        } catch (DefaultException e) {
            log.error("修改部门错误:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("修改部门错误:{}", e.getMessage());
            return R.error("修改失败,请求异常");
        }
    }


    /**
    *    删除部门
    */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public DataResult<Object> deleteDepartmentById(@PathVariable(name = "id") String depId){

        try {
            sysDepartmentInfoService.deleteDepartmentById(depId);
            return R.success();
        } catch (DefaultException e) {
            log.error("删除部门错误:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("删除部门错误:{}", e.getMessage());
            return R.error("删除失败,请求异常");
        }

    }

    /**
     *
     *
     * @author               PengQiang
     * @description          查询部门信息(树形返回)
     * @date                 2020/12/31 17:36
     * @param                companyId
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/departmentInfo/{id}", method = RequestMethod.GET)
    public DataResult<List<SysDepartmentInfoVo>> getAllDepByCompanyId(@PathVariable(name = "id") String companyId){
    return R.success(sysDepartmentInfoService.getAllDepByCompanyId(companyId));
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          设置部门管理员
     * @date                 2021/1/8 15:52
     * @param                dto
     * @return
     */
    @RequestMapping(value = "/admin-accout", method = RequestMethod.PUT)
    public DataResult<Object> updateAdminAccout(@RequestBody SetAdminAccountDto dto){
        try {
            sysDepartmentInfoService.updateAdminAccout(dto);
            return R.success();
        } catch (DefaultException e) {
            log.error("设置部门管理员错误:{}", e.getMessage(), e);
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("设置部门管理员错误:{}", e.getMessage(), e);
            return R.error("设置部门管理员错误,请求异常");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取下级部门组织架构
     * @date                 2020/12/31 9:57
     * @param
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.SysComapnyInfoVo>>
     */
    @RequestMapping(value = "/subordinate/structure", method = RequestMethod.GET)
    public DataResult<List<SysDepartmentInfoVo>> getSubordinateStructure(){

        try {

            UserVo user = UserUtils.getUser();
            return R.success(this.sysDepartmentInfoService.getSubordinateStructure(user));
        } catch (DefaultException e) {
            log.error("获取下级部门组织架构错误:{}", e.getMessage(),e);
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("获取下级部门组织架构错误:{}", e.getMessage(),e);
            return R.error("获取下级部门组织架构错误,请求异常");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取下级直属部门
     * @date                 2020/12/31 9:57
     * @param
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.SysComapnyInfoVo>>
     */
    @Override
    @RequestMapping(value = "/subordinate/directly-depart/inner", method = RequestMethod.GET)
    public DataResult<List<UserDepartInfoVo>> getSubordinateDirectlyDepart(@RequestParam("departId") String departId){

        try {
            List<UserDepartInfoVo> departs = this.sysDepartmentInfoService.getSubordinateDirectlyDepart(departId);
            return R.success(departs);
        } catch (DefaultException e) {
            log.error("获取下级直属部门错误:{}", e.getMessage(),e);
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("获取下级直属部门错误:{}", e.getMessage(),e);
            return R.error("获取下级直属部门错误,请求异常");
        }
    }


    /**************************************************************************************************
     **
     *  根据部门id获取负责人的用户信息
     *
     * @param null
     * @return {@link null }
     * @author DaBai
     * @date 2021/4/2  10:41
     */
    @Override
    @RequestMapping(value = "/depart/get/charge-user/inner", method = RequestMethod.GET)
    public DataResult<SysUserInfo> getChargeUser(@RequestParam("departId") String departId) {

        try {
            return R.success(this.sysDepartmentInfoService.getChargeUser(departId));
        } catch (DefaultException e) {
            log.error("获取部门负责人错误:{}", e.getMessage(),e);
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("获取部门负责人错误:{}", e.getMessage(),e);
            return R.error("获取部门负责人错误,请求异常");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取下级直属部门和用户
     * @date                 2020/12/31 9:57
     * @param
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.SysComapnyInfoVo>>
     */
    @RequestMapping(value = "/get/directly-depart-user", method = RequestMethod.GET)
    public DataResult<List<UserStructureVo>> getDirectLyDepartOrUser(@RequestParam(value = "companyId", required = false) String companyId,
                                                  @RequestParam(value = "departId", required = false) String departId){

        try {
            UserVo user = null;
            if (StringUtils.isEmpty(companyId) && StringUtils.isEmpty(departId)) {
                user = UserUtils.getUser();
            }
            List<UserStructureVo> departs = this.sysDepartmentInfoService.getDirectLyDepartOrUser(companyId, departId, user);
            return R.success(departs);
        } catch (DefaultException e) {
            log.error("获取下级直属部门错误:{}", e.getMessage(),e);
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("获取下级直属部门错误:{}", e.getMessage(),e);
            return R.error("获取下级直属部门错误,请求异常");
        }
    }
}
