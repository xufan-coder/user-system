package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.dto.DeptInfo;
import com.zerody.user.api.vo.DepartInfoVo;
import com.zerody.user.api.vo.UserDepartInfoVo;
import com.zerody.user.dto.SysCompanyInfoDto;
import com.zerody.user.dto.SysDepartmentInfoDto;
import com.zerody.user.domain.SysDepartmentInfo;
import com.zerody.user.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:52
 * @param
 * @return
 */
public interface SysDepartmentInfoMapper extends BaseMapper<SysDepartmentInfo> {

    /**
     *
     * 分页查询部门信息
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:52
     * @param                sysDepartmentInfoDto
     * @param                iPage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    IPage<SysDepartmentInfoVo> getPageDepartment(@Param("dep") SysDepartmentInfoDto sysDepartmentInfoDto, IPage<SysDepartmentInfoVo> iPage);

    /**
     *
     * ——
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:53
     * @param                depId
     * @return               java.util.List<java.lang.String>
     */
    List<String> selectUserLoginIdByDepId(String depId);

    /**
     *
     * 查询当前企业下的所有部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:53
     * @param                companyId
     * @return               java.util.List<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    List<SysDepartmentInfoVo> getAllDepByCompanyId(String companyId);

    List<SysDepartmentInfoVo> getAllDepByDepartId(@Param("companyId") String companyId,@Param("departId") String departId,@Param("isDepartAdmin")Integer isDepartAdmin);

    /**
     *
     * 根据用户获取部门
     * @author               PengQiang
     * @description
     * @date                 2021/1/6 16:43
     * @param                id
     * @return               com.zerody.user.domain.SysDepartmentInfo
     */
    SysDepartmentInfo selectUserDep(String id);

    /**
     *  获取下级部门
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/26 15:15
     * @param                user
     * @return               java.util.List<com.zerody.user.vo.SysDepartmentInfoVo>
     */
    List<SysDepartmentInfoVo> getSubordinateStructure(@Param("user") UserVo user);

    /**
     *
     *  获取下级直属部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/30 20:00
     * @param                departId
     * @return               java.util.List<com.zerody.user.api.vo.UserDepartInfoVo>
     */
    List<UserDepartInfoVo> getSubordinateDirectlyDepart(@Param("departId") String departId);

    /**
     *
     *  获取部门名称
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/31 19:17
     * @param                departId
     * @return               com.zerody.user.vo.UserStructureVo
     */
    UserStructureVo getDepartNameById(@Param("departId") String departId);

    /**
     *
     *  根据上级部门 或者 企业id获取直属部门名称
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/3/31 19:17
     * @param                companyId
     * @param                departId
     * @return               com.zerody.user.vo.UserStructureVo
     */
    List<UserStructureVo> getDepartNameByCompanyIdOrParentId(@Param("companyId")String companyId,@Param("departId") String departId);

    /**
     *
     * 获取部门名称状态为修改状态的 部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/15 20:07
     * @return               java.util.List<com.zerody.user.api.dto.DeptInfo>
     */
    List<DeptInfo> getModilyDepartName();

    /**
     *
     * 部门名称修改状态修改回 未修改状态
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/4/15 20:28
     * @param                depts
     * @return               void
     */
    void updateDepartIsUpdateName(@Param("depts") List<DeptInfo> depts);

    List<Map<String, String>> getDepartmentEditInfo();

    void updateDepartEditInfo(@Param("departs") List<Map<String, String>> departMap);

    List<DepartSubordinateVo> getDepartByParentId(@Param("id") String id, @Param("companyId") String companyId);

    List<CustomerQueryDimensionalityVo> getCustomerQuerydimensionality(@Param("user") UserVo user);

    DepartInfoVo getDepartInfoInner(@Param("id")String departId);

    List<ReportFormsQueryVo> getDepartBusiness(@Param("companyId") String companyId, @Param("departId") String departId, @Param("roleIds") List<String> roleIds);

    List<String> getSubordinateIdsById(@Param("id") String departId);

    /**获取部门人数*/
    Integer getDepartUserCountById(@Param("departId") String departId);

    List<SysDepartmentInfoVo> getAllDepPersonByCompanyId(String companyId);

    List<SubordinateUserQueryVo> getSuperiorParentList(@Param("departId")String departId);

    /**获取企业下的所有部门负责人id*/
    List<String> getUserIds(@Param("companyId") String companyId,@Param("parentId")String parentId);

    List<String> getUserIdsByDepartmentId(@Param("departmentId") String departmentId);

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取副总
    * @Date: 2022/10/20 16:46
    */
    List<CompanyAdminVo> queryVicePresident(@Param("companyIds") List<String> companyIds);
}