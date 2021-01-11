package com.zerody.user.mapper;

import java.util.List;

import com.zerody.user.vo.SysUserClewCollectVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.api.vo.UserDeptVo;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.SysStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;

public interface SysStaffInfoMapper extends BaseMapper<SysStaffInfo> {


    SysStaffInfoVo selectByUserIdAndCompId(@Param("userId") String userId, @Param("compId")String compId);

    IPage<BosStaffInfoVo> getPageAllStaff(@Param("staff")SysStaffInfoPageDto sysStaffInfoPageDto,IPage iPage);

    List<String> selectUserByCompanyId(String companyId);

    SysUserInfoVo getStaffInfoByOpenId(String openId);

    SysUserInfoVo selectStaffById(String id);

    List<String> selectStaffRoles(@Param("userId")String userId, @Param("companyId")String companyId);

    List<BosStaffInfoVo> getStaff(@Param("companyId")String companyId, @Param("departId")String departId, @Param("positionId")String positionId);

    IPage<BosStaffInfoVo> selectAdmins(@Param("dto")AdminsPageDto dto, IPage voIPage);
    
    @Select({ "<script> select id from sys_staff_info where user_id=#{userId} limit 0,1 </script>" })
	public String getStaffIdByUserId(@Param("userId")String userId);

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
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.SysUserClewCollectVo>
     */
    IPage<SysUserClewCollectVo> getStaffByDepIds(@Param("ids") List<String> deps, IPage<SysUserClewCollectVo> iPage);
}