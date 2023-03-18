package com.zerody.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserInductionSplitRecord;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.dto.UserInductionSplitDto;
import com.zerody.user.dto.UserInductionVerificationDto;
import com.zerody.user.vo.UserInductionGroupRecordInfoVo;
import com.zerody.user.vo.UserInductionGroupRecordVo;
import com.zerody.user.vo.UserInductionRecordInfoVo;

import java.util.List;

public interface UserInductionSplitRecordService extends IService<UserInductionSplitRecord> {


    JSONObject verification(UserInductionVerificationDto param);

    void addOrUpdateRecord(UserInductionSplitDto param);

    void doRenewInduction(UserInductionSplitRecord induction);

    List<UserInductionGroupRecordVo> getInductionPage(String userId);

    UserInductionGroupRecordInfoVo getInductionInfo(String id);
}
