package com.zerody.user.service;

import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.vo.DepartInfoVo;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import com.zerody.user.vo.StaffInfoByCompanyVo;
import com.zerody.user.vo.SysAddressBookVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月24日 16:22
 */

public interface SysAddressBookService {
    /***
     * @description 获取公司下拉
     * @author zhangpingping
     * @date 2021/9/24
     * @param []
     * @return
     */
    List<SysAddressBookVo> queryAddressBook();

    /***
     * @description 部门
     * @author zhangpingping
     * @date 2021/9/25
     * @param [id]
     * @return
     */
    List<DepartInfoVo> queryDepartInfo(String id);

    /***
     * @description 团队
     * @author zhangpingping
     * @date 2021/9/25
     * @param [id]
     * @return
     */
    List<DepartInfoVo> queryTeam(String id,String departmentId);

    /***
     * @description  查询员工
     * @author zhangpingping
     * @date 2021/9/25
     * @param [staffByCompanyDto]
     * @return
     */
    List<StaffInfoByAddressBookVo> getStaffByCompany(StaffByCompanyDto staffByCompanyDto);
}
