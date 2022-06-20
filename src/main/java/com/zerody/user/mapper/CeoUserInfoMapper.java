package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.CeoRefVo;
import com.zerody.user.vo.CompanyRefVo;
import com.zerody.user.vo.SysUserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DaBai
 * @date 2020/12/29 16:19
 */

public interface CeoUserInfoMapper extends BaseMapper<CeoUserInfo> {

    IPage<BosStaffInfoVo> getCeoPage(@Param("param") SysStaffInfoPageDto param, IPage<BosStaffInfoVo> iPage);

    SysUserInfoVo getCeoInfoByUserId(@Param("userId") String userId);

    List<StaffInfoVo> getStaffInfoByIds(@Param("userIds") List<String> userId);
}
