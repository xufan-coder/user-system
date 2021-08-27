package com.zerody.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.domain.Image;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年08月25日 17:51
 */
@Data
public class StaffHistoryDto {
    private String id;
    /**
     * 描述
     */
    @NotEmpty(message = "事件不能为空")
    private String describe;
    /**
     * 员工ID
     */
    @NotEmpty(message = "员工ID不能为空")
    private String staffId;
    /**
     * 类型
     */
    @NotEmpty(message = "类型不能为空")
    private String type;
    /**
     * 时间
     */
    private String time;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 图片
     */
    private List<Image> imageList;
}
