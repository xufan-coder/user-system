package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.api.dto.RatioPageDto;
import com.zerody.user.api.vo.CompanyInfoVo;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.vo.CustomerQueryDimensionalityVo;
import com.zerody.user.vo.SysComapnyInfoVo;
import com.zerody.user.vo.UserStructureVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:52
 * @param
 * @return
 */
public interface SysCompanyInfoMapper extends BaseMapper<SysCompanyInfo> {

    /**
     *
     *  分页查询企业
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/5 10:49
     * @param                companyInfoDto
     * @param                 page
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.SysComapnyInfoVo>
     */
    IPage<SysComapnyInfoVo> getPageCompany(@Param("company") SysCompanyInfoDto companyInfoDto, IPage<SysComapnyInfoVo> page);

    /**
     *
     *  查询所有企业
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/5 10:50
     * @param
     * @return               java.util.List<com.zerody.user.vo.SysComapnyInfoVo>
     */
    List<SysComapnyInfoVo> getAllCompnay();

    /**
     *
     *  查询企业详情
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/5 10:50
     * @param                 id
     * @return               com.zerody.user.vo.SysComapnyInfoVo
     */
    SysComapnyInfoVo selectCompanyInfoById(String id);

    /**
     *
     *  查询当前用户的企业
     * @author               PengQiang
     * @description
     * @date                 2021/1/5 10:50
     * @param                userId
     * @return               com.zerody.user.vo.SysComapnyInfoVo
     */
    SysComapnyInfoVo getCompanyByUserId(String userId);

    /**
     *
     * 通过地址获取企业
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/2/24 19:17
     * @param                cityCodes
     * @return               java.util.List<com.zerody.user.vo.SysComapnyInfoVo>
     */
    List<SysComapnyInfoVo> getCompanyInfoByAddr(@Param("cityCodes") List<String> cityCodes);

    /**
     *
     *  根据多个id查询企业
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/9 11:36
     * @param                ids
     * @return               java.util.List<com.zerody.user.api.vo.CompanyInfoVo>
     */
    List<CompanyInfoVo> getCompanyInfoByIds(@Param("ids")List<String> ids);

    List<SysComapnyInfoVo> getCompanyAll();

    /**
     *
     * 获取企业名称
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/31 19:12
     * @param                companyId
     * @return               com.zerody.user.vo.UserStructureVo
     */
    UserStructureVo getCompanyNameById(@Param("companyId") String companyId);

    /**
     *
     *  获取修改名称的企业
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/30 10:40
     * @param
     * @return               java.util.List<com.zerody.user.domain.SysCompanyInfo>
     */
    List<CompanyInfoVo> getHaveUpdateCompanyName();

    /**
     *
     *  修改状态
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/30 10:59
     * @param                companyInfos
     * @return               void
     */
    void updateIsUpdateName(@Param("param") List<CompanyInfoVo> companyInfos);

    List<Map<String, String>> getCompangEditInfo();

    void updateCompanyEdit(@Param("param") List<Map<String, String>> companyInfoMap);

    Page<CompanyInfoVo> getPageCompanyInner(@Param("param")RatioPageDto param, IPage<SysComapnyInfoVo> iPage);

    /**
     *
     *  查询全部企业
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/9/10 9:44
     * @param
     * @return               java.util.List<com.zerody.user.vo.CustomerQueryDimensionalityVo>
     */
    List<CustomerQueryDimensionalityVo> getCustomerQuerydimensionality();

    /**
     *
     *  获取未发送短信的企业
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/11/23 10:04
     * @param
     * @return               java.util.List<java.lang.String>
     */
    List<String> getNotSmsCompany();
}
