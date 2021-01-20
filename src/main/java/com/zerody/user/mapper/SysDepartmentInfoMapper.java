package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.vo.SysDepartmentInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:52
 * @param
 * @return
 */
public interface SysDepartmentInfoMapper extends BaseMapper<SysDepartmentInfo> {

    /**
     *
     * 分页查询部门信息
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:52
     * @param                sysDepartmentInfoDto
     * @param                iPage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    IPage<SysDepartmentInfoVo> getPageDepartment(@Param("dep") SysDepartmentInfoDto sysDepartmentInfoDto, IPage<SysDepartmentInfoVo> iPage);

    /**
     *
     * ——
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:53
     * @param                depId
     * @return               java.util.List<java.lang.String>
     */
    List<String> selectUserLoginIdByDepId(String depId);

    /**
     *
     * 查询当前企业下的所有部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:53
     * @param                companyId
     * @return               java.util.List<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    List<SysDepartmentInfoVo> getAllDepByCompanyId(String companyId);

    /**
     *
     * 根据用户获取部门
     * @author               PengQiang
     * @description
     * @date                 2021/1/6 16:43
     * @param                id
     * @return               com.zerody.user.domain.SysDepartmentInfo
     */
    SysDepartmentInfo selectUserDep(String id);
}