package com.zerody.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserInductionSplitRecord;
import com.zerody.user.dto.UserInductionSplitDto;
import com.zerody.user.dto.UserInductionVerificationDto;

public interface UserInductionSplitRecordService extends IService<UserInductionSplitRecord> {


    JSONObject verification(UserInductionVerificationDto param);

    void addOrUpdateRecord(UserInductionSplitDto param);

    void doRenewInduction(UserInductionSplitRecord induction);
}
