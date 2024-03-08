package com.zerody.user.vo;

import com.zerody.user.domain.UserOpinion;
import lombok.Data;

import java.util.List;

/**
 * @author kuang
 */
@Data
public class UserOpinionDetailVo extends UserOpinion {

    /**公司名称*/
    private String companyName;

    /**部门名称*/
    private String departName;

    /**分类名称*/
    private String typeName;

    /**查看人名称*/
    private String seeUserName;

    /**回复列表*/
    private List<UserReplyVo> replyList;

    /**图片列表*/
    private List<String> replyImageList;

    /** 协助人名称 */
    private String assistantUserName;
}
