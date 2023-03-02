package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.oss.api.feign.OssFeignService;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.domain.ConvertImage;
import com.zerody.user.domain.Image;
import com.zerody.user.service.CommonFileService;
import com.zerody.user.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zerody.user.mapper.ConvertImageMapper;
import com.zerody.user.service.ConvertImageService;

import java.util.ArrayList;
import java.util.List;

/**
 *@ClassName ConvertImageServiceImpl
 *@author    PengQiang
 *@DateTime  2023/2/24 15:09
 */
@Service
public class ConvertImageServiceImpl extends ServiceImpl<ConvertImageMapper, ConvertImage> implements ConvertImageService{

    @Autowired
    private OssFeignService ossFeignService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommonFileService commonFileService;

    @Override
    public List<ConvertImage> dohaveNotConvert() {

        List<ConvertImage> list = this.baseMapper.getHaveNotConvert();
        if (list == null) {
            list = new ArrayList<>();
        }
        List<ConvertImage> list2 = this.baseMapper.getHaveNotConvert2();
        if (DataUtil.isNotEmpty(list2)) {
            list.addAll(list2);
        }
        if (DataUtil.isEmpty(list)) {
            return list;
        }
        this.updateBatchById(list);
        return list;
    }

    @Override
    public void doConvertToImage(ConvertImage convert) {
        DataResult<String> ossResult = this.ossFeignService.getToImage(convert.getOriginalFileUrl());
        if (!ossResult.isSuccess() || DataUtil.isEmpty(ossResult.getData())) {
            throw new DefaultException("转换图片出错");
        }
        String newImageUrl = ossResult.getData();
        Image image = this.imageService.getById(convert.getConnectId());
        if (DataUtil.isNotEmpty(image)) {
            image.setImageUrl(newImageUrl);
            this.imageService.updateById(image);
            return;
        }
        CommonFile file = this.commonFileService.getById(convert.getConnectId());
        if (DataUtil.isEmpty(file)) {
            throw new DefaultException("找不到源文件");
        }
        if (DataUtil.isNotEmpty(file.getFileName())) {
            String suffix = newImageUrl.substring(newImageUrl.lastIndexOf("."));
            String fileName = file.getFileName().substring(0, file.getFileName().lastIndexOf("."));
            file.setFileName(fileName + suffix);
        }
        file.setFileUrl(newImageUrl);
        this.commonFileService.updateById(file);


    }
}
