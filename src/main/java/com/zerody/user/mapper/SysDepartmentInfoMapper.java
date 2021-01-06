package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.vo.SysDepartmentInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDepartmentInfoMapper extends BaseMapper<SysDepartmentInfo> {

    IPage<SysDepartmentInfoVo> getPageDepartment(@Param("dep") SysDepartmentInfoDto sysDepartmentInfoDto, IPage<SysDepartmentInfoVo> iPage);

    List<String> selectUserLoginIdByDepId(String depId);

    List<SysDepartmentInfoVo> getAllDepByCompanyId(String companyId);

    /**
     *
     *
     * @author               PengQiang
     * @description          根据用户获取部门
     * @date                 2021/1/6 16:43
     * @param                id
     * @return               com.zerody.user.domain.SysDepartmentInfo
     */
    SysDepartmentInfo selectUserDep(String id);
}