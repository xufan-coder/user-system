package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.api.service.CompanyRemoteService;
import com.zerody.user.api.vo.CompanyInfoVo;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.vo.SysComapnyInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoController
 * @DateTime 2020/12/18_15:50
 * @Deacription TODO
 */
@Slf4j
@RestController
@RequestMapping("/company-info")
public class SysCompanyInfoController implements CompanyRemoteService {

    @Autowired
    private SysCompanyInfoService sysCompanyInfoService;

    /**
     *  添加企业
     *
     * @author               PengQiang
     * @description          添加企业
     * @date                 2020/12/30 20:07
     * @param                sysCompanyInfo
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public DataResult<Object> addCompany(@Validated @RequestBody SysCompanyInfo sysCompanyInfo){
        try {
            sysCompanyInfoService.addCompany(sysCompanyInfo);
            return R.success();
        } catch (DefaultException e){
            log.error("企业添加错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("添加企业失败,请求异常");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          修改企业状态
     * @date                 2020/12/31 9:41
     * @param                [companyId, loginStatus]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @RequestMapping(value = "/{id}/{status}", method = RequestMethod.PUT)
    public DataResult<Object> updateCompanyStatus(@PathVariable("id")String companyId,@PathVariable("status") Integer loginStatus){
        try {
            sysCompanyInfoService.updateCompanyStatus(companyId, loginStatus);
            return R.success();
        } catch (DefaultException e){
            log.error("企业状态修改错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("修改企业登录状态失败,请求异常");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          修改企业信息
     * @date                 2020/12/31 9:47
     * @param                [sysCompanyInfo]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public DataResult<Object> updataCompany(@Validated @RequestBody SysCompanyInfo sysCompanyInfo){
        try {
            sysCompanyInfoService.updataCompany(sysCompanyInfo);
            return R.success();
        } catch (DefaultException e){
            log.error("企业修改错误:{}",e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("修改企业失败,请求异常");
        }
    }
    
    /**
     *
     *
     * @author               PengQiang
     * @description          分页查询企业
     * @date                 2020/12/31 9:55
     * @param                companyInfoDto
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.SysComapnyInfoVo>>
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public DataResult<IPage<SysComapnyInfoVo>> getPageCompany(SysCompanyInfoDto companyInfoDto){
        return R.success(sysCompanyInfoService.getPageCompany(companyInfoDto));
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          删除企业
     * @date                 2020/12/31 9:55
     * @param                [companyId]
     * @return               com.zerody.common.api.bean.DataResult
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public DataResult<Object> deleteCompanyById(@PathVariable(name = "id") String companyId){
        try {
            sysCompanyInfoService.deleteCompanyById(companyId);
            return R.success();
        } catch (DefaultException e){
            log.error("企业删除错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("企业修改错误:{}",e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取组织树形结构
     * @date                 2020/12/31 9:57
     * @param
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.SysComapnyInfoVo>>
     */
    @RequestMapping(value = "/structure", method = RequestMethod.GET)
    public DataResult<List<SysComapnyInfoVo>> getAllCompany(String companyId){
        return R.success(sysCompanyInfoService.getAllCompany(companyId));
    }

    /**
     *
     *  获取企业详情
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/5 11:02
     * @param                [id]
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SysComapnyInfoVo>
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DataResult<SysComapnyInfoVo> getCompanyInfoById(@PathVariable(name = "id") String id){

        return R.success(sysCompanyInfoService.getCompanyInfoById(id));
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          设置企业管理员
     * @date                 2021/1/8 16:41
     * @param                [dto]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @RequestMapping(value = "/admin-account", method = RequestMethod.PUT)
    public DataResult<Object> updateAdminAccout(@RequestBody SetAdminAccountDto dto){
        try {
            this.sysCompanyInfoService.updateAdminAccout(dto);
            return R.success();
        } catch (DefaultException e){
            log.error("设置企业管理员错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("设置企业管理员错误:{}",e.getMessage());
            return R.error("设置企业管理员错误,请求异常");
        }
    }

    /**
     *
     *  获取企业详情inner
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/5 11:02
     * @param                [id]
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SysComapnyInfoVo>
     */
    @Override
    @RequestMapping(value = "/get/company-info/inner", method = RequestMethod.GET)
    public DataResult<CompanyInfoVo> getCompanyInfoByIdInner(@PathVariable(name = "companyId") String id){
        SysComapnyInfoVo companyInfo = sysCompanyInfoService.getCompanyInfoById(id);
        CompanyInfoVo companyInfoInner = new CompanyInfoVo();
        BeanUtils.copyProperties(companyInfo, companyInfoInner);
        return R.success(companyInfoInner);
    }

    @GetMapping("/get/addr-filtrate")
    public DataResult<List<SysComapnyInfoVo>> getCompanyInfoByAddr(@RequestParam("provinceCode") String provinceCode,
                                                             @RequestParam("cityCode") String cityCode){
        try {
            if (StringUtils.isEmpty(provinceCode) || StringUtils.isEmpty(cityCode)){
                return R.error("地址code值均为必填");
            }
           return R.success(this.sysCompanyInfoService.getCompanyInfoByAddr(provinceCode, cityCode));
        } catch (DefaultException e){
            log.error("通过地址获取企业错误!", e , e);
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("通过地址获取企业错误!", e , e);
            return R.error(e.getMessage());
        }
    }
}
