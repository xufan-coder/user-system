package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.Image;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageMapper extends BaseMapper<Image> {
    List<String> getImageListByConnectIdAndType(@Param("connectId") String connectId, @Param("imageType") String imageType);
}