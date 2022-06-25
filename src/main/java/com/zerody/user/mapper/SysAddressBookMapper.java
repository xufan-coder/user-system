package com.zerody.user.mapper;

import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.vo.DepartInfoVo;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import com.zerody.user.vo.StaffInfoByCompanyVo;
import com.zerody.user.vo.SysAddressBookVo;
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
    List<SysAddressBookVo> queryAddressBook(@Param("companyIds")List<String> companyIds);

    /***
     * @description 部门
     * @author zhangpingping
     * @date 2021/9/24
     * @param []
     * @return
     */
    List<DepartInfoVo> queryDepartInfo(@Param("id") String id);

    /**
     * @description   团队
     * @author zhangpingping
     * @date 2021/9/25
     * @param
     * @return
     */
    List<DepartInfoVo> queryTeam(@Param("id") String id,@Param("departmentId")String departmentId);
    /***
     * @description  查询员工
     * @author zhangpingping
     * @date 2021/9/25
     * @param [staffByCompanyDto]
     * @return
     */
    List<StaffInfoByAddressBookVo> getStaffByCompany(@Param("param")StaffByCompanyDto staffByCompanyDto);


}
