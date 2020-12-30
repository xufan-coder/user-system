package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.LoginCheckParamDto;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.vo.SysLoginUserInfoVo;
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

    DataResult deleteUserBatchByIds(List<String> ids);

    DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto);

    DataResult batchImportUser(MultipartFile file);

    DataResult deleteUserRole(String staffId, String roleId);

    /**************************************************************************************************
     **
     * 获取用户信息
     *
     * @param userName
     * @return {@link SysLoginUserInfoVo }
     * @author DaBai
     * @date 2020/12/29  17:59
     */
    SysLoginUserInfoVo getUserInfo(String userName);

    /**************************************************************************************************
     **
     * 根据ID获取用户信息
     *
     * @param id
     * @return {@link SysLoginUserInfoVo }
     * @author DaBai
     * @date 2020/12/29  18:00
     */
    SysUserInfo getUserById(String id);

    /**************************************************************************************************
     **
     * 校验密码
     *
     * @param userPwd 原密码
     * @param paramPwd   参数密码
     * @return {@link null }
     * @author DaBai
     * @date 2020/12/29  18:09
     */
    boolean checkPassword(String userPwd, String paramPwd);
}
