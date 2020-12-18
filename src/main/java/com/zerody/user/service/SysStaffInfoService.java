package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.pojo.SysStaffInfo;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoService
 * @DateTime 2020/12/17_17:30
 * @Deacription TODO
 */
public interface SysStaffInfoService {
    DataResult addStaff(SysStaffInfo staff);

    DataResult deleteStaffRole(String staffId, String roleId);

    DataResult staffAddRole(String staffId, String roleId);

    DataResult selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto);

    DataResult getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto);
}
