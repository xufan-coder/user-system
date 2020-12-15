package com.zerody.user.mapper;

import com.zerody.user.pojo.SysLoginInfo;

public interface SysLoginInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysLoginInfo record);

    int insertSelective(SysLoginInfo record);

    SysLoginInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLoginInfo record);

    int updateByPrimaryKey(SysLoginInfo record);
}