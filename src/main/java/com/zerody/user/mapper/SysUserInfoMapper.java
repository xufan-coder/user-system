package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.vo.CheckLoginVo;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysLoginUserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author  DaBai
 * @date  2020/12/29 16:19
 */

public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

    /**
     *  通过手机号 查询用户
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:16
     * @param                userInfo
     * @return               java.util.List<com.zerody.user.domain.SysUserInfo>
     */
    List<SysUserInfo> selectUserByPhoneOrLogName(@Param("userInfo") SysUserInfo userInfo);

    /**
     * description
     * * @param userName
     * * @return {@link SysLoginUserInfoVo }
     */
    SysLoginUserInfoVo selectUserInfo(@Param("userName")String userName);

    /**
     * description
     * * @param userName
     * * @return {@link String }
     */
    CheckLoginVo selectByUserNameOrPhone(@Param("userName")String userName);

    /**
     * description
     * * @param id
     * * @return {@link LoginUserInfoVo }
     */
    LoginUserInfoVo selectLoginUserInfo(@Param("id")String id);

    /**
     * description
     * * @param phoneNumber
     * * @return {@link Boolean }
     */
    Boolean selectUserByPhone(@Param("phoneNumber")String phoneNumber);

    /**
     *
     *  获取员工下级
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/6 20:06
     * @param                deps
     * @return               java.util.List<com.zerody.user.api.vo.SysUserSubordinateVo>
     */
    List<String> getUserSubordinates(@Param("deps") List<SysDepartmentInfo> deps);

    /**
     *
     * 查询是否是企业管理员
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:16
     * @param                userId
     * @return               java.lang.Boolean
     */
    Boolean checkUserAdmin(@Param("userId")String userId);

    /**
     *
     * 是否是后台管理员
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:17
     * @param                userId
     * @return               java.lang.Boolean
     */
    Boolean checkBackAdminUser(@Param("userId")String userId);

    /**
     *
     * 查询全部用户id
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:18
     * @param
     * @return               java.util.List<java.lang.String>
     */
    List<Map<String, String>> selectAllUserId();
}
