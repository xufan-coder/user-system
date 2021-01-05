package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.SysStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysStaffInfoMapper extends BaseMapper<SysStaffInfo> {


    SysStaffInfoVo selectByUserIdAndCompId(@Param("userId") String userId, @Param("compId")String compId);

    IPage<BosStaffInfoVo> getPageAllStaff(@Param("staff")SysStaffInfoPageDto sysStaffInfoPageDto,IPage iPage);

    List<String> selectUserByCompanyId(String companyId);

    SysUserInfoVo getStaffInfoByOpenId(String openId);

    SysUserInfoVo selectStaffById(String id);

    List<String> selectStaffRoles(@Param("userId")String userId, @Param("companyId")String companyId);

    List<BosStaffInfoVo> getStaff(@Param("companyId")String companyId, @Param("departId")String departId, @Param("positionId")String positionId);

    IPage<BosStaffInfoVo> selectAdmins(@Param("dto")AdminsPageDto dto, IPage voIPage);
}