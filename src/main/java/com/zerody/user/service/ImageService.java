package com.zerody.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.Image;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName ImageService
 * @DateTime 2021/8/4_9:23
 * @Deacription TODO
 */
public interface ImageService extends IService<Image> {

    /**
     *
     *  添加图片
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/8/4 9:51
     * @param                removeQw 删除条件
     * @param                images 图片对象
     * @return               void
     */
    void addImages(QueryWrapper<Image> removeQw, List<Image> images);
}
