package com.zerody.user.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.domain.ConvertImage;
import com.zerody.user.domain.Image;
import com.zerody.user.service.CommonFileService;
import com.zerody.user.service.ConvertImageService;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.SysStaffInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kuang
 */
@Component
public class StaffInfoUtil {

    private static ImageService imageService;


    private static CommonFileService commonFileService;


    private static ConvertImageService convertImageService;

    @Autowired
    public void setFeign(ImageService imageService,CommonFileService commonFileService,ConvertImageService convertImageService) {
        StaffInfoUtil.imageService = imageService;
        StaffInfoUtil.commonFileService = commonFileService;
        StaffInfoUtil.convertImageService = convertImageService;
    }


    public static  void saveImage(List<String> images, String userId, String type){
        List<Image> imageAdds = new ArrayList<>();
        //图片转换
        List<ConvertImage> convertImages = new ArrayList<>(images.size());
        Image image;
        Date now = new Date();
        if(DataUtil.isNotEmpty(images)) {

            for (String s : images) {
                image = new Image();
                image.setConnectId(userId);
                image.setId(UUIDutils.getUUID32());
                image.setImageType(type);
                image.setImageUrl(s);
                image.setCreateTime(new Date());
                imageAdds.add(image);
            }
        }
        QueryWrapper<Image> imageRemoveQw = new QueryWrapper<>();
        imageRemoveQw.lambda().eq(Image::getConnectId, userId);
        imageRemoveQw.lambda().eq(Image::getImageType, type);
        imageService.addImages(imageRemoveQw, imageAdds);
    }

    public static void saveFile(List<CommonFile> cooperationFiles, String userId, String type){

        List<CommonFile> files = new ArrayList<>();
        //图片转换
        List<ConvertImage> convertImages = new ArrayList<>(cooperationFiles.size());
        CommonFile file;
        Date now = new Date();

        if(DataUtil.isNotEmpty(cooperationFiles)) {
            for (CommonFile s : cooperationFiles) {
                file = new CommonFile();
                file.setConnectId(userId);
                file.setId(UUIDutils.getUUID32());
                file.setFileType(type);
                file.setFileUrl(s.getFileUrl());
                file.setFileName(s.getFileName());
                file.setFormat(s.getFormat());
                file.setCreateTime(new Date());

                files.add(file);
            }
        }
        QueryWrapper<CommonFile> remQ = new QueryWrapper<>();
        remQ.lambda().eq(CommonFile::getConnectId, userId);
        remQ.lambda().eq(CommonFile::getFileType, type);
        //删除之前的文件，再批量新增文件
        commonFileService.addFiles(remQ, files);
        if (DataUtil.isNotEmpty(convertImages)) {
            convertImageService.saveBatch(convertImages);
        }
    }
}
