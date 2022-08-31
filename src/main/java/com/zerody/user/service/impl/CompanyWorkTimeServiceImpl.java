package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.dto.CompanyWorkTimeDto;
import com.zerody.user.vo.CompanyWorkTimeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.mapper.CompanyWorkTimeMapper;
import com.zerody.user.domain.CompanyWorkTime;
import com.zerody.user.service.CompanyWorkTimeService;

import java.util.ArrayList;
import java.util.List;

/**
 * 公司上下班时间表 业务实现层
 *
 * @author chenKeFeng
 * @date 2022-08-29 11:59:15
 */
@Service
public class CompanyWorkTimeServiceImpl extends ServiceImpl<CompanyWorkTimeMapper, CompanyWorkTime> implements CompanyWorkTimeService {

    @Override
    public IPage<CompanyWorkTimeVo> getPageCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto) {
        List<CompanyWorkTimeVo> voList = Lists.newArrayList();
        LambdaQueryWrapper<CompanyWorkTime> wrapper = new LambdaQueryWrapper<>();
        Page<CompanyWorkTime> iPage = new Page<>(companyWorkTimeDto.getCurrent(), companyWorkTimeDto.getPageSize());
        Page<CompanyWorkTime> companyWorkTimePage = this.baseMapper.selectPage(iPage, wrapper);
        List<CompanyWorkTime> records = companyWorkTimePage.getRecords();
        for (CompanyWorkTime record : records) {
            CompanyWorkTimeVo companyWorkTimeVo = new CompanyWorkTimeVo();
            BeanUtils.copyProperties(record, companyWorkTimeVo);
            voList.add(companyWorkTimeVo);
        }
        IPage<CompanyWorkTimeVo> vo = new Page<>();
        vo.setRecords(voList);
        vo.setTotal(companyWorkTimePage.getTotal());
        vo.setSize(companyWorkTimePage.getSize());
        vo.setPages(companyWorkTimePage.getPages());
        return vo;
    }

    @Override
    public List<CompanyWorkTimeVo> getCompanyWorkTimeList(CompanyWorkTimeDto companyWorkTimeDto) {
        List<CompanyWorkTimeVo> arr = new ArrayList<>();
        LambdaQueryWrapper<CompanyWorkTime> wrapper = new LambdaQueryWrapper<>();
        if (DataUtil.isNotEmpty(companyWorkTimeDto.getClass())) {
            wrapper.eq(CompanyWorkTime::getCompanyId, companyWorkTimeDto.getCompanyId());
        }
        List<CompanyWorkTime> companyWorkTimeList = this.baseMapper.selectList(wrapper);
        for (CompanyWorkTime companyWorkTime : companyWorkTimeList) {
            CompanyWorkTimeVo companyWorkTimeVo = new CompanyWorkTimeVo();
            BeanUtils.copyProperties(companyWorkTime, companyWorkTimeVo);
            arr.add(companyWorkTimeVo);
        }
        return arr;
    }

    @Override
    public CompanyWorkTime getCompanyWorkTimeById(CompanyWorkTimeDto companyWorkTimeDto) {
        LambdaQueryWrapper<CompanyWorkTime> wrapper = new LambdaQueryWrapper<>();
        if (DataUtil.isNotEmpty(companyWorkTimeDto.getId())) {
            wrapper.eq(CompanyWorkTime::getId, companyWorkTimeDto.getId());
        }
        if (DataUtil.isNotEmpty(companyWorkTimeDto.getCompanyId())) {
            wrapper.eq(CompanyWorkTime::getCompanyId, companyWorkTimeDto.getCompanyId());
        }
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Integer addCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto) {
        CompanyWorkTime companyWorkTime = new CompanyWorkTime();
        BeanUtils.copyProperties(companyWorkTimeDto, companyWorkTime);
        return baseMapper.insert(companyWorkTime);
    }

    @Override
    public Integer editCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto) {
        CompanyWorkTime companyWorkTime = new CompanyWorkTime();
        BeanUtils.copyProperties(companyWorkTimeDto, companyWorkTime);
        LambdaUpdateWrapper<CompanyWorkTime> updateWrapper = new LambdaUpdateWrapper<>();
        if (DataUtil.isNotEmpty(companyWorkTime.getId())) {
            updateWrapper.eq(CompanyWorkTime::getId, companyWorkTime.getId());
        }
        if (DataUtil.isNotEmpty(companyWorkTimeDto.getCompanyId())) {
            updateWrapper.eq(CompanyWorkTime::getCompanyId, companyWorkTimeDto.getCompanyId());
        }
        return baseMapper.update(companyWorkTime, updateWrapper);
    }

    @Override
    public Integer setCommuteTime(CompanyWorkTimeDto companyWorkTimeDto) {
        //获取企业上下班详情
        CompanyWorkTime companyWorkTime = getCompanyWorkTimeById(companyWorkTimeDto);
        if (DataUtil.isNotEmpty(companyWorkTime)) {
            //编辑上下班时间
            return editCompanyWorkTime(companyWorkTimeDto);
        } else {
            //新增上下班时间
            return addCompanyWorkTime(companyWorkTimeDto);
        }
    }

}