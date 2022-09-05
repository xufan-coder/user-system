package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.user.domain.BirthdayBlessing;
import com.zerody.user.mapper.BirthdayBlessingMapper;
import com.zerody.user.service.BirthdayBlessingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kuang
 */
@Service
public class BirthdayBlessingServiceImpl extends ServiceImpl<BirthdayBlessingMapper,BirthdayBlessing> implements BirthdayBlessingService {
    @Override
    public List<BirthdayBlessing> getBlessingList() {
        QueryWrapper<BirthdayBlessing> qw = new QueryWrapper<>();
        qw.lambda().eq(BirthdayBlessing::getDeleted, YesNo.YES);

        return this.list(qw);
    }
}
