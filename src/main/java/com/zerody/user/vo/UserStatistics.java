package com.zerody.user.vo;

import lombok.Data;

/**
 * 伙伴数据统计
 * 企业管理员:0、 伙伴:1、 团队长:2、 副总:3
 * 状态  0在职、 1离职、 3合作
 *
 * @author : chenKeFeng
 * @date : 2022/11/11 9:51
 */
@Data
public class UserStatistics {

    /**
     * 总经理
     */
    private Integer managerNum =0;

    /**
     * 副总经理
     */
    private Integer vicePresidentNum =0;

    /**
     * 团队长
     */
    private Integer teamLeaderNum =0;

    /**
     * 合作伙伴
     */
    private Integer partnerNum =0;

    /**
     * 合约中
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

}
