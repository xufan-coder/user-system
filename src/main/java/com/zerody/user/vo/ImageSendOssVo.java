package com.zerody.user.vo;

import lombok.Data;

@Data
public class ImageSendOssVo {
    /**
     * 路径
     */
    private String fileKey;
    /**
     * 新的企业id
     */
    private String companyId;

    /**
     * 后缀
     */
    private String suffix;
}
