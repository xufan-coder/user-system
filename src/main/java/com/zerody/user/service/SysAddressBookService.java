package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.DepartInfoDto;
import com.zerody.user.dto.DepartureDetailsDto;
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

    /**
     * @Author: ljj
     * @param
     * @Description: 获取公司下拉(是否显示)
     * @Date: 2023/11/8
     */
    List<SysAddressBookVo> selectAddressBooks(List<String> list,Integer isProData);

    /***
     * @description 部门
     * @author zhangpingping
     * @date 2021/9/25
     * @param departInfoDto
     * @return
     */
    List<DepartInfoVo> queryDepartInfo(DepartInfoDto departInfoDto);
    /**
     *
     *  @description   部门(有是否显示两种)
     *  @author        YeChangWei
     *  @date          2023/9/1 16:02
     *  @return        java.util.List<com.zerody.user.vo.DepartInfoVo>
     */
    List<DepartInfoVo> queryDepartInfoAll(DepartInfoDto departInfoDto);


    /***
     * @description 团队
     * @author zhangpingping
     * @date 2021/9/25
     * @param departInfoDto
     * @return
     */
    List<DepartInfoVo> queryTeam(DepartInfoDto departInfoDto);

    /**
     *
     *  @description   团队(有是否显示两种)
     *  @author        YeChangWei
     *  @date          2023/9/1 16:05
     *  @return        java.util.List<com.zerody.user.vo.DepartInfoVo>
     */
    List<DepartInfoVo> queryTeamAll(DepartInfoDto departInfoDto);

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
    IPage<DepartureDetailsVo> getDepartureUserList(DepartureDetailsDto param);
}
