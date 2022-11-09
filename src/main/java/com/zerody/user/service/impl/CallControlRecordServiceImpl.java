package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.CallControl;
import com.zerody.user.domain.CallControlRecord;
import com.zerody.user.dto.CallControlRecordPageDto;
import com.zerody.user.mapper.CallControlMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallControlRecordMapper;
import com.zerody.user.service.CallControlRecordService;

import java.util.List;

@Service
public class CallControlRecordServiceImpl extends ServiceImpl<CallControlMapper, CallControl> implements CallControlRecordService{

    @Override
    public IPage<CallControlRecord> getPageList(CallControlRecordPageDto pageDto) {
        return null;
    }

    @Override
    public void relieveCallControlRecordList(List<String> ids) {

    }

    @Override
    public void relieveCallControlRecord(String id) {

    }
}
