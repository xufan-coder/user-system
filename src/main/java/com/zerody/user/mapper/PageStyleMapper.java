package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.PageStyle;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Author: YeChangWei
 * @Date: 2022/12/30 9:44
 */
public interface PageStyleMapper extends BaseMapper<PageStyle> {
    /**
    *
    *  @description   获取当前时段的页面风格
    *  @author        YeChangWei
    *  @date          2022/12/30 15:08
    *  @return        com.zerody.user.domain.PageStyle
    */
    PageStyle getNowPageStyle(@Param("time")Date time);
}
