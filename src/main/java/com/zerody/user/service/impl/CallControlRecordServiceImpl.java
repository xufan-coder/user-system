package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.CallControl;
import com.zerody.user.mapper.CallControlMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallControlRecordMapper;
import com.zerody.user.service.CallControlRecordService;
@Service
public class CallControlRecordServiceImpl extends ServiceImpl<CallControlMapper, CallControl> implements CallControlRecordService{

}
