package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.pojo.SysUserInfo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

    List<SysUserInfo> selectUserByPhoneOrLogName(@Param("userInfo") SysUserInfo userInfo);

}