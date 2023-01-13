package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.domain.PageStyle;
import com.zerody.user.vo.PageStyleVo;
import org.apache.ibatis.annotations.Param;


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
    PageStyle getNowPageStyle();
    /**
    *
    *  @description   分页查询所有页面风格
    *  @author        YeChangWei
    *  @date          2022/12/30 16:28
    *  @return        com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.PageStyleVo>
    */
    IPage<PageStyleVo> getAllPageStyle(IPage<PageStyleVo> page);

    /**
     *  页面风格
     * @param endTime
     * @return
     */
    PageStyle getNowPageStyleEstimate(@Param("startTime") String startTime);
}
