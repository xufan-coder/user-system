package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.domain.AppVersionInfo;
import com.zerody.user.dto.AppVersionInfoAddDto;
import com.zerody.user.dto.AppVersionInfoListDto;
import com.zerody.user.dto.AppVersionInfoModifyDto;
import com.zerody.user.dto.AppVersionInfoPageDto;
import com.zerody.user.mapper.AppVersionInfoMapper;
import com.zerody.user.service.AppVersionInfoService;
import com.zerody.user.vo.AppVersionInfoPageVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangpingping
 * @date 2021年12月16日 14:17
 */
@Slf4j
@Service
public class AppVersionInfoServiceImpl extends ServiceImpl<AppVersionInfoMapper, AppVersionInfo> implements AppVersionInfoService {

    @Override
    public void addAppVersionInfo(AppVersionInfoAddDto appVersionInfoAddDto) {
        AppVersionInfo appVersionInfo = new AppVersionInfo();
        BeanUtils.copyProperties(appVersionInfoAddDto, appVersionInfo);
        appVersionInfo.setId(UUIDutils.getUUID32());
        appVersionInfo.setDeleted(YesNo.NO);
        appVersionInfo.setCreateTime(new Date());
        this.save(appVersionInfo);
    }

    @Override
    public void modifyAppVersionInfo(AppVersionInfoModifyDto appVersionInfoModifyDto) {
        AppVersionInfo appVersionInfo = this.getById(appVersionInfoModifyDto.getId());
        BeanUtils.copyProperties(appVersionInfoModifyDto, appVersionInfo);
        appVersionInfo.setUpdateTime(new Date());
        this.updateById(appVersionInfo);
    }

    @Override
    public void removeAppVersionInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new DefaultException("参数ID不能为空");
        }
        this.removeById(id);

    }

    @Override
    public IPage<AppVersionInfoPageVo> queryAppVersionInfoPage(AppVersionInfoPageDto param) {
        IPage<AppVersionInfo> iPage = new Page<>(param.getCurrent(), param.getSize());
        QueryWrapper<AppVersionInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotEmpty(param.getId()), AppVersionInfo::getId, param.getId());
        queryWrapper.lambda().eq(AppVersionInfo::getDeleted, YesNo.NO);
        queryWrapper.lambda().orderByDesc(AppVersionInfo::getCreateTime);
        iPage = this.baseMapper.selectPage(iPage, queryWrapper);
        IPage<AppVersionInfoPageVo> infoPageVoIPage = new Page<>();
        if (Objects.nonNull(iPage)) {
            infoPageVoIPage.setCurrent(iPage.getCurrent());
            infoPageVoIPage.setSize(iPage.getSize());
            infoPageVoIPage.setPages(iPage.getPages());
            infoPageVoIPage.setTotal(iPage.getTotal());
            List<AppVersionInfoPageVo> appVersionInfoPageVos = Lists.newArrayList();
            if (Objects.nonNull(iPage.getRecords())) {
                iPage.getRecords().forEach(item -> {
                    AppVersionInfoPageVo appVersionInfoPageVo = new AppVersionInfoPageVo();
                    BeanUtils.copyProperties(item, appVersionInfoPageVo);
                    appVersionInfoPageVo.setCreateTime(item.getCreateTime());
                    appVersionInfoPageVo.setUpdateTime(item.getUpdateTime());
                    appVersionInfoPageVos.add(appVersionInfoPageVo);
                });
            }
            infoPageVoIPage.setRecords(appVersionInfoPageVos);
        }
        return infoPageVoIPage;
    }

    @Override
    public List<AppVersionInfoPageVo> queryAppVersionInfoList(AppVersionInfoListDto param) {
        List<AppVersionInfoPageVo> voList = Lists.newArrayList();
        QueryWrapper<AppVersionInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotEmpty(param.getId()), AppVersionInfo::getId, param.getId());
        queryWrapper.lambda().eq(AppVersionInfo::getDeleted, YesNo.NO);
        queryWrapper.lambda().orderByDesc(AppVersionInfo::getCreateTime);
        this.list(queryWrapper).forEach(item -> {
            AppVersionInfoPageVo appVersionInfo = new AppVersionInfoPageVo();
            BeanUtils.copyProperties(item, appVersionInfo);
            voList.add(appVersionInfo);
        });
        return voList;
    }
}
