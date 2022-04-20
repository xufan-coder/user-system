package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.bean.DataResult;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminUserInfo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.dto.SetUpdateAvatarDto;
import com.zerody.user.dto.SubordinateUserQueryDto;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName SysUserInfoService
 * @DateTime 2020/12/16_17:12
 * @Deacription TODO
 */
public interface SysUserInfoService extends IService<SysUserInfo> {
    /**
     *
     * 添加用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:26
     * @param                userInfo
     * @return               com.zerody.common.bean.DataResult
     */
    DataResult addUser(SysUserInfo userInfo);

    /**
     *
     * 修改用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:26
     * @param                userInfo
     * @return               com.zerody.common.bean.DataResult
     */
    DataResult updateUser(SysUserInfo userInfo);

    /**
     *
     * 修改用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:26
     * @param                userId
     * @return               com.zerody.common.bean.DataResult
     */
    DataResult deleteUserById(String userId);

    /**
     *
     * 批量删除用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:26
     * @param                ids
     * @return               com.zerody.common.bean.DataResult
     */
    DataResult deleteUserBatchByIds(List<String> ids);

    /**
     *
     *  分页查询用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:27
     * @param                sysUserInfoPageDto
     * @return               com.zerody.common.bean.DataResult
     */
    DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto);



    /**
     *
     * 删除用户角色
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:27
     * @param                staffId
     * @param                roleId
     * @return               com.zerody.common.bean.DataResult
     */
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
    CheckLoginVo checkLoginUser(String userName);

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

    /**************************************************************************************************
     **
     *  查询用户是否是企业管理员
     *
     * @param userId
     * @return {@link null }
     * @author DaBai
     * @date 2021/1/11  9:58
     */
    Boolean checkUserAdmin(String userId);

    /**************************************************************************************************
     **
     *  查询用户是否是admin账户
     *
     * @param userId
     * @return {@link Boolean }
     * @author DaBai
     * @date 2021/1/11  9:59
     */
    Boolean checkBackAdminUser(String userId);

    /**************************************************************************************************
     **
     * 获取所有yonghuID
     *
     * @param null
     * @return {@link List<String> }
     * @author DaBai
     * @date 2021/1/11  18:42
     */
    List<Map<String, String>> selectAllUserId();

    /**
     *
     *  通过企业id 员工id获取用户id
     * @author               PengQiang
     * @description
     * @date                 2021/1/25 14:26
     * @param                 staffId
     * @return               java.lang.String
     */
    String getUserIdByCompIdOrStaffId(String staffId);

    void updatePerformancePassword(AdminUserInfo userInfo);

    String getShowPerformancePassword(String id);

    void updateUserIsSignOrder(String userId);

    void updateUserAvatar(SetUpdateAvatarDto param);

    void getAvatarImageByUserId(String userId, HttpServletRequest request, HttpServletResponse response);

    UserTypeInfoVo getUserTypeInfo(UserVo user);

    List<String> doUserIsDeleted();

    void updateRedundancyUserName();

    StaffInfoVo getUserByCardUserId(String id);

    void doUserEditInfo();

    SysLoginUserInfoVo selectTransUserInfo(String userId);


    List<SubordinateUserQueryVo> getSubordinateUser(SubordinateUserQueryDto param);


    List<SubordinateUserQueryVo> getSubordinateUserPartner(SubordinateUserQueryDto param);

    List<String> getAllBeUserOrceoIdsInner();
}
