package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.CallUseControl;
import com.zerody.user.dto.CallControlPageDto;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallUseControlMapper;
import com.zerody.user.service.CallUseControlService;

import java.util.List;

@Service
public class CallUseControlServiceImpl extends ServiceImpl<CallUseControlMapper, CallUseControl> implements CallUseControlService{

    @Override
    public IPage<CallUseControl> getPageList(CallControlPageDto pageDto) {
        return null;
    }

    @Override
    public void removeNameList(String id) {

    }

    @Override
    public void addNameList(List<String> userIds) {

    }
}
