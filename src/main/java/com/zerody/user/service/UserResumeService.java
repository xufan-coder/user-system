package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.domain.UserResume;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/11/28 14:40
 */

public interface UserResumeService extends IService<UserResume> {
    void saveOrUpdateBatchResume(List<UserResume> userResumes, StaffInfoVo staffInfoVo);

    List<UserResume> getResumeList(String userId);
}
