package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.UseControl;
import com.zerody.user.dto.UseControlDto;
import com.zerody.user.mapper.UseControlMapper;
import com.zerody.user.service.UseControlService;
import com.zerody.user.vo.UseControlVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author  DaBai
 * @date  2022/3/1 14:01
 */

@Slf4j
@Service
public class UseControlServiceImpl extends ServiceImpl<UseControlMapper, UseControl> implements UseControlService {

    @Override
    public void addOrUpdate(UseControlDto param) {

    }

    @Override
    public UseControlVo getByCompany(String companyId) {
        return null;
    }
}
