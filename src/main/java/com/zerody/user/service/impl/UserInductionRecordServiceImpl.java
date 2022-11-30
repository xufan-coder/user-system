package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.UserInductionRecord;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.mapper.SysStaffInfoMapper;
import com.zerody.user.mapper.UserInductionRecordMapper;
import com.zerody.user.service.UserInductionRecordService;
import com.zerody.user.vo.LeaveUserInfoVo;
import com.zerody.user.vo.UserInductionRecordInfoVo;
import com.zerody.user.vo.UserInductionRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kuang
 */
@Service
public class UserInductionRecordServiceImpl extends ServiceImpl<UserInductionRecordMapper, UserInductionRecord> implements UserInductionRecordService {

    @Autowired
    private SysStaffInfoMapper sysStaffInfoMapper;

    @Override
    public Page<UserInductionRecordVo> getInductionPage(UserInductionPage queryDto) {

        Page<UserInductionRecordVo> page = new Page<>(queryDto.getCurrent(),queryDto.getPageSize());

        return this.baseMapper.getInductionPage(page,queryDto);
    }

    @Override
    public UserInductionRecordInfoVo getInductionInfo(String id) {

        UserInductionRecordInfoVo infoVo = this.baseMapper.getInductionInfo(id);
        if(infoVo == null) {
            throw  new DefaultException("未找到相关入职申请");
        }

        LeaveUserInfoVo leaveInfo = sysStaffInfoMapper.getLeaveUserInfo(infoVo.getLeaveUserId());
        infoVo.setLeaveInfo(leaveInfo);
        return infoVo;
    }

}
