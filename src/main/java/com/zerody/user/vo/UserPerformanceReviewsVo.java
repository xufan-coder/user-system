package com.zerody.user.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.zerody.common.utils.DataUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;

import java.math.BigDecimal;

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
    @Excel(name = "企业名称",orderNum = "1",width = 20)
    private String companyName;

    /** 部门名称 */
    @Excel(name = "部门名称",orderNum = "2",width = 20)
    private String departmentName;

    /** 角色id */

    private String roleId;

    /** 角色名称 */
    @Excel(name = "角色名称",orderNum = "3",width = 20)
    private String roleName;

    /** 用户id */
    private String userId;

    /** 用户名称 */
    @Excel(name = "用户名称",orderNum = "4",width = 20)
    private String userName;

    /** 业绩收入 */
    @Excel(name = "业绩收入",orderNum = "5",width = 20,type = 10,isStatistics = true)
    private String performanceIncome;

    /** 回款笔数 */
    @Excel(name = "回款笔数",orderNum = "6",width = 20,type = 10,isStatistics = true)
    private BigDecimal paymentNumber;

    /** 放款金额 */
    @Excel(name = "放款金额",orderNum = "7",width = 20,type = 10,isStatistics = true)
    private String  loanMoney;

    /** 放款笔数 */
    @Excel(name = "放款笔数",orderNum = "8",width = 20,type = 10,isStatistics = true)
    private Integer loanNumber;

    /** 签约金额 */
    @Excel(name = "签约金额",orderNum = "9",width = 20,type = 10,isStatistics = true)
    private String signOrderMoney;

    /** 签约笔数 */
    @Excel(name = "签约笔数",orderNum = "10",width = 20,type = 10,isStatistics = true)
    private Integer signOrderNumber;

    /** 在审批金额 */
    @Excel(name = "在审批金额",orderNum = "11",width = 20,type = 10,isStatistics = true)
    private String waitApprovalMoney;

    /** 在审批笔数 */
    @Excel(name = "在审批笔数",orderNum = "12",width = 20,type = 10,isStatistics = true)
    private Integer waitApprovalNumber;

    /** 月份 */
    @Excel(name = "月份",orderNum = "13",width = 20)
    private String month;

    public String getPerformanceIncome(){
        return StringUtils.isEmpty(this.performanceIncome) ? "0.00" : String .valueOf(new BigDecimal(this.performanceIncome).setScale(2,BigDecimal.ROUND_HALF_UP));
    }

    public BigDecimal getPaymentNumber(){
        return DataUtil.isEmpty(this.paymentNumber) ? new BigDecimal("0") : this.paymentNumber;
    }

    public String getLoanMoney(){
        return StringUtils.isEmpty(this.loanMoney) ? "0.00" : this.loanMoney;
    }

    public Integer getLoanNumber(){
        return DataUtil.isEmpty(this.loanNumber) ? 0 : this.loanNumber;
    }

    public String getSignOrderMoney(){
        return StringUtils.isEmpty(this.signOrderMoney) ? "0.00" : this.signOrderMoney;
    }

    public Integer getSignOrderNumber() {
        return DataUtil.isEmpty(this.signOrderNumber) ? 0 : this.signOrderNumber;
    }

    public String getWaitApprovalMoney(){
        return StringUtils.isEmpty(this.waitApprovalMoney) ? "0.00" : this.waitApprovalMoney;
    }

    public Integer getWaitApprovalNumber(){
        return DataUtil.isEmpty(this.waitApprovalNumber) ? 0 : this.waitApprovalNumber;
    }
}
