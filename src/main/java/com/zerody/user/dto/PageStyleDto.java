package com.zerody.user.dto;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author: YeChangWei
 * @Date: 2022/12/30 9:50
 */
@Data
public class PageStyleDto {

    /**
     * 页面样式名称
     */
    @NotBlank
    @Length(max = 20,message = "名称不能超过20个字")
    private String name;
    /**
     * 图片路径
     */
    @NotBlank
    private String pictureUrl;
    /**
     * 生效时间
     */
    private Date startTime;
    /**
     * 失效时间
     */
    private Date endTime;
    /**
     * 状态（0停用 1启用）
     */
    private Integer state;

}
