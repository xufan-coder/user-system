package com.zerody.user.mapper;

import com.zerody.user.dto.statis.UserStatisQueryDto;
import org.apache.ibatis.annotations.Param;

/**
 * @author PengQiang
 * @ClassName UserStatisMapper
 * @DateTime 2023/4/29 10:58
 */
public interface UserStatisMapper  {


    /**
     *
     * 查询签约用户
     * @author PengQiang
     * @description 彭强
     * @date 2023/4/29 15:48
     * @param param 查询参数
     * @return int
     */

    int getStatisSigning(@Param("param") UserStatisQueryDto param);

    /**
     *
     * 查询解约用户
     * @author PengQiang
     * @description 彭强
     * @date 2023/4/29 15:48
     * @param param 查询参数
     * @return int
     */

    int getStatisUnSigning(@Param("param") UserStatisQueryDto param);
}
