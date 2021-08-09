package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.Image;
import com.zerody.user.mapper.ImageMapper;
import com.zerody.user.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName ImageServiceImpl
 * @DateTime 2021/8/4_9:24
 * @Deacription TODO
 */
@Slf4j
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
    @Override
    public void addImages(QueryWrapper<Image> removeQw, List<Image> images) {
        if (DataUtil.isNotEmpty(removeQw)) {
            this.remove(removeQw);
        }
        if (CollectionUtils.isEmpty(images)) {
            return;
        }
        this.saveBatch(images);

    }
}
