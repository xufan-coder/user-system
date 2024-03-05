package com.zerody.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author kuang
 */
@Data
public class UserReplyDto extends UserOpinionDto{

    /**意见id*/
    @NotNull(message = "意见id不能为空")
    private String opinionId;

    /**用于消息推送*/
    private String userName;

    /** 是否是boss账号 */
    private Boolean isCeo;
}
