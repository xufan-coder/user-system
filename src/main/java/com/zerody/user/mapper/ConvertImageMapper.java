package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.ConvertImage;

import java.util.List;

/**
 *@ClassName ConvertImageMapper
 *@author    PengQiang
 *@DateTime  2023/2/24 15:09
 */
public interface ConvertImageMapper extends BaseMapper<ConvertImage> {
    List<ConvertImage> getHaveNotConvert();

    List<ConvertImage> getHaveNotConvert2();
}