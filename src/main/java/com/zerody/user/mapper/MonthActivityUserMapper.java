package com.zerody.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.MonthActivityUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *  月初在职人数mapper
 * @author               PengQiang
 * @description          DELL
 * @date                 2022/1/15 14:34
 */
public interface MonthActivityUserMapper extends BaseMapper<MonthActivityUser> {

    /**
     *
     * 获取企业业务人在职人数
     * @author               PengQiang
     * @description          DELL
     * @date                 2022/1/15 15:12
     * @param                roleIds
     * @return               java.util.List<com.zerody.user.domain.MonthActivityUser>
     */
    List<MonthActivityUser> getComapnyMonthAcitvityUser(@Param("roleIds") List<String> roleIds);

    /**
     *
     * 获取部门、团队业务人在职人数
     * @author               PengQiang
     * @description          DELL
     * @date                 2022/1/15 15:12
     * @param                roleIds
     * @return               java.util.List<com.zerody.user.domain.MonthActivityUser>
     */
    List<MonthActivityUser> getDepartMonthAcitvityUser(@Param("roleIds") List<String> roleIds);
}