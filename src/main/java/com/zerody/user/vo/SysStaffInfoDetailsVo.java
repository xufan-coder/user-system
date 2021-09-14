package com.zerody.user.vo;

import lombok.Data;

/**
 * @author zhangpingping
 * @date 2021年09月11日 16:30
 */
@Data
public class SysStaffInfoDetailsVo {
    private String userId;
    /**
     * 个人图片路径
     */
    private String avatar;
    /**
     * 员工名称
     */
    private String userName;
    /**
     * 手机号码
     */
    private String phoneNumber;
    /**
     * 身份证
     */
    private String certificateCard;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 部门名称
     */
    private String departName;
    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 客户数
     */
    private Integer customerCount;
    /**
     * A类客户数
     */
    private Integer customerTypeCount;
    /**
     * 签单金额
     */
    private String signOrderMoney;

    /**
     * 签单失败统计
     */
    private Integer signFailNumber;

    /**
     * 回款金额
     */
    private String paymentMoney;

    /**
     * 放款金额
     */
    private String loanMoney;
}
