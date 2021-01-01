package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.pojo.SysDepartmentInfo;
import com.zerody.user.vo.SysDepartmentInfoVo;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoService
 * @DateTime 2020/12/19_13:20
 * @Deacription TODO
 */
public interface SysDepartmentInfoService {

    IPage<SysDepartmentInfoVo> getPageDepartment(SysDepartmentInfoDto sysDepartmentInfoDto);

    void updateDepartmentStatus(String depId, Integer loginStauts);

    void addDepartment(SysDepartmentInfo sysDepartmentInfo);

    void updateDepartment(SysDepartmentInfo sysDepartmentInfo);

    void deleteDepartmentById(String depId);

    List<SysDepartmentInfoVo> getAllDepByCompanyId(String companyId);
}
