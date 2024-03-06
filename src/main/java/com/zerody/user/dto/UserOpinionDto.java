package com.zerody.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author kuang
 */
@Data
public class UserOpinionDto {

    /**
     * 内容
     */
    @NotNull(message = "内容不能为空")
    private String content;

    /**视频*/
    private String video;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;

    /**图片*/
    List<String> replyImageList;

    /**分类id*/
    @NotNull(message = "分类不能为空")
    private String typeId;

    /**查看人员列表*/
    private List<String> seeUserIds;

    /**  0 董事长信箱，1 意见箱 */
    private Integer source;

}
