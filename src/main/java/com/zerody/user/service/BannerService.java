package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.PageQueryDto;

import com.zerody.user.domain.Banner;
import com.zerody.user.dto.AdvertisingUpdateDto;
import com.zerody.user.dto.BannerCreateDto;
import com.zerody.user.dto.BannerListDto;
import com.zerody.user.vo.BannerListVo;
import com.zerody.user.vo.BannerVo;

import java.util.List;

/**
 * @author yumiaoxia
 * @since 2021-07-06
 */
public interface BannerService extends IService<Banner> {

    void create(BannerCreateDto param);

    IPage<BannerListVo> pageAd(BannerListDto param, PageQueryDto pageParam);

    List<BannerListVo> pageApp(BannerListDto param);

    BannerVo  detail(String id);

    void update(String id, AdvertisingUpdateDto param);

    void delete(String id);
}
