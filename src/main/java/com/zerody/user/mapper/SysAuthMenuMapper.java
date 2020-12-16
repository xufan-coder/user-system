package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.pojo.SysAuthMenu;
import com.zerody.user.vo.SysAuthRoleInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAuthMenuMapper extends BaseMapper<SysAuthMenu> {
    int deleteByPrimaryKey(String id);

    int insert(SysAuthMenu record);

    int insertSelective(SysAuthMenu record);

    SysAuthMenu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysAuthMenu record);

    int updateByPrimaryKeyWithBLOBs(SysAuthMenu record);

    int updateByPrimaryKey(SysAuthMenu record);

    List<String> selectCodeAdmin();

    List<String> selectCodeByRoleId(@Param("roles") List<SysAuthRoleInfoVo> roles);
}