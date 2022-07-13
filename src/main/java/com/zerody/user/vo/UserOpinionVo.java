package com.zerody.user.vo;

import com.zerody.user.domain.UserOpinion;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author kuang
 */
@Data
public class UserOpinionVo extends UserOpinion {

    /**回复内容*/
    private String replyContent;

    /**分类名称*/
    private String typeName;

}
