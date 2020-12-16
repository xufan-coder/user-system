package com.zerody.user.mapper;

import com.zerody.user.pojo.SysAuthRoleInfo;
import com.zerody.user.vo.SysAuthRoleInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAuthRoleInfoMapper {

    List<SysAuthRoleInfoVo> selectRolesByStaffId(@Param("staffId") String staffId);

    int deleteByPrimaryKey(String id);

    int insert(SysAuthRoleInfo record);

    int insertSelective(SysAuthRoleInfo record);

    SysAuthRoleInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysAuthRoleInfo record);

    int updateByPrimaryKeyWithBLOBs(SysAuthRoleInfo record);

    int updateByPrimaryKey(SysAuthRoleInfo record);
}