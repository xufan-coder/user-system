package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.IUser;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.dto.UserCopyDto;
import com.zerody.user.api.vo.*;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.*;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.vo.*;
import com.zerody.user.vo.SysUserInfoVo;
import com.zerody.user.vo.statis.DegreeVo;
import com.zerody.user.vo.statis.SignAndRescindVo;
import com.zerody.user.vo.statis.UserStatisTrendVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoService
 * @DateTime 2020/12/17_17:30
 * @Deacription TODO
 */
public interface SysStaffInfoService extends IService<SysStaffInfo> {
    static String getInitPwd() {
        StringBuffer sb = new StringBuffer();
        sb.append((char) (Math.random() * 26 + 'A'));
        double num = Math.random() * 10;
        if (num < 1) {
            num += 1;
        }
        sb.append((int) (num * 100000));
        sb.append((char) (Math.random() * 26 + 'a'));
        return sb.toString();
    }

    /**
     * 新增伙伴
     *
     * @param setSysUserInfoDto
     * @return
     */
    SysStaffInfo addStaff(SetSysUserInfoDto setSysUserInfoDto);


    UserCopyResultVo doCopyStaffInner(UserCopyDto param);

    IPage<BosStaffInfoVo> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto);


    IPage<BosStaffInfoVo> getPageAllSuperiorStaff(SysStaffInfoPageDto sysStaffInfoPageDto);

    void updateStaffStatus(String userId, Integer status, String leaveReason, String leaveType,UserVo user);


    void updateStaff(SetSysUserInfoDto setSysUserInfoDto, UserVo user,boolean isTraverse) throws ParseException, IllegalAccessException;

    /**
     * 获取伙伴详情
     *
     * @param id 员工id
     * @return
     */
    SysUserInfoVo selectStaffById(String id,boolean isTraverse,UserVo userVo);

    void deleteStaffById(String staffId);

    List<String> getStaffRoles(String userId, String companyId);

    String doBatchImportUser(MultipartFile file, UserVo user) throws Exception;

    String doBatchImportCompanyUser(MultipartFile file, UserVo user) throws Exception;

    List<BosStaffInfoVo> getStaff(String companyId, String departId, String positionId);

    IPage<BosStaffInfoVo> getAdmins(AdminsPageDto dto);

    /**
     * 获取员工数据，包含公司，部门，岗位
     *
     * @param userId
     * @return
     */
    UserDeptVo getUserDeptVo(String userId);

    /**
     * 获取员工下属部门
     *
     * @param userId
     * @return
     */
    List<String> getUserSubordinates(String userId);


    /**
     * 获取下级员工 线索汇总
     *
     * @param userId 当前登录用户id
     * @param dto    分页参数
     * @return java.lang.Object
     * @author PengQiang
     * @description DELL
     * @date 2021/1/9 12:52
     */
    IPage<SysUserClewCollectVo> getSubordinatesUserClewCollect(PageQueryDto dto, String userId);


    CopyStaffInfoVo selectStaffInfo(String staffId);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 根据用户id查询员工信息
    * @Date: 2022/12/9 9:55
    */
    SysUserInfoVo selectStaffByUserId(String userId,UserVo userVo,boolean isTraverse);

    IPage<BosStaffInfoVo> getWxPageAllStaff(SysStaffInfoPageDto dto);

    void doEmptySubordinatesUserClew(String id);

    AdminVo getIsAdmin(UserVo user);

    String getDepartId(String userId);

    StaffInfoVo getStaffInfo(String userId);

    List<com.zerody.user.api.vo.SysUserInfoVo> getUserByDepartOrRole(String departId, String roleId, String companyId);

    List<com.zerody.user.api.vo.SysUserInfoVo> getUserByDepartOrRoleJob(String departId, String roleId, String companyId);

    List<com.zerody.user.api.vo.SysUserInfoVo> getSuperiorUesrByUserAndRole(String userId, String roleId);

    IPage<UserPerformanceReviewsVo> getPagePerformanceReviews(UserPerformanceReviewsPageDto param) throws ParseException;
    List<UserPerformanceReviewsVo> getPagePerformanceReviewsList(UserPerformanceReviewsPageDto param)throws ParseException;

    List<UserPerformanceReviewsVo> doPerformanceReviewsExport(UserPerformanceReviewsPageDto param, HttpServletResponse res) throws IOException, ParseException;

    StaffInfoVo getStaffInfoByCardUserId(String cardUserId);

    List<SysDepartmentInfoVo> getUserSubordinateStructure(String userId);

    List<StaffInfoVo> getDepartDirectStaffInfo(String departId);

    IPage<BosStaffInfoVo> getPageAllActiveDutyStaff(SysStaffInfoPageDto sysStaffInfoPageDto);

    List<StaffInfoVo> getStaffInfoByIds(List<String> userId);

    List<String> getSubordinateUserByUserId(String userId);

    List<StaffInfoByCompanyVo> getStaffByCompany(String companyId, Integer isShowLeave);

    List<CustomerQueryDimensionalityVo> getCustomerQuerydimensionality(UserVo user);

    SysStaffInfoDetailsVo getStaffDetailsCount(String userId);

    List<CheckLoginVo> getNotSendPwdSmsStaff(String companyId);

    void doSendStaffPwdSms(List<CheckLoginVo> subList);

    Boolean getLoginUserIsSuperion(UserVo user, String userId);

    List<SysUserInfo> getJobUser(String parentId);

    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 查询所有在职用户
     * @Date: 2023/1/4 21:14
     */
    List<StaffInfoVo> getAllDuytUserInner();

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取员工详情
    * @Date: 2022/10/20 16:23
    */
    SysStaffInfo getUserInfo(String id);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 伙伴数据统计
    * @Date: 2022/11/11 10:13
    */
    UserStatisticsVo statisticsUsers(SetSysUserInfoDto userInfoDto);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 查询伙伴概况
    * @Date: 2023/4/28 17:28
    */
    UserStatisticsVo getUserOverview(UserStatisQueryDto param);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 统计伙伴签约与解约(今日、本月)
    * @Date: 2023/4/28 20:19
     * @return
    */
    SignAndRescindVo statisticsContractAndRescind(UserStatisQueryDto param);

    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 获取解约原因分析
     * @Date: 2023/4/29 10:30
     */
    List<TerminationAnalysisVo> getTerminationAnalysis(UserStatisQueryDto param);

    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 获取学历分析
     * @Date: 2023/4/29 10:30
     */
    DegreeAnalysisVo getDegreeAnalysis(UserStatisQueryDto param);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 统计学历
    * @Date: 2023/5/8 14:31
    */
    int getDegree(UserStatisQueryDto param);

    /**
     * 获取高中以下
     *
     * @return
     */
    int getBelowHighSchool(UserStatisQueryDto param);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 签约数据汇总报表
    * @Date: 2023/5/3 15:47
    */
    List<SignSummaryVo> getSignSummary(UserStatisQueryDto param, UserVo user);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 伙伴档案分析(pc)
    * @Date: 2023/5/4 18:25
    */
    IPage<SignSummaryVo> getFileSummary(UserStatisQueryDto param);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取员工信息
    * @Date: 2022/11/28 9:31
    */
    List<AppUserVo> querySysStaffInfoList(String departmentId);

    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 查询企业下的员工信息
     * @Date: 2022/11/28 10:21
     */
    List<AppUserVo> queryCompStaff(String compId);

    /**
     * 根据用户id 校验是否为企业负责人
     *
     * @param userId
     * @return
     */
    Boolean checkCompInCharge(String userId);

    /**
    *
    *  @description   查询所有在职伙伴
    *  @author        YeChangWei
    *  @date          2022/11/28 17:34
    *  @return        java.util.List<com.zerody.user.api.vo.StaffInfoVo>
    */
    List<StaffInfoByAddressBookVo> getAllUser(ComUserQueryDto queryDto);

    Map<String, Object> getSameDept(String userId, String chooseUserId);

    List<String> getLeaderUserId(String userId,Integer sameDept);
    String getLeaderUserId(String userId);

    /**获取离职伙伴信息*/
    LeaveUserInfoVo getQuitUserInfo(String userId);

    void updateIdCard(IdCardUpdateDto dto);

    Integer getCheckUserId(String userId, String chargeId);

    Map<String, Object> getChargeLevel(String chargeId);

    StaffInfoVo getOneStaffInfo(String phone, String idCard, String companyId);

    List<String> getDeptLeader(String userId,String signDeptId);
    /**
    *
    *  @description   通过公司id获取公司在职用户
    *  @author        YeChangWei
    *  @date          2023/4/7 19:56
    *  @return        java.util.List<com.zerody.user.api.vo.StaffInfoVo>
    */
    List<StaffInfoVo> getCompanyIdInner(String companyId);

    SysUserInfoVo getUserById(String userId);

    List<StaffInfoByAddressBookVo> pageGetUserList(SysStaffInfoPageDto dto);

    List<String> getUserIdByCompanyIds(List<String> companyIds);

    List<String> getUserIdByDeptIds(List<String> deptIds);

    List<ExpireTimeNoticeVo> getExpireTimeStaff(ExpireTimeNoticeDto dto);

    List<AdviserUserVo> getDeptUserList(String userId);

    List<UserInductionVo> getUserInductionList();

    void doSyncCrmUserAdviser(String staffId, IUser userData);

    void doSyncAdviserState(AdviserStateDto param);

    void doSyncAdviserStateBatch(List<String> ids);

    void doSyncCrmDept(String companyId);
}
