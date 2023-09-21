package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.StaffBlacklistApprover;
import com.zerody.user.dto.StaffBlacklistApproverPageDto;
import com.zerody.user.vo.StaffBlacklistApproverVo;

/**
 * 内控名单申请记录
 */
public interface StaffBlacklistApproverService extends IService<StaffBlacklistApprover> {
    StaffBlacklistApprover addStaffBlaklistRecord(StaffBlacklistApprover param);

    IPage<StaffBlacklistApproverVo> getBlacklistApproverPage(StaffBlacklistApproverPageDto param);
}

