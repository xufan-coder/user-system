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
}
