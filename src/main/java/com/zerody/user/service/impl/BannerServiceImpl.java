package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcoon.common.exception.ex.Assertion;
import com.itcoon.transform.starter.Transformer;
import com.zerody.common.api.bean.PageQueryDto;
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
        banner.setEffectiveStartTime(DateUtil.getyMdHmsDate(param.getEffectiveStartTime()));
        banner.setEffectiveEndTime(DateUtil.getyMdHmsDate(param.getEffectiveEndTime()));
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
        wrapper.ge(param.getEffectiveStartTime()!=null,Banner::getEffectiveStartTime,param.getEffectiveStartTime());
        wrapper.le(param.getEffectiveEndTime()!=null,Banner::getEffectiveEndTime,param.getEffectiveEndTime());
        IPage<Banner> page = this.baseMapper.selectPage(PageUtils.getPageRequest(pageParam, "create_time", PageUtils.OrderType.DESC), wrapper);
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
        wrapper.eq(param.getEnable() != null, Banner::getEnable, param.getEnable());
        wrapper.ge(Banner::getEffectiveStartTime,new Date());
        wrapper.le(Banner::getEffectiveEndTime,new Date());
        wrapper.orderByDesc(Banner::getCreateTime);
        List<Banner> page = this.baseMapper.selectList(wrapper);
        List<BannerListVo> listVos=new ArrayList<>();
        if(Objects.nonNull(page)&&page.size()>0) {
            page.forEach(item -> {
                BannerListVo bannerListVo=new BannerListVo();
                BeanUtils.copyProperties(item,bannerListVo);
                listVos.add(bannerListVo);
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
        if(!StringUtils.isEmpty(param.getEffectiveStartTime())) {
            banner.setEffectiveStartTime(DateUtil.getyMdHmsDate(param.getEffectiveStartTime()));
        }
        if(!StringUtils.isEmpty(param.getEffectiveEndTime())) {
            banner.setEffectiveEndTime(DateUtil.getyMdHmsDate(param.getEffectiveEndTime()));
        }

        this.baseMapper.updateById(banner);
    }

    @Override
    public void delete(String id) {
        this.baseMapper.deleteById(id);
    }
}
