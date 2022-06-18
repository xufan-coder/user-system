package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.domain.CeoCompanyRef;
import com.zerody.user.dto.CeoRefDto;
import com.zerody.user.mapper.CeoCompanyRefMapper;
import com.zerody.user.service.CeoCompanyRefService;
import com.zerody.user.vo.CeoRefVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author  DaBai
 * @date  2022/6/18 11:52
 */

@Slf4j
@Service
public class CeoCompanyRefServiceImpl extends ServiceImpl<CeoCompanyRefMapper, CeoCompanyRef> implements CeoCompanyRefService {

    @Override
    public void saveCompanyRef(CeoRefDto data) {

    }

    @Override
    public CeoRefVo getCeoRef(String id) {
        return null;
    }
}
