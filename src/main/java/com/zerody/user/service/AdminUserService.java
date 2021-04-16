package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.AdminUserInfo;
import com.zerody.user.dto.AdminUserDto;
/**
 *
 *
 * @author               PengQiang
 * @description          DELL
 * @date                 2021/1/19 14:35
 * @param
 * @return
 */
public interface AdminUserService extends IService<AdminUserInfo> {

    /**
     *
     * 登录验证
     * @author               dabai
     * @description          DELL
     * @date                 2021/1/19 14:34
     * @param                phone
     * @return               com.zerody.user.api.vo.AdminUserInfo
     */
    com.zerody.user.api.vo.AdminUserInfo checkLoginAdmin(String phone);

    /**
     *
     * 添加管理员
     * @author               dabai
     * @description          DELL
     * @date                 2021/1/19 14:35
     * @param                data
     * @return               com.zerody.user.domain.AdminUserInfo
     */
    AdminUserInfo addAdminUser(AdminUserDto data);

    /**
     *
     * 修改管理员
     * @author               dabai
     * @description          DELL
     * @date                 2021/1/19 14:35
     * @param                data
     * @return               void
     */
    void updateAdminUser(AdminUserInfo data);

    /**
     *
     *  删除管理员
     * @author               dabai
     * @description          DELL
     * @date                 2021/1/19 14:36
     * @param                id
     * @return               void
     */
    void removeAdminUser(String id);


    /**
     *
     *  修改管理员角色
     * @author               dabai
     * @description          DELL
     * @date                 2021/1/19 14:36
     * @param                id
     * @param                 roleId
     * @return               void
     */
    void updateRole(String id, String roleId);

    /**************************************************************************************************
     **
     * 根据手机号获取管理员用户
     *
     * @param null
     * @return {@link null }
     * @author DaBai
     * @date 2021/1/21  10:45
     */
    com.zerody.user.api.vo.AdminUserInfo getByMobile(String mobile);

    /**************************************************************************************************
     **
     * 获取平台角色ID
     *
     * @param userId
     * @return {@link String }
     * @author DaBai
     * @date 2021/1/29  14:59
     */
    String getPlatfoemRoles(String userId);

    /**************************************************************************************************
     **
     * 根据用户id获取平台管理员信息
     *
     * @param null
     * @return {@link AdminUserInfo }
     * @author DaBai
     * @date 2021/1/29  15:11
     */
    com.zerody.user.api.vo.AdminUserInfo getUserById(String userId);
}
