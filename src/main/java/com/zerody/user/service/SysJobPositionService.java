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

    DataResult addJob(SysJobPosition sysJobPosition);

    DataResult updateJob(SysJobPosition sysJobPosition);

    DataResult deleteJobById(String jobId);

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
