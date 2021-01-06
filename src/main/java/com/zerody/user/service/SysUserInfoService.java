package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysLoginUserInfoVo;

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

    /**************************************************************************************************
     **
     *  检查用户是否存在
     *
     * @param userName 账户名和号码
     * @return {@link String }
     * @author DaBai
     * @date 2020/12/30  9:57
     */
    String checkLoginUser(String userName);

    /**************************************************************************************************
     **
     * 登陆成功后获取用户信息
     *
     * @param id
     * @return {@link LoginUserInfoVo }
     * @author DaBai
     * @date 2021/1/4  10:13
     */
    LoginUserInfoVo getUserInfoById(String id);

    /**************************************************************************************************
     **
     * 查询角色是否绑定员工
     *
     * @param roleId
     * @return {@link Boolean }
     * @author DaBai
     * @date 2021/1/4  16:43 
     */
    Boolean checkRoleBind(String roleId);

    /**************************************************************************************************
     **
     *  查询角色是否绑定管理员
     *
     * @param roleId
     * @return {@link Boolean }
     * @author DaBai
     * @date 2021/1/4  18:12
     */
    Boolean checkPlatformRoleBind(String roleId);

    /**
     *
     *  根据id查询用户的下属
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/6 16:19
     * @param                id 用户id
     * @return               java.util.List<com.zerody.user.vo.SysUserSubordinateVo>
     */
    List<SysUserSubordinateVo> getUserSubordinates(String id);
}
