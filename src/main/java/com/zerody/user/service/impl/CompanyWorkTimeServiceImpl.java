package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CompanyWeek;
import com.zerody.user.domain.UnionCompanyWorkTime;
import com.zerody.user.dto.CompanyWorkTimeAddDto;
import com.zerody.user.dto.CompanyWorkTimeDto;
import com.zerody.user.dto.UnionCompanyWorkTimeDto;
import com.zerody.user.enums.WeeKEnum;
import com.zerody.user.service.CompanyWeekService;
import com.zerody.user.service.UnionCompanyWorkTimeService;
import com.zerody.user.vo.CompanyWorkTimeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.user.mapper.CompanyWorkTimeMapper;
import com.zerody.user.domain.CompanyWorkTime;
import com.zerody.user.service.CompanyWorkTimeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公司上下班时间表 业务实现层
 *
 * @author chenKeFeng
 * @date 2022-08-29 11:59:15
 */
@Service
public class CompanyWorkTimeServiceImpl extends ServiceImpl<CompanyWorkTimeMapper, CompanyWorkTime> implements CompanyWorkTimeService {


    @Autowired
    private CompanyWeekService companyWeekService;

    @Autowired
    private UnionCompanyWorkTimeService unionCompanyWorkTimeService;


    @Override
    public CompanyWorkTimeVo getPageCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto) {
        //获取企业上下班时间详情
        CompanyWorkTime companyWorkTime = getCompanyWorkTimeById(companyWorkTimeDto);

        CompanyWorkTimeVo companyWorkTimeVo = new CompanyWorkTimeVo();
        if (DataUtil.isNotEmpty(companyWorkTime)) {
            BeanUtils.copyProperties(companyWorkTime, companyWorkTimeVo);

            CompanyWeek companyWeek = new CompanyWeek();
            BeanUtils.copyProperties(companyWorkTimeDto, companyWeek);
            List<CompanyWeek> companyWeekList = companyWeekService.getPageCompanyWeek(companyWeek);

            List<Integer> companyWeeks = new ArrayList<>();
            for (CompanyWeek week : companyWeekList) {
                Integer numberByText = WeeKEnum.getNumberByText(week.getWhichDayName());
                if (DataUtil.isNotEmpty(numberByText)) {
                    companyWeeks.add(numberByText);
                }
            }
            companyWorkTimeVo.setWorkingHours(companyWeeks);

            UnionCompanyWorkTime unionCompanyWorkTime = new UnionCompanyWorkTime();
            BeanUtils.copyProperties(companyWorkTimeDto, unionCompanyWorkTime);
            List<UnionCompanyWorkTime> unionCompanyWorkTimeList = unionCompanyWorkTimeService.getUnionCompanyWorkTime(unionCompanyWorkTime);
            companyWorkTimeVo.setCompanyWorkTimes(unionCompanyWorkTimeList);
            return companyWorkTimeVo;
        } else {
            throw new DefaultException("企业上下班时间为空");
        }
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
        if (DataUtil.isNotEmpty(companyWorkTimeDto.getType())) {
            wrapper.eq(CompanyWorkTime::getType, companyWorkTimeDto.getType());
        }
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public CompanyWorkTime getCeoCompanyWorkTimeById() {
        LambdaQueryWrapper<CompanyWorkTime> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CompanyWorkTime::getType, 0);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Integer addCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto) {
        CompanyWorkTime companyWorkTime = new CompanyWorkTime();
        BeanUtils.copyProperties(companyWorkTimeDto, companyWorkTime);
        companyWorkTime.setCreateTime(new Date());
        return baseMapper.insert(companyWorkTime);
    }

    @Override
    public Integer updateCompanyWorkTime(CompanyWorkTimeDto companyWorkTimeDto) {
        CompanyWorkTime companyWorkTime = new CompanyWorkTime();
        BeanUtils.copyProperties(companyWorkTimeDto, companyWorkTime);
        LambdaUpdateWrapper<CompanyWorkTime> updateWrapper = new LambdaUpdateWrapper<>();
        if (DataUtil.isNotEmpty(companyWorkTime.getId())) {
            updateWrapper.eq(CompanyWorkTime::getId, companyWorkTime.getId());
        }
        if (DataUtil.isNotEmpty(companyWorkTimeDto.getCompanyId())) {
            updateWrapper.eq(CompanyWorkTime::getCompanyId, companyWorkTimeDto.getCompanyId());
        }
        if (DataUtil.isNotEmpty(companyWorkTimeDto.getType())) {
            updateWrapper.eq(CompanyWorkTime::getType, companyWorkTimeDto.getType());
        }
        return baseMapper.update(companyWorkTime, updateWrapper);
    }


    @Override
    public Integer setCommuteTime(CompanyWorkTimeAddDto companyWorkTimeAddDto) {
        List<Integer> workingHours = companyWorkTimeAddDto.getWorkingHours();
        if (DataUtil.isEmpty(workingHours)) {
            throw new DefaultException("企业上下班周期为空");
        }
        List<UnionCompanyWorkTimeDto> companyWorkTimes = companyWorkTimeAddDto.getCompanyWorkTimes();
        if (DataUtil.isEmpty(companyWorkTimes)) {
            throw new DefaultException("企业上下班时间为空");
        }

        CompanyWorkTimeDto cdo = new CompanyWorkTimeDto();
        cdo.setCompanyId(companyWorkTimeAddDto.getCompanyId());
        cdo.setType(companyWorkTimeAddDto.getType());
        //获取企业上下班时间详情
        CompanyWorkTime companyWorkTime = getCompanyWorkTimeById(cdo);
        if (DataUtil.isNotEmpty(companyWorkTime)) {
            //编辑企业上下班时间
            CompanyWorkTimeDto companyWorkTimeDto = new CompanyWorkTimeDto();
            BeanUtils.copyProperties(companyWorkTimeAddDto, companyWorkTimeDto);
            Integer integer = updateCompanyWorkTime(companyWorkTimeDto);
            if (integer > 0) {
                //删除已有的上班时间
                companyWeekService.deleteCompanyWeek(companyWorkTimeAddDto.getCompanyId(), companyWorkTimeAddDto.getType());
                //删除已有的打卡时间
                unionCompanyWorkTimeService.deleteUnionCompanyWorkTime(companyWorkTimeAddDto.getCompanyId(), companyWorkTimeAddDto.getType());
                for (Integer workingHour : workingHours) {
                    //转为中文存储
                    String textByNumber = WeeKEnum.getTextByNumber(workingHour);
                    CompanyWeek companyWeek = new CompanyWeek();
                    companyWeek.setCompanyId(companyWorkTimeDto.getCompanyId());
                    companyWeek.setWhichDayName(textByNumber);
                    companyWeek.setType(companyWorkTimeAddDto.getType());
                    //新增上班时间
                    companyWeekService.addCompanyWeek(companyWeek);
                }
                for (UnionCompanyWorkTimeDto workTime : companyWorkTimes) {
                    workTime.setCompanyId(companyWorkTimeDto.getCompanyId());
                    workTime.setType(companyWorkTimeAddDto.getType());
                    //新增打卡时间
                    unionCompanyWorkTimeService.addUnionCompanyWorkTime(workTime);
                }
            }
            return integer;
        } else {
            //新增企业上下班时间
            CompanyWorkTimeDto companyWorkTimeDto = new CompanyWorkTimeDto();
            BeanUtils.copyProperties(companyWorkTimeAddDto, companyWorkTimeDto);
            Integer integer = addCompanyWorkTime(companyWorkTimeDto);
            if (integer > 0) {
                for (Integer workingHour : workingHours) {
                    //转为中文存储
                    String textByNumber = WeeKEnum.getTextByNumber(workingHour);
                    CompanyWeek companyWeek = new CompanyWeek();
                    companyWeek.setCompanyId(companyWorkTimeDto.getCompanyId());
                    companyWeek.setWhichDayName(textByNumber);
                    companyWeek.setType(companyWorkTimeAddDto.getType());
                    //新增上班时间
                    companyWeekService.addCompanyWeek(companyWeek);
                }
                for (UnionCompanyWorkTimeDto workTime : companyWorkTimes) {
                    workTime.setCompanyId(companyWorkTimeDto.getCompanyId());
                    workTime.setType(companyWorkTimeAddDto.getType());
                    //新增打卡时间
                    unionCompanyWorkTimeService.addUnionCompanyWorkTime(workTime);
                }
            }
            return integer;
        }
    }

}