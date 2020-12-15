package com.zerody.user.mapper;

import com.zerody.user.pojo.SysJobPosition;

public interface SysJobPositionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysJobPosition record);

    int insertSelective(SysJobPosition record);

    SysJobPosition selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysJobPosition record);

    int updateByPrimaryKey(SysJobPosition record);
}