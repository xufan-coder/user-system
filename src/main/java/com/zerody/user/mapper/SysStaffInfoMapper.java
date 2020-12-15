package com.zerody.user.mapper;

import com.zerody.user.pojo.SysStaffInfo;

public interface SysStaffInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysStaffInfo record);

    int insertSelective(SysStaffInfo record);

    SysStaffInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysStaffInfo record);

    int updateByPrimaryKey(SysStaffInfo record);
}