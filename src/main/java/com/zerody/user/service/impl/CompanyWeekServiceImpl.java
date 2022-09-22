package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zerody.common.utils.DataUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.mapper.CompanyWeekMapper;
import com.zerody.user.domain.CompanyWeek;
import com.zerody.user.service.CompanyWeekService;

import java.util.Date;
import java.util.List;

/**
 * 业务实现层
 *
 * @author chenKeFeng
 * @date 2022-08-31 11:22:16
 */
@Service
public class CompanyWeekServiceImpl extends ServiceImpl<CompanyWeekMapper, CompanyWeek> implements CompanyWeekService {

    @Override
    public List<CompanyWeek> getPageCompanyWeek(CompanyWeek companyWeek) {
        LambdaQueryWrapper<CompanyWeek> wrapper = new LambdaQueryWrapper<>();
        if (DataUtil.isNotEmpty(companyWeek.getCompanyId())) {
            wrapper.eq(CompanyWeek::getCompanyId, companyWeek.getCompanyId());
        }
        if (DataUtil.isNotEmpty(companyWeek.getType())) {
            wrapper.eq(CompanyWeek::getType, companyWeek.getType());
        }
        if (DataUtil.isNotEmpty(companyWeek.getCeoUserId())) {
            wrapper.eq(CompanyWeek::getCeoUserId, companyWeek.getCeoUserId());
        }
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public CompanyWeek getCompanyWeekById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Integer addCompanyWeek(CompanyWeek companyWeek) {
        companyWeek.setCreateTime(new Date());
        return baseMapper.insert(companyWeek);
    }

    @Override
    public Integer updateCompanyWeek(CompanyWeek companyWeek) {
        LambdaUpdateWrapper<CompanyWeek> updateWrapper = new LambdaUpdateWrapper<>();
        return baseMapper.update(companyWeek, updateWrapper);
    }

    @Override
    public Integer deleteCompanyWeek(String id, Integer type, String ceoId) {
        LambdaQueryWrapper<CompanyWeek> wrapper = new LambdaQueryWrapper<>();
        if (type.equals(1)) {
            if (DataUtil.isNotEmpty(id)) {
                wrapper.eq(CompanyWeek::getCompanyId, id);
            }
        } else {
            if (DataUtil.isNotEmpty(ceoId)) {
                wrapper.eq(CompanyWeek::getCeoUserId, ceoId);
            }
        }
        return this.baseMapper.delete(wrapper);
    }

}