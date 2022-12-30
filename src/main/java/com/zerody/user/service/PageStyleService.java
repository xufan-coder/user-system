package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.user.domain.PageStyle;
import com.zerody.user.dto.PageStyleDto;
import com.zerody.user.vo.PageStyleVo;

import java.util.Date;

/**
 * @Author: YeChangWei
 * @Date: 2022/12/30 9:42
 */
public interface PageStyleService extends IService<PageStyle> {
    /**
    *
    *  @description   编辑页面风格
    *  @author        YeChangWei
    *  @date          2022/12/30 11:26
    *  @return        void
    */
    void updatePageStyle(PageStyleDto dto);
    /**
    *
    *  @description   查询页面风格详情
    *  @author        YeChangWei
    *  @date          2022/12/30 11:58
    *  @return        com.zerody.user.vo.PageStyleVo
    */
    PageStyleVo getPageStyleInfo(String id);
    /**
    *
    *  @description   获取当前时段的页面风格
    *  @author        YeChangWei
    *  @date          2022/12/30 13:47
    *  @return        java.lang.String
    */
    PageStyle getNowPageStyle();
    /**
    *
    *  @description   分页查询所有页面风格
    *  @author        YeChangWei
    *  @date          2022/12/30 16:20
    *  @return        com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.PageStyleVo>
    */
    IPage<PageStyleVo> getAllPageStyle(PageQueryDto dto);
}
