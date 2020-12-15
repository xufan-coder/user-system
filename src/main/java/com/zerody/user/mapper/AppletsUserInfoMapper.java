package com.zerody.user.mapper;

import com.zerody.user.pojo.AppletsUserInfo;

public interface AppletsUserInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(AppletsUserInfo record);

    int insertSelective(AppletsUserInfo record);

    AppletsUserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AppletsUserInfo record);

    int updateByPrimaryKeyWithBLOBs(AppletsUserInfo record);

    int updateByPrimaryKey(AppletsUserInfo record);
}