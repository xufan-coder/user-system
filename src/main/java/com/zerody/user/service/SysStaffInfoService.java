package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;
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

    IPage<SysUserInfoVo> selectPageStaffByRoleId(SysStaffInfoPageDto sysStaffInfoPageDto);

    IPage<SysUserInfoVo> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto);

    void updateStaffStatus(String staffId, Integer status);


    void updateStaff(SetSysUserInfoDto setSysUserInfoDto);

    SysUserInfoVo selectStaffById(String id);

    void batchDeleteStaff(List<String> staffIds);

    void deleteStaffById(String staffId);

    List<String> getStaffRoles(String userId, String companyId);

    Map<String, Object> batchImportStaff(MultipartFile file);

    List<BosStaffInfoVo> getStaff(String companyId, String departId, String positionId);

    IPage<BosStaffInfoVo> getAdmins(AdminsPageDto dto);
}
