package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.UserDepartInfoVo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.vo.SysDepartmentInfoVo;
import com.zerody.user.vo.UserStructureVo;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoService
 * @DateTime 2020/12/19_13:20
 * @Deacription TODO
 */
public interface SysDepartmentInfoService extends IService<SysDepartmentInfo> {

    /**
     *  添加部门
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:02
     * @param                sysDepartmentInfo
     * @return               void
     */
    void addDepartment(SysDepartmentInfo sysDepartmentInfo);

    /**
     *
     * 修改部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:03
     * @param                sysDepartmentInfo
     * @return               void
     */
    void updateDepartment(SysDepartmentInfo sysDepartmentInfo);

    /**
     *
     * 删除部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:03
     * @param                depId
     * @return               void
     */
    void deleteDepartmentById(String depId);

    /**
     *
     * 查询企业下的部门 树形返回
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:03
     * @param                companyId
     * @return               java.util.List<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    List<SysDepartmentInfoVo> getAllDepByCompanyId(String companyId);

    /**
     *  查询企业部门
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:03
     * @param                compId
     * @return               java.util.List<com.zerody.user.domain.SysDepartmentInfo>
     */
    List<SysDepartmentInfo> getDepartmentByComp(String compId);

    /**************************************************************************************************
     **
     * 按部门名字查询本企业部门是否有，
     *
     * @param name
     * @param compId
     * @return {@link SysDepartmentInfo }
     * @author DaBai
     * @date 2021/1/7  9:41
     */
    SysDepartmentInfo getByName(String name,String compId);

    /**
     *
     * 设置部门管理员
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/8 15:55
     * @param                dto
     * @return               void
     */
    void updateAdminAccout(SetAdminAccountDto dto);

    /**
     *
     * 获取下级部门架构
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/26 14:51
     * @param                user
     * @return               java.util.List<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    List<SysDepartmentInfoVo> getSubordinateStructure(UserVo user);

    /**
     *
     *  获取下级直属部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/30 19:58
     * @param                departId
     * @return               java.util.List<com.zerody.user.api.vo.UserDepartInfoVo>
     */
    List<UserDepartInfoVo> getSubordinateDirectlyDepart(String departId);

    /**
     *
     *  获取直属部门或者用户
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/31 18:20
     * @param                departId
     * @param                companyId
     * @param                user
     * @return               java.util.List<com.zerody.user.api.vo.UserDepartInfoVo>
     */
    List<UserStructureVo> getDirectLyDepartOrUser(String companyId, String departId, UserVo user);

    com.zerody.user.api.vo.SysUserInfo getChargeUser(String departId);

    /**
     *
     *  根据用户权限获取直属部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/2 12:32
     * @param                userId
     * @return               java.util.List<com.zerody.user.api.vo.UserDepartInfoVo>
     */
    List<UserDepartInfoVo> getJurisdictionDirectly(String userId);
}
