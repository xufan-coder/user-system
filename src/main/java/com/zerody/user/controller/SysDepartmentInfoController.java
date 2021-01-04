package com.zerody.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.service.SysDepartmentInfoService;
import com.zerody.user.vo.SysDepartmentInfoVo;
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
public class SysDepartmentInfoController {

    @Autowired
    private SysDepartmentInfoService sysDepartmentInfoService;

    /**
     *
     *
     * @author               PengQiang
     * @description          分页查询部门
     * @date                 2020/12/31 17:32
     * @param                [sysDepartmentInfoDto]
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/departmentInfo", method = RequestMethod.GET)
    public DataResult<IPage<SysDepartmentInfoVo>> getPageDepartment(SysDepartmentInfoDto sysDepartmentInfoDto){
        return R.success(sysDepartmentInfoService.getPageDepartment(sysDepartmentInfoDto));
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          修改部门登录状态
     * @date                 2020/12/31 17:32
     * @param                [depId, loginStauts]
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/departmentInfo/loginStatus/{id}/{status}", method = RequestMethod.PUT)
    public DataResult<Object> updateDepartmentStatus(@PathVariable(name = "id") String depId,@PathVariable(name = "status") Integer loginStauts){
        try {
            sysDepartmentInfoService.updateDepartmentStatus(depId, loginStauts);
            return R.success();
        } catch (DefaultException e) {
            log.error("修改部门登录状态错误:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("修改部门登录状态错误:{}", e.getMessage());
            return R.error("修改失败,请求异常");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          添加部门
     * @date                 2020/12/31 17:33
     * @param                [sysDepartmentInfo]
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/departmentInfo", method = RequestMethod.POST)
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
     *
     *
     * @author               PengQiang
     * @description          修改部门
     * @date                 2020/12/31 17:35
     * @param                [sysDepartmentInfo]
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/departmentInfo", method = RequestMethod.PUT)
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
     *
     *
     * @author               PengQiang
     * @description          删除部门
     * @date                 2020/12/31 17:35
     * @param                [depId]
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/departmentInfo/{id}", method = RequestMethod.DELETE)
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
     * @param                [companyId]
     * @return               com.zerody.common.bean.DataResult
     */
    @RequestMapping(value = "/user/departmentInfo/{id}", method = RequestMethod.GET)
    public DataResult<List<SysDepartmentInfoVo>> getAllDepByCompanyId(@PathVariable(name = "id") String companyId){
    return R.success(sysDepartmentInfoService.getAllDepByCompanyId(companyId));
    }
}
