package com.zerody.user.mapper;

import com.zerody.user.pojo.UnionRoleStaff;

public interface UnionRoleStaffMapper {
    int deleteByPrimaryKey(String id);

    int insert(UnionRoleStaff record);

    int insertSelective(UnionRoleStaff record);

    UnionRoleStaff selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UnionRoleStaff record);

    int updateByPrimaryKey(UnionRoleStaff record);
}