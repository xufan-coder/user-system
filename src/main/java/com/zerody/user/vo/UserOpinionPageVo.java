package com.zerody.user.vo;

import com.zerody.user.domain.UserOpinion;
import lombok.Data;

/**
 * @author kuang
 */

@Data
public class UserOpinionPageVo extends UserOpinionVo {

    /**公司名称*/
    private String companyName;

    /**部门名称*/
    private String departName;


    /**分类名称*/
    private String typeName;


    private String seeUserName;
}
