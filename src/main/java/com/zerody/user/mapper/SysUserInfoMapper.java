package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.pojo.SysUserInfo;

public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {
    int deleteByPrimaryKey(String id);

    int insert(SysUserInfo record);

    int insertSelective(SysUserInfo record);

    SysUserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUserInfo record);

    int updateByPrimaryKeyWithBLOBs(SysUserInfo record);

    int updateByPrimaryKey(SysUserInfo record);
}