package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.pojo.SysUserInfo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

    List<SysUserInfo> selectUserByPhoneOrLogName(SysUserInfo userInfo);

}