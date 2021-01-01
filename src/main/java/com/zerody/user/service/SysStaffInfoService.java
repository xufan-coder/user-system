package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.pojo.SysStaffInfo;
import com.zerody.user.pojo.SysUserInfo;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoService
 * @DateTime 2020/12/17_17:30
 * @Deacription TODO
 */
public interface SysStaffInfoService {
    DataResult addStaff(SysStaffInfo staff);

    DataResult addStaff(SetSysUserInfoDto setSysUserInfoDto);

    DataResult deleteStaffRole(String staffId, String roleId);

    DataResult staffAddRole(String staffId, String roleId);

    DataResult selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto);

    DataResult getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto);

    DataResult updateStaffStatus(String staffId, Integer status);

    DataResult getStaffInfoByOpenId(String openId);

    DataResult updateStaff(SetSysUserInfoDto setSysUserInfoDto);

    DataResult selectStaffById(String id);

    DataResult batchDeleteStaff(List<String> staffIds);

    DataResult deleteStaffById(String staffId);

    List<String> getStaffRoles(String userId, String companyId);
}
