package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.pojo.SysUserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysUserInfoService
 * @DateTime 2020/12/16_17:12
 * @Deacription TODO
 */
public interface SysUserInfoService {
    DataResult addUser(SysUserInfo userInfo);

    DataResult updateUser(SysUserInfo userInfo);

    DataResult deleteUserById(String userId);

    DataResult deleteUserBatchByIds(List<Integer> ids);

    DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto);

    DataResult batchImportUser(MultipartFile file);

    DataResult deleteUserRole(String staffId, String roleId);
}
