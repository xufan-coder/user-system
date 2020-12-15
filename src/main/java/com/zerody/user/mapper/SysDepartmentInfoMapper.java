package com.zerody.user.mapper;

import com.zerody.user.pojo.SysDepartmentInfo;

public interface SysDepartmentInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysDepartmentInfo record);

    int insertSelective(SysDepartmentInfo record);

    SysDepartmentInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysDepartmentInfo record);

    int updateByPrimaryKeyWithBLOBs(SysDepartmentInfo record);

    int updateByPrimaryKey(SysDepartmentInfo record);
}