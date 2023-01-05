package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.api.vo.UserIdentifierQueryVo;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.domain.UnionRoleStaff;
import com.zerody.user.dto.ComUserQueryDto;
import com.zerody.user.dto.SubordinateUserQueryDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author DaBai
 * @date 2020/12/29 16:19
 */

public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

    /**
     * 通过手机号 查询用户
     *
     * @param userInfo
     * @return java.util.List<com.zerody.user.domain.SysUserInfo>
     * @author PengQiang
     * @description DELL
     * @date 2021/1/19 15:16
     */
    List<SysUserInfo> selectUserByPhoneOrLogName(@Param("userInfo") SysUserInfo userInfo);

    /**
     * description
     * * @param userName
     * * @return {@link SysLoginUserInfoVo }
     */
    SysLoginUserInfoVo selectUserInfo(@Param("userName") String userName);

    /**
     * description
     * * @param userName
     * * @return {@link String }
     */
    CheckLoginVo selectByUserNameOrPhone(@Param("userName") String userName);

    /**
     * description
     * * @param id
     * * @return {@link LoginUserInfoVo }
     */
    LoginUserInfoVo selectLoginUserInfo(@Param("id") String id);

    /**
     * description
     * * @param phoneNumber
     * * @return {@link Boolean }
     */
    Boolean selectUserByPhone(@Param("phoneNumber") String phoneNumber);

    /**
     * 获取员工下级
     *
     * @param deps
     * @return java.util.List<com.zerody.user.api.vo.SysUserSubordinateVo>
     * @author PengQiang
     * @description DELL
     * @date 2021/1/6 20:06
     */
    List<String> getUserSubordinates(@Param("deps") List<SysDepartmentInfo> deps);

    /**
     * 查询是否是企业管理员
     *
     * @param userId
     * @return java.lang.Boolean
     * @author PengQiang
     * @description DELL
     * @date 2021/1/19 15:16
     */
    Boolean checkUserAdmin(@Param("userId") String userId);

    /**
     * 是否是后台管理员
     *
     * @param userId
     * @return java.lang.Boolean
     * @author PengQiang
     * @description DELL
     * @date 2021/1/19 15:17
     */
    Boolean checkBackAdminUser(@Param("userId") String userId);

    /**
     * 查询全部用户id
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author PengQiang
     * @description DELL
     * @date 2021/1/19 15:18
     */
    List<Map<String, String>> selectAllUserId();

    /**
     * 找到要删除的用户id
     *
     * @param []
     * @return java.util.List<java.lang.String>
     * @author PengQiang
     * @description DELL
     * @date 2021/4/1 20:56
     */
    @Select("SELECT id FROM sys_user_info where is_deleted = 1")
    List<String> getUserIdsByIsDeleted();

    /**
     * 修改用户状态
     *
     * @param userIds
     * @return void
     * @author PengQiang
     * @description DELL
     * @date 2021/4/1 20:59
     */
    void updateUserStatusAndIsDeleted(@Param("userIds") List<String> userIds);

    /**
     * 获取修改用户名称的用户
     *
     * @param
     * @return java.util.List<com.zerody.user.api.vo.StaffInfoVo>
     * @author PengQiang
     * @description DELL
     * @date 2021/4/15 19:40
     */
    List<StaffInfoVo> getUserMobilyNameInfo();

    /**
     * 修改修改名称状态为 未修改
     *
     * @param staffInfos
     * @return void
     * @author PengQiang
     * @description DELL
     * @date 2021/4/15 19:54
     */
    void updateUserNameModilyState(@Param("users") List<StaffInfoVo> staffInfos);

    List<Map<String, String>> getDepartmentEditInfo();

    void updateDepartEditInfo(@Param("users") List<Map<String, String>> userMap);


    SysLoginUserInfoVo selectTransUserInfo(@Param("userId") String userId);

    List<SubordinateUserQueryVo> getSubordinateUser(@Param("param") SubordinateUserQueryDto param);

    List<ReportFormsQueryVo> getUserByDepartId(@Param("departId") String departId, @Param("roleIds") List<String> roleIds);

    List<ReportFormsQueryVo> getUserById(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

    List<String> getAllBeUserOrceoIds();

    IPage<BosStaffInfoVo> getUserPage(@Param("param") SysStaffInfoPageDto param, IPage<BosStaffInfoVo> ceoPage);

    List<UserIdentifierQueryVo> getUserIdentifierByIds(@Param("ids") List<String> ids);


    StaffInfoVo getUserInfoByMobile(@Param("mobile") String mobile);

    List<AppUserNotPushVo> getNotPushAppUser();

    String getDepartIdByUserId(@Param("userId") String userId);

    List<String> getUserIdsByRoleNames(@Param("roleName") String roleNames);

    List<String> getAllCompanyAdmin();

    List<String> getAllDepartAdmin();

    List<AppUserNotPushVo> getBirthdayUserIds(@Param("month") String month,@Param("day") String day,@Param("userId") String userId);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 查询所有在职用户
    * @Date: 2023/1/4 21:14
    */
    List<StaffInfoVo> getAllDuytUser();
    /**
    *
    *  @description   通过客户负责人id查询角色
    *  @author        YeChangWei
    *  @date          2022/11/8 18:18
    *  @return        com.zerody.user.domain.UnionRoleStaff
    */
    UnionRoleStaff getUserIdUnionRoleStaff(@Param("userId") String userId);
    /**
    *
    *  @description   查询所有在职人员 or 入职时间是在当天的
    *  @author        YeChangWei
    *  @date          2022/11/10 14:20
    *  @return        java.util.List<com.zerody.user.vo.AppUserNotPushVo>
    */
    List<AppUserNotPushVo> getAnniversaryUserList(@Param("userId") String userId);
    /**
    *
    *  @description   查询所有在职伙伴
    *  @author        YeChangWei
    *  @date          2022/11/28 17:40
    *  @return        java.util.List<com.zerody.user.api.vo.StaffInfoVo>
    */
    List<StaffInfoByAddressBookVo> getAllUser(@Param("queryDto") ComUserQueryDto queryDto);

    @Select({ "<script> update sys_user_info set status = 0 where id=#{userId} </script>" })
    void updateLeaveState(@Param("userId")  String userId);

    List<SubordinateUserQueryVo> getLeaveUser(@Param("param") SubordinateUserQueryDto param);

    Boolean getByMobileOrCard(@Param("mobile") String mobile, @Param("certificateCard") String certificateCard);
}
