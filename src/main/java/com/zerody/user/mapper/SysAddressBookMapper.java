package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.dto.DepartInfoDto;
import com.zerody.user.dto.DepartureDetailsDto;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.dto.statis.UserStatisQueryDto;
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

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取企业列表
    * @Date: 2023/5/3 17:00
    */
    List<SysAddressBookVo> queryCompanyList(@Param("param") UserStatisQueryDto param);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 分页获取企业列表
    * @Date: 2023/5/4 18:28
    */
    IPage<SignSummaryVo> pageCompanyList(@Param("param") UserStatisQueryDto param, Page<SignSummaryVo> iPage);

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
    IPage<DepartureDetailsVo> getDepartureUserList(@Param("param") DepartureDetailsDto staffByCompanyDto, Page<DepartureDetailsVo> iPage);

    /**
    *
    *  @description    部门(有是否显示两种)
    *  @author        YeChangWei
    *  @date          2023/9/1 16:08
    *  @return        java.util.List<com.zerody.user.vo.DepartInfoVo>
    */
    List<DepartInfoVo> queryDepartInfoAll(@Param("param")DepartInfoDto departInfoDto);
    /**
    *
    *  @description    团队(有是否显示两种)
    *  @author        YeChangWei
    *  @date          2023/9/1 16:08
    *  @return        java.util.List<com.zerody.user.vo.DepartInfoVo>
    */
    List<DepartInfoVo> queryTeamAll(@Param("param")DepartInfoDto departInfoDto);
}
