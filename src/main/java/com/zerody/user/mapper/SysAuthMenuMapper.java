package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysAuthMenuPageDto;
import com.zerody.user.pojo.SysAuthMenu;
import com.zerody.user.vo.SysAuthRoleInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAuthMenuMapper extends BaseMapper<SysAuthMenu> {

    List<String> selectCodeAdmin();

    List<String> selectCodeByRoleId(@Param("roles") List<SysAuthRoleInfoVo> roles);

    IPage<SysAuthMenu> getMenuPage(SysAuthMenuPageDto sysAuthMenuPageDto, IPage<SysAuthMenu> iPage);
}