package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.vo.SysComapnyInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    List<SysComapnyInfoVo> getCompanyInfoByAddr(List<String> cityCodes);
}