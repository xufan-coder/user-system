package com.zerody.user.service;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysJobPositionDto;
import com.zerody.user.pojo.SysJobPosition;

/**
 * @author PengQiang
 * @ClassName SysJobPositionService
 * @DateTime 2020/12/18_18:12
 * @Deacription TODO
 */
public interface SysJobPositionService {
    DataResult getPageJob(SysJobPositionDto sysJobPositionDto);

    DataResult addOrUpdateJob(SysJobPosition sysJobPosition);

    DataResult deleteJobById(String jobId);
}
