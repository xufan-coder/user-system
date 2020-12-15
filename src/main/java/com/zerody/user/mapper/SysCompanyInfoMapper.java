package com.zerody.user.mapper;

import com.zerody.user.pojo.SysCompanyInfo;

public interface SysCompanyInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysCompanyInfo record);

    int insertSelective(SysCompanyInfo record);

    SysCompanyInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysCompanyInfo record);

    int updateByPrimaryKeyWithBLOBs(SysCompanyInfo record);

    int updateByPrimaryKey(SysCompanyInfo record);
}