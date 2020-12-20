package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.pojo.SysDepartmentInfo;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoService
 * @DateTime 2020/12/19_13:20
 * @Deacription TODO
 */
public interface SysDepartmentInfoService {

    DataResult getPageDepartment(SysDepartmentInfoDto sysDepartmentInfoDto);

    DataResult updateDepartmentStatus(String depId, Integer loginStauts);

    DataResult addDepartment(SysDepartmentInfo sysDepartmentInfo);

    DataResult updateDepartment(SysDepartmentInfo sysDepartmentInfo);

    DataResult sysDepartmentInfoService(String depId);
}
