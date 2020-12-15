package com.zerody.user.mapper;

import com.zerody.user.pojo.UnionStaffPosition;

public interface UnionStaffPositionMapper {
    int deleteByPrimaryKey(String id);

    int insert(UnionStaffPosition record);

    int insertSelective(UnionStaffPosition record);

    UnionStaffPosition selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UnionStaffPosition record);

    int updateByPrimaryKey(UnionStaffPosition record);
}