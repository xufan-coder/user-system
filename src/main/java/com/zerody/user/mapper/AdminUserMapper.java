package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.AdminUserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author  DaBai
 * @date  2021/1/9 13:30
 */

public interface AdminUserMapper extends  BaseMapper<AdminUserInfo>{

    String selectRoleByUserId(@Param("userId") String userId);
}
