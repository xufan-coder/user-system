package com.zerody.user.dto;

import com.zerody.user.domain.SysCompanyInfo;
import lombok.Data;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月25日 10:48
 */
@Data
public class StaffByCompanyDto {
    /**
     * 企业ID
     */
    private String companyId;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 是否部门（0 是部门 1团队）
     */
    private Integer isDepartment;

    /**
    *    关联企业隔离数据
    */
    private List<String> companyIds;
    /**
    *   标记企业
    */
    private Integer isProData;
    /**
     * 员工类型(企业管理员:0、 伙伴:1、 团队长:2、 副总:3)
     */
    private String userType;
    /**
     * 状态:0生效、1离职、2删除 3合作
     */
    private Integer status;

    /**查询伙伴状态  0-查询有效伙伴   1-查询含离职伙伴*/
    private Integer isShowLeave;

    /**是否查询离职伙伴   0-否 1-是*/
    private Integer isQuit;

    /**
     * 是否钻石会员（0-否 1-是）
     */
    private Integer isDiamondMember;

    /** 是否预备高管 0表示否 1表示是 2表示退学*/
    private Integer isPrepareExecutive;

    /**
     * 是否二次签约
     */
    private Boolean isSecondContract;
}
