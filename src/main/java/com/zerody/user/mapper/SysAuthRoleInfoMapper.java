package com.zerody.user.mapper;

import com.zerody.user.pojo.SysAuthRoleInfo;

public interface SysAuthRoleInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysAuthRoleInfo record);

    int insertSelective(SysAuthRoleInfo record);

    SysAuthRoleInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysAuthRoleInfo record);

    int updateByPrimaryKeyWithBLOBs(SysAuthRoleInfo record);

    int updateByPrimaryKey(SysAuthRoleInfo record);
}