package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.PageStyle;
import com.zerody.user.dto.PageStyleDto;
import com.zerody.user.mapper.PageStyleMapper;
import com.zerody.user.service.PageStyleService;
import com.zerody.user.vo.PageStyleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: YeChangWei
 * @Date: 2022/12/30 9:43
 */
@Service
public class PageStyleServiceImpl extends ServiceImpl<PageStyleMapper, PageStyle> implements PageStyleService {

    @Override
    public void updatePageStyle(PageStyleDto dto) {
        PageStyle pageStyle = new PageStyle();
        BeanUtils.copyProperties(dto,pageStyle);
        this.updateById(pageStyle);

       /* PageStyle one = this.getById(dto.getId());
       if(DataUtil.isNotEmpty(one)){
            pageStyle.setId(one.getId());
            this.baseMapper.updateById(pageStyle);
        } else {
            pageStyle.setCreateTime(new Date());
            this.baseMapper.insert(pageStyle);
        }*/
    }

    @Override
    public PageStyleVo getPageStyleInfo(String id) {
        PageStyleVo pageStyleVo = new PageStyleVo();
        QueryWrapper<PageStyle> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PageStyle::getDeleted, YesNo.NO);
        wrapper.lambda().eq(PageStyle::getId,id);
        PageStyle pageStyle = this.baseMapper.selectOne(wrapper);
        BeanUtils.copyProperties(pageStyle,pageStyleVo);
        return pageStyleVo;
    }

    @Override
    public PageStyle getNowPageStyle(Date time) {
        PageStyle pageStyle = this.baseMapper.getNowPageStyle(time);
        return pageStyle;
    }
}
