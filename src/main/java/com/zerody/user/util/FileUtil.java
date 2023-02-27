package com.zerody.user.util;

import com.zerody.common.utils.DataUtil;
import com.zerody.oss.api.dto.SlOssFile;
import com.zerody.oss.api.util.Uploader;
import com.zerody.user.enums.TemplateTypeEnum;
import com.zerody.user.feign.OssFeignService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author PengQiang
 * @ClassName FileUtil
 * @DateTime 2023/2/24 10:39
 */
@Component
public class FileUtil {

    @Autowired
    private OssFeignService ossFeignService;

    private static OssFeignService ossFeignStaicService;

    @Autowired
    private ResourceLoader resourceLoader;

    private static ResourceLoader resourceStaticLoader;

    @PostConstruct
    private void init() {
        ossFeignStaicService = this.ossFeignService;
        resourceStaticLoader = this.resourceLoader;
    }

    public static String uploadOssTemate(TemplateTypeEnum fileName) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();

        try {

            String path = "static/template" + "/" + fileName.getUrl();
            org.springframework.core.io.Resource resource = resourceStaticLoader.getResource("classpath:" + path);
            Uploader uploader = new Uploader();
            MultipartFile file = uploader.getMulFile(resource.getInputStream(),path);
            response.flushBuffer();
            return uploadOssPrivate(file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 召唤jvm的垃圾回收器
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String uploadOssPrivate(MultipartFile file) {
        SlOssFile data = ossFeignStaicService.fileUploadPrivate2(file).getData();
        if (DataUtil.isNotEmpty(data)) {
            return data.getFileUrl();
        }
        return null;
    }

}
