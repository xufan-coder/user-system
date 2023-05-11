package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcoon.common.exception.ex.Assertion;
import com.itcoon.transform.starter.Transformer;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.enums.util.TimeFormat;
import com.zerody.common.utils.DateUtil;
import com.zerody.user.constant.CommonConstants;
import com.zerody.user.domain.Banner;
import com.zerody.user.dto.AdvertisingUpdateDto;
import com.zerody.user.dto.BannerCreateDto;
import com.zerody.user.dto.BannerListDto;
import com.zerody.user.enums.LinkType;
import com.zerody.user.execption.PlatformResponseEnum;
import com.zerody.user.mapper.BannerMapper;
import com.zerody.user.service.BannerService;
import com.zerody.user.util.LocalBeanUtils;
import com.zerody.user.util.PageUtils;
import com.zerody.user.vo.BannerListVo;
import com.zerody.user.vo.BannerVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author yumiaoxia
 * @since 2021-07-06
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public void create(BannerCreateDto param) {
        Assertion.assertTrue(param.getLinkType() == LinkType.NONE.code() || StringUtils.hasText(param.getLinkUrl())).raise(PlatformResponseEnum.LINK_URL_EMPTY);
        Banner banner = new Banner();
        BeanUtils.copyProperties(param, banner);
        banner.setCreateTime(new Date());
        try {
            banner.setEffectiveStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(param.getEffectiveStartTime()));
            banner.setEffectiveEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(param.getEffectiveEndTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.baseMapper.insert(banner);
    }

    @Override
    public IPage<BannerListVo> pageAd(BannerListDto param, PageQueryDto pageParam) {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(param.getName()), Banner::getName, String.format(CommonConstants.QUERY_LIKE_RAW, param.getName()));
       // wrapper.eq(param.getType() != null, Banner::getType, param.getType());
        wrapper.eq(param.getLocation() != null, Banner::getLocation, param.getLocation());
        wrapper.eq(param.getLinkType() != null, Banner::getLinkType, param.getLinkType());
        wrapper.eq(param.getEnable() != null, Banner::getEnable, param.getEnable());
        wrapper.ge(param.getEffectiveStartTime()!=null,Banner::getCreateTime,param.getEffectiveStartTime());
        wrapper.le(param.getEffectiveEndTime()!=null,Banner::getCreateTime,param.getEffectiveEndTime());
        IPage<Banner> page = this.baseMapper.selectPage(PageUtils.getPageRequest(pageParam, "order_num", PageUtils.OrderType.DESC), wrapper);
        IPage<BannerListVo> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resultPage.setRecords(Transformer.toList(BannerListVo.class).apply(page.getRecords()).done());
        return resultPage;
    }

    @Override
    public List<BannerListVo> pageApp(BannerListDto param) {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(param.getName()), Banner::getName, String.format(CommonConstants.QUERY_LIKE_RAW, param.getName()));
        // wrapper.eq(param.getType() != null, Banner::getType, param.getType());
        wrapper.eq(param.getLocation() != null, Banner::getLocation, param.getLocation());
        wrapper.eq(param.getLinkType() != null, Banner::getLinkType, param.getLinkType());
        wrapper.eq( Banner::getEnable, true);
        wrapper.orderByDesc(Banner::getOrderNum);
        List<Banner> page = this.baseMapper.selectList(wrapper);
        List<BannerListVo> listVos = new ArrayList<>();
        if (Objects.nonNull(page) && page.size() > 0) {
            page.forEach(item -> {
                try {
                    if (Objects.nonNull(item.getEffectiveStartTime()) &&
                            DateUtil.timeCompareGtEq(new Date(), item.getEffectiveStartTime(), TimeFormat.YYYY_MM_DD_HH_MM_SS)) {
                        if (Objects.nonNull(item.getEffectiveEndTime()) &&
                                DateUtil.timeCompareGtEq(item.getEffectiveEndTime(), new Date(), TimeFormat.YYYY_MM_DD_HH_MM_SS)) {
                            BannerListVo bannerListVo = new BannerListVo();
                            BeanUtils.copyProperties(item, bannerListVo);
                            listVos.add(bannerListVo);
                        } else if (Objects.isNull(item.getEffectiveEndTime())) {
                            BannerListVo bannerListVo = new BannerListVo();
                            BeanUtils.copyProperties(item, bannerListVo);
                            listVos.add(bannerListVo);
                        }
                    } else if (Objects.isNull(item.getEffectiveStartTime()) && Objects.isNull(item.getEffectiveEndTime())) {
                        BannerListVo bannerListVo = new BannerListVo();
                        BeanUtils.copyProperties(item, bannerListVo);
                        listVos.add(bannerListVo);
                    } else if (Objects.isNull(item.getEffectiveStartTime()) && Objects.nonNull(item.getEffectiveEndTime()) &&
                            DateUtil.timeCompareGtEq(item.getEffectiveEndTime(), new Date(), TimeFormat.YYYY_MM_DD_HH_MM_SS)) {
                        BannerListVo bannerListVo = new BannerListVo();
                        BeanUtils.copyProperties(item, bannerListVo);
                        listVos.add(bannerListVo);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        return listVos;
    }

    @Override
    public BannerVo detail(String id) {
        Banner banner = this.baseMapper.selectById(id);
        return Transformer.to(BannerVo.class).apply(banner).done();
    }

    @Override
    public void update(String id, AdvertisingUpdateDto param) {
        Banner banner = this.baseMapper.selectById(id);
        LocalBeanUtils.copyNotNullProperties(param, banner);
        try {
            if (!StringUtils.isEmpty(param.getEffectiveStartTime())) {
                banner.setEffectiveStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(param.getEffectiveStartTime()));
            }
            if (!StringUtils.isEmpty(param.getEffectiveEndTime())) {
                banner.setEffectiveEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(param.getEffectiveEndTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.baseMapper.updateById(banner);
    }

    @Override
    public void delete(String id) {
        this.baseMapper.deleteById(id);
    }
}
