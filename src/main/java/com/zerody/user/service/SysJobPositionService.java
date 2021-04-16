package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysJobPositionDto;
import com.zerody.user.domain.SysJobPosition;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysJobPositionService
 * @DateTime 2020/12/18_18:12
 * @Deacription TODO
 */
public interface SysJobPositionService {

    /**
     * 添加岗位
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:05
     * @param                sysJobPosition
     * @return               com.zerody.common.bean.DataResult
     */
    DataResult addJob(SysJobPosition sysJobPosition);

    /**
     * 修改岗位
     *
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:05
     * @param                sysJobPosition
     * @return               com.zerody.common.bean.DataResult
     */
    DataResult updateJob(SysJobPosition sysJobPosition);

    /**
     *
     * 删除岗位
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:05
     * @param                jobId
     * @return               com.zerody.common.bean.DataResult
     */
    DataResult deleteJobById(String jobId);

    /**
     *
     * 部门id查岗位
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 15:05
     * @param                departId
     * @return               java.util.List<com.zerody.user.domain.SysJobPosition>
     */
    List<SysJobPosition> getJob(String departId);

    /**************************************************************************************************
     **
     * 按岗位名称查询本企业本部门下是否有该岗位
     *
     * @param name
     * @param compId
     * @param departId
     * @return {@link SysJobPosition }
     * @author DaBai
     * @date 2021/1/7  9:44 
     */
    SysJobPosition getJobByDepart(String name,String compId,String departId);

    /**************************************************************************************************
     **
     *  按岗位名称查询本企业下是否有该岗位
     *
     * @param jobName
     * @param companyId
     * @return {@link SysJobPosition }
     * @author DaBai
     * @date 2021/1/7  16:47
     */
    SysJobPosition getJobByComp(String jobName, String companyId);
}
