package com.zerody.user.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 伙伴数据统计
 * 企业管理员:0、 伙伴:1、 团队长:2、 副总:3
 * 状态  0合约中、 1已解约、 3合作中
 *
 * @author : chenKeFeng
 * @date : 2022/11/11 9:51
 */
@Data
public class UserStatisticsVo {

    /**
     * 总经理
     */
    private Integer managerNum =0;

    /**
     * 总经理占比
     */
    private BigDecimal managerRate;

    /**
     * 副总经理
     */
    private Integer vicePresidentNum =0;

    /**
     * 副总经理占比
     */
    private BigDecimal vicePresidentRate;

    /**
     * 团队长
     */
    private Integer teamLeaderNum =0;

    /**
     * 团队长占比
     */
    private BigDecimal teamLeaderRate;

    /**
     * 合作伙伴
     */
    private Integer partnerNum =0;

    /**
     * 合作伙伴占比
     */
    private BigDecimal partnerRate;

    /**
     * 合约中(签约中)
     */
    private Integer contractNum =0;

    /**
     * 已解约
     */
    private Integer cancelledNum =0;

    /**
     * 合作中
     */
    private Integer inCooperationNum =0;

    //-------------------------------------------------

    /**
     * 历史签约 = 签约中 + 已解约 + 合作中
     */
    private Integer historicalContractNum =0;

    /**
     * 砖石会员
     */
    private Integer masonryMemberNum =0;

    /**
     * 砖石会员占比
     */
    private BigDecimal masonryMemberRate;

    /**
     * 预备高管
     */
    private Integer prospectiveExecutiveNum =0;

    /**
     * 预备高管占比
     */
    private BigDecimal prospectiveExecutiveRate;

    /**
     * 内控伙伴
     */
    private Integer internalControlUserNum =0;

    /**
     * 二次签约
     */
    private Integer secondContractNum =0;

    /**
     * 二次签约占比
     */
    private BigDecimal secondContractRate;

    //-------------------------------------------------

    /**
     * 本月流失率=本月员工流失人数/当前员工总数*100%
     * 本月离职员工数/在职员工数
     */
    private BigDecimal toRunOff;

}
