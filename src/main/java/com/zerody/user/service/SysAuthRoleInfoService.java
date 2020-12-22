package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysAuthRoleDto;
import com.zerody.user.pojo.SysAuthRoleInfo;
import com.zerody.user.dto.SysAuthRolePageDto;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysAuthRoleInfoService
 * @DateTime 2020/12/17_18:19
 * @Deacription TODO
 */
public interface SysAuthRoleInfoService {
    DataResult selectRolePage(SysAuthRolePageDto sysAuthRolePageDto);

    DataResult addRole(SysAuthRoleInfo sysAuthRoleInfo);

    DataResult updateRole(SysAuthRoleInfo sysAuthRoleInfo);

    DataResult deleteRoleById(String roleId);

    DataResult deleteBatchByIdds(List<String> roleIds);

    DataResult deleteRoleDownStaff(SysAuthRoleDto sysAuthRoleDto);

    DataResult selectRoleByStaffId(String staffId );

    DataResult operationRoleDownMenu(SysAuthRoleDto sysAuthRoleDto);
}
