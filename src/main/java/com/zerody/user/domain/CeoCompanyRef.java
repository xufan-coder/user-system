package com.zerody.user.domain;

import java.util.Date;
import lombok.Data;

/**
 *@ClassName CeoCompanyRef
 *@author    PengQiang
 *@DateTime  2022/6/18_10:50
 *@Deacription TODO
 */
/**
    * ceo、企业 关联表
    */
@Data
public class CeoCompanyRef {
    private String id;

    /**
    * ceo用户id
    */
    private String ceoId;

    /**
    * 企业id
    */
    private String companyId;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    *   类型 0 后台账户，1 CEO账户
    */
    private Integer type;
}
