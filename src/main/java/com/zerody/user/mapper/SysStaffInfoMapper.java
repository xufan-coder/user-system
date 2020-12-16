package com.zerody.user.mapper;

import com.zerody.user.pojo.SysStaffInfo;
import com.zerody.user.vo.SysStaffInfoVo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

public interface SysStaffInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysStaffInfo record);

    int insertSelective(SysStaffInfo record);

    SysStaffInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysStaffInfo record);

    int updateByPrimaryKey(SysStaffInfo record);

    SysStaffInfoVo selectByUserIdAndCompId(@Param("userId") String userId, @Param("compId")String compId);
}