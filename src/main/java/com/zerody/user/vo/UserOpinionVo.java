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

    /**公司名称*/
    private String companyName;

    /**部门名称*/
    private String departName;

    /**回复内容*/
    private String replyContent;

    /**分类名称*/
    private String typeName;

    /**查看人名称*/
    private String seeUserName;

}
