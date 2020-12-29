package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.vo.SysLoginUserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

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
}
