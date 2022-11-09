package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.CallUseControl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallUseControlMapper;
import com.zerody.user.service.CallUseControlService;
@Service
public class CallUseControlServiceImpl extends ServiceImpl<CallUseControlMapper, CallUseControl> implements CallUseControlService{

}
