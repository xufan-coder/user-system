package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.pojo.SysStaffInfo;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.vo.SysStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

public interface SysStaffInfoMapper extends BaseMapper<SysStaffInfo> {


    SysStaffInfoVo selectByUserIdAndCompId(@Param("userId") String userId, @Param("compId")String compId);

    IPage<SysUserInfoVo> selectPageStaffByRoleId(@Param("staff") SysStaffInfoPageDto sysStaffInfoPageDto,IPage iPage);

    IPage<SysUserInfoVo> getPageAllStaff(@Param("staff") SysStaffInfoPageDto sysStaffInfoPageDto,IPage iPage);

    List<String> selectUserByCompanyId(String companyId);

    SysUserInfoVo getStaffInfoByOpenId(String openId);

    SysUserInfoVo selectStaffById(String id);

    List<String> selectStaffRoles(@Param("userId")String userId, @Param("companyId")String companyId);
}