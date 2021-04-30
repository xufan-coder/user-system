package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.api.vo.CompanyInfoVo;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.vo.SysComapnyInfoVo;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoService
 * @DateTime 2020/12/18_15:51
 * @Deacription TODO
 */
public interface SysCompanyInfoService {
    void addCompany(SysCompanyInfo sysCompanyInfo);

    void updateCompanyStatus(String companyId, Integer loginStatus);

    IPage<SysComapnyInfoVo> getPageCompany(SysCompanyInfoDto companyInfoDto);

    void updateCompany(SysCompanyInfo sysCompanyInfo);

    void deleteCompanyById(String companyId);

    List<SysComapnyInfoVo> getAllCompany(String companyId);

    SysComapnyInfoVo getCompanyInfoById(String id);

    void updateAdminAccout(SetAdminAccountDto dto);

    List<SysComapnyInfoVo> getCompanyInfoByAddr(List<String> cityCodes);

    List<CompanyInfoVo> getCompanyInfoByIds(List<String> ids);

    List<SysComapnyInfoVo> getCompanyAll();

    String getNameById(String id);

    void updateRedundancyCompanyName();
}
