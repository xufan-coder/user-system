package com.zerody.user.vo;

import lombok.Data;

/**
 * 伙伴数据统计
 *
 * @author : chenKeFeng
 * @date : 2022/11/11 9:51
 */
@Data
public class UserStatistics {

    /**
     * 总经理
     */
    private Integer managerNum;

    /**
     * 副总经理
     */
    private Integer vicePresidentNum;

    /**
     * 团队长
     */
    private Integer teamLeaderNum;

    /**
     * 合作伙伴
     */
    private Integer partnerNum;

    /**
     * 合约中
     */
    private Integer contractNum;

    /**
     * 已解约
     */
    private Integer cancelledNum;

    /**
     * 合作中
     */
    private Integer inCooperationNum;

}
