package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.user.domain.StaffBlacklistApprover;
import com.zerody.user.dto.StaffBlacklistApproverPageDto;
import com.zerody.user.mapper.StaffBlacklistApproverMapper;
import com.zerody.user.service.StaffBlacklistApproverService;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
import com.zerody.user.vo.StaffBlacklistApproverVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Slf4j
@Service
public class StaffBlacklistApproverServiceImpl extends ServiceImpl<StaffBlacklistApproverMapper, StaffBlacklistApprover> implements StaffBlacklistApproverService {

    @Override
    public StaffBlacklistApprover addStaffBlaklistRecord(StaffBlacklistApprover param) {
        return null;
    }
    @Override
    public IPage<StaffBlacklistApproverVo> getBlacklistApproverPage(StaffBlacklistApproverPageDto param) {
        IPage<StaffBlacklistApproverVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.baseMapper.getBlacklistApproverPage(param, iPage);
        return iPage;
    }
}