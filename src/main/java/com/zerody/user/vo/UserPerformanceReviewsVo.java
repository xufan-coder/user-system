package com.zerody.user.vo;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;

/**
 * 业务总结报表分页vo
 * @author PengQiang
 * @ClassName UserPerformanceReviewsVo
 * @DateTime 2021/3/10_16:31
 * @Deacription TODO
 */
@Data
public class UserPerformanceReviewsVo {

    /** 企业名称 */
    private String companyName;

    /** 部门名称 */
    private String departmentName;

    /** 角色id */
    private String roleId;

    /** 角色名称 */
    private String roleName;

    /** 用户id */
    private String userId;

    /** 用户名称 */
    private String userName;

    /** 业绩收入 */
    private String performanceIncome;

    /** 回款笔数 */
    private Integer paymentNumber;

    /** 放款金额 */
    private String  loanMoney;

    /** 放款笔数 */
    private Integer loanNumber;

    /** 签约金额 */
    private String signOrderMoney;

    /** 签约笔数 */
    private Integer signOrderNumber;

    /** 在审批金额 */
    private String waitApprovalMoney;

    /** 在审批笔数 */
    private Integer waitApprovalNumber;

    /** 月份 */
    private String month;

    public String getPerformanceIncome(){
        return StringUtils.isEmpty(this.performanceIncome) ? "0.00" : this.performanceIncome;
    }

}
