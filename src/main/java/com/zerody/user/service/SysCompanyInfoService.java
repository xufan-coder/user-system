package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.pojo.SysCompanyInfo;
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

    void updataCompany(SysCompanyInfo sysCompanyInfo);

    void deleteCompanyById(String companyId);

    List<SysComapnyInfoVo> getAllCompany();
}
