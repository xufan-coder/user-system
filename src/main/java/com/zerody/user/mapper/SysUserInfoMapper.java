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
}
