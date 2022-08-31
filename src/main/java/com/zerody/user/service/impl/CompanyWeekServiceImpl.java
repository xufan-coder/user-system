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
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public CompanyWeek getCompanyWeekById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Integer addCompanyWeek(CompanyWeek companyWeek) {
        return baseMapper.insert(companyWeek);
    }

    @Override
    public Integer updateCompanyWeek(CompanyWeek companyWeek) {
        LambdaUpdateWrapper<CompanyWeek> updateWrapper = new LambdaUpdateWrapper<>();
        return baseMapper.update(companyWeek, updateWrapper);
    }

    @Override
    public Integer deleteCompanyWeek(String id) {
        LambdaQueryWrapper<CompanyWeek> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CompanyWeek::getCompanyId, id);
        return this.baseMapper.delete(wrapper);
    }

}