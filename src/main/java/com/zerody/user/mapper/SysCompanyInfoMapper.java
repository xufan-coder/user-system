package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.vo.SysComapnyInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysCompanyInfoMapper extends BaseMapper<SysCompanyInfo> {

    IPage<SysComapnyInfoVo> getPageCompany(@Param("company") SysCompanyInfoDto companyInfoDto, IPage<SysComapnyInfoVo> page);

    List<SysComapnyInfoVo> getAllCompnay();

    SysComapnyInfoVo selectCompanyInfoById(String id);
}