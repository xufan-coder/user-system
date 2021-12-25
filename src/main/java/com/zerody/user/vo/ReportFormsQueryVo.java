package com.zerody.user.vo;

import com.zerody.common.utils.DataUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author PengQiang
 * @ClassName ReportFormsQueryVo
 * @DateTime 2021/12/15_10:44
 * @Deacription TODO
 */
@Data
@Slf4j
public class ReportFormsQueryVo {

    /** id */
    private String id;

    /** 名称 */
    private String name;

    /** 签单金额 */
    private String signMoney;

    /** 签单笔数 */
    private Integer signNum;

    /** 签单总金额 */
    private String signMoneyTotal;

    /** 签单总笔数 */
    private Integer signNumTotal;

    /** 审批金额 */
    private String approveMoney;

    /** 审批笔数 */
    private Integer approveNum;

    /** 审批总金额 */
    private String approveMoneyTotal;

    /** 审批总笔数 */
    private Integer approveNumTotal;

    /** 放款金额 */
    private String loansMoney;

    /** 放款笔数 */
    private Integer loansNum;

    /** 放款总金额 */
    private String loansMoneyTotal;

    /** 放款总笔数 */
    private Integer loansNumTotal;

    /** 已放款未回款金额 */
    private String notProceedsMoney;

    /** 已放款未回款笔数 */
    private Integer notProceedsNum;

    /** 业绩金额 */
    private String performanceMoney;

    /** 业绩笔数 */
    private Integer performanceNum;

    /** 业绩总金额 */
    private String performanceMoneyTotal;

    /** 业绩总笔数 */
    private Integer performanceNumTotal;

    /** 回款点数 */
    private String paymentCount;

    /** 人均业绩 */
    private String perCapitaPerformance;

    /** 人员回款率 */
    private String staffPaymentRate;

    /** 邀约人数 */
    private Integer inviteNum;


    /** 上门人数 */
    private Integer visitNum;

    /** 业务员数量 */
    private Integer salesmanNum;

    /** 业务员数量 */
    private Integer paymentUserNum;

    private String paymentMoney;

    public String getSignMoney() {
        if (StringUtils.isEmpty(this.signMoney) || "0.00".equals(this.signMoney)) {
            return "0";
        }
        return this.signMoney;
    }

    public Integer getSignNum() {
        if (DataUtil.isEmpty(this.signNum)) {
            return 0;
        }
        return this.signNum;
    }

    public String getSignMoneyTotal() {
        if (StringUtils.isEmpty(this.signMoneyTotal) || "0.00".equals(this.signMoneyTotal)) {
            return "0";
        }
        return this.signMoneyTotal;
    }

    public Integer getSignNumTotal() {
        if (DataUtil.isEmpty(this.signNumTotal)) {
            return 0;
        }
        return this.signNumTotal;
    }


    public String getApproveMoney(){
        if (StringUtils.isEmpty(this.approveMoney) || "0.00".equals(this.approveMoney)) {
            return "0";
        }
        return this.approveMoney;
    }

    public Integer getApproveNum(){
        if (DataUtil.isEmpty(this.approveNum)) {
            return 0;
        }
        return this.approveNum;
    }

    public String getApproveMoneyTotal(){
        if (StringUtils.isEmpty(this.approveMoneyTotal) || "0.00".equals(this.approveMoneyTotal)) {
            return "0";
        }
        return this.approveMoneyTotal;
    }

    public Integer getApproveNumTotal(){
        if (DataUtil.isEmpty(this.approveNumTotal)) {
            return 0;
        }
        return this.approveNumTotal;
    }

    public String getLoansMoney(){
        if (StringUtils.isEmpty(this.loansMoney) || "0.00".equals(this.loansMoney)) {
            return "0";
        }
        return this.loansMoney;
    }


    public Integer getLoansNum() {
        if (DataUtil.isEmpty(this.loansNum)) {
            return 0;
        }
        return this.loansNum;
    }

    public String getLoansMoneyTotal(){
        if (StringUtils.isEmpty(this.loansMoneyTotal) || "0.00".equals(this.loansMoneyTotal)) {
            return "0";
        }
        return this.loansMoneyTotal;
    }


    public Integer getLoansNumTotal() {
        if (DataUtil.isEmpty(this.loansNumTotal)) {
            return 0;
        }
        return this.loansNumTotal;
    }


    public String getNotProceedsMoney() {
        if (StringUtils.isEmpty(this.notProceedsMoney) || "0.00".equals(this.notProceedsMoney)) {
            return "0";
        }
        return this.notProceedsMoney;
    }

    public Integer getNotProceedsNum() {
        if (DataUtil.isEmpty(this.notProceedsNum)) {
            return 0;
        }
        return this.notProceedsNum;
    }


    public String getPerformanceMoney() {
        if (StringUtils.isEmpty(this.performanceMoney) || "0.00".equals(this.performanceMoney)) {
            return "0";
        }
        return this.performanceMoney;
    }


    public Integer getPerformanceNum() {
        if (DataUtil.isEmpty(this.performanceNum)) {
            return 0;
        }
        return this.performanceNum;
    }

    public String getPerformanceMoneyTotal() {
        if (StringUtils.isEmpty(this.performanceMoneyTotal) || "0.00".equals(this.performanceMoneyTotal)) {
            return "0";
        }
        return this.performanceMoneyTotal;
    }


    public Integer getPerformanceNumTotal() {
        if (DataUtil.isEmpty(this.performanceNumTotal)) {
            return 0;
        }
        return this.performanceNumTotal;
    }


    public String getPaymentCount() {
        BigDecimal paymentMoney = new BigDecimal(StringUtils.isEmpty(this.paymentMoney) ? "0" : this.paymentMoney);
        BigDecimal lonasMoney = new BigDecimal(StringUtils.isEmpty(this.loansMoneyTotal) ? "0" : this.loansMoneyTotal);
        if (lonasMoney.compareTo(new BigDecimal("0")) == 0) {
            return "0";
        }
        BigDecimal ave = paymentMoney.divide(lonasMoney,4, BigDecimal.ROUND_HALF_UP);
        ave.multiply(new BigDecimal("100"));
        ave.setScale(2, BigDecimal.ROUND_HALF_UP);
        return ave.toString();
    }

    public String getPerCapitaPerformance() {
        BigDecimal money = new BigDecimal(StringUtils.isEmpty(this.performanceMoneyTotal) ? "0" :this.performanceMoneyTotal);
        BigDecimal num  = new BigDecimal(this.salesmanNum == null ? 0 : this.salesmanNum);
        if (num.compareTo(new BigDecimal(0)) == 0) {
            return "0";
        }
        BigDecimal ave = money.divide(num, 2, BigDecimal.ROUND_HALF_UP);
        return ave.toString();
    }

    public String getStaffPaymentRate(){
        BigDecimal num =  new BigDecimal(this.paymentUserNum == null ? 0 : this.paymentUserNum);
        BigDecimal numTotal =  new BigDecimal(this.salesmanNum == null ? 0 : this.salesmanNum);
        if (numTotal.compareTo(new BigDecimal(0)) == 0) {
            return "0";
        }
        BigDecimal rate = num.divide(numTotal, 4, BigDecimal.ROUND_HALF_UP);
        rate.multiply(new BigDecimal(100));
        rate.setScale(2, BigDecimal.ROUND_HALF_UP);
        return rate.toString();
    }



    public Integer getInviteNum(){
        if (DataUtil.isEmpty(this.inviteNum)) {
            return 0;
        }
        return this.inviteNum;
    }

    public Integer getVisitNum(){
        if (DataUtil.isEmpty(this.visitNum)) {
            return 0;
        }
        return this.visitNum;
    }


}
