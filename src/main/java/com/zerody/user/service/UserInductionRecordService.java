package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.UserInductionRecord;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.vo.UserInductionRecordInfoVo;
import com.zerody.user.vo.UserInductionRecordVo;

/**
 * @author kuang
 */
public interface UserInductionRecordService extends IService<UserInductionRecord> {

    Page<UserInductionRecordVo> getInductionPage(UserInductionPage queryDto);

    UserInductionRecordInfoVo getInductionInfo(String id);

    UserInductionRecord addOrUpdateRecord(UserInductionRecord param);
}
