package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.DepartInfoVo;
import com.zerody.user.api.vo.UserDepartInfoVo;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.DirectLyDepartOrUserQueryDto;
import com.zerody.user.dto.SetAdminAccountDto;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.vo.DepartSubordinateVo;
import com.zerody.user.vo.ReportFormsQueryVo;
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
    List<UserStructureVo> getDirectLyDepartOrUser(DirectLyDepartOrUserQueryDto param);

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

    /**
     *
     *  获取部门类型 0部门 1团队部门(没有下级部门)
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/7 19:07
     * @param                departId
     * @return               java.lang.Integer
     */
    Integer getDepartType(String departId);

    /**
     *
     *  修改冗余的部门名称
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/15 20:03
     * @param
     * @return               void
     */
    void updateRedundancyDepartName();

    void doDepartmentEditInfo();

    /**
     *
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/2 17:40
     * @param                id
     * @param                companyId
     * @return               java.util.List<com.zerody.user.vo.DepartSubordinateVo>
     */
    List<DepartSubordinateVo> getDepartByParentId(String id, String companyId);

    /**
     *
     *  判断部门是否最后一级
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/4 17:40
     * @param               departId
     * @param                isShow
     * @return               java.lang.Boolean
     */
    Boolean getDepartIsFinally(String departId, Boolean isShow);

    List<DepartSubordinateVo> getDepartSubordinateByParentId(String id, String companyId,UserVo user);

    DepartInfoVo getDepartInfoInner(String departId);

    List<ReportFormsQueryVo> getDepartBusiness(String companyId, String departId, List<String> roleIds);

    List<String> getSubordinateIdsById(String departId);

    List<SysDepartmentInfoVo>  getAllDepByDepartId(String companyId,String departId,Integer isDepartAdmin);

    List<SysDepartmentInfoVo> getAllDepPersonByCompanyId(String companyId);
    /**
    *
    *  @description   查询当前企业下的所有部门
    *  @author        YeChangWei
    *  @date          2023/1/2 16:08
    *  @return        java.util.List<com.zerody.user.vo.SysDepartmentInfoVo>
    */
    List<String> getAllDepByCompany(String companyId);

    DepartInfoVo getDepartInfoShow(String departId);
}
