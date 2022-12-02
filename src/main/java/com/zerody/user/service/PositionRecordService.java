package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.PositionRecord;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.vo.PositionRecordListVo;

import java.util.List;

public interface PositionRecordService  extends IService<PositionRecord> {

    List<PositionRecordListVo> queryPositionRecord(String certificateCard);

    List<PositionRecordListVo> getPositionRecord(String userId);
}
