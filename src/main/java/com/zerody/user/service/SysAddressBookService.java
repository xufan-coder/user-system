package com.zerody.user.service;

import com.zerody.user.dto.DepartInfoDto;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.vo.DepartInfoVo;
import com.zerody.user.vo.DepartureDetailsVo;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import com.zerody.user.vo.SysAddressBookVo;

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
    List<SysAddressBookVo> queryAddressBook(List<String> list,Integer isProData);

    /***
     * @description 部门
     * @author zhangpingping
     * @date 2021/9/25
     * @param departInfoDto
     * @return
     */
    List<DepartInfoVo> queryDepartInfo(DepartInfoDto departInfoDto);

    /***
     * @description 团队
     * @author zhangpingping
     * @date 2021/9/25
     * @param departInfoDto
     * @return
     */
    List<DepartInfoVo> queryTeam(DepartInfoDto departInfoDto);

    /***
     * @description  查询员工
     * @author zhangpingping
     * @date 2021/9/25
     * @param [staffByCompanyDto]
     * @return
     */
    List<StaffInfoByAddressBookVo> getStaffByCompany(StaffByCompanyDto staffByCompanyDto);

    /**
    * @Author: chenKeFeng
    * @param  
    * @Description: 获取离职伙伴列表明细
    * @Date: 2023/5/3 14:40
    */
    List<DepartureDetailsVo> getDepartureUserList(StaffByCompanyDto staffByCompanyDto);
    
}
