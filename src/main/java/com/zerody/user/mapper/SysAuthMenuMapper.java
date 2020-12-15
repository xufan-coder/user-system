package com.zerody.user.mapper;

import com.zerody.user.pojo.SysAuthMenu;

public interface SysAuthMenuMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysAuthMenu record);

    int insertSelective(SysAuthMenu record);

    SysAuthMenu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysAuthMenu record);

    int updateByPrimaryKeyWithBLOBs(SysAuthMenu record);

    int updateByPrimaryKey(SysAuthMenu record);
}