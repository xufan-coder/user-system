package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.dto.RatioPageDto;
import com.zerody.user.api.vo.AllCompanyDataVo;
import com.zerody.user.api.vo.CompanyInfoVo;
import com.zerody.user.dto.ReportFormsQueryDto;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.dto.company.SysCompanyQueryDto;
import com.zerody.user.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoService
 * @DateTime 2020/12/18_15:51
 * @Deacription TODO
 */
public interface SysCompanyInfoService extends IService<SysCompanyInfo> {
    void addCompany(SysCompanyInfo sysCompanyInfo);

    void updateCompanyStatus(String companyId, Integer loginStatus);

    IPage<SysComapnyInfoVo> getPageCompany(SysCompanyInfoDto companyInfoDto);

    void updateCompany(SysCompanyInfo sysCompanyInfo);

    void  deleteCompanyById(String companyId);

    List<SysComapnyInfoVo> getAllCompany(String companyId);

    List<SysComapnyInfoVo> getUserCompany(UserVo userVo);

    SysComapnyInfoVo getCompanyInfoById(String id);

    void updateAdminAccout(SetAdminAccountDto dto);

    List<SysComapnyInfoVo> getCompanyInfoByAddr(List<String> cityCodes);

    List<CompanyInfoVo> getCompanyInfoByIds(List<String> ids);

    List<SysComapnyInfoVo> getCompanyAll(List<String> companyIds);

    String getNameById(String id);

    void updateRedundancyCompanyName();

    void doCompangEditInfo();

    Page<CompanyInfoVo> getPageInner(RatioPageDto pageQueryDto);

    List<String> getNotSmsCompany();

    List<ReportFormsQueryVo> getReportForms(ReportFormsQueryDto param);

    void getReportFormsExport(HttpServletResponse response, ReportFormsQueryDto param) throws IOException;

    List<SalesmanRoleInfoVo> getSalesmanRole(List<String> companyId, List<String> departId, List<String> userId);

    List<SysComapnyInfoVo> getAllCompanyPersons(String companyId);

    List<SysComapnyInfoVo> getSysCompanyAll();

    List<SysComapnyInfoVo> getAllStructure();

    List<SysCompanyNameQueryVo> getAllCompanyName(SysCompanyQueryDto param);
    /**
    *
    *  @description   获取全集团公司
    *  @author        YeChangWei
    *  @date          2023/6/21 10:08
    *  @return        java.util.List<com.zerody.user.vo.CustomerQueryDimensionalityVo>
    */
    List<AllCompanyDataVo> getAllCompanyData();
}
