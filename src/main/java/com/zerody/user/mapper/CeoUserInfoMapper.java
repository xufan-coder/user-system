package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.domain.CeoUserInfo;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.vo.BosStaffInfoVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author  DaBai
 * @date  2020/12/29 16:19
 */

public interface CeoUserInfoMapper extends BaseMapper<CeoUserInfo> {

    IPage<BosStaffInfoVo> getCeoPage(@Param("param") SysStaffInfoPageDto param, IPage<BosStaffInfoVo> iPage);
}
