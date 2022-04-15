package com.zerody.user.vo;

import com.zerody.user.domain.UserReply;
import lombok.Data;

import java.util.List;

/**
 * @author kuang
 */
@Data
public class UserReplyVo extends UserReply {

    /**图片列表*/
    private List<String> replyImageList;
}
