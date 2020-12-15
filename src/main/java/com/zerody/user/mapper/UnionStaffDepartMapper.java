package com.zerody.user.mapper;

import com.zerody.user.pojo.UnionStaffDepart;

public interface UnionStaffDepartMapper {
    int deleteByPrimaryKey(String id);

    int insert(UnionStaffDepart record);

    int insertSelective(UnionStaffDepart record);

    UnionStaffDepart selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UnionStaffDepart record);

    int updateByPrimaryKey(UnionStaffDepart record);
}