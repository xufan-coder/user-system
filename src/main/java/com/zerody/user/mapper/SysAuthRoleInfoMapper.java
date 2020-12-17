package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.pojo.SysAuthRoleInfo;
import com.zerody.user.vo.SysAuthRoleInfoVo;
import com.zerody.user.dto.SysAuthRolePageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysAuthRoleInfoMapper extends BaseMapper<SysAuthRoleInfo> {

    List<SysAuthRoleInfoVo> selectRolesByStaffId(@Param("staffId") String staffId);
}