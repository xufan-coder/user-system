package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.CallControl;
import com.zerody.user.domain.CallControlRecord;
import com.zerody.user.dto.CallControlRecordPageDto;
import com.zerody.user.vo.CallControlRecordVo;

import java.util.List;

public interface CallControlRecordService extends IService<CallControlRecord> {


    IPage<CallControlRecord> getPageList(CallControlRecordPageDto pageDto);

    void relieveCallControlRecordList(List<String> ids);

    void relieveCallControlRecord(String id);

    void saveRecord(String userId);

    List<CallControlRecordVo> getList(CallControlRecordPageDto pageDto);
}
