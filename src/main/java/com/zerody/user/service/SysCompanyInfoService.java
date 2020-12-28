package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.pojo.SysCompanyInfo;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoService
 * @DateTime 2020/12/18_15:51
 * @Deacription TODO
 */
public interface SysCompanyInfoService {
    DataResult addCompany(SysCompanyInfo sysCompanyInfo);

    DataResult updateCompanyStatus(String companyId, Integer loginStatus);

    DataResult getPageCompany(SysCompanyInfoDto companyInfoDto);

    DataResult updataCompany(SysCompanyInfo sysCompanyInfo);

    DataResult deleteCompanyById(String companyId);

    DataResult getAllCompany();
}
