package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.domain.SysStaffInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoService
 * @DateTime 2020/12/17_17:30
 * @Deacription TODO
 */
public interface SysStaffInfoService {
    void addStaff(SysStaffInfo staff);

    void addStaff(SetSysUserInfoDto setSysUserInfoDto);

    void deleteStaffRole(String staffId, String roleId);

    void staffAddRole(String staffId, String roleId);

    DataResult selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto);

    DataResult getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto);

    void updateStaffStatus(String staffId, Integer status);

    DataResult getStaffInfoByOpenId(String openId);

    void updateStaff(SetSysUserInfoDto setSysUserInfoDto);

    DataResult selectStaffById(String id);

    void batchDeleteStaff(List<String> staffIds);

    void deleteStaffById(String staffId);

    List<String> getStaffRoles(String userId, String companyId);

    Map<String, Object> batchImportStaff(MultipartFile file);
}
