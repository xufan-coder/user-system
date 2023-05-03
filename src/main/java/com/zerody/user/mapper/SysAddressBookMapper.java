package com.zerody.user.mapper;

import com.zerody.user.dto.DepartInfoDto;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月24日 16:20
 */

public interface SysAddressBookMapper {

    /***
     * @description 获取公司下拉
     * @author zhangpingping
     * @date 2021/9/24
     * @param []
     * @return
     */
    List<SysAddressBookVo> queryAddressBook(@Param("companyIds")List<String> companyIds,@Param("isProData")Integer isProData);

    /***
     * @description 部门
     * @author zhangpingping
     * @date 2021/9/24
     * @param []
     * @return
     */
    List<DepartInfoVo> queryDepartInfo(@Param("param") DepartInfoDto departInfoDto);

    /**
     * @description   团队
     * @author zhangpingping
     * @date 2021/9/25
     * @param
     * @return
     */
    List<DepartInfoVo> queryTeam(@Param("param") DepartInfoDto departInfoDto);

    /***
     * @description  查询员工
     * @author zhangpingping
     * @date 2021/9/25
     * @param [staffByCompanyDto]
     * @return
     */
    List<StaffInfoByAddressBookVo> getStaffByCompany(@Param("param")StaffByCompanyDto staffByCompanyDto);

    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 获取离职伙伴列表明细
     * @Date: 2023/5/3 14:29
     */
    List<DepartureDetailsVo> getDepartureUserList(@Param("param") StaffByCompanyDto staffByCompanyDto);

}
