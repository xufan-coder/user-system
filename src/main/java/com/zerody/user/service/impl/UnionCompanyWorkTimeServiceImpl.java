package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CompanyWeek;
import com.zerody.user.dto.UnionCompanyWorkTimeDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.mapper.UnionCompanyWorkTimeMapper;
import com.zerody.user.domain.UnionCompanyWorkTime;
import com.zerody.user.service.UnionCompanyWorkTimeService;

import java.util.List;

/**
 * 业务实现层
 *
 * @author chenKeFeng
 * @date 2022-08-31 11:22:16
 */
@Service
public class UnionCompanyWorkTimeServiceImpl extends ServiceImpl<UnionCompanyWorkTimeMapper, UnionCompanyWorkTime> implements UnionCompanyWorkTimeService {

    @Override
    public List<UnionCompanyWorkTime> getUnionCompanyWorkTime(UnionCompanyWorkTime unionCompanyWorkTime) {
        LambdaQueryWrapper<UnionCompanyWorkTime> wrapper = new LambdaQueryWrapper<>();
        if (DataUtil.isNotEmpty(unionCompanyWorkTime.getCompanyId())) {
            wrapper.eq(UnionCompanyWorkTime::getCompanyId, unionCompanyWorkTime.getCompanyId());
        }
        if (DataUtil.isNotEmpty(unionCompanyWorkTime.getType())) {
            wrapper.eq(UnionCompanyWorkTime::getType, unionCompanyWorkTime.getType());
        }
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public UnionCompanyWorkTime getUnionCompanyWorkTimeById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Integer addUnionCompanyWorkTime(UnionCompanyWorkTimeDto unionCompanyWorkTimeDto) {
        UnionCompanyWorkTime unionCompanyWorkTime = new UnionCompanyWorkTime();
        BeanUtils.copyProperties(unionCompanyWorkTimeDto, unionCompanyWorkTime);
        return baseMapper.insert(unionCompanyWorkTime);
    }

    @Override
    public Integer editUnionCompanyWorkTime(UnionCompanyWorkTime unionCompanyWorkTime) {
        LambdaUpdateWrapper<UnionCompanyWorkTime> updateWrapper = new LambdaUpdateWrapper<>();
        return baseMapper.update(unionCompanyWorkTime, updateWrapper);
    }

    @Override
    public Integer deleteUnionCompanyWorkTime(String id, Integer type) {
        LambdaQueryWrapper<UnionCompanyWorkTime> wrapper = new LambdaQueryWrapper<>();
        if (type.equals(YesNo.YES)) {
            if (DataUtil.isNotEmpty(id)) {
                wrapper.eq(UnionCompanyWorkTime::getCompanyId, id);
            }
        } else {
            wrapper.eq(UnionCompanyWorkTime::getType, 0);
        }
        return this.baseMapper.delete(wrapper);
    }

}