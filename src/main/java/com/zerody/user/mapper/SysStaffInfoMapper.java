package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.pojo.SysStaffInfo;
import com.zerody.user.vo.SysStaffInfoVo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

public interface SysStaffInfoMapper extends BaseMapper<SysStaffInfo> {


    SysStaffInfoVo selectByUserIdAndCompId(@Param("userId") String userId, @Param("compId")String compId);
}