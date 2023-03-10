package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.BlacklistOperationRecord;
import com.zerody.user.dto.BlacklistOperationRecordPageDto;
import com.zerody.user.mapper.BlacklistOperationRecordMapper;
import com.zerody.user.service.BlacklistOperationRecordService;
import com.zerody.user.vo.BlacklistOperationRecordPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author : xufan
 * @create 2023/3/8 18:55
 */

@Slf4j
@Service
public class BlacklistOperationRecordServiceImpl extends ServiceImpl<BlacklistOperationRecordMapper, BlacklistOperationRecord> implements BlacklistOperationRecordService {

    @Override
    public IPage<BlacklistOperationRecordPageVo> getPageBlacklistOperationRecord(BlacklistOperationRecordPageDto param) {
        IPage<BlacklistOperationRecordPageVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.baseMapper.getPageBlacklistOperationRecord(param,iPage);
        return iPage;
    }
}
