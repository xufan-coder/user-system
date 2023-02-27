package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.ConvertImage;

import java.util.List;

/**
 *@ClassName ConvertImageService
 *@author    PengQiang
 *@DateTime  2023/2/24 15:09
 */
public interface ConvertImageService extends IService<ConvertImage> {


    List<ConvertImage> dohaveNotConvert();

    void convertToImage(List<ConvertImage> converts);
}
