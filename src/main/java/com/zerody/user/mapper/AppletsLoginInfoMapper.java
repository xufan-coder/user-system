package com.zerody.user.mapper;

import com.zerody.user.pojo.AppletsLoginInfo;

public interface AppletsLoginInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(AppletsLoginInfo record);

    int insertSelective(AppletsLoginInfo record);

    AppletsLoginInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AppletsLoginInfo record);

    int updateByPrimaryKeyWithBLOBs(AppletsLoginInfo record);

    int updateByPrimaryKey(AppletsLoginInfo record);
}