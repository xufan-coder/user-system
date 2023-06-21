package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.dto.RatioPageDto;
import com.zerody.user.api.service.CompanyRemoteService;
import com.zerody.user.api.vo.CompanyInfoVo;
import com.zerody.user.dto.ReportFormsQueryDto;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.dto.company.SysCompanyQueryDto;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Autowired
    private CheckUtil checkUtil;


    @Value("${flow.oaCompany:oa-flow-001}")
    private String oaCompany;
    @Value("${flow.oaCompanyName:OA审批企业}")
    private String oaCompanyName;

    /**
     * 添加企业
     *
     * @param sysCompanyInfo
     * @return com.zerody.common.api.bean.DataResult<java.lang.Object>
     * @author PengQiang
     * @description 添加企业
     * @date 2020/12/30 20:07
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public DataResult<Object> addCompany(@Validated @RequestBody SysCompanyInfo sysCompanyInfo) {
        try {
            sysCompanyInfoService.addCompany(sysCompanyInfo);
            return R.success();
        } catch (DefaultException e) {
            log.error("企业添加错误:{}", JSON.toJSONString(sysCompanyInfo), e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("企业添加错误:{}", JSON.toJSONString(sysCompanyInfo), e);
            return R.error("添加企业失败,请求异常");
        }
    }

    /**
     * @param [companyId, loginStatus]
     * @return com.zerody.common.api.bean.DataResult<java.lang.Object>
     * @author PengQiang
     * @description 修改企业状态
     * @date 2020/12/31 9:41
     */
    @RequestMapping(value = "/{id}/{status}", method = RequestMethod.PUT)
    public DataResult<Object> updateCompanyStatus(@PathVariable("id") String companyId, @PathVariable("status") Integer loginStatus) {
        try {
            sysCompanyInfoService.updateCompanyStatus(companyId, loginStatus);
            return R.success();
        } catch (DefaultException e) {
            log.error("企业状态修改错误:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("企业状态修改错误:{}", e.getMessage());
            return R.error("修改企业登录状态失败,请求异常");
        }
    }

    /**
     * @param [sysCompanyInfo]
     * @return com.zerody.common.api.bean.DataResult<java.lang.Object>
     * @author PengQiang
     * @description 修改企业信息
     * @date 2020/12/31 9:47
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public DataResult<Object> updateCompany(@Validated @RequestBody SysCompanyInfo sysCompanyInfo) {
        try {
            sysCompanyInfoService.updateCompany(sysCompanyInfo);
            return R.success();
        } catch (DefaultException e) {
            log.error("企业修改错误:{}", JSON.toJSONString(sysCompanyInfo), e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("企业修改错误:{}", JSON.toJSONString(sysCompanyInfo), e);
            return R.error("修改企业失败,请求异常");
        }
    }

    /**
     *   查询企业
     *   流程专用
     */
    @RequestMapping(value = "/flow/page", method = RequestMethod.GET)
    public DataResult<IPage<SysComapnyInfoVo>> getflowPageCompany(SysCompanyInfoDto companyInfoDto) {
        IPage<SysComapnyInfoVo> pageCompany = sysCompanyInfoService.getPageCompany(companyInfoDto);
        SysComapnyInfoVo vo =new SysComapnyInfoVo();
        vo.setCompanyName(oaCompanyName);
        vo.setId(oaCompany);
        pageCompany.getRecords().add(0,vo);
        return R.success(pageCompany);
    }

    /**
     * @param companyInfoDto
     * @return com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage < com.zerody.user.vo.SysComapnyInfoVo>>
     * @author PengQiang
     * @description 分页查询企业
     * @date 2020/12/31 9:55
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public DataResult<IPage<SysComapnyInfoVo>> getPageCompany(SysCompanyInfoDto companyInfoDto) {
        return R.success(sysCompanyInfoService.getPageCompany(companyInfoDto));
    }

    /**
     * @param [companyId]
     * @return com.zerody.common.api.bean.DataResult
     * @author PengQiang
     * @description 删除企业
     * @date 2020/12/31 9:55
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public DataResult<Object> deleteCompanyById(@PathVariable(name = "id") String companyId) {
        try {
            sysCompanyInfoService.deleteCompanyById(companyId);
            return R.success();
        } catch (DefaultException e) {
            log.error("企业删除错误:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("企业删除错误:{}", e.getMessage());
            return R.error("删除企业失败,请求异常");
        }
    }

    /**
     * @param
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.SysComapnyInfoVo>>
     * @author PengQiang
     * @description 获取组织树形结构
     * @date 2020/12/31 9:57
     */
    @RequestMapping(value = "/structure", method = RequestMethod.GET)
    public DataResult<List<SysComapnyInfoVo>> getAllCompany(String companyId) {
        return R.success(sysCompanyInfoService.getAllCompany(companyId));
    }

    /**
     * @param
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.SysComapnyInfoVo>>
     * @author PengQiang
     * @description 获取组织树形结构
     * @date 2020/12/31 9:57
     */
    @RequestMapping(value = "/structure-depart", method = RequestMethod.GET)
    public DataResult<List<SysComapnyInfoVo>> getUserCompany() {
        UserVo userVo = UserUtils.getUser();
        if (Objects.isNull(userVo)) {
            throw new DefaultException("用户不存在");
        }
        List<SysComapnyInfoVo> sysComapnyInfoVos = sysCompanyInfoService.getUserCompany(userVo);
        return R.success(sysComapnyInfoVos);
    }


    /**
     * @param
     * @author kuang
     * @description 获取组织树形结构 -包含部门人数
     * @date 2020/12/31 9:57
     */
    @RequestMapping(value = "/structure/persons", method = RequestMethod.GET)
    public DataResult<List<SysComapnyInfoVo>> getAllCompanyPersons(String companyId) {
        return R.success(sysCompanyInfoService.getAllCompanyPersons(companyId));
    }


    /**
     * 获取企业详情
     *
     * @param [id]
     * @return com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SysComapnyInfoVo>
     * @author PengQiang
     * @description DELL
     * @date 2021/1/5 11:02
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DataResult<SysComapnyInfoVo> getCompanyInfoById(@PathVariable(name = "id") String id) {

        return R.success(sysCompanyInfoService.getCompanyInfoById(id));
    }


    /**
     * @param [dto]
     * @return com.zerody.common.api.bean.DataResult<java.lang.Object>
     * @author PengQiang
     * @description 设置企业管理员
     * @date 2021/1/8 16:41
     */
    @RequestMapping(value = "/admin-account", method = RequestMethod.PUT)
    public DataResult<Object> updateAdminAccout(@RequestBody SetAdminAccountDto dto) {
        try {
            this.sysCompanyInfoService.updateAdminAccout(dto);
            return R.success();
        } catch (DefaultException e) {
            log.error("设置企业管理员错误:{}", JSON.toJSONString(dto), e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("设置企业管理员错误:{}", JSON.toJSONString(dto), e);
            return R.error("设置企业管理员错误,请求异常");
        }
    }

    /**
     * 获取企业详情inner
     *
     * @param [id]
     * @return com.zerody.common.api.bean.DataResult<com.zerody.user.vo.SysComapnyInfoVo>
     * @author PengQiang
     * @description DELL
     * @date 2021/1/5 11:02
     */
    @Override
    @RequestMapping(value = "/get/company-info/inner", method = RequestMethod.GET)
    public DataResult<CompanyInfoVo> getCompanyInfoByIdInner(@PathVariable(name = "companyId") String id) {
        SysComapnyInfoVo companyInfo = sysCompanyInfoService.getCompanyInfoById(id);
        CompanyInfoVo companyInfoInner = new CompanyInfoVo();
        BeanUtils.copyProperties(companyInfo, companyInfoInner);
        companyInfoInner.setAdminUserName(companyInfo.getAdminName());
        return R.success(companyInfoInner);
    }

    @GetMapping("/get/addr-filtrate")
    public DataResult<List<SysComapnyInfoVo>> getCompanyInfoByAddr(@RequestParam("cityCodes") List<String> cityCodes) {
        try {
            if (CollectionUtils.isEmpty(cityCodes)) {
                return R.error("地址code值必填");
            }
            return R.success(this.sysCompanyInfoService.getCompanyInfoByAddr(cityCodes));
        } catch (DefaultException e) {
            log.error("通过地址获取企业错误!", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("通过地址获取企业错误!", e, e);
            return R.error(e.getMessage());
        }
    }

    @Override
    @RequestMapping(value = "/get/company-infos/inner", method = RequestMethod.GET)
    public DataResult<List<CompanyInfoVo>> getCompanyInfoByIdsInner(@PathVariable(name = "companyIds") List<String> ids) {
        List<CompanyInfoVo> companyInfos = sysCompanyInfoService.getCompanyInfoByIds(ids);
        return R.success(companyInfos);
    }

    /**
     * @param []
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.SysComapnyInfoVo>>
     * @author PengQiang
     * @description 获取全部企业
     * @date 2021/3/10 10:40
     */
    @GetMapping("/get/all")
    public DataResult<List<SysComapnyInfoVo>> getCompanyAll() {
        try {
                List<String> companyIds = null;
                if (UserUtils.getUser().isBack()) {
                    companyIds = this.checkUtil.setBackCompany(UserUtils.getUser().getUserId());
                }
                if (UserUtils.getUser().isCEO()) {
                    companyIds = this.checkUtil.setCeoCompany(UserUtils.getUser().getUserId());
                }
            return R.success(this.sysCompanyInfoService.getCompanyAll(companyIds));
        } catch (DefaultException e) {
            log.error("获取全部企业错误!", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取全部企业错误!", e, e);
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/get/sys-all")
    public DataResult<List<SysComapnyInfoVo>> getSysCompanyAll() {
        try {

            List<SysComapnyInfoVo> result =  this.sysCompanyInfoService.getSysCompanyAll();
            return R.success(result);
        } catch (DefaultException e) {
            log.error("获取系统所有企业错误!", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取系统所有企业错误!", e, e);
            return R.error(e.getMessage());
        }
    }

    /**
     * @param [name]
     * @return com.zerody.common.api.bean.DataResult<java.lang.String>
     * @author PengQiang
     * @description 通过id获取企业名称
     * @date 2021/4/22 14:08
     */
    @Override
    @GetMapping("/get/com-name")
    public DataResult<String> getNameById(@RequestParam("id") String id) {
        try {
            return R.success(this.sysCompanyInfoService.getNameById(id));
        } catch (DefaultException e) {
            log.error("查询企业名称错误!", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询企业名称错误!", e, e);
            return R.error(e.getMessage());
        }
    }

    @Override
    @PostMapping("/get/page/inner")
    public DataResult<Page<CompanyInfoVo>> getPageInner(@RequestBody RatioPageDto pageQueryDto) {
        Page<CompanyInfoVo> companyInfos = sysCompanyInfoService.getPageInner(pageQueryDto);
        return R.success(companyInfos);
    }

    /**
     * @param [param]
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.ReportFormsQueryVo>>
     * @author PengQiang
     * @description 获取报表
     * @date 2021/12/15 11:41
     */
    @GetMapping("/report-forms")
    public DataResult<List<ReportFormsQueryVo>> getReportForms(ReportFormsQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            this.checkUtil.setFiltrateTime(param);
            List<ReportFormsQueryVo> list = this.sysCompanyInfoService.getReportForms(param);
            return R.success(list);
        } catch (Exception e) {
            log.error("获取报表出错：{}", e, e);
            return R.error(e.getMessage());
        }
    }

    /**
     * @param [param]
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.ReportFormsQueryVo>>
     * @author PengQiang
     * @description 导出报表
     * @date 2021/12/15 11:41
     */
    @PostMapping("/report-forms/export")
    public DataResult<Object> getReportFormsExport(HttpServletResponse response, @RequestBody ReportFormsQueryDto param) {
        try {
            this.checkUtil.SetUserPositionInfo(param);
            this.checkUtil.setFiltrateTime(param);
            this.sysCompanyInfoService.getReportFormsExport(response, param);
            return R.success();
        } catch (Exception e) {
            log.error("获取报表出错：{}", e, e);
            return R.error(e.getMessage());
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取业务人员数量
     * @date                 2022/1/11 10:45
     * @param                [companyId]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Integer>
     */
    @GetMapping("/get/salesman-role/inner")
    public DataResult<List<SalesmanRoleInfoVo>> getSalesmanRole(@RequestParam(value = "companyId", required = false) List<String> companyId, @RequestParam(value = "departId", required = false) List<String> departId, @RequestParam(value = "userId", required = false) List<String> userId) {

        try {
            List<SalesmanRoleInfoVo> result =  this.sysCompanyInfoService.getSalesmanRole(companyId, departId, userId);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("获取业务人员出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取业务人员出错:{}", e, e);
            return R.error(e.getMessage());
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取组织架构
     * @date                 2022/10/21 18:17
     * @param
     * @return
     */
    @GetMapping("/get/all/structure/inner")
    public DataResult<List<SysComapnyInfoVo>> getAllStructure() {
        try {
            return R.success(this.sysCompanyInfoService.getAllStructure());
        } catch (DefaultException e) {
            log.error("获取企业组织架构出错:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取企业组织架构出错:{}", e, e);
            return R.error("获取企业组织架构出错");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          企业列表(名称、id)
     * @date                 2022/12/9 16:12
     * @param                []
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.SysCompanyNameQueryVo>>
     */
    @GetMapping("/get/all-company/comp-name")
    public DataResult<List<SysCompanyNameQueryVo>> getAllCompanyName() {
        try {
            SysCompanyQueryDto param = new SysCompanyQueryDto();
            List<String> companyIds = new ArrayList<>();
            UserVo user = UserUtils.getUser();
            companyIds.add(user.getCompanyId());
            if (user.isBack()) {
                companyIds = checkUtil.setBackCompany(user.getUserId());
            }
            if (user.isCEO()) {
                companyIds = this.checkUtil.setCeoCompany(user.getUserId());
            }
            List<SysCompanyNameQueryVo> data = this.sysCompanyInfoService.getAllCompanyName(param);
            return R.success(data);
        } catch (DefaultException e) {
            log.warn("获取关联企业校验：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.warn("获取关联企业出错：{}", e, e);
            return R.error(e.getMessage());
        }
    }
    /**
    *
    *  @description   获取全集团公司（1正式数据）
    *  @author        YeChangWei
    *  @date          2023/6/21 10:07
    *  @return        com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.CustomerQueryDimensionalityVo>>
    */
    @GetMapping("/get/all-company/inner")
    public DataResult<List<CustomerQueryDimensionalityVo>> getAllCompanyData(){
        try {
            List<CustomerQueryDimensionalityVo> allCompany = this.sysCompanyInfoService.getAllCompanyData();
            return R.success(allCompany);
        } catch (DefaultException e) {
            log.error("获取全集团公司出错:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取全集团公司出错:{}", e, e);
            return R.error("获取全集团公司出错");
        }
    }
}
