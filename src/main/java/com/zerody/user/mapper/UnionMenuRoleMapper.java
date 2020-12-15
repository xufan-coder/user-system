package com.zerody.user.mapper;

import com.zerody.user.pojo.UnionMenuRole;

public interface UnionMenuRoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(UnionMenuRole record);

    int insertSelective(UnionMenuRole record);

    UnionMenuRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UnionMenuRole record);

    int updateByPrimaryKey(UnionMenuRole record);
}