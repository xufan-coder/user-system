package com.zerody.user.mapper;

import com.zerody.user.dto.statis.UserAgeStatisQueryDto;
import com.zerody.user.dto.statis.UserSexStatisQueryDto;
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


    /**
     *
     * 伙伴年龄统计
     *
     * @author PengQiang
     * @description 彭强
     * @date 2023/5/3 11:33
     * @param param 过滤条件
     * @return java.lang.Integer
     */

    Integer getStatisAge(@Param("param") UserAgeStatisQueryDto param);


    /**
     *
     * 性别统计
     * @author PengQiang
     * @description 彭强
     * @date 2023/5/3 15:44
     * @param param 条件参数
     * @return java.lang.Integer
     */

    Integer getSexStatis(@Param("param") UserSexStatisQueryDto param);
}
