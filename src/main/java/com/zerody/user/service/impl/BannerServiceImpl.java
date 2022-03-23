package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcoon.common.exception.ex.Assertion;
import com.itcoon.transform.starter.Transformer;
import com.zerody.common.api.bean.PageQueryDto;
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

import java.util.Date;

/**
 * @author yumiaoxia
 * @since 2021-07-06
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public void create(BannerCreateDto param) {
        Assertion.assertTrue(param.getLinkType() == LinkType.NONE || StringUtils.hasText(param.getLinkUrl())).raise(PlatformResponseEnum.LINK_URL_EMPTY);
        Banner Banner = new Banner();
        BeanUtils.copyProperties(param, Banner);
        Banner.setCreateTime(new Date());
        this.baseMapper.insert(Banner);
    }

    @Override
    public IPage<BannerListVo> pageAd(BannerListDto param, PageQueryDto pageParam) {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(param.getName()), Banner::getName, String.format(CommonConstants.QUERY_LIKE_RAW, param.getName()));
        wrapper.eq(param.getType() != null, Banner::getType, param.getType());
        wrapper.eq(param.getLocation() != null, Banner::getLocation, param.getLocation());
        wrapper.eq(param.getLinkType() != null, Banner::getLinkType, param.getLinkType());
        wrapper.eq(param.getEnable() != null, Banner::getEnable, param.getEnable());

        IPage<Banner> page = this.baseMapper.selectPage(PageUtils.getPageRequest(pageParam, "create_time", PageUtils.OrderType.DESC), wrapper);
        IPage<BannerListVo> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resultPage.setRecords(Transformer.toList(BannerListVo.class).apply(page.getRecords()).done());
        return resultPage;
    }

    @Override
    public BannerVo detail(String id) {
        Banner Banner = this.baseMapper.selectById(id);
        return Transformer.to(BannerVo.class).apply(Banner).done();
    }

    @Override
    public void update(String id, AdvertisingUpdateDto param) {
        Banner Banner = this.baseMapper.selectById(id);
        LocalBeanUtils.copyNotNullProperties(param, Banner);
        this.baseMapper.updateById(Banner);
    }

    @Override
    public void delete(String id) {
        this.baseMapper.deleteById(id);
    }
}
