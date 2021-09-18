package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.SysStaffRelation;
import com.zerody.user.dto.SysStaffRelationDto;
import com.zerody.user.vo.SysStaffRelationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月09日 16:19
 */

public interface SysStaffRelationMapper extends BaseMapper<SysStaffRelation> {

    List<SysStaffRelationVo> queryRelationList(@Param("param") SysStaffRelationDto param);
}
