package com.zerody.user.vo;

import com.alibaba.druid.util.StringUtils;
import com.zerody.common.utils.DataUtil;
import lombok.Data;

/**
 * @author zhangpingping
 * @date 2021年09月11日 16:07
 */
@Data
public class PerformanceInfoVo {
    /** 回款金额 */
    private String paymentMoney;

    /** 回款笔数 */
    private Integer paymentNumber;

    /** 放款金额 */
    private String  loanMoney;

    /** 放款笔数 */
    private Integer loanNumber;

    /** 签单金额 */
    private String signOrderMoney;

    /** 签单笔数 */
    private Integer signOrderNumber;

    /** 放款在审金额 */
    private String waitApprovalMoney;

    /** 放款在审批笔数 */
    private Integer waitApprovalNumber;

    /** 回款再审金额 */
    private String paymentApprovalMoney;

    /** 回款在审笔数 */
    private Integer paymentApprovalNumber;
    /**签单失败统计*/
    private Integer signFailNumber;
    /**签单失败金额*/
    private String signFailMoney;

    private Integer number;

    private String  money;

    public Integer getPaymentNumber() {
        return DataUtil.isEmpty(this.paymentNumber) ? 0 : this.paymentNumber;
    }

    public Integer getLoanNumber() {
        return DataUtil.isEmpty(this.loanNumber) ? 0 : this.loanNumber;
    }

    public Integer getSignOrderNumber() {
        return DataUtil.isEmpty(this.signOrderNumber) ? 0 : this.signOrderNumber;
    }

    public Integer getWaitApprovalNumber() {
        return DataUtil.isEmpty(this.waitApprovalNumber) ? 0 : this.waitApprovalNumber;
    }

    public Integer getPaymentApprovalNumber(){
        return DataUtil.isEmpty(this.paymentApprovalNumber) ? 0 : this.paymentApprovalNumber;
    }

    public Integer getNumber(){
        return DataUtil.isEmpty(this.paymentApprovalNumber) ? 0 : this.paymentApprovalNumber;
    }

    public String getPaymentMoney(){
        return StringUtils.isEmpty(this.paymentMoney) || "0.00".equals(this.paymentMoney) ? "0" : this.paymentMoney;
    }

    public String getLoanMoney() {
        return StringUtils.isEmpty(this.loanMoney) || "0.00".equals(this.loanMoney) ? "0" : this.loanMoney;
    }

    public String getSignOrderMoney() {
        return StringUtils.isEmpty(this.signOrderMoney) || "0.00".equals(this.signOrderMoney) ? "0" : this.signOrderMoney;
    }

    public String getWaitApprovalMoney() {
        return StringUtils.isEmpty(this.waitApprovalMoney) || "0.00".equals(this.waitApprovalMoney) ? "0" : this.waitApprovalMoney;
    }

    public String getPaymentApprovalMoney() {
        return StringUtils.isEmpty(this.paymentApprovalMoney) || "0.00".equals(this.paymentApprovalMoney) ? "0" : this.paymentApprovalMoney;
    }


    public String getMoney() {
        return StringUtils.isEmpty(this.money) || "0.00".equals(this.money) ? "0" : this.money;
    }

}
