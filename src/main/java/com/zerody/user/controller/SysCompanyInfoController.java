package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.pojo.SysCompanyInfo;
import com.zerody.user.service.SysCompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoController
 * @DateTime 2020/12/18_15:50
 * @Deacription TODO
 */
@RestController
@RequestMapping("/companyInfo")
public class SysCompanyInfoController {

    @Autowired
    private SysCompanyInfoService sysCompanyInfoService;

    //添加企业
    @PostMapping("/addCompany")
    public DataResult addCompany(@Validated @RequestBody SysCompanyInfo sysCompanyInfo){

        return sysCompanyInfoService.addCompany(sysCompanyInfo);
    }

    //修改企业状态的同时修改该企业下的用户的登录状态
    @PostMapping("/updateComanyStatus")
    public DataResult updateComanyStatus(String companyId, Integer loginStatus){
        return sysCompanyInfoService.updateComanyStatus(companyId, loginStatus);
    }

    //分页查询企业
    @GetMapping("/getPageCompany")
    public DataResult getPageCompany(SysCompanyInfoDto companyInfoDto){
        return sysCompanyInfoService.getPageCompany(companyInfoDto);
    }
}
