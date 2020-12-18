package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysJobPositionDto;
import com.zerody.user.pojo.SysJobPosition;
import com.zerody.user.vo.SysJobPositionVo;
import org.mapstruct.Mapper;

@Mapper
public interface SysJobPositionMapper extends BaseMapper<SysJobPosition> {

    IPage<SysJobPositionVo> getPageJob(SysJobPositionDto sysJobPositionDto,IPage iPage);
}