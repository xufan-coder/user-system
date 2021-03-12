package com.zerody.user.mapper;

import java.util.List;

import com.zerody.user.dto.UserPerformanceReviewsPageDto;
import com.zerody.user.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SysStaffInfoPageDto;

/**
 * @author DELL
 */
public interface SysStaffInfoMapper extends BaseMapper<SysStaffInfo> {


    /**
     *
     * 通过企业或者用户id查询员工
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:06
     * @param                userId
     * @param                compId
     * @return               com.zerody.user.vo.SysStaffInfoVo
     */
    SysStaffInfoVo selectByUserIdAndCompId(@Param("userId") String userId, @Param("compId")String compId);

    /**
     *
     * 分页查询员工
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:07
     * @param                sysStaffInfoPageDto
     * @param                iPage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BosStaffInfoVo>
     */
    IPage<BosStaffInfoVo> getPageAllStaff(@Param("staff")SysStaffInfoPageDto sysStaffInfoPageDto,IPage iPage);

    /**
     * 查询用户
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:07
     * @param                companyId
     * @return               java.util.List<java.lang.String>
     */
    List<String> selectUserByCompanyId(String companyId);

    /**
     *
     * openId查询员工
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:08
     * @param                openId
     * @return               com.zerody.user.vo.SysUserInfoVo
     */
    SysUserInfoVo getStaffInfoByOpenId(String openId);

    /**
     *
     * 查看员工详情
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:08
     * @param                id
     * @return               com.zerody.user.vo.SysUserInfoVo
     */
    SysUserInfoVo selectStaffById(String id);

    /**
     *
     *  通过用户查询员工
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:08
     * @param                userId
     * @return               com.zerody.user.vo.SysUserInfoVo
     */
    SysUserInfoVo selectStaffByUserId(@Param("userId")String userId);

    /**
     *
     * 用角色查询员工
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:09
     * @param                userId
     * @param                companyId
     * @return               java.util.List<java.lang.String>
     */
    List<String> selectStaffRoles(@Param("userId")String userId, @Param("companyId")String companyId);

    /**
     *
     * 通过 (企业、部门、岗位) 查询员工
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:14
     * @param                companyId
     * @param                departId
     * @param                 positionId
     * @return               java.util.List<com.zerody.user.vo.BosStaffInfoVo>
     */
    List<BosStaffInfoVo> getStaff(@Param("companyId")String companyId, @Param("departId")String departId, @Param("positionId")String positionId);

    /**
     *
     *  分页查询
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:13
     * @param                dto
     * @param                voIpage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BosStaffInfoVo>
     */
    IPage<BosStaffInfoVo> selectAdmins(@Param("dto")AdminsPageDto dto, IPage voIpage);

    /**
     *
     * 用户id查询员工id
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:13
     * @param                userId
     * @return               java.lang.String
     */
    @Select({ "<script> select id from sys_staff_info where user_id=#{userId} limit 0,1 </script>" })
	public String getStaffIdByUserId(@Param("userId")String userId);

    /**
     *
     * 查询用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:13
     * @param                staffId
     * @return               com.zerody.user.api.vo.UserDeptVo
     */
    public UserDeptVo selectUserDeptInfoById(@Param("staffId")String staffId);

    /**
     *
     * 获取员工的部门岗位
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/9 13:40
     * @param                staffId
     * @return               com.zerody.user.vo.SysUserClewCollectVo
     */
    SysUserClewCollectVo selectUserInfo(String staffId);

    /**
     * 查询员工信息
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/9 14:51
     * @param                deps
     * @param               iPage
     * @param               compId
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.SysUserClewCollectVo>
     */
    IPage<SysUserClewCollectVo> getStaffByDepIds(@Param("ids") List<String> deps, IPage<SysUserClewCollectVo> iPage, @Param("compId") String compId,@Param("adminId")String adminId);


    List<SysUserClewCollectVo> getStaffAllByDepIds(@Param("ids") List<String> deps, @Param("compId") String compId);

    /**************************************************************************************************
     **
     *  查询员工信息//创建管理员账户使用
     *
     * @param staffId
     * @return {@link CopyStaffInfoVo }
     * @author DaBai
     * @date 2021/1/11  10:50
     */
    CopyStaffInfoVo selectStaffInfo(@Param("staffId")String staffId);

    /**
     *
     *  获取企业全部在职员工
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/13 15:15
     * @param                dto
     * @param                iPage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BosStaffInfoVo>
     */
    IPage<BosStaffInfoVo> getWxPageAllStaff(@Param("dto") SysStaffInfoPageDto dto, IPage<BosStaffInfoVo> iPage);

    IPage<BosStaffInfoVo> getPageAllSuperiorStaff(@Param("dto")  SysStaffInfoPageDto sysStaffInfoPageDto, IPage<BosStaffInfoVo> infoVoIPage,@Param("depIds")  List<String> depIds);

    /**
     *  根据角色或部门查询用户
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/2/26 17:30
     * @param                departId
     * @param                roleId
     * @param                companyId
     * @return               com.zerody.user.vo.SysUserInfoVo
     */
    List<com.zerody.user.vo.SysUserInfoVo> getUserByDepartOrRole(@Param("departId") String departId,
                                                                 @Param("roleId") String roleId,

                                                                 @Param("companyId") String companyId);

    /**
     *
     * 通过员工id查询用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/3 16:02
     * @param                staffIds
     * @return               com.zerody.user.vo.SysUserInfoVo
     */
    List<com.zerody.user.vo.SysUserInfoVo> getStaffByIds(@Param("staffIds") List<String> staffIds);

    /**
     *
     *  获取企业用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/10 17:02
     * @param                param
     * @param                iPage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.UserPerformanceReviewsVo>
     */
    IPage<UserPerformanceReviewsVo> getPagePerformanceReviews(@Param("param") UserPerformanceReviewsPageDto param, IPage<UserPerformanceReviewsVo> iPage);
}