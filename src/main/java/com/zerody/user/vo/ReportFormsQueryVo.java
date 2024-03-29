package com.zerody.user.vo;

import com.zerody.common.utils.DataUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;

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

    /** 企业id */
    private String companyId;

    /** 企业名称 */
    private String companyName;

    /** 部门id */
    private String departId;

    /** 部门名称 */
    private String departName;

    /** 负责人id */
    private String adminId;

    /** 负责人名称 */
    private String adminName;

    /** 签单金额 */
    private String signMoney = "0";

    /** 签单笔数 */
    private Integer signNum = 0;

    /** 签单总金额 */
    private String signMoneyTotal = "0";

    /** 签单总笔数 */
    private Integer signNumTotal = 0;

    /** 审批金额 */
    private String approveMoney = "0";

    /** 审批笔数 */
    private Integer approveNum = 0;

    /** 审批总金额 */
    private String approveMoneyTotal = "0";

    /** 审批总笔数 */
    private Integer approveNumTotal = 0;

    /** 放款金额 */
    private String loansMoney = "0";

    /** 放款笔数 */
    private Integer loansNum = 0;

    /** 放款总金额 */
    private String loansMoneyTotal = "0";

    /** 放款总笔数 */
    private Integer loansNumTotal = 0;

    /** 已放款未回款金额 */
    private String notProceedsMoney = "0";

    /** 已放款未回款笔数 */
    private Integer notProceedsNum = 0;

    /** 业绩金额 */
    private String performanceMoney = "0";

    /** 业绩笔数 */
    private Integer performanceNum = 0;

    /** 业绩总金额 */
    private String performanceMoneyTotal = "0";

    /** 业绩总笔数 */
    private Integer performanceNumTotal = 0;

    /** 回款点数 */
    private String paymentCount = "0";

    /** 人均业绩 */
    private String perCapitaPerformance = "0";

    /** 人员回款率 */
    private String staffPaymentRate = "0";

    /** 邀约人数 */
    private Integer inviteNum = 0;


    /** 上门人数 */
    private Integer visitNum = 0;

    /** 业务员数量 */
    private Integer salesmanNum = 0;

    /** 业务员数量 */
    private Integer paymentUserNum = 0;

    private String paymentMoney = "0";

    /** 当月业绩金额 */
    private String monthPerformance = "0";
    /** 当月业绩笔数 */
    private Integer monthPerformanceNum = 0;

    /** 当月放款金额 */
    private String monthLoansMoney = "0";

    /** 当月放款笔数 */
    private Integer monthLoansNum = 0;

    /**
     * 大额签单总金额
     */
    private String largeSignMoney;

    /**
     * 大额签单笔数
     */
    private Integer largeSignNumber = 0;

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
        if (StringUtils.isNotEmpty(this.paymentCount)) {
            return this.paymentCount;
        }
        return "0";
    }

    public String getPerCapitaPerformance() {
        if (StringUtils.isNotEmpty(this.perCapitaPerformance)) {
            return this.perCapitaPerformance;
        }
        return "0";
    }

    public String getStaffPaymentRate(){
        if (StringUtils.isNotEmpty(this.staffPaymentRate)) {
            return this.staffPaymentRate;
        }
        return "0";
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

    public Integer getPaymentUserNum() {
        if (DataUtil.isEmpty(this.paymentUserNum)) {
            return 0;
        }
        return this.paymentUserNum;
    }



    /**
     * 大额签单总金额
     */
    public String getLargeSignMoney() {
        if (StringUtils.isEmpty(this.largeSignMoney) || "0.00".equals(this.largeSignMoney)) {
            return "0";
        }
        return this.largeSignMoney;
    }

    /**
     * 大额签单笔数
     */
    public Integer getLargeSignNumber() {

        if (DataUtil.isEmpty(this.largeSignNumber)) {
            return 0;
        }
        return this.largeSignNumber;
    }

    public void count() {
        BigDecimal num =  new BigDecimal(this.paymentUserNum == null ? 0 : this.paymentUserNum);
        BigDecimal numTotal =  new BigDecimal(this.salesmanNum == null ? 0 : this.salesmanNum);
        BigDecimal money = new BigDecimal(StringUtils.isEmpty(this.monthPerformance) ? "0" :this.monthPerformance);
        if (numTotal.compareTo(new BigDecimal(0)) != 0) {
            BigDecimal rate = num.divide(numTotal, 4, BigDecimal.ROUND_HALF_UP);
            rate = rate.multiply(new BigDecimal(100));
            rate = rate.setScale(2, BigDecimal.ROUND_HALF_UP);
            this.staffPaymentRate = rate.toString();
            BigDecimal ave = money.divide(numTotal, 2, BigDecimal.ROUND_HALF_UP);
            this.perCapitaPerformance = ave.toString();
        }

        BigDecimal paymentMoney = new BigDecimal(StringUtils.isEmpty(this.paymentMoney) ? "0" : this.paymentMoney);
        BigDecimal lonasMoney = new BigDecimal(StringUtils.isEmpty(this.monthLoansMoney) ? "0" : this.monthLoansMoney);
        if (lonasMoney.compareTo(new BigDecimal("0")) != 0) {
            BigDecimal ave = paymentMoney.divide(lonasMoney,4, BigDecimal.ROUND_HALF_UP);
            ave = ave.multiply(new BigDecimal(100));
            ave = ave.setScale(2, BigDecimal.ROUND_HALF_UP);
            this.paymentCount = ave.toString();
        }
    }

}
