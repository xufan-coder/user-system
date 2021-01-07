package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.vo.SysDepartmentInfoVo;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoService
 * @DateTime 2020/12/19_13:20
 * @Deacription TODO
 */
public interface SysDepartmentInfoService {

    void addDepartment(SysDepartmentInfo sysDepartmentInfo);

    void updateDepartment(SysDepartmentInfo sysDepartmentInfo);

    void deleteDepartmentById(String depId);

    List<SysDepartmentInfoVo> getAllDepByCompanyId(String companyId);

    List<SysDepartmentInfo> getDepartmentByComp(String compId);

    /**************************************************************************************************
     **
     * 按部门名字查询本企业部门是否有，
     *
     * @param name
     * @return {@link SysDepartmentInfo }
     * @author DaBai
     * @date 2021/1/7  9:41
     */
    SysDepartmentInfo getByName(String name,String compId);
}
