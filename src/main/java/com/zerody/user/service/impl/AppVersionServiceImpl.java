package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itcoon.common.exception.ex.Assertion;
import com.itcoon.transform.starter.Transformer;
import com.zerody.common.api.bean.IUser;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.constant.CommonConstants;
import com.zerody.user.domain.AppVersion;
import com.zerody.user.dto.AppVersionCreateDto;
import com.zerody.user.dto.AppVersionListDto;
import com.zerody.user.dto.AppVersionUpdateDto;
import com.zerody.user.execption.PlatformResponseEnum;
import com.zerody.user.mapper.AppVersionMapper;
import com.zerody.user.service.AppVersionService;
import com.zerody.user.vo.AppVersionListVo;
import com.zerody.user.vo.AppVersionVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
@Service
@Slf4j
class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    @Override
    public void createAppVersion(AppVersionCreateDto param) {
        IUser userData = UserUtils.getUserData();
        Assertion.assertNotNull(userData).raise(PlatformResponseEnum.INVALID_TOKEN);
        AppVersion appVersion = new AppVersion();
        BeanUtils.copyProperties(param, appVersion);

        Date now = new Date();
        appVersion.setUpdateTime(now);
        appVersion.setCreateTime(now);
        appVersion.setCreateBy(userData.getUserName());
        this.baseMapper.insert(appVersion);
    }

    @Override
    public void updateAppVersion(String id, AppVersionUpdateDto param) {
        AppVersion appVersion = this.baseMapper.selectById(id);
        Assertion.assertNotNull(appVersion).raise(PlatformResponseEnum.APP_VERSION_MISSING);
        BeanUtils.copyProperties(param, appVersion);
        appVersion.setUpdateTime(new Date());
        this.baseMapper.updateById(appVersion);
    }

    @Override
    public Page<AppVersionListVo> pageVersion(AppVersionListDto param, PageQueryDto pageParam) {
        LambdaQueryWrapper<AppVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(param.getOsType() != null, AppVersion::getOsType, param.getOsType());
        wrapper.eq(param.getSystemType() != null, AppVersion::getSystemType, param.getSystemType());
        wrapper.like(!StringUtils.isEmpty(param.getName()), AppVersion::getName, String.format(CommonConstants.QUERY_LIKE_RAW, param.getName()));
        wrapper.like(!StringUtils.isEmpty(param.getVersion()), AppVersion::getVersion, String.format(CommonConstants.QUERY_LIKE_RAW, param.getVersion()));

        Page pageRequest = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        if (!StringUtils.isEmpty(pageParam.getOrderProp())) {
            if ("asc".equals(pageParam.getOrderType())) {
                pageRequest.setOrders(Lists.newArrayList(OrderItem.asc(pageParam.getOrderColumn())));
            } else {
                pageRequest.setOrders(Lists.newArrayList(OrderItem.desc(pageParam.getOrderColumn())));
            }
        }
        Page<AppVersion> appVersionPage = this.baseMapper.selectPage(pageRequest, wrapper);
        Page<AppVersionListVo> resultPage = new Page<>(appVersionPage.getCurrent(), appVersionPage.getSize(), appVersionPage.getTotal());
        resultPage.setRecords(Transformer.toList(AppVersionListVo.class).apply(appVersionPage.getRecords()).done());
        return resultPage;
    }

    @Override
    public AppVersionVo detail(String id) {
        AppVersion appVersion = this.baseMapper.selectById(id);
        Assertion.assertNotNull(appVersion).raise(PlatformResponseEnum.APP_VERSION_MISSING);
        return Transformer.to(AppVersionVo.class).apply(appVersion).done();
    }

    @Override
    public void deleteVersion(String id) {
        AppVersion appVersion = this.baseMapper.selectById(id);
        Assertion.assertNotNull(appVersion).raise(PlatformResponseEnum.APP_VERSION_MISSING);
        this.baseMapper.deleteById(id);
    }

    @Override
    public List<AppVersion> queryDetail(AppVersionListDto param) {
        QueryWrapper<AppVersion> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().gt(StringUtils.isNotEmpty(param.getVersion()), AppVersion::getVersion, param.getVersion());
        queryWrapper.lambda().eq(Objects.nonNull(param.getOsType()), AppVersion::getOsType, param.getOsType());
        queryWrapper.lambda().eq(Objects.nonNull(param.getSystemType()), AppVersion::getSystemType, param.getSystemType());
        queryWrapper.lambda().orderByDesc(AppVersion::getVersion);
        List<AppVersion> appVersion = this.baseMapper.selectList(queryWrapper);
        return appVersion;
    }

    @Override
    public AppVersion queryNewestVersion(AppVersionListDto param) {
        if (Objects.isNull(param) || Objects.isNull(param.getOsType())) {
            throw new DefaultException("版本类型不能为空");
        }
        QueryWrapper<AppVersion> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Objects.nonNull(param.getOsType()), AppVersion::getOsType, param.getOsType());
        queryWrapper.lambda().eq(Objects.nonNull(param.getSystemType()), AppVersion::getSystemType, param.getSystemType());
        queryWrapper.lambda().orderByDesc(AppVersion::getVersion);
        List<AppVersion> appVersion = this.baseMapper.selectList(queryWrapper);
        AppVersion appVersion1 = new AppVersion();
        appVersion.stream().limit(1).collect(Collectors.toList()).forEach(item -> {
            BeanUtils.copyProperties(item, appVersion1);
        });
        return appVersion1;
    }


}