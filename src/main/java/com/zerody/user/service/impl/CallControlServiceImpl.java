package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.CallControl;
import com.zerody.user.vo.CallControlVo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallControlMapper;
import com.zerody.user.service.CallControlService;
@Service
public class CallControlServiceImpl extends ServiceImpl<CallControlMapper, CallControl> implements CallControlService{

    @Override
    public CallControlVo getByCompany(String companyId) {
        return null;
    }

    @Override
    public void addOrUpdate(CallControlVo param) {

    }
}
