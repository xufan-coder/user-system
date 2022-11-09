package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CallControl;
import com.zerody.user.domain.CallControlRecord;
import com.zerody.user.domain.CallUseControl;
import com.zerody.user.dto.CallControlRecordPageDto;
import com.zerody.user.mapper.CallControlMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallControlRecordMapper;
import com.zerody.user.service.CallControlRecordService;

import java.util.List;

@Service
public class CallControlRecordServiceImpl extends ServiceImpl<CallControlRecordMapper,  CallControlRecord> implements CallControlRecordService{

    @Override
    public IPage<CallControlRecord> getPageList(CallControlRecordPageDto pageDto) {
        Page<CallControlRecord> page = new Page<>();
        page.setCurrent(pageDto.getCurrent());
        page.setSize(pageDto.getPageSize());
        QueryWrapper<CallControlRecord> qw = new QueryWrapper<>();
        if(DataUtil.isNotEmpty(pageDto.getCompanyId())){
            qw.lambda().eq(CallControlRecord::getCompanyId,pageDto.getCompanyId());
        }
        if(DataUtil.isNotEmpty(pageDto.getUserId())){
            qw.lambda().eq(CallControlRecord::getCompanyId,pageDto.getUserId());
        }
        if(DataUtil.isNotEmpty(pageDto.getDepartId())){
            pageDto.setDepartId(pageDto.getDepartId().concat("%"));
            qw.lambda().like(CallControlRecord::getCompanyId,pageDto.getDepartId());
        }
        IPage<CallControlRecord> pageResult = this.page(page,qw);
        return pageResult;
    }

    @Override
    public void relieveCallControlRecordList(List<String> ids) {

    }

    @Override
    public void relieveCallControlRecord(String id) {

    }
}
