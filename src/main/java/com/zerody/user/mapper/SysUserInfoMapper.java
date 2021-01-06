package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.vo.LoginUserInfoVo;
import com.zerody.user.vo.SysLoginUserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author  DaBai
 * @date  2020/12/29 16:19
 */

public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

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
    String selectByUserNameOrPhone(@Param("userName")String userName);

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

    List<SysUserSubordinateVo> getUserSubordinates(List<SysDepartmentInfo> deps);
}
