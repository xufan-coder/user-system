package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.pojo.SysDepartmentInfo;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.SysDepartmentInfoVo;

import java.util.List;

public interface SysDepartmentInfoMapper extends BaseMapper<SysDepartmentInfo> {

    IPage<SysDepartmentInfoVo> getPageDepartment(SysDepartmentInfoDto sysDepartmentInfoDto, IPage<SysDepartmentInfoVo> iPage);

    List<String> selectUserLoginIdByDepId(String depId);
}