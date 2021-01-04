package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysJobPositionDto;
import com.zerody.user.domain.SysJobPosition;
import com.zerody.user.vo.SysJobPositionVo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SysJobPositionMapper extends BaseMapper<SysJobPosition> {

    IPage<SysJobPositionVo> getPageJob(@Param("job") SysJobPositionDto sysJobPositionDto, IPage iPage);

    List<SysJobPositionVo> getAllJobByCompanyId(String companuId);
}