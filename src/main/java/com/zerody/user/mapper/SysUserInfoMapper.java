package com.zerody.user.mapper;

import com.zerody.user.pojo.SysUserInfo;

public interface SysUserInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysUserInfo record);

    int insertSelective(SysUserInfo record);

    SysUserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUserInfo record);

    int updateByPrimaryKeyWithBLOBs(SysUserInfo record);

    int updateByPrimaryKey(SysUserInfo record);
}