package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * app ceo生日推送
 *
 * @author : chenKeFeng
 * @date : 2022/10/20 13:55
 */
@Data
public class AppCeoUserNotPushVo {

    /**
     * ceoId
     */
    private String ceoId;

    /**
     * 企业id
     */
    private List<String> companyIds;

    /**
     * ceo名称
     */
    private String userName;

    /**
     * ceo头像
     */
    private String avatar;

    /**
     * 负责人部门id
     */
    //private String departmentId;

    /**
     * 上级部门id
     */
    //private String parentId;

    /**
     * 用户部门id
     */
    //private String userDepartmentId;

    /**
     * 用户部门名称
     */
    //private String userDepartmentName;

}
